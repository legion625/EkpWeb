package ekp.data.service.invt;

import ekp.serviceFacade.rmi.invt.InvtOrderCreateObjRemote;
import ekp.serviceFacade.rmi.invt.InvtOrderItemCreateObjRemote;
import ekp.serviceFacade.rmi.invt.InvtOrderItemRemote;
import ekp.serviceFacade.rmi.invt.InvtOrderRemote;
import ekp.serviceFacade.rmi.invt.MaterialBinStockBatchCreateObjRemote;
import ekp.serviceFacade.rmi.invt.MaterialBinStockBatchRemote;
import ekp.serviceFacade.rmi.invt.MaterialBinStockCreateObjRemote;
import ekp.serviceFacade.rmi.invt.MaterialBinStockRemote;
import ekp.serviceFacade.rmi.invt.MaterialInstCreateObjRemote;
import ekp.serviceFacade.rmi.invt.MaterialInstRemote;
import ekp.serviceFacade.rmi.invt.MaterialInstSrcConjCreateObjRemote;
import ekp.serviceFacade.rmi.invt.MaterialInstSrcConjRemote;
import ekp.serviceFacade.rmi.invt.MaterialMasterCreateObjRemote;
import ekp.serviceFacade.rmi.invt.MaterialMasterRemote;
import ekp.serviceFacade.rmi.invt.MbsbStmtCreateObjRemote;
import ekp.serviceFacade.rmi.invt.MbsbStmtRemote;
import ekp.serviceFacade.rmi.invt.WrhsBinCreateObjRemote;
import ekp.serviceFacade.rmi.invt.WrhsBinRemote;
import ekp.serviceFacade.rmi.invt.WrhsLocCreateObjRemote;
import ekp.serviceFacade.rmi.invt.WrhsLocRemote;

public class InvtFO {

	// -------------------------------------------------------------------------------
	// ------------------------------------WrhsLoc------------------------------------
	public static WrhsLocInfo parseWrhsLoc(WrhsLocRemote _remote) {
		WrhsLocInfoDto dto = new WrhsLocInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setId(_remote.getId());
		dto.setName(_remote.getName());
		return dto;
	}

