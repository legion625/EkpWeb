package ekp.pu;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.PuDataService;
import legion.DataServiceFactory;

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

}
