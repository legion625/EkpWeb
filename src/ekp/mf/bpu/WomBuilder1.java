package ekp.mf.bpu;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.WorkorderMaterialInfo;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public class WomBuilder1 extends WomBuilder {

	/* base */
	// none

	/* data */
	private WorkorderInfo wo;
	private MaterialMasterInfo mm;
	private double qty;

	@Override
	protected Bpu<WorkorderMaterialInfo> appendBase() {
		/* base */
		// none
		
		/* data */
		// none
		
		return this;
	}
	
	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	WomBuilder1 appendWo(WorkorderInfo wo) {
		this.wo = wo;
		appendWoUid(wo.getUid()).appendWoNo(wo.getWoNo());
		return this;
	}

	 WomBuilder1 appendMm(MaterialMasterInfo mm) {
		this.mm = mm;
		appendMmUid(mm.getUid()).appendMmMano(mm.getMano()).appendMmName(mm.getName());
		return this;
	}
	 
	 WomBuilder1 appendQty(double qty) {
		 this.qty = qty;
		 return this;
	 }
	 

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public WorkorderInfo getWo() {
		return wo;
	}

	public MaterialMasterInfo getMm() {
		return mm;
	}
	
	public double getQty() {
		return qty;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = true;
		if (!super.verify(_msg))
			v = false;

		if (getQty() <= 0) {
			_msg.append("qty error.").append(System.lineSeparator());
			v = false;
		}
		return v;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected WorkorderMaterialInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		//
		WorkorderMaterialInfo wom = mfDataService.createWorkorderMaterial(packWorkorderMaterialCreateObj());
		if (wom == null) {
			tt.travel();
			log.error("mfDataService.createWorkorderMaterial return null.");
			return null;
		}
		tt.addSite("revert createWorkorderMaterial", () -> mfDataService.deleteWorkorderMaterial(wom.getUid()));
		log.info("mfDataService.createWorkorderMaterial [{}][{}][{}][{}]", wom.getUid(), wom.getWoNo(), wom.getMmMano(),
				wom.getMmName());

		//
		if (!mfDataService.womAddQty0(wom.getUid(), getQty())) {
			tt.travel();
			log.error("mfDataService.womAddQty0 return false.");
			return null;
		}
		tt.addSite("revert womAddQty0", () -> mfDataService.womAddQty0(wom.getUid(), -getQty()));
		WorkorderMaterialInfo newWom = wom.reload();
		log.info("mfDataService.womAddQty0 [{}][{}]", newWom.getUid(), newWom.getQty0());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return newWom;
	}

}

