package ekp.invt.bpu.invtOrder;

import java.time.LocalDate;
import java.util.Date;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MbsbStmtInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.material.MaterialInstBuilder0;
import ekp.invt.bpu.material.MbsbStmtBuilderByWo;
import ekp.invt.type.InvtOrderType;
import ekp.invt.type.MaterialInstAcqChannel;
import legion.biz.BpuFacade;
import legion.util.DateFormatUtil;
import legion.util.TimeTraveler;

public class InvtOrderItemBuilder12 extends InvtOrderItemBuilder {
	/* base */
	private WorkorderInfo wo;

	/* data */
	private MaterialInstBuilder0 miBuilder;
	private MbsbStmtBuilderByWo mbsbStmtBuilder;
	private WrhsBinInfo wb;

	@Override
	protected InvtOrderItemBuilder12 appendBase() {
		/* base */
		wo = (WorkorderInfo) args[0];

		String mmUid = wo.getPart().getMmUid();
		double qty = wo.getRqQty();
		appendMmUid(mmUid);
		appendIoType(InvtOrderType.I2);
		appendOrderQty(qty);
		// orderValue to be assigned

		/* data */
		miBuilder = BpuFacade.getInstance().getBuilder(InvtBpuType.MI_0);
		miBuilder.appendMmUid(mmUid);
		miBuilder.appendMiac(MaterialInstAcqChannel.SELF_PRODUCING).appendMiacSrcNo(wo.getWoNo());
		miBuilder.appendQty(qty); // value to be assigned
		Date dateEff = DateFormatUtil.getEarliestTimeInDate(new Date(System.currentTimeMillis()));
		miBuilder.appendEffDate(dateEff.getTime());
		LocalDate ldEff = DateFormatUtil.parseLocalDate(dateEff);
		LocalDate ldExp = ldEff.plusYears(2); // 預設帶2年
		miBuilder.appendExpDate(DateFormatUtil.parseLong(ldExp));

		// mbsbStmtBuilder
		mbsbStmtBuilder = new MbsbStmtBuilderByWo();
		mbsbStmtBuilder.init(wo);

		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public InvtOrderItemBuilder12 appendWb(WrhsBinInfo wb) {
		this.wb = wb;
		mbsbStmtBuilder.appendWb(wb);
		return this;
	}

	public InvtOrderItemBuilder12 appendOrderValue(double orderValue) {
		super.appendOrderValue(orderValue);
		mbsbStmtBuilder.appendStmtValue(orderValue);
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public WrhsBinInfo getWb() {
		return wb;
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
		if (!verifyThis(_msg, _full))
			v = false;

		//
		if (getWb() == null) {
			_msg.append("WrhsBin should NOT be null.").append(System.lineSeparator());
			v = false;
		}

		if (getOrderValue() <= 0) {
			_msg.append("Ordervlue should be GREATER than 0.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected InvtOrderItemInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		/* 1.MaterialInst */
		MaterialInstInfo mi = miBuilder.build(new StringBuilder(), tt);
		if (mi == null) {
			tt.travel();
			log.error("miBuilder.build return null.");
			return null;
		} // copy sites inside

		/* 2.InvtOrderItem */
		InvtOrderItemInfo ioi = buildInvtOrderItem(tt);
		if (ioi == null) {
			tt.travel();
			log.error("buildInvtOrderItem return null.");
			return null;
		} // copy sites inside

		/* 3.MbsbStmt */
		mbsbStmtBuilder.appendMi(mi).appendIoi(ioi);
		StringBuilder msg = new StringBuilder();
		MbsbStmtInfo mbsbStmt = mbsbStmtBuilder.build(msg, tt);
		if (mbsbStmt == null) {
			tt.travel();
			log.error("mbsbStmtBuilder.build return null. {}", msg.toString());
			return null;
		} // copy sites inside

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return ioi.reload();
	}

}
