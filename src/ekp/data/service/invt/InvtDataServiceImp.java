package ekp.data.service.invt;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import ekp.data.InvtDataService;
import ekp.data.service.invt.query.InvtOrderItemQueryParam;
import ekp.data.service.invt.query.InvtOrderQueryParam;
import ekp.data.service.invt.query.MaterialMasterQueryParam;
import ekp.data.service.invt.query.MbsbStmtQueryParam;
import ekp.invt.type.MaterialInstAcqChannel;
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
import legion.util.DataFO;
import legion.util.LogUtil;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public class InvtDataServiceImp implements InvtDataService {
	private Logger log = LoggerFactory.getLogger(InvtDataServiceImp.class);

	private String srcEkpKernelRmi;

	@Override
	public void register(Map<String, String> _params) {
		if (_params == null || _params.isEmpty())
			return;

		srcEkpKernelRmi = _params.get("srcEkpKernelRmi");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	// -------------------------------------------------------------------------------
	@Override
	public String getSrcEkpKernelRmi() {
		return srcEkpKernelRmi;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------WrhsLoc------------------------------------
	@Override
	public WrhsLocInfo createWrhsLoc(WrhsLocCreateObj _dto) {
		try {
			WrhsLocCreateObjRemote dto = InvtFO.parseWrhsLocCreateObjRemote(_dto);
			return InvtFO.parseWrhsLoc(getEkpKernelRmi().createWrhsLoc(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteWrhsLoc(String _uid) {
		try {
			return getEkpKernelRmi().deleteWrhsLoc(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public WrhsLocInfo loadWrhsLoc(String _uid) {
		try {
			WrhsLocRemote remote = getEkpKernelRmi().loadWrhsLoc(_uid);
			return remote == null ? null : InvtFO.parseWrhsLoc(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public WrhsLocInfo loadWrhsLocById(String _id) {
		try {
			WrhsLocRemote remote = getEkpKernelRmi().loadWrhsLocById(_id);
			return remote == null ? null : InvtFO.parseWrhsLoc(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<WrhsLocInfo> loadWrhsLocList() {
		try {
			List<WrhsLocRemote> remoteList = getEkpKernelRmi().loadWrhsLocList();
			List<WrhsLocInfo> list = remoteList.stream().map(InvtFO::parseWrhsLoc).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------WrhsBin------------------------------------
	@Override
	public WrhsBinInfo createWrhsBin(WrhsBinCreateObj _dto) {
		try {
			WrhsBinCreateObjRemote dto = InvtFO.parseWrhsBinCreateObjRemote(_dto);
			return InvtFO.parseWrhsBin(getEkpKernelRmi().createWrhsBin(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteWrhsBin(String _uid) {
		try {
			return getEkpKernelRmi().deleteWrhsBin(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public WrhsBinInfo loadWrhsBin(String _uid) {
		try {
			WrhsBinRemote remote = getEkpKernelRmi().loadWrhsBin(_uid);
			return remote == null ? null : InvtFO.parseWrhsBin(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public WrhsBinInfo loadWrhsBin(String _wlUid, String _id) {
		// XXX
		if (DataFO.isEmptyString(_wlUid) || DataFO.isEmptyString(_id))
			return null;

		List<WrhsBinInfo> list = loadWrhsBinList(_wlUid);
		if (list == null)
			return null;
		return list.stream().filter(wb -> _id.equalsIgnoreCase(wb.getId())).findAny().orElse(null);
	}

	@Override
	public List<WrhsBinInfo> loadWrhsBinList(String _wlUid) {
		try {
			List<WrhsBinRemote> remoteList = getEkpKernelRmi().loadWrhsBinList(_wlUid);
			List<WrhsBinInfo> list = remoteList.stream().map(InvtFO::parseWrhsBin).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------InvtOrder-----------------------------------
	@Override
	public InvtOrderInfo createInvtOrder(InvtOrderCreateObj _dto) {
		try {
			InvtOrderCreateObjRemote dto = InvtFO.parseInvtOrderCreateObjRemote(_dto);
			return InvtFO.parseInvtOrder(getEkpKernelRmi().createInvtOrder(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteInvtOrder(String _uid) {
		try {
			return getEkpKernelRmi().deleteInvtOrder(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public InvtOrderInfo loadInvtOrder(String _uid) {
		try {
			InvtOrderRemote remote = getEkpKernelRmi().loadInvtOrder(_uid);
			return remote == null ? null : InvtFO.parseInvtOrder(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public InvtOrderInfo loadInvtOrderByIosn(String _iosn) {
		try {
			InvtOrderRemote remote = getEkpKernelRmi().loadInvtOrderByIosn(_iosn);
			return remote == null ? null : InvtFO.parseInvtOrder(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public QueryOperation<InvtOrderQueryParam, InvtOrderInfo> searchInvtOrder(
			QueryOperation<InvtOrderQueryParam, InvtOrderInfo> _param,
			Map<InvtOrderQueryParam, QueryValue[]> _existsDetailMap) {
		try {
			QueryOperation<InvtOrderQueryParam, InvtOrderRemote> paramRemote = (QueryOperation<InvtOrderQueryParam, InvtOrderRemote>) _param
					.copy();
			paramRemote = getEkpKernelRmi().searchInvtOrder(paramRemote, _existsDetailMap);
			List<InvtOrderInfo> list = paramRemote.getQueryResult().stream().map(InvtFO::parseInvtOrder)
					.collect(Collectors.toList());
			_param.setQueryResult(list);
			_param.setTotal(paramRemote.getTotal());
			return _param;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean invtOrderToApv(String _uid) {
		try {
			return getEkpKernelRmi().invtOrderToApv(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean invtOrderRevertToApv(String _uid) {
		try {
			return getEkpKernelRmi().invtOrderRevertToApv(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean invtOrderApprove(String _uid, long _apvTime) {
		try {
			return getEkpKernelRmi().invtOrderApprove(_uid, _apvTime);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean invtOrderRevertApprove(String _uid) {
		try {
			return getEkpKernelRmi().invtOrderRevertApprove(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	// -------------------------------------------------------------------------------
	// ---------------------------------InvtOrderItem---------------------------------
	@Override
	public InvtOrderItemInfo createInvtOrderItem(InvtOrderItemCreateObj _dto) {
		try {
			InvtOrderItemCreateObjRemote dto = InvtFO.parseInvtOrderItemCreateObjRemote(_dto);
			return InvtFO.parseInvtOrderItem(getEkpKernelRmi().createInvtOrderItem(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteInvtOrderItem(String _uid) {
		try {
			return getEkpKernelRmi().deleteInvtOrderItem(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public InvtOrderItemInfo loadInvtOrderItem(String _uid) {
		try {
			InvtOrderItemRemote remote = getEkpKernelRmi().loadInvtOrderItem(_uid);
			return remote == null ? null : InvtFO.parseInvtOrderItem(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<InvtOrderItemInfo> loadInvtOrderItemList(String _ioUid) {
		try {
			List<InvtOrderItemRemote> remoteList = getEkpKernelRmi().loadInvtOrderItemList(_ioUid);
			List<InvtOrderItemInfo> list = remoteList.stream().map(InvtFO::parseInvtOrderItem)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<InvtOrderItemInfo> loadInvtOrderItemListByMm(String _mmUid) {
		try {
			List<InvtOrderItemRemote> remoteList = getEkpKernelRmi().loadInvtOrderItemListByMm(_mmUid);
			List<InvtOrderItemInfo> list = remoteList.stream().map(InvtFO::parseInvtOrderItem)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public QueryOperation<InvtOrderItemQueryParam, InvtOrderItemInfo> searchInvtOrderItem(
			QueryOperation<InvtOrderItemQueryParam, InvtOrderItemInfo> _param,
			Map<InvtOrderItemQueryParam, QueryValue[]> _existsDetailMap) {
		try {
			QueryOperation<InvtOrderItemQueryParam, InvtOrderItemRemote> paramRemote = (QueryOperation<InvtOrderItemQueryParam, InvtOrderItemRemote>) _param
					.copy();
			paramRemote = getEkpKernelRmi().searchInvtOrderItem(paramRemote, _existsDetailMap);
			List<InvtOrderItemInfo> list = paramRemote.getQueryResult().stream().map(InvtFO::parseInvtOrderItem)
					.collect(Collectors.toList());
			_param.setQueryResult(list);
			_param.setTotal(paramRemote.getTotal());
			return _param;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean invtOrderItemMbsbStmtCreated(String _uid) {
		try {
			return getEkpKernelRmi().invtOrderItemMbsbStmtCreated(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}
	
	@Override
	public boolean invtOrderItemRevertMbsbStmtCreated(String _uid) {
		try {
			return getEkpKernelRmi().invtOrderItemRevertMbsbStmtCreated(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}
	

	// -------------------------------------------------------------------------------
	// --------------------------------MaterialMaster---------------------------------
	@Override
	public MaterialMasterInfo createMaterialMaster(MaterialMasterCreateObj _dto) {
		try {
			MaterialMasterCreateObjRemote dto = InvtFO.parseMaterialMasterCreateObjRemote(_dto);
			return InvtFO.parseMaterialMaster(getEkpKernelRmi().createMaterialMaster(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteMaterialMaster(String _uid) {
		try {
			return getEkpKernelRmi().deleteMaterialMaster(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public MaterialMasterInfo loadMaterialMaster(String _uid) {
		try {
			MaterialMasterRemote remote = getEkpKernelRmi().loadMaterialMaster(_uid);
			return remote == null ? null : InvtFO.parseMaterialMaster(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public MaterialMasterInfo loadMaterialMasterByMano(String _mano) {
		try {
			MaterialMasterRemote remote = getEkpKernelRmi().loadMaterialMasterByMano(_mano);
			return remote == null ? null : InvtFO.parseMaterialMaster(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public QueryOperation<MaterialMasterQueryParam, MaterialMasterInfo> searchMaterialMaster(
			QueryOperation<MaterialMasterQueryParam, MaterialMasterInfo> _param) {
		try {
			QueryOperation<MaterialMasterQueryParam, MaterialMasterRemote> paramRemote = (QueryOperation<MaterialMasterQueryParam, MaterialMasterRemote>) _param
					.copy();
			paramRemote = getEkpKernelRmi().searchMaterialMaster(paramRemote);
			List<MaterialMasterInfo> list = paramRemote.getQueryResult().stream().map(InvtFO::parseMaterialMaster)
					.collect(Collectors.toList());
			_param.setQueryResult(list);
			_param.setTotal(paramRemote.getTotal());
			return _param;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// ---------------------------------MaterialInst----------------------------------
	@Override
	public MaterialInstInfo createMaterialInst(MaterialInstCreateObj _dto) {
		try {
			MaterialInstCreateObjRemote dto = InvtFO.parseMaterialInstCreateObjRemote(_dto);
			return InvtFO.parseMaterialInst(getEkpKernelRmi().createMaterialInst(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteMaterialInst(String _uid) {
		try {
			return getEkpKernelRmi().deleteMaterialInst(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public MaterialInstInfo loadMaterialInst(String _uid) {
		try {
			MaterialInstRemote remote = getEkpKernelRmi().loadMaterialInst(_uid);
			return remote == null ? null : InvtFO.parseMaterialInst(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public MaterialInstInfo loadMaterialInstByMisn(String _misn) {
		try {
			MaterialInstRemote remote = getEkpKernelRmi().loadMaterialInstByMisn(_misn);
			return remote == null ? null : InvtFO.parseMaterialInst(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}
	
//	@Override
//	public MaterialInstInfo loadMaterialInstByMiacSrcNo(String _miacSrcNo) {
//		try {
//			MaterialInstRemote remote = getEkpKernelRmi().loadMaterialInstByMiacSrcNo(_miacSrcNo);
//			return remote == null ? null : InvtFO.parseMaterialInst(remote);
//		} catch (Throwable e) {
//			LogUtil.log(log, e, Level.ERROR);
//			return null;
//		}
//	}
	@Override
	public List<MaterialInstInfo> loadMaterialInstList(String _mmUid,MaterialInstAcqChannel _miac,  String _miacSrcNo) {
		try {
			List<MaterialInstRemote> remoteList = getEkpKernelRmi().loadMaterialInstList(_mmUid, _miac,  _miacSrcNo);
			List<MaterialInstInfo> list = remoteList.stream().map(InvtFO::parseMaterialInst)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean materialInstToAssignSrcMi(String _uid) {
		try {
			return getEkpKernelRmi().materialInstToAssignSrcMi(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean materialInstRevertToAssignSrcMi(String _uid) {
		try {
			return getEkpKernelRmi().materialInstRevertToAssignSrcMi(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean materialInstFinishAssignedSrcMi(String _uid) {
		try {
			return getEkpKernelRmi().materialInstFinishAssignedSrcMi(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean materialInstRevertFinishAssignedSrcMi(String _uid) {
		try {
			return getEkpKernelRmi().materialInstRevertFinishAssignedSrcMi(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean materialInstNotAssignSrcMi(String _uid) {
		try {
			return getEkpKernelRmi().materialInstNotAssignSrcMi(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean materialInstRevertNotAssignSrcMi(String _uid) {
		try {
			return getEkpKernelRmi().materialInstRevertNotAssignSrcMi(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	// -------------------------------------------------------------------------------
	// ------------------------------MaterialInstSrcConj------------------------------
	@Override
	public MaterialInstSrcConjInfo createMaterialInstSrcConj(MaterialInstSrcConjCreateObj _dto) {
		try {
			MaterialInstSrcConjCreateObjRemote dto = InvtFO.parseMaterialInstSrcConjCreateObjRemote(_dto);
			return InvtFO.parseMaterialInstSrcConj(getEkpKernelRmi().createMaterialInstSrcConj(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteMaterialInstSrcConj(String _uid) {
		try {
			return getEkpKernelRmi().deleteMaterialInstSrcConj(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public MaterialInstSrcConjInfo loadMaterialInstSrcConj(String _uid) {
		try {
			MaterialInstSrcConjRemote remote = getEkpKernelRmi().loadMaterialInstSrcConj(_uid);
			return remote == null ? null : InvtFO.parseMaterialInstSrcConj(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<MaterialInstSrcConjInfo> loadMaterialInstSrcConjList(String _miUid) {
		try {
			List<MaterialInstSrcConjRemote> remoteList = getEkpKernelRmi().loadMaterialInstSrcConjList(_miUid);
			List<MaterialInstSrcConjInfo> list = remoteList.stream().map(InvtFO::parseMaterialInstSrcConj)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<MaterialInstSrcConjInfo> loadMaterialInstSrcConjListBySrcMi(String _srcMiUid) {
		try {
			List<MaterialInstSrcConjRemote> remoteList = getEkpKernelRmi()
					.loadMaterialInstSrcConjListBySrcMi(_srcMiUid);
			List<MaterialInstSrcConjInfo> list = remoteList.stream().map(InvtFO::parseMaterialInstSrcConj)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// -------------------------------MaterialBinStock--------------------------------
	@Override
	public MaterialBinStockInfo createMaterialBinStock(MaterialBinStockCreateObj _dto) {
		try {
			MaterialBinStockCreateObjRemote dto = InvtFO.parseMaterialBinStockCreateObjRemote(_dto);
			return InvtFO.parseMaterialBinStock(getEkpKernelRmi().createMaterialBinStock(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteMaterialBinStock(String _uid) {
		try {
			return getEkpKernelRmi().deleteMaterialBinStock(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public MaterialBinStockInfo loadMaterialBinStock(String _uid) {
		try {
			MaterialBinStockRemote remote = getEkpKernelRmi().loadMaterialBinStock(_uid);
			return remote == null ? null : InvtFO.parseMaterialBinStock(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public MaterialBinStockInfo loadMaterialBinStock(String _mmUid, String _wrhsBinUid) {
		try {
			MaterialBinStockRemote remote = getEkpKernelRmi().loadMaterialBinStock(_mmUid, _wrhsBinUid);
			return remote == null ? null : InvtFO.parseMaterialBinStock(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<MaterialBinStockInfo> loadMaterialBinStockList(String _mmUid) {
		try {
			List<MaterialBinStockRemote> remoteList = getEkpKernelRmi().loadMaterialBinStockList(_mmUid);
			List<MaterialBinStockInfo> list = remoteList.stream().map(InvtFO::parseMaterialBinStock)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<MaterialBinStockInfo> loadMaterialBinStockListByWrhsBin(String _wbUid) {
		try {
			List<MaterialBinStockRemote> remoteList = getEkpKernelRmi().loadMaterialBinStockListByWrhsBin(_wbUid);
			List<MaterialBinStockInfo> list = remoteList.stream().map(InvtFO::parseMaterialBinStock)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// -----------------------------MaterialBinStockBatch-----------------------------
	@Override
	public MaterialBinStockBatchInfo createMaterialBinStockBatch(MaterialBinStockBatchCreateObj _dto) {
		try {
			MaterialBinStockBatchCreateObjRemote dto = InvtFO.parseMaterialBinStockBatchCreateObjRemote(_dto);
			return InvtFO.parseMaterialBinStockBatch(getEkpKernelRmi().createMaterialBinStockBatch(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteMaterialBinStockBatch(String _uid) {
		try {
			return getEkpKernelRmi().deleteMaterialBinStockBatch(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public MaterialBinStockBatchInfo loadMaterialBinStockBatch(String _uid) {
		try {
			MaterialBinStockBatchRemote remote = getEkpKernelRmi().loadMaterialBinStockBatch(_uid);
			return remote == null ? null : InvtFO.parseMaterialBinStockBatch(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public MaterialBinStockBatchInfo loadMaterialBinStockBatch(String _mbsUid, String _miUid) {
		try {
			MaterialBinStockBatchRemote remote = getEkpKernelRmi().loadMaterialBinStockBatch(_mbsUid, _miUid);
			return remote == null ? null : InvtFO.parseMaterialBinStockBatch(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<MaterialBinStockBatchInfo> loadMaterialBinStockBatchList(String _mbsUid) {
		try {
			List<MaterialBinStockBatchRemote> remoteList = getEkpKernelRmi().loadMaterialBinStockBatchList(_mbsUid);
			List<MaterialBinStockBatchInfo> list = remoteList.stream().map(InvtFO::parseMaterialBinStockBatch)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<MaterialBinStockBatchInfo> loadMaterialBinStockBatchListByMi(String _miUid) {
		try {
			List<MaterialBinStockBatchRemote> remoteList = getEkpKernelRmi().loadMaterialBinStockBatchListByMi(_miUid);
			List<MaterialBinStockBatchInfo> list = remoteList.stream().map(InvtFO::parseMaterialBinStockBatch)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------MbsbStmt------------------------------------
	@Override
	public MbsbStmtInfo createMbsbStmt(MbsbStmtCreateObj _dto) {
		try {
			MbsbStmtCreateObjRemote dto = InvtFO.parseMbsbStmtCreateObjRemote(_dto);
			return InvtFO.parseMbsbStmt(getEkpKernelRmi().createMbsbStmt(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteMbsbStmt(String _uid) {
		try {
			return getEkpKernelRmi().deleteMbsbStmt(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public MbsbStmtInfo loadMbsbStmt(String _uid) {
		try {
			MbsbStmtRemote remote = getEkpKernelRmi().loadMbsbStmt(_uid);
			return remote == null ? null : InvtFO.parseMbsbStmt(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<MbsbStmtInfo> loadMbsbStmtList(String _mbsbUid) {
		try {
			List<MbsbStmtRemote> remoteList = getEkpKernelRmi().loadMbsbStmtList(_mbsbUid);
			List<MbsbStmtInfo> list = remoteList.stream().map(InvtFO::parseMbsbStmt).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<MbsbStmtInfo> loadMbsbStmtListByIoi(String _ioiUid) {
		try {
			List<MbsbStmtRemote> remoteList = getEkpKernelRmi().loadMbsbStmtListByIoi(_ioiUid);
			List<MbsbStmtInfo> list = remoteList.stream().map(InvtFO::parseMbsbStmt).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public QueryOperation<MbsbStmtQueryParam, MbsbStmtInfo> searchMbsbStmt(
			QueryOperation<MbsbStmtQueryParam, MbsbStmtInfo> _param) {
		try {
			QueryOperation<MbsbStmtQueryParam, MbsbStmtRemote> paramRemote = (QueryOperation<MbsbStmtQueryParam, MbsbStmtRemote>) _param
					.copy();
			paramRemote = getEkpKernelRmi().searchMbsbStmt(paramRemote);
			List<MbsbStmtInfo> list = paramRemote.getQueryResult().stream().map(InvtFO::parseMbsbStmt)
					.collect(Collectors.toList());
			_param.setQueryResult(list);
			_param.setTotal(paramRemote.getTotal());
			return _param;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean mbsbStmtPost(String _uid) {
		try {
			return getEkpKernelRmi().mbsbStmtPost(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean mbsbStmtRevertPost(String _uid) {
		try {
			return getEkpKernelRmi().mbsbStmtRevertPost(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

}
