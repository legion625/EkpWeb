package ekp.data.service.invt;

import java.util.List;

import ekp.mbom.type.PartUnit;
import legion.ObjectModelInfo;

public interface MaterialMasterInfo extends ObjectModelInfo{

	String getMano();

	String getName();

	String getSpecification();

	PartUnit getStdUnit();

	double getSumStockQty();

	double getSumStockValue();
	
	// -------------------------------------------------------------------------------
	default String getStdUnitChtName() {
		return (getStdUnit()==null?PartUnit.UNDEFINED:getStdUnit()).getChtName();
	}
	
	List<MaterialInstInfo> getMiList(boolean _reload);
	default 	List<MaterialInstInfo> getMiList(){
		return getMiList(false);
	}

}