package ekp.data.service.invt;

import java.util.List;

import ekp.invt.type.InvtOrderType;
import legion.ObjectModelInfo;

public interface InvtOrderItemInfo extends ObjectModelInfo{

	String getIoUid();

	public String getMmUid();

	InvtOrderType getIoType();

	double getOrderQty();

	double getOrderValue();
	
	boolean isMbsbStmtCreated();
	
	// -------------------------------------------------------------------------------
	default String getIoTypeName() {
		return (getIoType() == null ? InvtOrderType.UNDEFINED : getIoType()).getName();
	}
	
	// -------------------------------------------------------------------------------
	InvtOrderItemInfo reload();
	
	// -------------------------------------------------------------------------------
	List<MbsbStmtInfo> getMbsbStmtList();
	
	

}