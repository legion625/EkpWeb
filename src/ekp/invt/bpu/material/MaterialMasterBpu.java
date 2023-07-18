package ekp.invt.bpu.material;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.InvtDataService;
import ekp.data.service.invt.MaterialMasterInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public abstract class MaterialMasterBpu extends Bpu<Boolean> {
	protected Logger log = LoggerFactory.getLogger(MaterialMasterBpu.class);
	protected static InvtDataService invtDataService = DataServiceFactory.getInstance()
			.getService(InvtDataService.class);

	/* base */
	private MaterialMasterInfo mm;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected MaterialMasterBpu appendMm(MaterialMasterInfo mm) {
		this.mm = mm;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public MaterialMasterInfo getMm() {
		return mm;
	}

	@Override
	public abstract boolean validate(StringBuilder _msg);

	@Override
	public abstract boolean verify(StringBuilder _msg);

	@Override
	protected abstract Boolean buildProcess(TimeTraveler _tt);
}
