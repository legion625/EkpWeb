package ekp.data.service.mbom;

import legion.ObjectModelInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.mbom.type.PartAcqStatus;
import ekp.mbom.type.PartAcquisitionType;

public interface PartAcqInfo extends ObjectModelInfo {

	String getPartUid();

	String getPartPin();

	PartAcqStatus getStatus();
	
	String getId();

	String getName();

	PartAcquisitionType getType();
	
	boolean isMmAssigned();

	String getMmUid();

	String getMmMano();
	
	long getPublishTime();
	
	double getRefUnitCost();
	
	
	
	// -------------------------------------------------------------------------------
	default String getStatusName() {
		return (getStatus() == null ? PartAcqStatus.UNDEFINED : getStatus()).getName();
	}
	
	default String getTypeName() {
		return (getType() == null ? PartAcquisitionType.UNDEFINED : getType()).getName();
	}
	
	// -------------------------------------------------------------------------------
	PartAcqInfo reload();
	
	PartInfo getPart(boolean _reload);
	
	List<ParsInfo> getParsList(boolean _reload);

	default List<ParsInfo> getParsList(){
		return getParsList(false);
	}
	
	List<PartCfgConjInfo> getPartCfgConjList(boolean _reload);

	default PartCfgConjInfo getPartCfgConj(String _partCfgUid, boolean _reload) {
		return getPartCfgConjList(_reload).stream().filter(pcc -> pcc.getPartCfgUid().equals(_partCfgUid)).findAny()
				.orElse(null);
	}
	
	default List<PartCfgInfo> getPartCfgList(boolean _reload) {
		return getPartCfgConjList(_reload).stream().map(PartCfgConjInfo::getPartCfg).collect(Collectors.toList());
	}
	
	default List<PpartInfo> getPpartList(){
		return getParsList().stream().flatMap(pars -> pars.getPpartList().stream()).collect(Collectors.toList());
	}

	default List<PartAcqInfo> getChildrenList(PartCfgInfo _partCfg) {
		return getPpartList().stream().map(ppart -> ppart.getPart().getPa(_partCfg)).filter(ppart -> ppart != null)
				.collect(Collectors.toList());
	}
	
	// -------------------------------------------------------------------------------
	MaterialMasterInfo getMm();
}