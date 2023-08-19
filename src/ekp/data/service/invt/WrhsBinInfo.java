package ekp.data.service.invt;

import java.util.List;

import legion.ObjectModelInfo;

public interface WrhsBinInfo extends ObjectModelInfo{

	String getWlUid();

	String getId();

	String getName();
	
	// -------------------------------------------------------------------------------
	WrhsLocInfo getWrhsLoc();
	
	List<MaterialBinStockInfo> getMbsList();

}