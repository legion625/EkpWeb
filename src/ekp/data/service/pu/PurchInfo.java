package ekp.data.service.pu;

import java.util.List;

import ekp.pu.type.PurchPerfStatus;
import legion.ObjectModelInfo;

public interface PurchInfo extends ObjectModelInfo {

	String getPuNo();

	String getTitle();

	String getSupplierName();

	String getSupplierBan();

	PurchPerfStatus getPerfStatus();

	long getPerfTime();
	
	// -------------------------------------------------------------------------------
	PurchInfo reload();
	
	List<PurchItemInfo> getPurchItemList();

}