package ekp.mbom;

import ekp.data.service.mbom.PartInfo;
import legion.BusinessService;

public interface MbomService extends BusinessService{
	
	// -------------------------------------------------------------------------------
	// -------------------------------------Part--------------------------------------
	public PartInfo loadPartByPin(String _pin);
}
