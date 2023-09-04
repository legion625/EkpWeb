package ekp.data.service.invt;

import java.util.List;
import java.util.stream.Collectors;

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
	MaterialMasterInfo reload();
	
	// -------------------------------------------------------------------------------
	default String getStdUnitStr() {
		PartUnit u = getStdUnit() == null ? PartUnit.UNDEFINED : getStdUnit();
		return u.getId() + "," + u.getChtName();
	}

	default String getStdUnitChtName() {
		return (getStdUnit() == null ? PartUnit.UNDEFINED : getStdUnit()).getChtName();
	}

	List<MaterialInstInfo> getMiList(boolean _reload);

	default List<MaterialInstInfo> getMiList() {
		return getMiList(false);
	}

	List<MaterialBinStockInfo> getMbsList(boolean _reload);

	default List<MaterialBinStockInfo> getMbsList() {
		return getMbsList(false);
	}
	
	default List<MaterialBinStockBatchInfo> getMbsbList() {
		return getMbsList().stream().flatMap(mbs -> mbs.getMbsbList().stream()).collect(Collectors.toList());
	}

}