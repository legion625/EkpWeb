package ekp.mbom.issue.prodMod;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.ProdModItemInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public class ProdModItemBuilder extends Bpu<ProdModItemInfo> {
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);

	/* base */
	// none

	/* data */
	private String prodModUid;
	private String prodCtlUid;

	// -------------------------------------------------------------------------------
	@Override
	protected Bpu<ProdModItemInfo> appendBase() {
		prodModUid = (String) args[0];
		prodCtlUid = (String) args[1];

		return this;
	}
	
	// -------------------------------------------------------------------------------
	public ProdModItemBuilder appendProdModUid(String prodModUid) {
		this.prodModUid = prodModUid;
		return this;
	}

	public ProdModItemBuilder appendProdCtlUid(String prodCtlUid) {
		this.prodCtlUid = prodCtlUid;
		return this;
	}

	// -------------------------------------------------------------------------------
	public String getProdModUid() {
		return prodModUid;
	}

	

	public String getProdCtlUid() {
		return prodCtlUid;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;

		if (_full) {
			if (DataFO.isEmptyString(getProdModUid())) {
				_msg.append("prodModUid should not be empty.").append(System.lineSeparator());
				v = false;
			}
		}
		
		if (DataFO.isEmptyString(getProdCtlUid())) {
			_msg.append("prodCtlUid should not be empty.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	@Override
	protected ProdModItemInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		ProdModItemInfo pmi = mbomDataService.createProdModItem(getProdModUid(), getProdCtlUid());
		if (pmi == null) {
			tt.travel();
			log.error("mbomDataSerivce.createProdModItem return null.");
			return null;
		}
		tt.addSite("revert createProdModItem", () -> mbomDataService.deleteProdModItem(pmi.getUid()));
		log.info("mbomDataService.createProdModItem [{}][{}]", pmi.getProdModUid(), pmi.getProdCtlUid());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return pmi;
	}

}
