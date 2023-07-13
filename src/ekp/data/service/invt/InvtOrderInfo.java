package ekp.data.service.invt;

import legion.ObjectModelInfo;

public interface InvtOrderInfo extends ObjectModelInfo {

	String getIosn();

	String getApplierId();

	String getApplierName();

	long getApvTime();

	String getRemark();

}