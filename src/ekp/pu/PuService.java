package ekp.pu;

import java.util.Map;

import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.query.PurchQueryParam;
import legion.BusinessService;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public interface PuService extends BusinessService {

	// -------------------------------------------------------------------------------
	// -------------------------------------Purch-------------------------------------
	public QueryOperation<PurchQueryParam, PurchInfo> searchPurch(QueryOperation<PurchQueryParam, PurchInfo> _param,
			Map<PurchQueryParam, QueryValue[]> _existsDetailMap);

}
