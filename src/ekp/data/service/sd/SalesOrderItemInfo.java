package ekp.data.service.sd;

import java.util.List;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import legion.ObjectModelInfo;

public interface SalesOrderItemInfo extends ObjectModelInfo{

	String getSoUid();

	String getMmUid();

	String getMmMano();

	String getMmName();

	String getMmSpec();

	double getQty();

	double getValue();

	boolean isAllDelivered();

	long getFinishDeliveredDate();
	
	// -------------------------------------------------------------------------------
	SalesOrderItemInfo reload();
	SalesOrderInfo getSo();
	
	MaterialMasterInfo getMm();
	
	List<InvtOrderItemInfo> getIoiList();
	
	default double getSumIoiOrderQty() {
		return getIoiList().stream().mapToDouble(InvtOrderItemInfo::getOrderQty).sum();
	}
	default double getSumIoiOrderValue() {
		return getIoiList().stream().mapToDouble(InvtOrderItemInfo::getOrderValue).sum();
	}

}