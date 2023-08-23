package ekp.invt.bpu.invtOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.InvtDataService;
import ekp.data.service.invt.InvtOrderInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public abstract class IoBpu extends Bpu<Boolean> {
	protected Logger log = LoggerFactory.getLogger(IoBpu.class);
	protected static InvtDataService invtDataService = DataServiceFactory.getInstance()
			.getService(InvtDataService.class);

	/* base */
	private InvtOrderInfo io;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected IoBpu appendIo(InvtOrderInfo io) {
		this.io = io;
		return this;
	}

	@Override
	public abstract boolean validate(StringBuilder _msg);

	@Override
	public abstract boolean verify(StringBuilder _msg);

	@Override
	protected abstract Boolean buildProcess(TimeTraveler _tt);

}
