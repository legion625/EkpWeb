package ekp.invt;

import java.util.List;
import java.util.Map;

import ekp.data.InvtDataService;
import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.MaterialBinStockCreateObj;
import ekp.data.service.invt.MaterialBinStockInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.WrhsLocInfo;
import ekp.data.service.invt.query.InvtOrderQueryParam;
import ekp.data.service.invt.query.MaterialMasterQueryParam;
import legion.DataServiceFactory;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public class InvtServiceImp implements InvtService {

	private static InvtDataService dataService;

	@Override
	public void register(Map<String, String> _params) {
		dataService = DataServiceFactory.getInstance().getService(InvtDataService.class);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------WrhsLoc------------------------------------
	@Override
	public List<WrhsLocInfo> loadWrhsLocList() {
		return dataService.loadWrhsLocList();
	}
	
	// -------------------------------------------------------------------------------
	// -----------------------------------InvtOrder-----------------------------------
	@Override
	public QueryOperation<InvtOrderQueryParam, InvtOrderInfo> searchInvtOrder(
			QueryOperation<InvtOrderQueryParam, InvtOrderInfo> _param,
			Map<InvtOrderQueryParam, QueryValue[]> _existsDetailMap){
		return dataService.searchInvtOrder(_param, _existsDetailMap);
	}
	
	// -------------------------------------------------------------------------------
	// --------------------------------MaterialMaster---------------------------------
	@Override
	public QueryOperation<MaterialMasterQueryParam, MaterialMasterInfo> searchMaterialMaster(
			QueryOperation<MaterialMasterQueryParam, MaterialMasterInfo> _param){
		return dataService.searchMaterialMaster(_param);
	}
	
	// -------------------------------------------------------------------------------
	// -------------------------------MaterialBinStock--------------------------------

}
