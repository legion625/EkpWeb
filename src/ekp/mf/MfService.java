package ekp.mf;

import java.util.Map;

import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.query.WorkorderQueryParam;
import legion.BusinessService;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public interface MfService extends BusinessService {
	
	// -------------------------------------------------------------------------------
	// -----------------------------------Workorder-----------------------------------
	public QueryOperation<WorkorderQueryParam, WorkorderInfo> searchWorkorder(
			QueryOperation<WorkorderQueryParam, WorkorderInfo> _param,
			Map<WorkorderQueryParam, QueryValue[]> _existsDetailMap);
}
