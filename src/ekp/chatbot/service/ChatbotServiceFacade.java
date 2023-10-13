package ekp.chatbot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.data.MbomDataService;
import ekp.data.MfDataService;
import ekp.data.PuDataService;
import ekp.data.SdDataService;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.data.service.mbom.query.PartQueryParam;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.query.WorkorderQueryParam;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.query.PurchQueryParam;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.query.SalesOrderQueryParam;
import ekp.mbom.MbomService;
import ekp.mbom.type.PartCfgStatus;
import legion.DataServiceFactory;
import legion.nlp.Nlp;
import legion.util.NumberFormatUtil;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.CompareBoolean;
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
	private static MfDataService mfDataService = DataServiceFactory.getInstance().getService(MfDataService.class);
	private static PuDataService puDataService = DataServiceFactory.getInstance().getService(PuDataService.class);
	private static SdDataService sdDataService = DataServiceFactory.getInstance().getService(SdDataService.class);

	// -------------------------------------------------------------------------------
	public String[] pc1() {
		List<String> list = new ArrayList<>();

		QueryOperation<PartCfgQueryParam, PartCfgInfo> param = new QueryOperation<>();
		// 手動過濾掉MCD開頭的
		param.appendCondition(QueryOperation.booleanOp(CompareBoolean.not,
				QueryOperation.value(PartCfgQueryParam.ROOT_PART_PIN, CompareOp.like, "MCD%")));
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
	// -------------------------------------MBOM--------------------------------------
	public String[] bom(List<String> _lemmaList, boolean _publishOnly) {
		log.debug(" _lemmaList.size(): {}", _lemmaList.size());
		if (_lemmaList.size() <= 0)
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
		List<PartCfgInfo> pcList = param == null ? new ArrayList<>() : param.getQueryResult();

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
		if (_lemmaList.size() <= 0)
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

	// -------------------------------------------------------------------------------
	// --------------------------------------MF---------------------------------------
	public String[] woList() {
		List<String> list = new ArrayList<>();

		QueryOperation<WorkorderQueryParam, WorkorderInfo> param = new QueryOperation<>();
		param = mfDataService.searchWorkorder(param, null);

		int count = param.getTotal();
		List<WorkorderInfo> woList = param.getQueryResult();

		StringBuilder msg = new StringBuilder();
		msg.append("There are ").append(count).append(" workorders.");
		list.add(msg.toString());

		for (WorkorderInfo wo : woList) {
			msg = new StringBuilder();
			msg.append(wo.getWoNo()).append("\t").append(wo.getPartPin()).append("\t").append(wo.getPart().getName())
					.append("\t").append(wo.getPartAcqMmMano()).append("\t")
					.append(NumberFormatUtil.getDecimalString(wo.getRqQty(), 2));
			list.add(msg.toString());
		}

		return list.toArray(new String[0]);
	}

	// -------------------------------------------------------------------------------
	// --------------------------------------PU---------------------------------------
	public String[] puList() {
		List<String> list = new ArrayList<>();

		QueryOperation<PurchQueryParam, PurchInfo> param = new QueryOperation<>();
		param = puDataService.searchPurch(param, null);

		int count = param.getTotal();
		List<PurchInfo> puList = param.getQueryResult();

		StringBuilder msg = new StringBuilder();
		msg.append("There are ").append(count).append(" purches.");
		list.add(msg.toString());

		for (PurchInfo pu : puList) {
			msg = new StringBuilder();
			msg.append(pu.getPuNo()).append("\t").append(pu.getTitle()).append("\t").append(pu.getSupplierName())
					.append("\t").append(NumberFormatUtil.getDecimalString(pu.getSumPurchItemAmt(), 2));
			list.add(msg.toString());
		}

		return list.toArray(new String[0]);
	}

	// -------------------------------------------------------------------------------
	// --------------------------------------SD---------------------------------------
	public String[] soList() {
		List<String> list = new ArrayList<>();
//
		QueryOperation<SalesOrderQueryParam, SalesOrderInfo> param = new QueryOperation<>();
		param = sdDataService.searchSalesOrder(param, null);

		int count = param.getTotal();
		List<SalesOrderInfo> soList = param.getQueryResult();

		StringBuilder msg = new StringBuilder();
		msg.append("There are ").append(count).append(" sales orders.");
		list.add(msg.toString());

		for (SalesOrderInfo so : soList) {
			msg = new StringBuilder();
			msg.append(so.getSosn()).append("\t").append(so.getTitle()).append("\t").append(so.getCustomerName())
					.append("\t").append(NumberFormatUtil.getDecimalString(so.getSumSoiAmt(), 2));
			list.add(msg.toString());
		}

		return list.toArray(new String[0]);
	}
}
