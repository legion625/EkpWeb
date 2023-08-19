package ekp.data.service.invt;

import legion.ObjectModelInfo;

public interface MaterialBinStockInfo extends ObjectModelInfo{

	String getMmUid();

	String getMano();

	String getWrhsBinUid();

	double getSumStockQty();

	double getSumStockValue();
	
	// -------------------------------------------------------------------------------
	MaterialMasterInfo getMm();
	
	WrhsBinInfo getWrhsBin();

	default String getWrhsLocId() {
		return getWrhsBin().getWrhsLoc().getId();
	}
	default String getWrhsLocName() {
		return getWrhsBin().getWrhsLoc().getName();
	}
	
	default String getWrhsBinId() {
		return getWrhsBin().getId();
	}
	
	default String getWrhsBinName() {
		return getWrhsBin().getName();
	}
	
	

}