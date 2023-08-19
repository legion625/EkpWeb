package ekp.data.service.invt;

import ekp.invt.type.InvtOrderType;
import legion.ObjectModelInfo;

public interface InvtOrderItemInfo extends ObjectModelInfo{

	String getIoUid();

	public String getMmUid();

	public String getMiUid();

	public String getWrhsBinUid();

	InvtOrderType getIoType();

	double getOrderQty();

	double getOrderValue();

}