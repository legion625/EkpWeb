package ekp.data.service.invt;

import java.util.List;
import java.util.stream.Collectors;

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
	
	default List<MbsbStmtInfo> getMbsbStmtList(boolean _reload) {
		return getMbsbList(_reload).stream().flatMap(mbsb -> mbsb.getStmtList(_reload).stream())
				.collect(Collectors.toList());
	}
	
	
	

}