package ekp.data.service.mbom;

import ekp.serviceFacade.rmi.mbom.ParsPartRemote;
import ekp.serviceFacade.rmi.mbom.ParsProcCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.ParsProcRemote;
import ekp.serviceFacade.rmi.mbom.PartAcqRoutingStepCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.PartAcqRoutingStepRemote;
import ekp.serviceFacade.rmi.mbom.PartAcquisitionCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.PartAcquisitionRemote;
import ekp.serviceFacade.rmi.mbom.PartCfgConjRemote;
import ekp.serviceFacade.rmi.mbom.PartCfgCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.PartCfgRemote;
import ekp.serviceFacade.rmi.mbom.PartCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.PartRemote;
import ekp.serviceFacade.rmi.mbom.ProdCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.ProdCtlCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.ProdCtlPartCfgConjRemote;
import ekp.serviceFacade.rmi.mbom.ProdCtlRemote;
import ekp.serviceFacade.rmi.mbom.ProdModCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.ProdModItemRemote;
import ekp.serviceFacade.rmi.mbom.ProdModRemote;
import ekp.serviceFacade.rmi.mbom.ProdRemote;

public class MbomFO {
	// -------------------------------------------------------------------------------
	// -------------------------------------Part--------------------------------------
	public static PartInfo parsePart(PartRemote _remote) {
		PartInfoDto dto = new PartInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setPin(_remote.getPin());
		dto.setName(_remote.getName());
		return dto;
	}

	public static PartCreateObjRemote parsePartCreateObjRemote(PartCreateObj _dto) {
		PartCreateObjRemote remote = new PartCreateObjRemote();
		remote.setPin(_dto.getPin());
		remote.setName(_dto.getName());
		return remote;
	}

	// -------------------------------------------------------------------------------
	// --------------------------------PartAcquisition--------------------------------
	public static PartAcquisitionInfo parsePartAcquisition(PartAcquisitionRemote _remote) {
		PartAcquisitionInfoDto dto = new PartAcquisitionInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setPartUid(_remote.getPartUid());
		dto.setPartPin(_remote.getPartPin());
		dto.setId(_remote.getId());
		dto.setName(_remote.getName());
		dto.setType(_remote.getType());
		return dto;
	}

