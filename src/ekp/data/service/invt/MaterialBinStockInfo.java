package ekp.data.service.invt;

import legion.ObjectModelInfo;

public interface MaterialBinStockInfo extends ObjectModelInfo{

	String getMmUid();

	String getMano();

	String getWrhsBinUid();

	double getSumStockQty();

	double getSumStockValue();

}