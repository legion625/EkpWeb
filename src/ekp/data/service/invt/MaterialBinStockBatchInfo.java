package ekp.data.service.invt;

import legion.ObjectModelInfo;

public interface MaterialBinStockBatchInfo extends ObjectModelInfo{

	String getMbsUid();

	String getMiUid();

	double getStockQty();

	double getStockValue();

}