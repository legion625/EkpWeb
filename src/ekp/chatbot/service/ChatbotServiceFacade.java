package ekp.chatbot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.data.MbomDataService;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.data.service.mbom.query.PartQueryParam;
import ekp.mbom.MbomService;
import ekp.mbom.type.PartCfgStatus;
import legion.DataServiceFactory;
import legion.nlp.Nlp;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.CompareOp;
import legion.util.query.QueryOperation.ConjunctiveOp;
import legion.util.query.QueryOperation.QueryValue;

public class ChatbotServiceFacade {
//	private Logger log = LoggerFactory.getLogger(ChatbotServiceFacade.class);
	private Logger log = LoggerFactory.getLogger(DebugLogMark.class);

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

	// -------------------------------------------------------------------------------
	public String[] bom(List<String> _lemmaList, boolean _publishOnly) {
		log.debug(" _lemmaList.size(): {}", _lemmaList.size());
		if(_lemmaList.size()<=0)
			_lemmaList.add("找不到");
		for (String _lemma : _lemmaList)
			log.debug("_lemma: {}", _lemma);
		
		QueryOperation<PartCfgQueryParam, PartCfgInfo> param = new QueryOperation<>();
		List<QueryValue<PartCfgQueryParam, ?>> qvList = new ArrayList<>();
		for (String _lemma : _lemmaList) {
			qvList.add(QueryOperation.value(PartCfgQueryParam.ID, CompareOp.like, "%" + _lemma + "%"));
			qvList.add(QueryOperation.value(PartCfgQueryParam.NAME, CompareOp.like, "%" + _lemma + "%"));
			qvList.add(QueryOperation.value(PartCfgQueryParam.ROOT_PART_PIN, CompareOp.like, "%" + _lemma + "%"));
		}
		param.appendCondition(QueryOperation.group(ConjunctiveOp.or, qvList.toArray(new QueryValue[0])));
		if (_publishOnly)
			param.appendCondition(QueryOperation.value(PartCfgQueryParam.STATUS_IDX, CompareOp.equal,
					PartCfgStatus.PUBLISHED.getIdx()));

		param = mbomDataService.searchPartCfg(param);
		List<PartCfgInfo> pcList =param==null?new ArrayList<>(): param.getQueryResult();	

		List<String> responseList = new ArrayList<>();
		responseList.add("Find " + param.getTotal() + " results.");
		for (PartCfgInfo pc : pcList) {
			responseList.add("=====================================================");
			responseList.add(pc.getRootPart().getName() + "\t" + pc.getRootPartPin() + "\t" + pc.getName() + "\t"
					+ pc.getId() + "\t" + pc.getDesp());
			PartInfo p = pc.getRootPart();
			PartAcqInfo pa = p.getPa(pc);
			appendBomPaResponse(responseList, pc, p, pa);
		}
		return responseList.toArray(new String[0]);
	}

	private void appendBomPaResponse(List<String> _responseList, PartCfgInfo _pc, PartInfo _p, PartAcqInfo _pa) {
		_responseList.add(_p.getName() + "\t" + _p.getPin() + "\t" + _pa.getTypeName());
		for (PartAcqInfo _childPa : _pa.getChildrenList(_pc))
			appendBomPaResponse(_responseList, _pc, _childPa.getPart(false), _childPa);
	}

	// -------------------------------------------------------------------------------
	public String[] cost(List<String> _lemmaList) {
		log.debug(" _lemmaList.size(): {}", _lemmaList.size());
		if(_lemmaList.size()<=0)
			_lemmaList.add("找不到");
		for (String _lemma : _lemmaList)
			log.debug("_lemma: {}", _lemma);
		
		QueryOperation<PartQueryParam, PartInfo> param = new QueryOperation<>();
		List<QueryValue<PartQueryParam, ?>> qvList = new ArrayList<>();
		for (String _lemma : _lemmaList) {
			qvList.add(QueryOperation.value(PartQueryParam.NAME, CompareOp.like, "%" + _lemma + "%"));
			qvList.add(QueryOperation.value(PartQueryParam.PIN, CompareOp.like, "%" + _lemma + "%"));
		}
		param.appendCondition(QueryOperation.group(ConjunctiveOp.or, qvList.toArray(new QueryValue[0])));

		param = mbomDataService.searchPart(param);
		log.debug("param.getTotal(): {}", param.getTotal());

		List<String> responseList = new ArrayList<>();
		responseList.add("Find " + param.getTotal() + " results.");
		List<PartInfo> partList = param.getQueryResult();
		for (PartInfo p : partList) {
			List<PartAcqInfo> paList = p.getPaList(false);
			double min = paList.parallelStream().map(pa -> pa.getRefUnitCost()).min(Double::compare).orElse(Double.NaN);
			double max = paList.parallelStream().map(pa -> pa.getRefUnitCost()).max(Double::compare).orElse(Double.NaN);
			String msg = "The cost range of [" + p.getName() + "][" + p.getPin() + "] is from " + min + " to " + max
					+ " ($/" + p.getUnitName() + ").";
			responseList.add(msg);
		}
		return responseList.toArray(new String[0]);
	}

}
