package ekp.data.service.invt;

import ekp.invt.type.MbsbFlowType;
import ekp.invt.type.PostingStatus;
import legion.ObjectModelInfo;

public interface MbsbStmtInfo extends ObjectModelInfo{

	String getMbsbUid();

	String getIoiUid();

	MbsbFlowType getMbsbFlowType();

	double getStmtQty();

	double getStmtValue();

	PostingStatus getPostingStatus();

	long getPostingTime();

}