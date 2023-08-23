package ekp.data.service.invt;

import java.util.List;

import ekp.invt.type.InvtOrderStatus;
import legion.ObjectModelInfo;

public interface InvtOrderInfo extends ObjectModelInfo {

	String getIosn();
	
	InvtOrderStatus getStatus();

	String getApplierId();

	String getApplierName();

	long getApplyTime();

	String getRemark();

	long getApvTime();
	
	// -------------------------------------------------------------------------------
	InvtOrderInfo reload();
	
	List<InvtOrderItemInfo> getIoiList();
}