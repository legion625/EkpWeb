package ekp.sd;

import java.util.Map;

import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.query.SalesOrderQueryParam;
import legion.BusinessService;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public interface SdService extends BusinessService {

	// -------------------------------------------------------------------------------
	// ----------------------------------SalesOrder-----------------------------------
	public QueryOperation<SalesOrderQueryParam, SalesOrderInfo> searchSalesOrder(
			QueryOperation<SalesOrderQueryParam, SalesOrderInfo> _param,
			Map<SalesOrderQueryParam, QueryValue[]> _existsDetailMap);
}
