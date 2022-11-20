package ekp.data.service.mbom;

import legion.ObjectModelInfo;

public interface PpartInfo extends ObjectModelInfo {

	String getParsUid();

	boolean isAssignPart();

	String getPartUid();

	String getPartPin();

	double getPartReqQty();

	// -------------------------------------------------------------------------------
	PpartInfo reload();
}