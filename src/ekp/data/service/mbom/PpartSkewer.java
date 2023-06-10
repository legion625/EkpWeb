package ekp.data.service.mbom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.data.BizObjLoader;
import ekp.data.MbomDataService;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.data.service.mbom.query.PpartSkewerQueryParam;
import legion.DataServiceFactory;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.CompareOp;
import legion.util.query.QueryOperation.QueryValue;

public class PpartSkewer {
	private Logger log = LoggerFactory.getLogger(PpartSkewer.class);
//	private Logger log = LoggerFactory.getLogger(DebugLogMark.class);
	
	/* p */
	private String pUid;
	private String pPin;
	private String pName;

	/* pa */
	private String paUid;
	private String paId;
	private String paName;

	/* pars */
	private String parsSeq;
	private String parsName;
	private String parsDesp;

	/* ppart */
	private String uid;
	private long objectCreateTime;
	private long objectUpdateTime;
	private String parsUid; // ref data key

	// assign part
	private boolean assignPart;
	private String partUid; // ref data key
	private String partPin; // ref biz key
	private double partReqQty; // required quantity (allow decimal in certain conditions)

	/* ppart-p */
	private String partName;

	// -------------------------------------------------------------------------------
	// ---------------------------------getter&setter---------------------------------
	public String getpUid() {
		return pUid;
	}

	void setpUid(String pUid) {
		this.pUid = pUid;
	}

	public String getpPin() {
		return pPin;
	}

	void setpPin(String pPin) {
		this.pPin = pPin;
	}

	public String getpName() {
		return pName;
	}

	void setpName(String pName) {
		this.pName = pName;
	}

	public String getPaUid() {
		return paUid;
	}

	void setPaUid(String paUid) {
		this.paUid = paUid;
	}

	public String getPaId() {
		return paId;
	}

	void setPaId(String paId) {
		this.paId = paId;
	}

	public String getPaName() {
		return paName;
	}

	void setPaName(String paName) {
		this.paName = paName;
	}

	public String getParsSeq() {
		return parsSeq;
	}

	void setParsSeq(String parsSeq) {
		this.parsSeq = parsSeq;
	}

	public String getParsName() {
		return parsName;
	}

	void setParsName(String parsName) {
		this.parsName = parsName;
	}

	public String getParsDesp() {
		return parsDesp;
	}

	void setParsDesp(String parsDesp) {
		this.parsDesp = parsDesp;
	}

	public String getUid() {
		return uid;
	}

	void setUid(String uid) {
		this.uid = uid;
	}

	public long getObjectCreateTime() {
		return objectCreateTime;
	}

	void setObjectCreateTime(long objectCreateTime) {
		this.objectCreateTime = objectCreateTime;
	}

	public long getObjectUpdateTime() {
		return objectUpdateTime;
	}

	void setObjectUpdateTime(long objectUpdateTime) {
		this.objectUpdateTime = objectUpdateTime;
	}

	public String getParsUid() {
		return parsUid;
	}

	void setParsUid(String parsUid) {
		this.parsUid = parsUid;
	}

	public boolean isAssignPart() {
		return assignPart;
	}

	void setAssignPart(boolean assignPart) {
		this.assignPart = assignPart;
	}

	public String getPartUid() {
		return partUid;
	}

	void setPartUid(String partUid) {
		this.partUid = partUid;
	}

	public String getPartPin() {
		return partPin;
	}

	void setPartPin(String partPin) {
		this.partPin = partPin;
	}

	public double getPartReqQty() {
		return partReqQty;
	}

	void setPartReqQty(double partReqQty) {
		this.partReqQty = partReqQty;
	}

	public String getPartName() {
		return partName;
	}

	void setPartName(String partName) {
		this.partName = partName;
	}
	
	// -------------------------------------------------------------------------------
	private BizObjLoader<PartAcqInfo> paLoader = BizObjLoader.PART_ACQ.get();
	
	public PartAcqInfo getPa(boolean _reload) {
		return paLoader.getObj(getPaUid(),_reload );
	}
	
	public PartCfgConjInfo getPartCfgConj(String _partCfgUid, boolean _reload) {
		PartAcqInfo pa = getPa(_reload);
		return pa == null ? null : pa.getPartCfgConj(_partCfgUid, _reload);
	}
	
	// -------------------------------------------------------------------------------
	public List<PpartSkewer> getParentList(String _partCfgId) {
		QueryOperation<PpartSkewerQueryParam, PpartSkewer> param = new QueryOperation<>();
		Map<PpartSkewerQueryParam, QueryValue[]> existsQvMap = new HashMap<>();
		param.appendCondition(QueryOperation.value(PpartSkewerQueryParam.P_UID, CompareOp.equal, getpUid()));
		log.debug("getpUid(): {}", getpUid());
		param.appendCondition(
				QueryOperation.value(PpartSkewerQueryParam.B_OF_PC$_PARENT_PART_EXISTS, CompareOp.equal, true));
		existsQvMap.put(PpartSkewerQueryParam.B_OF_PC$_PARENT_PART_EXISTS, new QueryValue[] { //
				QueryOperation.value(PartCfgQueryParam.ID, CompareOp.equal, _partCfgId), //
		});
		log.debug("_partCfgId: {}", _partCfgId);
		param = DataServiceFactory.getInstance().getService(MbomDataService.class).searchPpartSkewer(param,
				existsQvMap);
		log.debug("param.getTotal(): {}", param.getTotal());
		List<PpartSkewer> parentList = param.getQueryResult();
		return parentList;
	}

	public boolean isRoot(PartCfgInfo _partCfg) {
		return _partCfg.getRootPartUid().equals(getpUid());
	}

	public boolean isOrphan(PartCfgInfo _partCfg) {
		return !isRoot(_partCfg) && getParentList(_partCfg.getId()).size() <= 0;
	}
}
