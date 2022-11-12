package ekp.data.service.mbom;

import ekp.ObjectModelInfo;

public interface ProdModItemInfo extends ObjectModelInfo{

	String getProdModUid();

	String getProdCtlUid();

	boolean isPartCfgAssigned();

	String getPartCfgUid();

}