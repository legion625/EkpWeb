package ekp.data.service.invt;

import java.util.List;

import legion.ObjectModelInfo;

public interface MaterialBinStockBatchInfo extends ObjectModelInfo{

	String getMbsUid();

	String getMiUid();

	double getStockQty();

	double getStockValue();
	
	// -------------------------------------------------------------------------------
	MaterialBinStockInfo getMbs(boolean _reload);
	default MaterialBinStockInfo getMbs() {
		return getMbs(false);
	}
	
	MaterialInstInfo getMi(boolean _reload);
	default MaterialInstInfo getMi() {
		return getMi(false);
	}
	
	List<MbsbStmtInfo> getStmtList(boolean _reload);
	default List<MbsbStmtInfo> getStmtList(){
		return getStmtList(false);
	}

}