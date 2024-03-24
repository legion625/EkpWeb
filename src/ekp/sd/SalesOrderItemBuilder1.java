package ekp.sd;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.sd.SalesOrderItemInfo;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public class SalesOrderItemBuilder1 extends SalesOrderItemBuilder{
	/* base */
	// none
	
	/* data */
	private MaterialMasterInfo mm;
	
	// -------------------------------------------------------------------------------
	@Override
	protected SalesOrderItemBuilder1 appendBase() {
		/* base */
		// none
		
		/* data */
		// none
		
		return this;
	}
	
	// -------------------------------------------------------------------------------
	public SalesOrderItemBuilder1 appendMm(MaterialMasterInfo mm) {
		this.mm = mm;
		if (mm == null)
			appendMmUid("").appendMmMano("").appendMmName("").appendMmSpec("");
		else
			appendMmUid(mm.getUid()).appendMmMano(mm.getMano()).appendMmName(mm.getName())
					.appendMmSpec(mm.getSpecification());
		return this;
	}
	public SalesOrderItemBuilder1 appendQty(double qty) {
		return (SalesOrderItemBuilder1) super.appendQty(qty);
	}
	public SalesOrderItemBuilder1 appendValue(double value) {
		return (SalesOrderItemBuilder1)super.appendValue(value);
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
		
		if (getMm() == null) {
			_msg.append("料件基本檔有誤。").append(System.lineSeparator());
			v = false;
		}

		if (getQty() <= 0 || getValue() <= 0) {
			_msg.append("數量或是金額有誤。").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	@Override
	protected SalesOrderItemInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		log.debug("getSoUid(): {}", getSoUid());
		SalesOrderItemInfo soi = buildSalesOrderItem(tt, getSoUid());
		if(soi == null) {
			tt.travel();
			log.error("buildSalesOrderItem return null.");
			return null;
		} // copy sites inside
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return soi;
	}

}
