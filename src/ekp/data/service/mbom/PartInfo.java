package ekp.data.service.mbom;

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

	List<PpartInfo> getPpartList(boolean _reload); // 被引用到的地方

	List<PartCfgInfo> getPartCfgList(boolean _reload);
	
	// 指定構型的下階ppart
	default List<PpartInfo> getPpartChildren(PartCfgInfo _partCfg){
//		getPartCfgList(false)
		Logger log = LoggerFactory.getLogger(DebugLogMark.class);
		PartAcqInfo thisPa = getPaList(false).stream().filter(pa -> pa.getPartCfgList(false).contains(_partCfg)).findAny()
				.orElse(null);
		log.debug("pa: {}\t{}\t{}", thisPa.getUid(), thisPa.getId(), thisPa.getName());
		List<PpartInfo> ppartList = thisPa.getParsList().stream().flatMap(pars -> pars.getPpartList().stream())
				.collect(Collectors.toList());
		return ppartList;
	}
}