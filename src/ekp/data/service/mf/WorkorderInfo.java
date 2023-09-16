package ekp.data.service.mf;

import java.util.List;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.mf.type.WorkorderStatus;
import legion.ObjectModelInfo;

public interface WorkorderInfo extends ObjectModelInfo {

	String getWoNo();

	WorkorderStatus getStatus();

	String getPartUid();

	String getPartPin();

	String getPartAcqUid();

	String getPartAcqId();
	
	String getPartAcqMmMano();
	String getPartCfgUid() ;
	String getPartCfgId();

	double getRqQty();

	long getStartWorkTime();

	long getFinishWorkTime();

	long getOverTime();

	// -------------------------------------------------------------------------------
	WorkorderInfo reload();

	// -------------------------------------------------------------------------------
	default String getStatusName() {
		return (getStatus() == null ? WorkorderStatus.UNDEFINED : getStatus()).getName();
	}

	// -------------------------------------------------------------------------------
	PartInfo getPart();
	
	PartAcqInfo getPartAcq();
	
	List<WorkorderMaterialInfo> getWomList();

	MaterialInstInfo getPartMi();

}