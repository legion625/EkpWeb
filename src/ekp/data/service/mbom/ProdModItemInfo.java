package ekp.data.service.mbom;

import legion.ObjectModelInfo;

public interface ProdModItemInfo extends ObjectModelInfo{

	String getProdModUid();

	String getProdCtlUid();

	boolean isPartAcqCfgAssigned();

	String getPartCfgUid();
	
	String getPartAcqUid();
	
	// -------------------------------------------------------------------------------
	ProdModItemInfo reload();
	
	ProdCtlInfo getProdCtl();
	PartCfgInfo getPartCfg();
	PartAcqInfo getPartAcq();

}