package ekp.invt.bpu.wrhsLoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.InvtDataService;
import ekp.data.service.invt.WrhsLocInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public abstract class WrhsLocBpu extends Bpu<Boolean> {
	protected Logger log = LoggerFactory.getLogger(WrhsLocBpu.class);
	protected static InvtDataService invtDataService = DataServiceFactory.getInstance()
			.getService(InvtDataService.class);
	

	/* base */
	private WrhsLocInfo wrhsLoc;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected WrhsLocBpu appendWrhsLoc(WrhsLocInfo wrhsLoc) {
		this.wrhsLoc = wrhsLoc;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public WrhsLocInfo getWrhsLoc() {
		return wrhsLoc;
	}

	@Override
	public abstract boolean validate(StringBuilder _msg);

	@Override
	public abstract boolean verify(StringBuilder _msg);

	@Override
	protected abstract Boolean buildProcess(TimeTraveler _tt);

}
