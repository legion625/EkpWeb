package ekp.chatbot;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.util.CoreMap;
import ekp.DebugLogMark;
import ekp.chatbot.service.ChatbotServiceFacade;
import legion.nlp.Nlp;
import legion.web.zk.ZkMsgBox;

public class RuleBasedBot implements SimpleBot{
//	private Logger log = LoggerFactory.getLogger(RuleBasedBot.class);
	private Logger log = LoggerFactory.getLogger(DebugLogMark.class);
	
	
	private ChatbotServiceFacade facade;
	
	public RuleBasedBot() {
		facade = ChatbotServiceFacade.getInstance();	
	}

	// -------------------------------------------------------------------------------
//	@Override
//	public String getResponse(String _utterance) {
//
//		if (_utterance.contains("configuration")) {
//			return facade.pc1();
//		} else if (_utterance.contains("publish")) {
//			return "publish";
//		} else {
//			return "default...";
//		}
//	}
	
	@Override
	public String[] getResponseNew(String _utterance) {
		Nlp nlp = Nlp.getInstance();
		List<CoreMap> sentences = nlp.parseSentences(_utterance);
		log.debug("sentences.size(): {}", sentences.size());
//		int wordsCntRaw = sentences.parallelStream().mapToInt(s -> s.get(TokensAnnotation.class).size()).sum();
		// 過濾POS並取出word
		List<String> lemmaList = nlp.parseLemma(sentences);
		log.debug("lemmaList.size(): {}", lemmaList.size());
		// 過濾stopwords
		// TODO
		
		for (String word : lemmaList) {
			log.debug("{}", word);
		}
		
		
		if (_utterance.contains("configuration")) {
			return facade.pc1();
		}else if(lemmaList.contains("cost")) {
			return facade.cost(lemmaList);
		} 
//		
//		else if (_utterance.contains("publish")) {
//			return new String[]{"publish"};
//		} 
		else {
			return new String[]{"default..."};
		}
	}

}
