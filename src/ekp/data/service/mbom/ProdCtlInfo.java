package ekp.data.service.mbom;

import java.util.List;

import legion.ObjectModelInfo;

public interface ProdCtlInfo extends ObjectModelInfo{

//	String getId();

	int getLv();

//	String getName();
	
	String getPartUid();
	String getPartPin();
	String getPartName();

	boolean isReq();

	String getParentUid();

	@Deprecated
	String getParentId();

	String getProdUid();
	
	// -------------------------------------------------------------------------------
	ProdCtlInfo reload();
	
	PartInfo getPart();
	
	List<ProdCtlInfo> getChildrenList();
	
	ProdInfo getProd();
	
	List<ProdCtlPartCfgConjInfo> getPcpccList();
	
	// -------------------------------------------------------------------------------

}