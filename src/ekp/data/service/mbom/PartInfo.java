package ekp.data.service.mbom;

import java.util.List;

import ekp.mbom.type.PartUnit;
import legion.ObjectModelInfo;

public interface PartInfo extends ObjectModelInfo{

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
	
	List<PpartInfo> getPpartList(boolean _reload);
	
	List<PartCfgInfo> getPartCfgList(boolean _reload);
}