package ekp.pu.bpu;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.mbom.type.PartUnit;
import legion.util.TimeTraveler;

public class PurchItemBuilder1 extends PurchItemBuilder{
	/* base */
	// none
	
	/* data */
	private MaterialMasterInfo mm;
	// none
	
	// -------------------------------------------------------------------------------
	@Override
	protected PurchItemBuilder1 appendBase() {
		/* base */
		// none
		
		/* data */
		// none
		
		return this;
	}

	// -------------------------------------------------------------------------------
	public PurchItemBuilder1 appendMm(MaterialMasterInfo mm) {
		this.mm = mm;
		appendMmUid(mm.getUid()).appendMmMano(mm.getMano()).appendMmName(mm.getName())
				.appendMmSpecification(mm.getSpecification()).appendMmStdUnit(mm.getStdUnit());
		return this;
	}

	public PurchItemBuilder1 appendQty(double qty) {
		return (PurchItemBuilder1) super.appendQty(qty);
	}

	public PurchItemBuilder1 appendValue(double value) {
		return (PurchItemBuilder1) super.appendValue(value);
	}

	public PurchItemBuilder1 appendRemark(String remark) {
		return (PurchItemBuilder1) super.appendRemark(remark);
	}

	// -------------------------------------------------------------------------------
	public MaterialMasterInfo getMm() {
		return mm;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		// none
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;

		if(!verifyThis(_msg,  _full))
			v = false;
		
		if (getMm() == null) {
			_msg.append("料件基本檔有誤。").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected PurchItemInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		PurchItemInfo pi = buildPurchItem(tt);
		if (pi == null) {
			tt.travel();
			log.error("buildPurchItem return null.");
			return null;
		} // copy sites inside

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return pi;
	}
	
}
