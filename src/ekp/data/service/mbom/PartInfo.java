package ekp.data.service.mbom;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.mbom.type.PartUnit;
import legion.ObjectModelInfo;

public interface PartInfo extends ObjectModelInfo {

	String getPin();

	String getName();

	PartUnit getUnit();

	// -------------------------------------------------------------------------------
	default String getUnitName() {
		return (getUnit() == null ? PartUnit.UNDEFINED : getUnit()).getName();
	}

	PartInfo reload();

	// -------------------------------------------------------------------------------
	List<PartAcqInfo> getPaList(boolean _reload);
	
	default List<PartCfgInfo> getReferencedPartCfgList(boolean _reload){
		return getPaList(_reload).stream().flatMap(pa->pa.getPartCfgList(_reload).stream()).distinct().collect(Collectors.toList());
	}

	List<PpartInfo> getPpartList(boolean _reload); // 被引用到的地方
	
	List<PartCfgInfo> getRootPartCfgList(boolean _reload);
	
	default PartAcqInfo getPa(PartCfgInfo _partCfg) {
		return getPa(_partCfg, false);
	} 
	
	default PartAcqInfo getPa(PartCfgInfo _partCfg, boolean _reload) {
		return  getPaList(_reload).stream().filter(pa -> pa.getPartCfgList(_reload).contains(_partCfg)).findAny()
				.orElse(null);
	}
	
	// 指定構型的下階ppart
	default List<PpartInfo> getPpartChildren(PartCfgInfo _partCfg){
//		getPartCfgList(false)
		Logger log = LoggerFactory.getLogger(DebugLogMark.class);
//		PartAcqInfo thisPa = getPaList(false).stream().filter(pa -> pa.getPartCfgList(false).contains(_partCfg)).findAny()
//				.orElse(null);
		PartAcqInfo thisPa  = getPa(_partCfg);
		if(thisPa==null)
			return new ArrayList<>();
		
		log.debug("thisPa: {}\t{}\t{}", thisPa.getUid(), thisPa.getId(), thisPa.getName());
		List<PpartInfo> ppartList = thisPa.getParsList().stream().flatMap(pars -> pars.getPpartList().stream())
				.collect(Collectors.toList());
		return ppartList;
	}
}