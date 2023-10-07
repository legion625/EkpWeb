package ekp.data.service.invt;

import java.util.List;
import java.util.stream.Collectors;

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
	default String getStatusName() {
		return (getStatus() == null ? InvtOrderStatus.UNDEFINED : getStatus()).getName();
	}

	// -------------------------------------------------------------------------------
	InvtOrderInfo reload();
	
	List<InvtOrderItemInfo> getIoiList();
	
	default double getSumIoiOrderValue() {
		return getIoiList().stream().mapToDouble(InvtOrderItemInfo::getOrderValue).sum();
	}
	
	default List<MbsbStmtInfo> getMbsbStmtList(){
		return getIoiList().stream().flatMap(ioi->ioi.getMbsbStmtList().stream()).collect(Collectors.toList());
	}
}