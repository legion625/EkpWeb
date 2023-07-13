package ekp.data.service.invt;

import ekp.invt.type.InvtOrderType;
import legion.ObjectModelInfo;

public interface InvtOrderItemInfo extends ObjectModelInfo{

	String getIoUid();

	String getMbsUid();

	InvtOrderType getIoType();

	double getOrderQty();

	double getOrderValue();

}