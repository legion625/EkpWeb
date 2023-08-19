package ekp.invt.bpu.wrhsLoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.InvtDataService;
import ekp.data.service.invt.WrhsBinInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public abstract class WrhsBinBpu extends Bpu<Boolean> {
	protected Logger log = LoggerFactory.getLogger(WrhsBinBpu.class);
	protected static InvtDataService invtDataService = DataServiceFactory.getInstance()
			.getService(InvtDataService.class);

	/* base */
	private WrhsBinInfo wrhsBin;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected WrhsBinBpu appendWrhsBin(WrhsBinInfo wrhsBin) {
		this.wrhsBin = wrhsBin;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public WrhsBinInfo getWrhsBin() {
		return wrhsBin;
	}

	@Override
	public abstract boolean validate(StringBuilder _msg);

	@Override
	public abstract boolean verify(StringBuilder _msg);

	@Override
	protected abstract Boolean buildProcess(TimeTraveler _tt);

}
