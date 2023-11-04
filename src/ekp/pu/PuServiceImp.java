package ekp.pu;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.PuDataService;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.query.PurchQueryParam;
import legion.DataServiceFactory;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public class PuServiceImp implements PuService{
	private Logger log = LoggerFactory.getLogger(PuServiceImp.class);
	
	private static PuDataService dataService;

	@Override
	public void register(Map<String, String> _params) {
		log.debug("MbomServiceImp.register");
		dataService= DataServiceFactory.getInstance().getService(PuDataService.class);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	
	// -------------------------------------------------------------------------------
	// -------------------------------------Purch-------------------------------------
	@Override
	public QueryOperation<PurchQueryParam, PurchInfo> searchPurch(QueryOperation<PurchQueryParam, PurchInfo> _param,
			Map<PurchQueryParam, QueryValue[]> _existsDetailMap){
		return dataService.searchPurch(_param, _existsDetailMap);
	}

}
