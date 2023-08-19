package ekp.data.service.invt;

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
}