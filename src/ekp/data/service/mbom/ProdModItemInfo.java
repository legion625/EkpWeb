package ekp.data.service.mbom;

import legion.ObjectModelInfo;

public interface ProdModItemInfo extends ObjectModelInfo{

	String getProdModUid();

	String getProdCtlUid();

	boolean isPartCfgAssigned();

	String getPartCfgUid();

}