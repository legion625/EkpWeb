package ekp.data.service.mbom;

import legion.ObjectModelInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.mbom.type.PartAcquisitionType;

public interface PartAcqInfo extends ObjectModelInfo {

	String getPartUid();

	String getPartPin();

	String getId();

	String getName();

	PartAcquisitionType getType();
	
	// -------------------------------------------------------------------------------
	default String getTypeName() {
		return (getType() == null ? PartAcquisitionType.UNDEFINED : getType()).getName();
	}
	
	// -------------------------------------------------------------------------------
	PartInfo getPart(boolean _reload);
	
	List<ParsInfo> getParsList(boolean _reload);

	default List<ParsInfo> getParsList(){
		return getParsList(false);
	}
	
	List<PartCfgConjInfo> getPartCfgConjList(boolean _reload);

	default List<PartCfgInfo> getPartCfgList(boolean _reload) {
		return getPartCfgConjList(_reload).stream().map(PartCfgConjInfo::getPartCfg).collect(Collectors.toList());
	}
	
	default List<PpartInfo> getPpartList(){
		return getParsList().stream().flatMap(pars -> pars.getPpartList().stream()).collect(Collectors.toList());
	}
	
	@Deprecated
	default List<PartAcqInfo> getChildrenList(PartCfgInfo _partCfg){
//		getPartCfgList(false)
//		Logger log = LoggerFactory.getLogger(DebugLogMark.class);
//		PartAcqInfo thisPa = getPaList(false).stream().filter(pa -> pa.getPartCfgList(false).contains(_partCfg)).findAny()
//				.orElse(null);
//		PartAcqInfo thisPa  = getPa(_partCfg);
//		if(thisPa==null)
//			return new ArrayList<>();
//		
//		log.debug("thisPa: {}\t{}\t{}", thisPa.getUid(), thisPa.getId(), thisPa.getName());
//		return getParsList().stream().flatMap(pars -> pars.getPpartList().stream())
//				.map(ppart->ppart.getPart().getPa(_partCfg)).filter(ppart->ppart!=null).collect(Collectors.toList());
		return getPpartList().stream().map(ppart -> ppart.getPart().getPa(_partCfg)).filter(ppart -> ppart != null)
				.collect(Collectors.toList());
//		return ppartList;
	}
}