package ekp.data.service.mbom;

import ekp.mbom.type.PartAcquisitionType;
import legion.ObjectModelInfo;

public interface PartAcquisitionInfo extends ObjectModelInfo {

	String getPartUid();

	String getPartPin();

	String getId();

	String getName();

	PartAcquisitionType getType();

}