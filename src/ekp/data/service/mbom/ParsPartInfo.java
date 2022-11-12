package ekp.data.service.mbom;

import ekp.ObjectModelInfo;

public interface ParsPartInfo extends ObjectModelInfo {

	String getParsUid();

	boolean isAssignPart();

	String getPartUid();

	String getPartPin();

	double getPartReqQty();

}