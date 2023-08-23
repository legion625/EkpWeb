package ekp.invt;

import java.util.List;

import ekp.data.service.invt.MaterialBinStockInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.WrhsLocInfo;
import ekp.data.service.invt.query.MaterialMasterQueryParam;
import legion.BusinessService;
import legion.util.query.QueryOperation;

public interface InvtService extends BusinessService {

	// -------------------------------------------------------------------------------
	// ------------------------------------WrhsLoc------------------------------------
	public List<WrhsLocInfo> loadWrhsLocList();
	
	// -------------------------------------------------------------------------------
	// --------------------------------MaterialMaster---------------------------------
	public QueryOperation<MaterialMasterQueryParam, MaterialMasterInfo> searchMaterialMaster(
			QueryOperation<MaterialMasterQueryParam, MaterialMasterInfo> _param);
	
	// -------------------------------------------------------------------------------
	// -------------------------------MaterialBinStock--------------------------------
}