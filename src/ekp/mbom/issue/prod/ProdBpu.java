package ekp.mbom.issue.prod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.ProdInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public abstract class ProdBpu extends Bpu<Boolean> {
	protected Logger log = LoggerFactory.getLogger(ProdBpu.class);
	protected static MbomDataService mbomDataService = DataServiceFactory.getInstance()
			.getService(MbomDataService.class);

	/* base */
	private ProdInfo prod;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected ProdBpu appendProd(ProdInfo prod) {
		this.prod = prod;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public ProdInfo getProd() {
		return prod;
	}

	// -------------------------------------------------------------------------------
	@Override
	public abstract boolean validate(StringBuilder _msg);

	@Override
	public abstract boolean verify(StringBuilder _msg, boolean _full);

	@Override
	protected abstract Boolean buildProcess(TimeTraveler _tt);
}
