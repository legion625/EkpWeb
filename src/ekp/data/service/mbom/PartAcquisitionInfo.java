package ekp.data.service.mbom;

import ekp.ObjectModelInfo;
import ekp.mbom.type.PartAcquisitionType;

public interface PartAcquisitionInfo extends ObjectModelInfo {

	String getPartUid();

	String getPartPin();

	String getId();

	String getName();

	PartAcquisitionType getType();

}