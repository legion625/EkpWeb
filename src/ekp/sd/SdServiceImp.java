package ekp.sd;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.PuDataService;
import ekp.data.SdDataService;
import legion.DataServiceFactory;

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
}
