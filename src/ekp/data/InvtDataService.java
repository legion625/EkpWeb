package ekp.data;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import ekp.data.service.invt.InvtOrderCreateObj;
import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.InvtOrderItemCreateObj;
import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialBinStockBatchCreateObj;
import ekp.data.service.invt.MaterialBinStockBatchInfo;
import ekp.data.service.invt.MaterialBinStockCreateObj;
import ekp.data.service.invt.MaterialBinStockInfo;
import ekp.data.service.invt.MaterialInstCreateObj;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MaterialInstSrcConjCreateObj;
import ekp.data.service.invt.MaterialInstSrcConjInfo;
import ekp.data.service.invt.MaterialMasterCreateObj;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.MbsbStmtCreateObj;
import ekp.data.service.invt.MbsbStmtInfo;
import ekp.data.service.invt.WrhsBinCreateObj;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.invt.WrhsLocCreateObj;
import ekp.data.service.invt.WrhsLocInfo;
import ekp.data.service.invt.query.InvtOrderItemQueryParam;
import ekp.data.service.invt.query.InvtOrderQueryParam;
import ekp.data.service.invt.query.MaterialMasterQueryParam;
import ekp.data.service.invt.query.MbsbStmtQueryParam;
import ekp.serviceFacade.rmi.invt.MaterialBinStockRemote;
import ekp.serviceFacade.rmi.invt.MaterialInstSrcConjCreateObjRemote;
import ekp.serviceFacade.rmi.invt.MaterialInstSrcConjRemote;
import legion.IntegrationService;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public interface InvtDataService extends IntegrationService, EkpKernelRmi {

	// -------------------------------------------------------------------------------
	// ------------------------------------WrhsLoc------------------------------------
	public WrhsLocInfo createWrhsLoc(WrhsLocCreateObj _dto);

	public boolean deleteWrhsLoc(String _uid);

	public WrhsLocInfo loadWrhsLoc(String _uid);

	public WrhsLocInfo loadWrhsLocById(String _id);

	public List<WrhsLocInfo> loadWrhsLocList();

	// -------------------------------------------------------------------------------
	// ------------------------------------WrhsBin------------------------------------
	public WrhsBinInfo createWrhsBin(WrhsBinCreateObj _dto);

	public boolean deleteWrhsBin(String _uid);

	public WrhsBinInfo loadWrhsBin(String _uid);
	
	public WrhsBinInfo loadWrhsBin(String _wlUid, String _id);

	public List<WrhsBinInfo> loadWrhsBinList(String _wlUid);

	// -------------------------------------------------------------------------------
	// -----------------------------------InvtOrder-----------------------------------
	public InvtOrderInfo createInvtOrder(InvtOrderCreateObj _dto);

	public boolean deleteInvtOrder(String _uid);

	public InvtOrderInfo loadInvtOrder(String _uid);

	public InvtOrderInfo loadInvtOrderByIosn(String _iosn);

	public QueryOperation<InvtOrderQueryParam, InvtOrderInfo> searchInvtOrder(
			QueryOperation<InvtOrderQueryParam, InvtOrderInfo> _param,
			Map<InvtOrderQueryParam, QueryValue[]> _existsDetailMap);
	
	public boolean invtOrderToApv(String _uid);

	public boolean invtOrderRevertToApv(String _uid);

	public boolean invtOrderApprove(String _uid, long _apvTime);

	public boolean invtOrderRevertApprove(String _uid);

	// -------------------------------------------------------------------------------
	// ---------------------------------InvtOrderItem---------------------------------
	public InvtOrderItemInfo createInvtOrderItem(InvtOrderItemCreateObj _dto);

	public boolean deleteInvtOrderItem(String _uid);

	public InvtOrderItemInfo loadInvtOrderItem(String _uid);

	public List<InvtOrderItemInfo> loadInvtOrderItemList(String _ioUid);
	
	public List<InvtOrderItemInfo> loadInvtOrderItemListByMm(String _mmUid);

	public QueryOperation<InvtOrderItemQueryParam, InvtOrderItemInfo> searchInvtOrderItem(
			QueryOperation<InvtOrderItemQueryParam, InvtOrderItemInfo> _param,
			Map<InvtOrderItemQueryParam, QueryValue[]> _existsDetailMap);
	
	public boolean invtOrderItemMbsbStmtCreated(String _uid);
	public boolean invtOrderItemRevertMbsbStmtCreated(String _uid);

	// -------------------------------------------------------------------------------
	// --------------------------------MaterialMaster---------------------------------
	public MaterialMasterInfo createMaterialMaster(MaterialMasterCreateObj _dto);

	public boolean deleteMaterialMaster(String _uid);

	public MaterialMasterInfo loadMaterialMaster(String _uid);

	public MaterialMasterInfo loadMaterialMasterByMano(String _mano);
	
	public QueryOperation<MaterialMasterQueryParam, MaterialMasterInfo> searchMaterialMaster(
			QueryOperation<MaterialMasterQueryParam, MaterialMasterInfo> _param) ;

	// -------------------------------------------------------------------------------
	// ---------------------------------MaterialInst----------------------------------
	public MaterialInstInfo createMaterialInst(MaterialInstCreateObj _dto);

	public boolean deleteMaterialInst(String _uid);

	public MaterialInstInfo loadMaterialInst(String _uid);

	public MaterialInstInfo loadMaterialInstByMisn(String _misn);

	public List<MaterialInstInfo> loadMaterialInstList(String _mmUid);
	
	public boolean materialInstToAssignSrcMi(String _uid);
	public boolean materialInstRevertToAssignSrcMi(String _uid);
	public boolean materialInstFinishAssignedSrcMi(String _uid);
	public boolean materialInstRevertFinishAssignedSrcMi(String _uid);
	public boolean materialInstNotAssignSrcMi(String _uid);
	public boolean materialInstRevertNotAssignSrcMi(String _uid);

	// -------------------------------------------------------------------------------
	// ------------------------------MaterialInstSrcConj------------------------------
	public MaterialInstSrcConjInfo createMaterialInstSrcConj(MaterialInstSrcConjCreateObj _dto);

	public boolean deleteMaterialInstSrcConj(String _uid);

	public MaterialInstSrcConjInfo loadMaterialInstSrcConj(String _uid);

	public List<MaterialInstSrcConjInfo> loadMaterialInstSrcConjList(String _miUid);

	public List<MaterialInstSrcConjInfo> loadMaterialInstSrcConjListBySrcMi(String _srcMiUid);
	
	// -------------------------------------------------------------------------------
	// -------------------------------MaterialBinStock--------------------------------
	public MaterialBinStockInfo createMaterialBinStock(MaterialBinStockCreateObj _dto);

	public boolean deleteMaterialBinStock(String _uid);

	public MaterialBinStockInfo loadMaterialBinStock(String _uid);
	
	public MaterialBinStockInfo loadMaterialBinStock(String _mmUid, String _wrhsBinUid);

	public List<MaterialBinStockInfo> loadMaterialBinStockList(String _mmUid);
	
	public List<MaterialBinStockInfo> loadMaterialBinStockListByWrhsBin(String _wbUid);

	// -------------------------------------------------------------------------------
	// -----------------------------MaterialBinStockBatch-----------------------------
	public MaterialBinStockBatchInfo createMaterialBinStockBatch(MaterialBinStockBatchCreateObj _dto);

	public boolean deleteMaterialBinStockBatch(String _uid);

	public MaterialBinStockBatchInfo loadMaterialBinStockBatch(String _uid);
	
	public MaterialBinStockBatchInfo loadMaterialBinStockBatch(String _mbsUid, String _miUid);

	public List<MaterialBinStockBatchInfo> loadMaterialBinStockBatchList(String _mbsUid);

	public List<MaterialBinStockBatchInfo> loadMaterialBinStockBatchListByMi(String _miUid);

	// -------------------------------------------------------------------------------
	// -----------------------------------MbsbStmt------------------------------------
	public MbsbStmtInfo createMbsbStmt(MbsbStmtCreateObj _dto);

	public boolean deleteMbsbStmt(String _uid);

	public MbsbStmtInfo loadMbsbStmt(String _uid);

	public List<MbsbStmtInfo> loadMbsbStmtList(String _mbsbUid);

	public List<MbsbStmtInfo> loadMbsbStmtListByIoi(String _ioiUid);

	public QueryOperation<MbsbStmtQueryParam, MbsbStmtInfo> searchMbsbStmt(
			QueryOperation<MbsbStmtQueryParam, MbsbStmtInfo> _param);

	public boolean mbsbStmtPost(String _uid);

	public boolean mbsbStmtRevertPost(String _uid);

}