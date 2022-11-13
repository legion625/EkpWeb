package ekp.data.service.mbom;

import legion.ObjectModelInfo;

public interface ProdCtlInfo extends ObjectModelInfo{

	String getId();

	int getLv();

	String getName();

	boolean isReq();

	String getParentUid();

	String getParentId();

	String getProdUid();

}