	public static WrhsLocCreateObjRemote parseWrhsLocCreateObjRemote(WrhsLocCreateObj _dto) {
		WrhsLocCreateObjRemote remote = new WrhsLocCreateObjRemote();
		remote.setId(_dto.getId());
		remote.setName(_dto.getName());
		return remote;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------WrhsBin------------------------------------
	public static WrhsBinInfo parseWrhsBin(WrhsBinRemote _remote) {
		WrhsBinInfoDto dto = new WrhsBinInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setWlUid(_remote.getWlUid());
		dto.setId(_remote.getId());
		dto.setName(_remote.getName());
		return dto;
	}

	public static WrhsBinCreateObjRemote parseWrhsBinCreateObjRemote(WrhsBinCreateObj _dto) {
		WrhsBinCreateObjRemote remote = new WrhsBinCreateObjRemote();
		remote.setWlUid(_dto.getWlUid());
		remote.setId(_dto.getId());
		remote.setName(_dto.getName());
		return remote;
	}
	
	// -------------------------------------------------------------------------------
	// -----------------------------------InvtOrder-----------------------------------
	public static InvtOrderInfo parseInvtOrder(InvtOrderRemote _remote) {
		InvtOrderInfoDto dto = new InvtOrderInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setIosn(_remote.getIosn());
		dto.setStatus(_remote.getStatus());
		dto.setApplierId(_remote.getApplierId());
		dto.setApplierName(_remote.getApplierName());
		dto.setApplyTime(_remote.getApplyTime());
		dto.setRemark(_remote.getRemark());
		dto.setApvTime(_remote.getApvTime());
		return dto;
	}
	
	public static InvtOrderCreateObjRemote parseInvtOrderCreateObjRemote(InvtOrderCreateObj _dto) {
		InvtOrderCreateObjRemote remote = new InvtOrderCreateObjRemote();
		remote.setApplierId(_dto.getApplierId());
		remote.setApplierName(_dto.getApplierName());
		remote.setApplyTime(_dto.getApplyTime());
		remote.setRemark(_dto.getRemark());
		return remote;
	}
	
	// -------------------------------------------------------------------------------
	// ---------------------------------InvtOrderItem---------------------------------
	public static InvtOrderItemInfo parseInvtOrderItem(InvtOrderItemRemote _remote) {
		InvtOrderItemInfoDto dto = new InvtOrderItemInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setIoUid(_remote.getIoUid());
		dto.setMmUid(_remote.getMmUid());
		dto.setIoType(_remote.getIoType());
		dto.setOrderQty(_remote.getOrderQty());
		dto.setOrderValue(_remote.getOrderValue());
		dto.setMbsbStmtCreated(_remote.isMbsbStmtCreated()); 
		return dto;
	}
	
	public static InvtOrderItemCreateObjRemote parseInvtOrderItemCreateObjRemote(InvtOrderItemCreateObj _dto) {
		InvtOrderItemCreateObjRemote remote = new InvtOrderItemCreateObjRemote();
		remote.setIoUid(_dto.getIoUid());
		remote.setMmUid(_dto.getMmUid());
		remote.setIoType(_dto.getIoType());
		remote.setOrderQty(_dto.getOrderQty());
		remote.setOrderValue(_dto.getOrderValue());
		return remote;
	}
	
	// -------------------------------------------------------------------------------
	// --------------------------------MaterialMaster---------------------------------
	public static MaterialMasterInfo parseMaterialMaster(MaterialMasterRemote _remote) {
		MaterialMasterInfoDto dto = new MaterialMasterInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setMano(_remote.getMano());
		dto.setName(_remote.getName());
		dto.setSpecification(_remote.getSpecification());
		dto.setStdUnit(_remote.getStdUnit());
		dto.setSumStockQty(_remote.getSumStockQty());
		dto.setSumStockValue(_remote.getSumStockValue());
		return dto;
	}
	
	public static MaterialMasterCreateObjRemote parseMaterialMasterCreateObjRemote(MaterialMasterCreateObj _dto) {
		MaterialMasterCreateObjRemote remote = new MaterialMasterCreateObjRemote();
		remote.setMano(_dto.getMano());
		remote.setName(_dto.getName());
		remote.setSpecification(_dto.getSpecification());
		remote.setStdUnit(_dto.getStdUnit());
		return remote;
	}
	
	// -------------------------------------------------------------------------------
	// ---------------------------------MaterialInst----------------------------------
	public static MaterialInstInfo parseMaterialInst(MaterialInstRemote _remote) {
		MaterialInstInfoDto dto = new MaterialInstInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setMmUid(_remote.getMmUid());
		dto.setMisn(_remote.getMisn());
		dto.setMiac(_remote.getMiac());
		dto.setMiacSrcNo(_remote.getMiacSrcNo());
		dto.setQty(_remote.getQty());
		dto.setValue(_remote.getValue());
		dto.setEffDate(_remote.getEffDate());
		dto.setExpDate(_remote.getExpDate());
		dto.setSrcStatus(_remote.getSrcStatus());
		return dto;
	}
	public static MaterialInstCreateObjRemote parseMaterialInstCreateObjRemote(MaterialInstCreateObj _dto) {
		MaterialInstCreateObjRemote remote = new MaterialInstCreateObjRemote();
		remote.setMmUid(_dto.getMmUid());
		remote.setMiac(_dto.getMiac());
		remote.setMiacSrcNo(_dto.getMiacSrcNo());
		remote.setQty(_dto.getQty());
		remote.setValue(_dto.getValue());
		remote.setEffDate(_dto.getEffDate());
		remote.setExpDate(_dto.getExpDate());
		return remote;
	}
	
	// -------------------------------------------------------------------------------
	// ------------------------------MaterialInstSrcConj------------------------------
	public static MaterialInstSrcConjInfo parseMaterialInstSrcConj(MaterialInstSrcConjRemote _remote) {
		MaterialInstSrcConjInfoDto dto = new MaterialInstSrcConjInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setMiUid(_remote.getMiUid());
		dto.setSrcMiUid(_remote.getSrcMiUid());
		dto.setSrcMiQty(_remote.getSrcMiQty());
		dto.setSrcMiValue(_remote.getSrcMiValue());
		return dto;
	}
	
	public static MaterialInstSrcConjCreateObjRemote parseMaterialInstSrcConjCreateObjRemote(MaterialInstSrcConjCreateObj _dto) {
		MaterialInstSrcConjCreateObjRemote remote = new MaterialInstSrcConjCreateObjRemote();
		remote.setMiUid(_dto.getMiUid());
		remote.setSrcMiUid(_dto.getSrcMiUid());
		remote.setSrcMiQty(_dto.getSrcMiQty());
		remote.setSrcMiValue(_dto.getSrcMiValue());
		return remote;
	}
	
	
	// -------------------------------------------------------------------------------
	// -------------------------------MaterialBinStock--------------------------------
	public static MaterialBinStockInfo parseMaterialBinStock(MaterialBinStockRemote _remote) {
		MaterialBinStockInfoDto dto = new MaterialBinStockInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setMmUid(_remote.getMmUid());
		dto.setMano(_remote.getMano());
		dto.setWrhsBinUid(_remote.getWrhsBinUid());
		dto.setSumStockQty(_remote.getSumStockQty());
		dto.setSumStockValue(_remote.getSumStockValue());
		return dto;
	}
	
	public static MaterialBinStockCreateObjRemote parseMaterialBinStockCreateObjRemote(MaterialBinStockCreateObj _dto) {
		MaterialBinStockCreateObjRemote remote = new MaterialBinStockCreateObjRemote();
		remote.setMmUid(_dto.getMmUid());
		remote.setMano(_dto.getMano());
		remote.setWrhsBinUid(_dto.getWrhsBinUid());
		return remote;
	}
	
	// -------------------------------------------------------------------------------
	// -----------------------------MaterialBinStockBatch-----------------------------
	public static MaterialBinStockBatchInfo parseMaterialBinStockBatch(MaterialBinStockBatchRemote _remote) {
		MaterialBinStockBatchInfoDto dto = new MaterialBinStockBatchInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setMbsUid(_remote.getMbsUid());
		dto.setMiUid(_remote.getMiUid());
		dto.setStockQty(_remote.getStockQty());
		dto.setStockValue(_remote.getStockValue());
		return dto;
	}
	
	public static MaterialBinStockBatchCreateObjRemote parseMaterialBinStockBatchCreateObjRemote(MaterialBinStockBatchCreateObj _dto) {
		MaterialBinStockBatchCreateObjRemote remote = new MaterialBinStockBatchCreateObjRemote();
		remote.setMbsUid(_dto.getMbsUid());
		remote.setMiUid(_dto.getMiUid());
		return remote;
	}
	
	// -------------------------------------------------------------------------------
	// -----------------------------------MbsbStmt------------------------------------
	public static MbsbStmtInfo parseMbsbStmt(MbsbStmtRemote _remote) {
		MbsbStmtInfoDto dto = new MbsbStmtInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setMbsbUid(_remote.getMbsbUid());
		dto.setIoiUid(_remote.getIoiUid());
		dto.setMbsbFlowType(_remote.getMbsbFlowType());
		dto.setStmtQty(_remote.getStmtQty());
		dto.setStmtValue(_remote.getStmtValue());
		dto.setPostingStatus(_remote.getPostingStatus());
		dto.setPostingTime(_remote.getPostingTime());
		return dto;
	}
	
	public static MbsbStmtCreateObjRemote parseMbsbStmtCreateObjRemote(MbsbStmtCreateObj _dto) {
		MbsbStmtCreateObjRemote remote = new MbsbStmtCreateObjRemote();
		remote.setMbsbUid(_dto.getMbsbUid());
		remote.setIoiUid(_dto.getIoiUid());
		remote.setMbsbFlowType(_dto.getMbsbFlowType());
		remote.setStmtQty(_dto.getStmtQty());
		remote.setStmtValue(_dto.getStmtValue());
		return remote;
	}
	
	// TODO
	
	
}
