package ekp.sd;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.PuDataService;
import ekp.data.SdDataService;
import ekp.data.service.sd.BizPartnerInfo;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.query.SalesOrderQueryParam;
import legion.DataServiceFactory;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public class SdServiceImp implements SdService {
	private Logger log = LoggerFactory.getLogger(SdServiceImp.class);
	private static SdDataService dataService;

	@Override
	public void register(Map<String, String> _params) {
		log.debug("MbomServiceImp.register");
		dataService = DataServiceFactory.getInstance().getService(SdDataService.class);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	// -------------------------------------------------------------------------------
	// ----------------------------------BizPartner-----------------------------------
	@Override
	public List<BizPartnerInfo> loadBizPartnerList(){
		return dataService.loadBizPartnerList();
	}
	@Override
	public boolean bpToggleSupplier(String _uid, boolean _supplier) {
		return dataService.bpToggleSupplier(_uid, _supplier);
	}
	@Override
	public boolean bpToggleCustomer(String _uid, boolean _customer) {
		return dataService.bpToggleCustomer(_uid, _customer);
	}
	
	// -------------------------------------------------------------------------------
	// ----------------------------------SalesOrder-----------------------------------
	@Override
	public QueryOperation<SalesOrderQueryParam, SalesOrderInfo> searchSalesOrder(
			QueryOperation<SalesOrderQueryParam, SalesOrderInfo> _param,
			Map<SalesOrderQueryParam, QueryValue[]> _existsDetailMap) {
		return dataService.searchSalesOrder(_param, _existsDetailMap);
	}
}
