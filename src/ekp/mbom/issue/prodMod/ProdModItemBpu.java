package ekp.mbom.issue.prodMod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.ProdModItemInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public abstract class ProdModItemBpu extends Bpu<Boolean> {
	protected Logger log = LoggerFactory.getLogger(ProdModItemBpu.class);
	protected static MbomDataService mbomDataService = DataServiceFactory.getInstance()
			.getService(MbomDataService.class);

	/* base */
	private ProdModItemInfo prodModItem;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected ProdModItemBpu appendProdModItem(ProdModItemInfo prodModItem) {
		this.prodModItem = prodModItem;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public ProdModItemInfo getProdModItem() {
		return prodModItem;
	}

	// -------------------------------------------------------------------------------
	@Override
	public abstract boolean validate(StringBuilder _msg);

	@Override
	public abstract boolean verify(StringBuilder _msg, boolean _full);

	@Override
	protected abstract Boolean buildProcess(TimeTraveler _tt);
}
