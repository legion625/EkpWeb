package ekp.chatbot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import ekp.DebugLogMark;
import ekp.chatbot.service.ChatbotServiceFacade;
import ekp.util.DataUtil;
import legion.SystemInfoDefault;
import legion.openai.ChatBot;
import legion.util.DataFO;
import legion.util.JsonUtil;
import legion.util.LogUtil;

public class OpenAiBot implements SimpleBot{
	private Logger log = LoggerFactory.getLogger(OpenAiBot.class);
//	private Logger log = LoggerFactory.getLogger(DebugLogMark.class);

	private String apiKey;
	
	public OpenAiBot() {
		apiKey = SystemInfoDefault.getInstance().getAttribute("openai.apiKey");
	}
	
	public IntentType getIntent(String _utterance) {
		String selStr = IntentType.getInputStr(_utterance);
		log.debug("selStr: {}", selStr);
		String select= ChatBot.sendQuery(selStr, apiKey);
		log.debug("select: {}", select);
		IntentType intentType = IntentType.I99;
		try {
			int s = DataUtil.findInt(select);
			log.debug("s: {}", s);	
			intentType = IntentType.get(s);
		}catch (NumberFormatException e) {
//			log.error("s: {}", e.get);
			LogUtil.log(e,  Level.ERROR);
		}
		 
		
//		IntentType intentType = IntentType.get(s);
		log.info("intentType: {}", intentType); 
		return intentType;
	}

	public List<String> getEntityValueList(String _utterance, EntityType _entity) {
		if (_entity == null || EntityType.NONE == _entity)
			return new ArrayList<>();
		String rawResponse = ChatBot.sendQuery(_entity.getInputStr(_utterance), apiKey);
		log.debug("rawResponse: {}", rawResponse);
		return DataUtil.findInCurlyBrackets(rawResponse);
	}
	
	@Override
	public String[] getResponseNew(String _utterance) {
		IntentType intentType = getIntent(_utterance);
		log.debug("intentType: {}", intentType);
		
		return intentType.getResponse(_utterance, apiKey);
	}
	
	// -------------------------------------------------------------------------------
	public enum IntentType{
		I11(11,"查詢[產品]清單、[構型]清單或[產品型錄]"), //
		I12(12,"展開指定[產品]的BOM(bill of material)、料表、或結構(structure)"), //
		I13(13,"詢問指定[產品]的價格"), //
		I21(21,"查詢銷售訂單(sales order)或有哪些客戶(customer)"), //
		I31(31,"查詢購案(purchsing)或相關供應商(supllier)"), //
		I41(41,"查詢工令(workorder)"), //
		I90(90,"詢問這個chatbot的操作說明，或功能指引。(how can you help me)"), //
		I99(99,"其他，和以上意圖相似度均不高"), //
		;
		
		private Logger log = LoggerFactory.getLogger(DebugLogMark.class);
		
		private int idx;
		private String desp;
		
		private IntentType(int idx, String desp) {
			this.idx = idx;
			this.desp = desp;
		}
		
		// ---------------------------------------------------------------------------
		public EntityType getEntityType() {
			switch (this) {
			case I12:
			case I13:
				return EntityType.E11;
			default:
				return EntityType.NONE;
			}
		}
		
		public String[] getResponse(String _utterance, String apiKey) {
			EntityType entityType  = null;
			switch (this) {
			case I11:
				return ChatbotServiceFacade.getInstance().pc1();
			case I12:{
				entityType =  EntityType.E11;
				String rawResponse = ChatBot.sendQuery(entityType.getInputStr(_utterance), apiKey);
				log.debug("rawResponse: {}", rawResponse);
				for (String n : entityType.entityNames)
					rawResponse = rawResponse.replaceAll(n, "");
				List<String> entityValuesList = DataUtil.findInCurlyBrackets(rawResponse);
				entityValuesList = entityValuesList.stream().map(DataUtil::sanitize).collect(Collectors.toList()); // 消毒
				for(String entityValue:entityValuesList)
					log.debug("entityValue: {}", entityValue);
				return ChatbotServiceFacade.getInstance().bom(entityValuesList, false);
			}
			case I13:{
				entityType =  EntityType.E11;
				String rawResponse = ChatBot.sendQuery(entityType.getInputStr(_utterance), apiKey);
				log.debug("rawResponse: {}", rawResponse);
				List<String> entityValuesList = DataUtil.findInCurlyBrackets(rawResponse);
				entityValuesList = entityValuesList.stream().map(DataUtil::sanitize).collect(Collectors.toList()); // 消毒
				return ChatbotServiceFacade.getInstance().cost(entityValuesList);
			}
			case I90:
				return new String[] { I11.desp, I12.desp, I13.desp };
			case I99:
			default:
				return new String[] { ChatBot.sendQuery(_utterance, apiKey) };
			}
		}
		
		
		// ---------------------------------------------------------------------------
		public static String getInputStr(String _utterance) {
			String str = "\"" + _utterance + "\"";
//			str += "\n這段文字的意圖和下列何者描述的意圖有高相似度? (請簡答「數字編號」即可。若相似度低，請選擇「其他」。)\n";
			str += "\n這段文字的意圖和下列何者描述的意圖有高相似度? (請簡答「編號」即可。若相似度低，請選擇「其他」。)\n";
			for (IntentType t : values())
				str += t.idx + "." + t.desp + "\n";
			return str;
		}

		public static IntentType get(int _idx) {
			for (IntentType t : values()) {
				if (t.idx == _idx)
					return t;
			}
			return IntentType.I99;
		}
	}
	
	// -------------------------------------------------------------------------------
	public enum EntityType {
		NONE(), //
//		E11("產品編號"), E12("產品名稱"), //
		E11("產品編號","產品名稱"), //
		;

		private Logger log = LoggerFactory.getLogger(EntityType.class);

		public String[] entityNames;
		
		private EntityType(String... entityNames) {
			this.entityNames = entityNames;
		}
		
		public String getInputStr(String _utterance) {
			String str = "\"" + _utterance + "\"";
			switch (this) {
//			case E1: {
//				return str + "\n這個句字中可能的「產品編號」是什麼?";
//			}
			default:
//				return str + "\n這個句字中可能的「" + entityName + "」是什麼?";
				String entityStr = "";
				for(String en: entityNames) {
					if(!DataFO.isEmptyString(entityStr))
						entityStr+=" or ";
					entityStr+=en;
				}
				
//				return str +"\n這個句字中有沒有「"+ entityStr+"」?"+"\n若有，請用json格式回答「"+entityStr+"的值」；無，請回答「無」";
//				return str + "\nFrom the sentence above, please find the potential \""+entityStr+"\", format answer in json format, and return the value";
//				return str + "\nFrom the sentence above, please find the potential \""+entityStr+"\", and return the value inside curly brackets. If not found, return empty string.";
				return str + "\nFrom the sentence above, please find the potential \""+entityStr+"\", and wrap the value in curly brackets. If not found, return empty string.";
				
//				log.error("EntityType error.");
//				return ""; //
			}
		}
	}

}
