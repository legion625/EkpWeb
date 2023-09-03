package ekp.invt.bpu.invtOrder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MbsbStmtInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.material.MaterialInstBuilder0;
import ekp.invt.bpu.material.MbsbStmtBuilderByPurchItem;
import ekp.invt.type.InvtOrderType;
import ekp.invt.type.MaterialInstAcqChannel;
import legion.biz.BpuFacade;
import legion.util.DateFormatUtil;
import legion.util.TimeTraveler;

public class InvtOrderItemBuilder11 extends InvtOrderItemBuilder {
	/* base */
	private PurchItemInfo pi;

	/* data */
	private MaterialInstBuilder0 miBuilder;
	private MbsbStmtBuilderByPurchItem mbsbStmtBuilder;
	private WrhsBinInfo wb;

	@Override
	protected InvtOrderItemBuilder11 appendBase() {
		/* base */
		pi = (PurchItemInfo) args[0];

		appendMmUid(pi.getMmUid());
		appendIoType(InvtOrderType.I1);
		appendOrderQty(pi.getQty()).appendOrderValue(pi.getValue());

		/* data */
		// miBuilder
		miBuilder = BpuFacade.getInstance().getBuilder(InvtBpuType.MI_0);
		miBuilder.appendMmUid(pi.getMmUid());
		miBuilder.appendMiac(MaterialInstAcqChannel.PURCHASING).appendMiacSrcNo(pi.getPurch().getPuNo());
		miBuilder.appendQty(pi.getQty()).appendValue(pi.getValue());
		Date dateEff = DateFormatUtil.getEarliestTimeInDate(new Date(System.currentTimeMillis()));
		miBuilder.appendEffDate(dateEff.getTime());
		LocalDate ldEff = DateFormatUtil.parseLocalDate(dateEff);
		LocalDate ldExp = ldEff.plusYears(2); // 預設帶2年
		miBuilder.appendExpDate(DateFormatUtil.parseLong(ldExp));

		// mbsbStmtBuilderList
		mbsbStmtBuilder = new MbsbStmtBuilderByPurchItem();
		mbsbStmtBuilder.init(pi);

		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	InvtOrderItemBuilder11 appendWb(WrhsBinInfo wb) {
		this.wb = wb;
		mbsbStmtBuilder.appendWb(wb);
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