	public static PartAcquisitionCreateObjRemote parsePartAcquisitionCreateObjRemote(PartAcquisitionCreateObj _dto) {
		PartAcquisitionCreateObjRemote remote = new PartAcquisitionCreateObjRemote();
		remote.setPartUid(_dto.getPartUid());
		remote.setPartPin(_dto.getPartPin());
		remote.setId(_dto.getId());
		remote.setName(_dto.getName());
		remote.setType(_dto.getType());
		return remote;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------PartAcqRoutingStep-------------------------------
	public static PartAcqRoutingStepInfo parsePartAcqRoutingStep(PartAcqRoutingStepRemote _remote) {
		PartAcqRoutingStepInfoDto dto = new PartAcqRoutingStepInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setPartAcqUid(_remote.getPartAcqUid());
		dto.setId(_remote.getId());
		dto.setName(_remote.getName());
		dto.setDesp(_remote.getDesp());
		return dto;
	}

	public static PartAcqRoutingStepCreateObjRemote parsePartAcqRoutingStepCreateObjRemote(PartAcqRoutingStepCreateObj _dto) {
		PartAcqRoutingStepCreateObjRemote remote = new PartAcqRoutingStepCreateObjRemote();
		remote.setPartAcqUid(_dto.getPartAcqUid());
		remote.setId(_dto.getId());
		remote.setName(_dto.getName());
		remote.setDesp(_dto.getDesp());
		return remote;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------ParsProc------------------------------------
	public static ParsProcInfo parseParsProc(ParsProcRemote _remote) {
		ParsProcInfoDto dto = new ParsProcInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setParsUid(_remote.getParsUid());
		dto.setSeq(_remote.getSeq());
		dto.setName(_remote.getName());
		dto.setDesp(_remote.getDesp());
		dto.setAssignProc(_remote.isAssignProc());
		dto.setProcUid(_remote.getProcUid());
		dto.setProcId(_remote.getProcId());
		return dto;
	}

	public static ParsProcCreateObjRemote parseParsProcCreateObjRemote(ParsProcCreateObj _dto) {
		ParsProcCreateObjRemote remote = new ParsProcCreateObjRemote();
		remote.setParsUid(_dto.getParsUid());
		remote.setSeq(_dto.getSeq());
		remote.setName(_dto.getName());
		remote.setDesp(_dto.getDesp());
		return remote;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------ParsPart------------------------------------
	public static ParsPartInfo parseParsPart(ParsPartRemote _remote) {
		ParsPartInfoDto dto = new ParsPartInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setParsUid(_remote.getParsUid());
		dto.setAssignPart(_remote.isAssignPart());
		dto.setPartUid(_remote.getPartUid());
		dto.setPartPin(_remote.getPartPin());
		dto.setPartReqQty(_remote.getPartReqQty());
		return dto;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------PartCfg------------------------------------
	public static PartCfgInfo parsePartCfg(PartCfgRemote _remote) {
		PartCfgInfoDto dto = new PartCfgInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setRootPartUid(_remote.getRootPartUid());
		dto.setRootPartPin(_remote.getRootPartPin());
		dto.setStatus(_remote.getStatus());
		dto.setId(_remote.getId());
		dto.setName(_remote.getName());
		dto.setDesp(_remote.getDesp());
		return dto;
	}

	public static PartCfgCreateObjRemote parsePartCfgCreateObjRemote(PartCfgCreateObj _dto) {
		PartCfgCreateObjRemote remote = new PartCfgCreateObjRemote();
		remote.setRootPartUid(_dto.getRootPartUid());
		remote.setRootPartPin(_dto.getRootPartPin());
		remote.setId(_dto.getId());
		remote.setName(_dto.getName());
		remote.setDesp(_dto.getDesp());
		return remote;
	}

	// -------------------------------------------------------------------------------
	// ----------------------------------PartCfgConj----------------------------------
	public static PartCfgConjInfo parsePartCfgConj(PartCfgConjRemote _remote) {
		PartCfgConjInfoDto dto = new PartCfgConjInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setPartCfgUid(_remote.getPartCfgUid());
		dto.setPartAcqUid(_remote.getPartAcqUid());
		return dto;
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------Prod--------------------------------------
	public static ProdInfo parseProd(ProdRemote _remote) {
		ProdInfoDto dto = new ProdInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setId(_remote.getId());
		dto.setName(_remote.getName());
		return dto;
	}

	public static ProdCreateObjRemote parseProdCreateObjRemote(ProdCreateObj _dto) {
		ProdCreateObjRemote remote = new ProdCreateObjRemote();
		remote.setId(_dto.getId());
		remote.setName(_dto.getName());
		return remote;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------ProdCtl------------------------------------
	public static ProdCtlInfo parseProdCtl(ProdCtlRemote _remote) {
		ProdCtlInfoDto dto = new ProdCtlInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setId(_remote.getId());
		dto.setLv(_remote.getLv());
		dto.setName(_remote.getName());
		dto.setReq(_remote.isReq());
		dto.setParentUid(_remote.getParentUid());
		dto.setParentId(_remote.getParentId());
		dto.setProdUid(_remote.getProdUid());
		return dto;
	}

	public static ProdCtlCreateObjRemote parseProdCtlCreateObjRemote(ProdCtlCreateObj _dto) {
		ProdCtlCreateObjRemote remote = new ProdCtlCreateObjRemote();
		remote.setId(_dto.getId());
		remote.setLv(_dto.getLv());
		remote.setName(_dto.getName());
		remote.setReq(_dto.isReq());
		return remote;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------ProdCtlPartCfgConj-------------------------------
	public static ProdCtlPartCfgConjInfo parseProdCtlPartCfgConj(ProdCtlPartCfgConjRemote _remote) {
		ProdCtlPartCfgConjInfoDto dto = new ProdCtlPartCfgConjInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setProdCtlUid(_remote.getProdCtlUid());
		dto.setPartCfgUid(_remote.getPartCfgUid());
		return dto;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------ProdMod------------------------------------
	public static ProdModInfo parseProdMod(ProdModRemote _remote) {
		ProdModInfoDto dto = new ProdModInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setProdUid(_remote.getProdUid());
		dto.setId(_remote.getId());
		dto.setName(_remote.getName());
		dto.setDesp(_remote.getDesp());
		return dto;
	}

	public static ProdModCreateObjRemote parseProdModCreateObjRemote(ProdModCreateObj _dto) {
		ProdModCreateObjRemote remote = new ProdModCreateObjRemote();
		remote.setProdUid(_dto.getProdUid());
		remote.setId(_dto.getId());
		remote.setName(_dto.getName());
		remote.setDesp(_dto.getDesp());
		return remote;
	}

	// -------------------------------------------------------------------------------
	// ----------------------------------ProdModItem----------------------------------
	public static ProdModItemInfo parseProdModItem(ProdModItemRemote _remote) {
		ProdModItemInfoDto dto = new ProdModItemInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setProdModUid(_remote.getProdModUid());
		dto.setProdCtlUid(_remote.getProdCtlUid());
		dto.setPartCfgAssigned(_remote.isPartCfgAssigned());
		dto.setPartCfgUid(_remote.getPartCfgUid());
		return dto;
	}


}
