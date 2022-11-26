package ekp.data.service.mbom;

import legion.ObjectModelInfo;

import java.util.List;
import java.util.stream.Collectors;

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
	
	List<ParsInfo> getParsList(boolean _reload);

	default List<ParsInfo> getParsList(){
		return getParsList(false);
	}
	
	List<PartCfgConjInfo> getPartCfgConjList(boolean _reload);

	default List<PartCfgInfo> getPartCfgList(boolean _reload) {
		return getPartCfgConjList(_reload).stream().map(PartCfgConjInfo::getPartCfg).collect(Collectors.toList());
	}
}