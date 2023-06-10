package ekp.mbom.issue.partAcq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public abstract class PaBpu extends Bpu<Boolean> {
	protected Logger log = LoggerFactory.getLogger(PaBpu.class);
	protected static MbomDataService mbomDataService = DataServiceFactory.getInstance()
			.getService(MbomDataService.class);

	/* base */
	private PartAcqInfo pa;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected PaBpu appendPa(PartAcqInfo pa) {
		this.pa = pa;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public PartAcqInfo getPa() {
		return pa;
	}

	@Override
	public abstract boolean validate(StringBuilder _msg);

	@Override
	public abstract boolean verify(StringBuilder _msg);

	@Override
	protected abstract Boolean buildProcess(TimeTraveler _tt);
}
