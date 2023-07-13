package ekp.data.service.invt;

import ekp.mbom.type.PartUnit;
import legion.ObjectModelInfo;

public interface MaterialMasterInfo extends ObjectModelInfo{

	String getMano();

	String getName();

	String getSpecification();

	PartUnit getStdUnit();

	double getSumStockQty();

	double getSumStockValue();

}