package ekp.data.service.invt;

import java.util.List;

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
	
	List<MaterialBinStockBatchInfo> getMbsbList(boolean _reload);
	default List<MaterialBinStockBatchInfo> getMbsbList(){
		return getMbsbList(false);
	}
	
	
	

}