package ekp.invt.bpu.material;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialBinStockBatchInfo;
import ekp.data.service.invt.MbsbStmtInfo;
import ekp.data.service.mf.WorkorderMaterialInfo;
import ekp.invt.type.MbsbFlowType;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public class MbsbStmtBuilderByWom extends MbsbStmtBuilder{
	/* base */
	// none
	
	/* data */
	private MaterialBinStockBatchInfo mbsb;
	private InvtOrderItemInfo ioi;

	// -------------------------------------------------------------------------------
	@Override
	protected MbsbStmtBuilderByWom appendBase() {
		/* base */
		// none
		
		/* data */
		appendMbsbFlowType(MbsbFlowType.OUT);
		
		return this;
	}
	
	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	@Override
	public MbsbStmtBuilderByWom appendMbsbUid(String mbsbUid) {
		return (MbsbStmtBuilderByWom) super.appendMbsbUid(mbsbUid);
	}

	public MbsbStmtBuilderByWom appendIoi(InvtOrderItemInfo ioi) {
		this.ioi = ioi;
		super.appendIoiUid(ioi.getUid());
		return this;
	}
	

	@Override
	public MbsbStmtBuilderByWom appendStmtQty(double stmtQty) {
		return (MbsbStmtBuilderByWom)super.appendStmtQty(stmtQty);
	}
	
	@Override
	public MbsbStmtBuilderByWom appendStmtValue(double stmtValue) {
		return (MbsbStmtBuilderByWom)super.appendStmtValue(stmtValue);
	}
	
	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public InvtOrderItemInfo getIoi() {
		return ioi;
	}
	
	
	// -------------------------------------------------------------------------------
	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;

		/* base */
		if (_full) {
			if (getIoi() == null) {
				_msg.append("InvtOrderItem null.").append(System.lineSeparator());
				v = false;
			} else {
				if (getIoi().isMbsbStmtCreated()) {
					_msg.append("InvtOrderItem mbsbStmtCreated should be FALSE.").append(System.lineSeparator());
					v = false;
				}
			}
		}

		/**/
		if (DataFO.isEmptyString(getMbsbUid())) {
			_msg.append("mbsbUid should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		
		if (getMbsbFlowType() == null || MbsbFlowType.UNDEFINED == getMbsbFlowType()) {
			_msg.append("mbsbFlowType error.").append(System.lineSeparator());
			v = false;
		}

		if (getStmtQty() == 0 && getStmtValue() == 0) {
			_msg.append("Qty/Value error.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	protected MbsbStmtInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		
		/* 1. */
		MbsbStmtInfo stmt = buildMbsbStmt(tt);
		if(stmt==null) {
			tt.travel();
			log.error("buildMbsbStmt return null");
			return null;
		}// copy inside
		
		/* 2. */
		if(!invtDataService.invtOrderItemMbsbStmtCreated(getIoiUid())) { 
			tt.travel();
			log.error("invtOrderItemMbsbStmtCreated return false");
			return null;
		}
		tt.addSite("revert invtOrderItemMbsbStmtCreated",
				() -> invtDataService.invtOrderItemRevertMbsbStmtCreated(getIoiUid()));
		log.info("invtDataService.invtOrderItemMbsbStmtCreated");
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return stmt;
	}
	
}