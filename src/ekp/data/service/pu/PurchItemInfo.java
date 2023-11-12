package ekp.data.service.pu;

import java.util.List;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartUnit;
import legion.ObjectModelInfo;

public interface PurchItemInfo extends ObjectModelInfo {

	String getPurchUid();

	String getMmUid();

	String getMmMano();

	String getMmName();

	String getMmSpecification();

	PartUnit getMmStdUnit();

	boolean isRefPa();

	String getRefPaUid();

	PartAcquisitionType getRefPaType();

	double getQty();

	double getValue();

	String getRemark();

	// -------------------------------------------------------------------------------
	PurchInfo getPurch();

	PartAcqInfo getRefPa();

	List<InvtOrderItemInfo> getIoiListIoType21();
	
	/** 取得所有供料供外的Ioi帳值。 */
	double getIoType21Value();

	
	List<MaterialInstInfo> getMiList();
	
}