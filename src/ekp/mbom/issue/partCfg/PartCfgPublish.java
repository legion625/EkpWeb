package ekp.mbom.issue.partCfg;

import ekp.data.service.mbom.PartCfgInfo;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public class PartCfgPublish extends PartCfgBpu{

	/* base */
	private PartCfgInfo partCfg;

	/* data */
	// none
	
	
	
	@Override
	public boolean validate(StringBuilder _msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Bpu<Boolean> appendBase() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
