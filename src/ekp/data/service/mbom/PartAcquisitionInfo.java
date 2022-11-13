package ekp.data.service.mbom;

import legion.ObjectModelInfo;
import ekp.mbom.type.PartAcquisitionType;

public interface PartAcquisitionInfo extends ObjectModelInfo {

	String getPartUid();

	String getPartPin();

	String getId();

	String getName();

	PartAcquisitionType getType();
	
	// -------------------------------------------------------------------------------
	default String getTypeName() {
		return (getType() == null ? PartAcquisitionType.UNDEFINED : getType()).getName();
	}

}