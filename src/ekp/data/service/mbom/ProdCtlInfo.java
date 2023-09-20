package ekp.data.service.mbom;

import java.util.List;

import legion.ObjectModelInfo;

public interface ProdCtlInfo extends ObjectModelInfo {

	int getLv();

	String getName();

	boolean isReq();

	String getParentUid();

	String getProdUid();

	// -------------------------------------------------------------------------------
	ProdCtlInfo reload();

	List<ProdCtlInfo> getChildrenList();

	ProdInfo getProd();

	List<ProdCtlPartCfgConjInfo> getPcpccList();

	// -------------------------------------------------------------------------------

}