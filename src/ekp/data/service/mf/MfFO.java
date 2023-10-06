package ekp.data.service.mf;

import ekp.serviceFacade.rmi.mf.WorkorderCreateObjRemote;
import ekp.serviceFacade.rmi.mf.WorkorderMaterialCreateObjRemote;
import ekp.serviceFacade.rmi.mf.WorkorderMaterialRemote;
import ekp.serviceFacade.rmi.mf.WorkorderRemote;

public class MfFO {

	// -------------------------------------------------------------------------------
	// -----------------------------------Workorder-----------------------------------
	public static WorkorderInfo parseWorkorder(WorkorderRemote _remote) {
		WorkorderInfoDto dto = new WorkorderInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setWoNo(_remote.getWoNo());
		dto.setStatus(_remote.getStatus());
		dto.setPartUid(_remote.getPartUid());
		dto.setPartPin(_remote.getPartPin());
		
		dto.setPartAcqUid(_remote.getPartAcqUid());
		dto.setPartAcqId(_remote.getPartAcqId());
		dto.setPartAcqMmMano(_remote.getPartAcqMmMano());
		dto.setPartCfgUid(_remote.getPartCfgUid());
		dto.setPartCfgId(_remote.getPartCfgId());
		dto.setRqQty(_remote.getRqQty());
		dto.setStartWorkTime(_remote.getStartWorkTime());
		dto.setFinishWorkTime(_remote.getFinishWorkTime());
		dto.setOverTime(_remote.getOverTime());
		return dto;
	}

	public static WorkorderCreateObjRemote parseWorkorderCreateObjRemote(WorkorderCreateObj _dto) {
		WorkorderCreateObjRemote remote = new WorkorderCreateObjRemote();
		remote.setPartUid(_dto.getPartUid());
		remote.setPartPin(_dto.getPartPin());
		remote.setPartAcqUid(_dto.getPartAcqUid());
		remote.setPartAcqId(_dto.getPartAcqId());
		remote.setPartAcqMmMano(_dto.getPartAcqMmMano());
		remote.setPartCfgUid(_dto.getPartCfgUid());
		remote.setPartCfgId(_dto.getPartCfgId());
		remote.setRqQty(_dto.getRqQty());
		return remote;
	}

	// -------------------------------------------------------------------------------
	// -------------------------------WorkorderMaterial-------------------------------
	public static WorkorderMaterialInfo parseWorkorderMaterial(WorkorderMaterialRemote _remote) {
		WorkorderMaterialInfoDto dto = new WorkorderMaterialInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setWoUid(_remote.getWoUid());
		dto.setWoNo(_remote.getWoNo());
		dto.setMmUid(_remote.getMmUid());
		dto.setMmMano(_remote.getMmMano());
		dto.setMmName(_remote.getMmName());
		dto.setQty0(_remote.getQty0());
		dto.setQty1(_remote.getQty1());
		return dto;
	}
	
	public static WorkorderMaterialCreateObjRemote parseWorkorderMaterialCreateObjRemote(WorkorderMaterialCreateObj _dto) {
		WorkorderMaterialCreateObjRemote remote = new WorkorderMaterialCreateObjRemote();
		remote.setWoUid(_dto.getWoUid());
		remote.setWoNo(_dto.getWoNo());
		remote.setMmUid(_dto.getMmUid());
		remote.setMmMano(_dto.getMmMano());
		remote.setMmName(_dto.getMmName());
		return remote;
	}
	
	

}
