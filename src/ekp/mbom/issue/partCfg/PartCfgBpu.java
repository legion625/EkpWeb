package ekp.mbom.issue.partCfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.TestLogMark;
import ekp.data.MbomDataService;
import ekp.data.service.mbom.PartCfgInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public abstract class PartCfgBpu extends Bpu<Boolean> {
//	protected Logger log = LoggerFactory.getLogger(PartCfgBpu.class);
	protected Logger log = LoggerFactory.getLogger(DebugLogMark.class);
	protected static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);

	/* base */
	private PartCfgInfo partCfg;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected PartCfgBpu appendPartCfg(PartCfgInfo partCfg) {
		this.partCfg = partCfg;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public PartCfgInfo getPartCfg() {
		return partCfg;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public abstract boolean validate(StringBuilder _msg);

	@Override
	public abstract boolean verify(StringBuilder _msg);

	@Override
	protected abstract Boolean buildProcess(TimeTraveler _tt);

}
