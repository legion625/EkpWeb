package ekp.mbom.issue.parsPart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.PpartInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public abstract class PpartBpu extends Bpu<Boolean> {
	protected Logger log = LoggerFactory.getLogger(PpartBpu.class);
	protected static MbomDataService mbomDataService = DataServiceFactory.getInstance()
			.getService(MbomDataService.class);

	/* base */
	private PpartInfo ppart;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected PpartBpu appendPpart(PpartInfo ppart) {
		this.ppart = ppart;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public PpartInfo getPpart() {
		return ppart;
	}

	@Override
	public abstract boolean validate(StringBuilder _msg);

	@Override
	public abstract boolean verify(StringBuilder _msg);

	@Override
	protected abstract Boolean buildProcess(TimeTraveler _tt);

}
