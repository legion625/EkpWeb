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

	// -------------------------------------------------------------------------------
	ParsInfo getPars();

	// -------------------------------------------------------------------------------
	PartInfo getPart();

	default String getPartName() {
		return isAssignPart() ? getPart().getName() : "(unassigned)";
	}
	
}