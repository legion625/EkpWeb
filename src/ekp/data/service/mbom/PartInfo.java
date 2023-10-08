package ekp.data.service.mbom;

import static legion.util.query.QueryOperation.CompareOp.equal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.logging.Log;

import ekp.DebugLogMark;
import ekp.data.MbomDataService;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.data.service.mbom.query.PpartSkewerQueryParam;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartUnit;
import legion.DataServiceFactory;
import legion.ObjectModelInfo;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.CompareOp;
import legion.util.query.QueryOperation.QueryValue;

public interface PartInfo extends ObjectModelInfo {

	String getPin();

	String getName();

	PartUnit getUnit();

//	boolean isMmAssigned();
//
//	String getMmUid();
//
//	String getMmMano();

	// -------------------------------------------------------------------------------
	default String getUnitName() {
		return (getUnit() == null ? PartUnit.UNDEFINED : getUnit()).getName();
	}

	PartInfo reload();

	// -------------------------------------------------------------------------------
	List<PartAcqInfo> getPaList(boolean _reload);
	
	default List<PartAcqInfo> getPaList(PartAcquisitionType _paType) {
		return getPaList(false).stream().filter(pa -> pa.getType() == _paType).collect(Collectors.toList());
	}
	
	default List<PartCfgInfo> getReferencedPartCfgList(boolean _reload){
		return getPaList(_reload).stream().flatMap(pa->pa.getPartCfgList(_reload).stream()).distinct().collect(Collectors.toList());
	}

	List<PpartInfo> getPpartList(boolean _reload); // 被引用到的地方
	
	List<PartCfgInfo> getRootPartCfgList(boolean _reload);
	
	default PartAcqInfo getPa(String _partCfgUid) {
		return getPa(_partCfgUid, false);
	} 

	default PartAcqInfo getPa(String _partCfgUid, boolean _reload) {
		return getPaList(_reload).stream().filter(pa -> pa.getPartCfgConj(_partCfgUid, _reload) != null).findAny()
				.orElse(null);
	}
	
	default PartAcqInfo getPa(PartCfgInfo _partCfg) {
		return getPa(_partCfg, false);
	} 
	
	default PartAcqInfo getPa(PartCfgInfo _partCfg, boolean _reload) {
		return  getPaList(_reload).stream().filter(pa -> pa.getPartCfgList(_reload).contains(_partCfg)).findAny()
				.orElse(null);
	}
	
	// 指定構型的下階ppart
	default List<PpartInfo> getPpartChildren(PartCfgInfo _partCfg){
		Logger log = LoggerFactory.getLogger(DebugLogMark.class);
		PartAcqInfo thisPa  = getPa(_partCfg);
		if(thisPa==null)
			return new ArrayList<>();
		
		log.debug("thisPa: {}\t{}\t{}", thisPa.getUid(), thisPa.getId(), thisPa.getName());
		List<PpartInfo> ppartList = thisPa.getParsList().stream().flatMap(pars -> pars.getPpartList().stream())
				.collect(Collectors.toList());
		return ppartList;
	}
	
	default PpartInfo getSrcPpart(PartCfgInfo _partCfg) {
		Logger log = LoggerFactory.getLogger(DebugLogMark.class);
		QueryOperation<PpartSkewerQueryParam, PpartSkewer> param = new QueryOperation<>();
		Map<PpartSkewerQueryParam, QueryValue[]> existsQvMap = new HashMap<>();
//		partUid
		param.appendCondition(QueryOperation.value(PpartSkewerQueryParam.PART_UID, CompareOp.equal, getUid()));
		param.appendCondition(QueryOperation.value(PpartSkewerQueryParam.B_OF_PC$_PA_EXISTS, CompareOp.equal, true));
		existsQvMap.put(PpartSkewerQueryParam.B_OF_PC$_PA_EXISTS,
				new QueryValue[] { QueryOperation.value(PartCfgQueryParam.ID, equal, _partCfg.getId()) });
//		param.appendCondition(QueryOperation.value(PpartSkewerQueryParam.B_OF_PC$_PARENT_PART_EXISTS, CompareOp.equal, getUid()));
		param = DataServiceFactory.getInstance().getService(MbomDataService.class).searchPpartSkewer(param,
				existsQvMap);
		log.debug("param.getQueryResult().size(): {}", param.getQueryResult().size());
		if(param.getQueryResult().size()!=1)
			return null;
		return param.getQueryResult().get(0).getPpart();
	}
	
//	// -------------------------------------------------------------------------------
//	MaterialMasterInfo getMm();
}