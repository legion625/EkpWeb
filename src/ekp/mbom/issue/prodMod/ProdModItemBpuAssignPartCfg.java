package ekp.mbom.issue.prodMod;

import ekp.data.service.mbom.ProdModItemInfo;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public class ProdModItemBpuAssignPartCfg extends ProdModItemBpu {
	/* base */
	private ProdModItemInfo prodModItem;

	/* data */
	private String partCfgUid;

	// -------------------------------------------------------------------------------
	@Override
	protected ProdModItemBpuAssignPartCfg appendBase() {
		/* base */
		prodModItem = (ProdModItemInfo) args[0];
		appendProdModItem(prodModItem);

		return this;
	}

	// -------------------------------------------------------------------------------
	public ProdModItemBpuAssignPartCfg appendPartCfgUid(String partCfgUid) {
		this.partCfgUid = partCfgUid;
		return this;
	}
	// -------------------------------------------------------------------------------

	public String getPartCfgUid() {
		return partCfgUid;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;

		/* ProdModItem */
		if (getProdModItem() == null) {
			_msg.append("ProdModItem null.").append(System.lineSeparator());
			v = false;
		}

		if (DataFO.isEmptyString(getPartCfgUid())) {
			_msg.append("partCfgUid null.").append(System.lineSeparator());
			v = false;
		}

		return v;

	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		//
		if (!mbomDataService.prodModItemAssignPartCfg(getProdModItem().getUid(), getPartCfgUid())) {
			tt.travel();
			log.error("mbomDataSerivce.prodModItemAssignPartCfg return false.");
			return null;
		}
		tt.addSite("revert prodModItemAssignPartCfg",
				() -> mbomDataService.prodModItemUnassignPartCfg(getProdModItem().getUid()));

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return true;
	}
}
