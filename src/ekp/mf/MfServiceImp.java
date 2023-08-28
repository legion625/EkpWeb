package ekp.mf;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MfDataService;
import legion.DataServiceFactory;

public class MfServiceImp implements MfService{
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
	

}
