package ekp.data.service.invt;

import ekp.invt.type.InvtOrderType;
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
	
	// -------------------------------------------------------------------------------
	MaterialBinStockBatchInfo getMbsb();
	
	InvtOrderItemInfo getIoi();
	
	default InvtOrderType getIoiIoType() {
		return getIoi()==null?InvtOrderType.UNDEFINED:getIoi().getIoType();
	}

}