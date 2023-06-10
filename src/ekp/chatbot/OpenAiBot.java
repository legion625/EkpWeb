package ekp.chatbot;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.util.DataUtil;
import legion.openai.ChatBot;
import legion.util.JsonUtil;

public class OpenAiBot implements SimpleBot{
	
	private Logger log = LoggerFactory.getLogger(DebugLogMark.class);

	private String apiKey;
	
	public OpenAiBot() {
		apiKey = "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";
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
		
		String selStr = "\""+_utterance+"\"";
		selStr +="\n這段文字的意圖和下列何者最接近? (請簡答「數字編號」即可。)\n";
		selStr +="1.詢問指定產品的價格\n";
		selStr +="2.展開指定產品的BOM結構\n";
		selStr +="3.查詢構型清單\n";
		selStr +="9.以上皆非\n";
		log.debug("selStr: {}", selStr);
		String select= ChatBot.sendQuery(selStr, apiKey);
		log.debug("select: {}", select);
		
		int s = DataUtil.findInt(select);
		log.debug("s: {}", s); 
		
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

}
