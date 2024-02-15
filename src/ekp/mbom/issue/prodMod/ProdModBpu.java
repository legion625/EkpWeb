package ekp.mbom.issue.prodMod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.ProdInfo;
import ekp.data.service.mbom.ProdModInfo;
import ekp.mbom.issue.prod.ProdBpu;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public abstract class ProdModBpu extends Bpu<Boolean> {
	protected Logger log = LoggerFactory.getLogger(ProdModBpu.class);
	protected static MbomDataService mbomDataService = DataServiceFactory.getInstance()
			.getService(MbomDataService.class);

	/* base */
	private ProdModInfo prodMod;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected ProdModBpu appendProdMod(ProdModInfo prodMod) {
		this.prodMod = prodMod;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public ProdModInfo getProdMod() {
		return prodMod;
	}

	// -------------------------------------------------------------------------------
	@Override
	public abstract boolean validate(StringBuilder _msg);

	@Override
	public abstract boolean verify(StringBuilder _msg, boolean _full);

	@Override
	protected abstract Boolean buildProcess(TimeTraveler _tt);
}
