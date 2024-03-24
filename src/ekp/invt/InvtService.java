package ekp.invt;

import java.util.List;
import java.util.Map;

import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialBinStockInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.WrhsLocInfo;
import ekp.data.service.invt.query.InvtOrderItemQueryParam;
import ekp.data.service.invt.query.InvtOrderQueryParam;
import ekp.data.service.invt.query.MaterialMasterQueryParam;
import legion.BusinessService;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public interface InvtService extends BusinessService {

	// -------------------------------------------------------------------------------
	// ------------------------------------WrhsLoc------------------------------------
	public List<WrhsLocInfo> loadWrhsLocList();

	// -------------------------------------------------------------------------------
	// -----------------------------------InvtOrder-----------------------------------
	public QueryOperation<InvtOrderQueryParam, InvtOrderInfo> searchInvtOrder(
			QueryOperation<InvtOrderQueryParam, InvtOrderInfo> _param,
			Map<InvtOrderQueryParam, QueryValue[]> _existsDetailMap);

	// -------------------------------------------------------------------------------
	// ---------------------------------InvtOrderItem---------------------------------
	public QueryOperation<InvtOrderItemQueryParam, InvtOrderItemInfo> searchInvtOrderItem(
			QueryOperation<InvtOrderItemQueryParam, InvtOrderItemInfo> _param,
			Map<InvtOrderItemQueryParam, QueryValue[]> _existsDetailMap);

	// -------------------------------------------------------------------------------
	// --------------------------------MaterialMaster---------------------------------
	public QueryOperation<MaterialMasterQueryParam, MaterialMasterInfo> searchMaterialMaster(
			QueryOperation<MaterialMasterQueryParam, MaterialMasterInfo> _param);

	// -------------------------------------------------------------------------------
	// -------------------------------MaterialBinStock--------------------------------
}