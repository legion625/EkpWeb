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
	InvtOrderInfo reload();
	
	List<InvtOrderItemInfo> getIoiList();
	
	default List<MbsbStmtInfo> getMbsbStmtList(){
		return getIoiList().stream().flatMap(ioi->ioi.getMbsbStmtList().stream()).collect(Collectors.toList());
	}
}