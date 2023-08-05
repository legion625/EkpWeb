package ekp.data.service.pu;

import java.util.List;

import legion.ObjectModelInfo;

public interface PurchInfo extends ObjectModelInfo {

	String getPuNo();

	String getTitle();

	String getSupplierName();

	String getSupplierBan();
	
	// -------------------------------------------------------------------------------
	List<PurchItemInfo> getPurchItemList();

}