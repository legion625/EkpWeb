package ekp.invt;

import java.util.List;

import ekp.data.service.invt.WrhsLocInfo;
import legion.BusinessService;

public interface InvtService extends BusinessService {

	// -------------------------------------------------------------------------------
	// ------------------------------------WrhsLoc------------------------------------
	public List<WrhsLocInfo> loadWrhsLocList();
}