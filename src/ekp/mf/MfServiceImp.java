package ekp.mf;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MfDataService;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.query.WorkorderQueryParam;
import legion.DataServiceFactory;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public class MfServiceImp implements MfService {
	private Logger log = LoggerFactory.getLogger(MfServiceImp.class);

	private static MfDataService dataService;

	@Override
	public void register(Map<String, String> _params) {
		log.debug("MfServiceImp.register");
		dataService = DataServiceFactory.getInstance().getService(MfDataService.class);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------Workorder-----------------------------------
	@Override
	public QueryOperation<WorkorderQueryParam, WorkorderInfo> searchWorkorder(
			QueryOperation<WorkorderQueryParam, WorkorderInfo> _param,
			Map<WorkorderQueryParam, QueryValue[]> _existsDetailMap) {
		return dataService.searchWorkorder(_param, _existsDetailMap);
	}

}
