package ekp.invt.bpu.invtOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MfDataService;
import ekp.data.SdDataService;
import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MbsbStmtInfo;
import ekp.data.service.mf.WorkorderMaterialInfo;
import ekp.data.service.sd.SalesOrderItemInfo;
import ekp.invt.bpu.material.MbsbStmtBuilder2;
import ekp.invt.type.InvtOrderType;
import ekp.invt.type.IoiTargetType;
import legion.DataServiceFactory;
import legion.util.DateFormatUtil;
import legion.util.TimeTraveler;

public class InvtOrderItemBuilder29 extends InvtOrderItemBuilder {
	protected Logger log = LoggerFactory.getLogger(InvtOrderItemBuilder29.class);

	private static SdDataService sdDataSerivce = DataServiceFactory.getInstance().getService(SdDataService.class);

	/* base */
	private SalesOrderItemInfo soi;

	/* data */
	private List<MbsbStmtBuilder2> mbsbStmtBuilderList;
	private long finishDeliveredDate;

	@Override
	protected InvtOrderItemBuilder29 appendBase() {
		/* base */
		soi = (SalesOrderItemInfo) args[0];

		/* data */
		appendMmUid(soi.getMmUid());
		appendIoType(InvtOrderType.O9);
		appendTargetType(IoiTargetType.SOI).appendTargetUid(soi.getUid()).appendTargetBizKey(soi.getSo().getSosn());
		// orderQty和orderValue必須依賴從Mbsb挑完才能決定
		mbsbStmtBuilderList = new ArrayList<>();

		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public MbsbStmtBuilder2 addMbsbStmtBuilder(String _mbsbUid, double _stmtQty, double _stmtValue) {
		MbsbStmtBuilder2 b = new MbsbStmtBuilder2();
		b.init();
		b.appendMbsbUid(_mbsbUid);
		b.appendStmtQty(_stmtQty).appendStmtValue(_stmtValue);
		mbsbStmtBuilderList.add(b);
		return b;
	}

	public InvtOrderItemBuilder29 appendFinishDeliveredDate(long finishDeliveredDate) {
		this.finishDeliveredDate = finishDeliveredDate;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	@Override
	public double getOrderQty() {
		return getSumMbsbStmtBuilderQty();
	}

	@Override
	public double getOrderValue() {
		return getSumMbsbStmtBuilderValue();
	}

	public SalesOrderItemInfo getSoi() {
		return soi;
	}

	public List<MbsbStmtBuilder2> getMbsbStmtBuilderList() {
		return mbsbStmtBuilderList;
	}

	public double getSumMbsbStmtBuilderQty() {
		return getMbsbStmtBuilderList().stream().mapToDouble(b -> b.getStmtQty()).sum();
	}

	public double getSumMbsbStmtBuilderValue() {
		return getMbsbStmtBuilderList().stream().mapToDouble(b -> b.getStmtValue()).sum();
	}

	public long getFinishDeliveredDate() {
		return finishDeliveredDate;
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

		if (getSoi().getQty() != getSumMbsbStmtBuilderQty()) {
			_msg.append("訂單項目的應領數量和欲領數量不同。").append(System.lineSeparator());
			v = false;
		}

		if (getFinishDeliveredDate() <= 0) {
			_msg.append("FinishDeliveredDate error").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected InvtOrderItemInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		/* 1.InvtOrderItem */
		InvtOrderItemInfo ioi = buildInvtOrderItem(tt);
		if (ioi == null) {
			tt.travel();
			log.error("buildInvtOrderItem return null.");
			return null;
		} // copy sites inside

		/* 2.MbsbStmt */
		for (MbsbStmtBuilder2 mbsbStmtBuilder : getMbsbStmtBuilderList()) {
			mbsbStmtBuilder.appendIoi(ioi);
			StringBuilder msg = new StringBuilder();
			MbsbStmtInfo mbsbStmt = mbsbStmtBuilder.build(msg, tt);
			if (mbsbStmt == null) {
				tt.travel();
				log.error("mbsbStmtBuilder.build return null. {}", msg.toString());
				return null;
			} // copy sites inside
		}

		/**/
		if (!sdDataSerivce.soiFinishDeliver(getSoi().getUid(), getFinishDeliveredDate())) {
			tt.travel();
			log.error("sdDataSerivce.soiFinishDeliver return false.");
			return null;
		}
		tt.addSite("revert soiFinishDeliver", () -> sdDataSerivce.soiRevertFinishDeliver(getSoi().getUid()));
		soi = soi.reload();
		log.info("sdDataSerivce.soiFinishDeliver {}\t{}\t{}\t{}", getSoi().getSo().getSosn(), getSoi().getMmMano(),
				getSoi().isAllDelivered(), DateFormatUtil.transToDate(new Date(getSoi().getFinishDeliveredDate())));

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return ioi.reload();
	}

}
