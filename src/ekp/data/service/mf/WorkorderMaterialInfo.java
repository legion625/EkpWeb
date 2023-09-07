package ekp.data.service.mf;

import java.util.List;
import java.util.stream.Collectors;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.MbsbStmtInfo;
import legion.ObjectModelInfo;

public interface WorkorderMaterialInfo extends ObjectModelInfo{

	String getWoUid();

	String getWoNo();

	String getMmUid();

	String getMmMano();

	String getMmName();

	double getQty0();

	double getQty1();
	
	// -------------------------------------------------------------------------------
	WorkorderMaterialInfo reload();
	
	// -------------------------------------------------------------------------------
	MaterialMasterInfo getMm();
	
	// -------------------------------------------------------------------------------
	List<InvtOrderItemInfo> getQty1IoiList();
	
//	default List<MaterialInstInfo> getQty1MiList() {
//		return getQty1IoiList().stream().flatMap(ioi -> ioi.getMbsbStmtList().stream()).map(s -> s.getMbsb().getMi())
//				.collect(Collectors.toList());
//	}
	
	default List<MbsbStmtInfo> getQty1MbsbStmtList() {
		return getQty1IoiList().stream().flatMap(ioi -> ioi.getMbsbStmtList().stream()).collect(Collectors.toList());
	}
	
	

}