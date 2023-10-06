package ekp.invt.bpu.material;

import ekp.data.InvtDataService;
import ekp.data.service.invt.MaterialInstInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public abstract class MaterialInstBpu extends Bpu<Boolean> {
	protected static InvtDataService invtDataService = DataServiceFactory.getInstance()
			.getService(InvtDataService.class);

	/* base */
	private MaterialInstInfo mi;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected MaterialInstBpu appendMi(MaterialInstInfo mi) {
		this.mi = mi;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public MaterialInstInfo getMi() {
		return mi;
	}

	@Override
	public abstract boolean validate(StringBuilder _msg);

	@Override
	public abstract boolean verify(StringBuilder _msg, boolean _full);

	@Override
	protected abstract Boolean buildProcess(TimeTraveler _tt);

}
