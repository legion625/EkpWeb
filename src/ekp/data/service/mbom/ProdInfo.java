package ekp.data.service.mbom;

import java.util.List;

import legion.ObjectModelInfo;

public interface ProdInfo extends ObjectModelInfo{

	String getId();

	String getName();
	
	// -------------------------------------------------------------------------------
	ProdInfo reload();
	
	List<ProdCtlInfo> getProdCtlListLv1();
	
	List<ProdModInfo> getProdModList();
	
	
	
}