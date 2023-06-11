package ekp.chatbot;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.util.DataUtil;
import legion.SystemInfoDefault;
import legion.openai.ChatBot;
import legion.util.JsonUtil;

public class OpenAiBot implements SimpleBot{
//	private Logger log = LoggerFactory.getLogger(OpenAiBot.class);
	private Logger log = LoggerFactory.getLogger(DebugLogMark.class);

	private String apiKey;
	
	public OpenAiBot() {
		apiKey = SystemInfoDefault.getInstance().getAttribute("openai.apiKey");
	}
	
	public IntentType getIntent(String _utterance) {
		String selStr = IntentType.getInputStr(_utterance);
		log.debug("selStr: {}", selStr);
		String select= ChatBot.sendQuery(selStr, apiKey);
		log.debug("select: {}", select);
		
		int s = DataUtil.findInt(select);
		log.debug("s: {}", s); 
		
		IntentType intentType = IntentType.get(s);
		log.debug("intentType: {}", intentType); 
		return intentType;
	}
	
	@Override
	public String[] getResponseNew(String _utterance) {
//		String inputStr = "Please tell me the 'intent' and 'entity' of the sentence: ";
//		inputStr +="\""+_utterance+"\"";
//		log.debug("inputStr: {}", inputStr);
		
//		String outputStr = ChatBot.sendQuery(inputStr, apiKey);
//		log.debug("outputStr: {}", outputStr);
//		
//		
//		String intentStr = ChatBot.sendQuery("what's the intent of the sentence: " +outputStr, apiKey);
//		String entityStr = ChatBot.sendQuery("what's the entity of the sentence: " +outputStr, apiKey);
//		log.debug("intentStr: {}", intentStr);
//		log.debug("entityStr: {}", entityStr);
		
		
		
		/* 當要詢問指定產品時，進一步找出產品編號。 */
////		String entityStr = "Please tell me the 'entity' of the sentence: ";
//		String entityStr = "這個句字中可能的「產品編號」是什麼?";
//		entityStr +="\""+_utterance+"\"";
//		log.debug("entityStr: {}", entityStr);
//		String entity= ChatBot.sendQuery(entityStr, apiKey);
//		log.debug("entity: {}", entity);
//		
		
		
		
//		// 測試，先寫死
////		String outputStr = "[\"intent: 詢問商品價格\\nentity: 商品編號為55A3-Z0\"]";
//		JSONObject jsonObj = new JSONObject(outputStr);
//		log.debug("jsonObj: {}", jsonObj);
////		log.debug("{}\t{}", jsonObj.get("intent"), jsonObj.get("entity"));
//		
		
		// TODO Auto-generated method stub
		return null;
	}
	
	// -------------------------------------------------------------------------------
	public enum IntentType{
		I11(11,"查詢[產品]清單、[構型]清單或[產品型錄]"), //
		I12(12,"展開指定[產品]的BOM(bill of material)或料表結構(structure)"), //
		I13(13,"詢問指定[產品]的價格"), //
		I90(90,"詢問這個chatbot的操作說明，或功能指引。"), //
		I99(99,"其他，和以上意圖相似度均不高"), //
		;
		
		private int idx;
		private String desp;
		
		private IntentType(int idx, String desp) {
			this.idx = idx;
			this.desp = desp;
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
		E1;

		private Logger log = LoggerFactory.getLogger(EntityType.class);

		public String getInputStr(String _utterance) {
			String str = "\"" + _utterance + "\"";
			switch (this) {
			case E1: {
				return str + "\n這個句字中可能的「產品編號」是什麼?";
			}
			default:
				log.error("EntityType error.");
				return ""; //
			}
		}
	}

}
