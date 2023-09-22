package ekp.data.service.mbom;

import legion.ObjectModelInfo;

public interface ProdCtlPartCfgConjInfo extends ObjectModelInfo{

	String getProdCtlUid();

	String getPartCfgUid();
	
	String getPartAcqUid();
	
	// -------------------------------------------------------------------------------
	PartCfgInfo getPartCfg();

	PartAcqInfo getPartAcq();
}