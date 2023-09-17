package ekp.data.service.mbom;

import java.util.List;

import legion.ObjectModelInfo;

public interface ProdModInfo extends ObjectModelInfo{

	String getProdUid();

	String getId();

	String getName();

	String getDesp();
	
	// -------------------------------------------------------------------------------
	ProdModInfo reload();
	
	ProdInfo getProd();
	
	List<ProdModItemInfo> getProdModItemList();

}