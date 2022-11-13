package ekp.mbom.issue.prodCtl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.ProdCtlInfo;
import legion.DataServiceFactory;
import legion.biz.BizObjBuilder;
import legion.util.TimeTraveler;

public abstract class ProdCtlBpu extends BizObjBuilder<Boolean> {
	protected Logger log = LoggerFactory.getLogger(ProdCtlBpu.class);
	protected static MbomDataService mbomDataService = DataServiceFactory.getInstance()
			.getService(MbomDataService.class);

	/* base */
	private ProdCtlInfo prodCtl;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected ProdCtlBpu appendProdCtl(ProdCtlInfo prodCtl) {
		this.prodCtl = prodCtl;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public ProdCtlInfo getProdCtl() {
		return prodCtl;
	}

	// -------------------------------------------------------------------------------
	@Override
	public abstract boolean validate(StringBuilder _msg);

	@Override
	public abstract boolean verify(StringBuilder _msg);

	@Override
	protected abstract Boolean buildProcess(TimeTraveler _tt);

}
