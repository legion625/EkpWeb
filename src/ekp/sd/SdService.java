package ekp.sd;

import java.util.List;
import java.util.Map;

import ekp.data.service.sd.BizPartnerInfo;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.query.SalesOrderQueryParam;
import legion.BusinessService;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public interface SdService extends BusinessService {

	// -------------------------------------------------------------------------------
	// ----------------------------------BizPartner-----------------------------------
	public List<BizPartnerInfo> loadBizPartnerList();
	
	default List<BizPartnerInfo> loadCustomerList(){
		return loadBizPartnerList().stream().filter(BizPartnerInfo::isCustomer).toList();
	}
	
	public boolean bpToggleSupplier(String _uid, boolean _supplier); 
	public boolean bpToggleCustomer(String _uid, boolean _customer);
	
	// -------------------------------------------------------------------------------
	// ----------------------------------SalesOrder-----------------------------------
	public QueryOperation<SalesOrderQueryParam, SalesOrderInfo> searchSalesOrder(
			QueryOperation<SalesOrderQueryParam, SalesOrderInfo> _param,
			Map<SalesOrderQueryParam, QueryValue[]> _existsDetailMap);
}
