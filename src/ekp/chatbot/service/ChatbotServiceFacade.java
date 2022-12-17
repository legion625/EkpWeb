package ekp.chatbot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.data.service.mbom.query.PartQueryParam;
import ekp.mbom.MbomService;
import legion.DataServiceFactory;
import legion.nlp.Nlp;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.CompareOp;
import legion.util.query.QueryOperation.ConjunctiveOp;
import legion.util.query.QueryOperation.QueryValue;

public class ChatbotServiceFacade {
	private Logger log = LoggerFactory.getLogger(ChatbotServiceFacade.class);

	// -------------------------------------------------------------------------------
	private final static ChatbotServiceFacade INSTANCE = new ChatbotServiceFacade();

	private ChatbotServiceFacade() {
	}

	public final static ChatbotServiceFacade getInstance() {
		return INSTANCE;
	}

	// -------------------------------------------------------------------------------
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);

	// -------------------------------------------------------------------------------
//	public String pc1() {
//		QueryOperation<PartCfgQueryParam, PartCfgInfo> param = new QueryOperation<>();
//		param = mbomDataService.searchPartCfg(param);
//
//		int count = param.getTotal();
//		List<PartCfgInfo> pcList = param.getQueryResult();
//
//		StringBuilder msg = new StringBuilder();
//		msg.append("There are ").append(count).append(" part configurations.");
//		for (PartCfgInfo pc : pcList)
//			msg.append(System.lineSeparator()).append(pc.getId()).append("\t").append(pc.getName())
//					.append(pc.getRootPartPin()).append(pc.getStatusName());
//		return msg.toString();
//	}
	
	public String[] pc1() {
		List<String> list = new ArrayList<>();

		QueryOperation<PartCfgQueryParam, PartCfgInfo> param = new QueryOperation<>();
		param = mbomDataService.searchPartCfg(param);

		int count = param.getTotal();
		List<PartCfgInfo> pcList = param.getQueryResult();

		StringBuilder msg = new StringBuilder();
		msg.append("There are ").append(count).append(" part configurations.");
		list.add(msg.toString());

		for (PartCfgInfo pc : pcList) {
			msg = new StringBuilder();
			msg.append(pc.getId()).append("\t").append(pc.getName()).append(pc.getRootPartPin())
					.append(pc.getStatusName());
			list.add(msg.toString());
		}

		return list.toArray(new String[0]);
	}
	
	public String[] cost(List<String> _lemmaList) {
//		Nlp nlp = Nlp.getInstance();
//		nlp.parseSentences(_text)
//		List<String> list = new ArrayList<>();
		QueryOperation<PartQueryParam, PartInfo> param = new QueryOperation<>();
		List<QueryValue<PartQueryParam, ?>> qvList = new ArrayList<>();
		for(String _lemma: _lemmaList) {
			qvList.add(QueryOperation.value(PartQueryParam.NAME, CompareOp.like, "%"+_lemma+"%"));
			qvList.add(QueryOperation.value(PartQueryParam.PIN, CompareOp.like, "%"+_lemma+"%"));
		}
		param.appendCondition(QueryOperation.group(ConjunctiveOp.or, qvList.toArray(new QueryValue[0])));
		
		param = mbomDataService.searchPart(param);
		log.debug("param.getTotal(): {}", param.getTotal());
	
		
		List<String> responseList = new ArrayList<>();
		responseList.add("Find "+param.getTotal()+" results.");
		List<PartInfo> partList = param.getQueryResult();
		for(PartInfo p: partList) {
			List<PartAcqInfo> paList = p.getPaList(false);
			double min = paList.parallelStream().map(pa->pa.getRefUnitCost()).min(Double::compare).orElse(Double.NaN);
			double max = paList.parallelStream().map(pa->pa.getRefUnitCost()).max(Double::compare).orElse(Double.NaN);
			String msg = "The cost range of ["+p.getName()+"]["+p.getPin()+"] is from "+min+" to "+max+" .";
			responseList.add(msg);
		}
		return responseList.toArray(new String[0]);
	}

}
