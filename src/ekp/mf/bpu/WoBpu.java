package ekp.mf.bpu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MfDataService;
import ekp.data.service.mf.WorkorderInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public abstract class WoBpu extends Bpu<Boolean> {
	protected Logger log = LoggerFactory.getLogger(WoBpu.class);
	protected static MfDataService mfDataSerivce = DataServiceFactory.getInstance().getService(MfDataService.class);

	/* base */
	private WorkorderInfo wo;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected WoBpu appendWo(WorkorderInfo wo) {
		this.wo = wo;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public WorkorderInfo getWo() {
		return wo;
	}

	@Override
	public abstract boolean validate(StringBuilder _msg);

	@Override
	public abstract boolean verify(StringBuilder _msg, boolean _full);

	@Override
	protected abstract Boolean buildProcess(TimeTraveler _tt);
}
