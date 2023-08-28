package ekp.data.service.invt;

import ekp.invt.type.InvtOrderType;
import legion.ObjectModelInfo;

public interface InvtOrderItemInfo extends ObjectModelInfo{

	String getIoUid();

	public String getMmUid();

	InvtOrderType getIoType();

	double getOrderQty();

	double getOrderValue();
	
	boolean isMiAssigned();
	
	public String getMiUid();
	
	boolean isWrhsBinAssigned();
	
	public String getWrhsBinUid();
	
	// -------------------------------------------------------------------------------
	InvtOrderItemInfo reload();
	
	// -------------------------------------------------------------------------------
	MaterialInstInfo getMi();

}