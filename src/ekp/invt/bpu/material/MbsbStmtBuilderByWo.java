package ekp.invt.bpu.material;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialBinStockBatchInfo;
import ekp.data.service.invt.MaterialBinStockInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MbsbStmtInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.invt.MaterialBinStockFacade;
import ekp.invt.type.MbsbFlowType;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public class MbsbStmtBuilderByWo extends MbsbStmtBuilder {
	/* base */
	private WorkorderInfo wo;

	/**/
	private InvtOrderItemInfo ioi;
	private MaterialInstInfo mi;
	private WrhsBinInfo wb;
	// mi+wb->mbsb，在InvtOrderItemBuilder12.buildProcess才能給定。
	// mbsbStmtBuilder的verify把mbsbUid和ioiUid放在_full裡才檢查。

	// -------------------------------------------------------------------------------
	@Override
	protected MbsbStmtBuilderByWo appendBase() {
		/* base */
		wo = (WorkorderInfo) args[0];

		// mbsbUid和ioiUid在執行面才能產生

		/* data */
		appendMbsbFlowType(MbsbFlowType.IN);
		// 工令依需求量入帳
		appendStmtQty(wo.getRqQty());
		// 金額待訂

		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public MbsbStmtBuilderByWo appendIoi(InvtOrderItemInfo ioi) {
		this.ioi = ioi;
		super.appendIoiUid(ioi.getUid());
		return this;
	}

	public MbsbStmtBuilderByWo appendMi(MaterialInstInfo mi) {
		this.mi = mi;
		return this;
	}

	public MbsbStmtBuilderByWo appendWb(WrhsBinInfo wb) {
		this.wb = wb;
		return this;
	}

	@Override
	public MbsbStmtBuilderByWo appendStmtValue(double stmtValue) {
		return (MbsbStmtBuilderByWo) super.appendStmtValue(stmtValue);
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	@Override
	public String getMbsbUid() {
		return getMbsb() == null ? null : getMbsb().getUid();
	}

	public InvtOrderItemInfo getIoi() {
		return ioi;
	}

	public WorkorderInfo getWo() {
		return wo;
	}

	public MaterialInstInfo getMi() {
		return mi;
	}

	public WrhsBinInfo getWb() {
		return wb;
	}

	private MaterialBinStockBatchInfo getMbsb() {
		if (getMi() == null || getWb() == null)
			return null;

		MaterialBinStockFacade mbsFacade = MaterialBinStockFacade.get();
		MaterialBinStockInfo mbs = mbsFacade.getMbs(getWo().getPartAcq().getMmUid(), getWb().getUid());
		if (mbs == null) {
			log.error("getMbs return null.");
			return null;
		}
		return mbsFacade.getMbsb(mbs, getMi().getUid());
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;

		/* base */
		if (_full) {
			if (DataFO.isEmptyString(getMbsbUid())) {
				_msg.append("mbsbUid should NOT be empty.").append(System.lineSeparator());
				v = false;
			}
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
		if (getMbsbFlowType() == null || MbsbFlowType.UNDEFINED == getMbsbFlowType()) {
			_msg.append("mbsbFlowType error.").append(System.lineSeparator());
			v = false;
		}

		if (getStmtQty() <= 0) {
			_msg.append("StmtQty error.").append(System.lineSeparator());
			v = false;
		}

		if (getStmtValue() <= 0) {
			_msg.append("StmtValue error.").append(System.lineSeparator());
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
		if (stmt == null) {
			tt.travel();
			log.error("buildMbsbStmt return null");
			return null;
		} // copy inside

		/* 2. */
		if (!invtDataService.invtOrderItemMbsbStmtCreated(getIoiUid())) {
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
