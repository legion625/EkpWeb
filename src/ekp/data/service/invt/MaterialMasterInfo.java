package ekp.data.service.invt;

import java.util.List;
import java.util.stream.Collectors;

import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.SalesOrderItemInfo;
import ekp.invt.type.InvtOrderType;
import ekp.mbom.type.PartAcquisitionType;
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

	default double getAvgStockValue() {
		return getSumStockValue() / getSumStockQty();
	}

	List<InvtOrderItemInfo> getIoiList();

	default List<InvtOrderItemInfo> getIoiList(InvtOrderType _ioType) {
		return getIoiList().stream().filter(ioi -> ioi.getIoType() == _ioType).collect(Collectors.toList());
	}

	default double getIoiAvgOrderValue(InvtOrderType _ioType) {
		List<InvtOrderItemInfo> ioiList = getIoiList(_ioType);
		double sumOrderQty = 0, sumOrderValue = 0;
		for (InvtOrderItemInfo ioi : ioiList) {
			sumOrderQty += ioi.getOrderQty();
			sumOrderValue += ioi.getOrderValue();
		}
		return sumOrderValue / sumOrderQty;
	}
	
	List<SalesOrderItemInfo> getSoiList();
	
	default double getSoiAvgValue() {
		List<SalesOrderItemInfo> soiList = getSoiList();
		double sumQty = 0, sumValue = 0;
		for (SalesOrderItemInfo soi : soiList) {
			sumQty += soi.getQty();
			sumValue += soi.getValue();
		}
		return sumValue / sumQty;
	}
	
	List<PurchItemInfo> getPiList();

	List<PartAcqInfo> getPaList();

	default List<PartAcqInfo> getPaList(PartAcquisitionType _paType) {
		return getPaList().stream().filter(pa -> pa.getType() == _paType).collect(Collectors.toList());
	}

}