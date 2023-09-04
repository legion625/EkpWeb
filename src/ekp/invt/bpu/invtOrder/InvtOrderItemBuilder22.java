package ekp.invt.bpu.invtOrder;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.data.MfDataService;
import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialBinStockBatchInfo;
import ekp.data.service.invt.MbsbStmtInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.WorkorderMaterialInfo;
import ekp.invt.bpu.material.MbsbStmtBuilderByWom;
import ekp.invt.type.InvtOrderType;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public class InvtOrderItemBuilder22 extends InvtOrderItemBuilder {
	protected Logger log = LoggerFactory.getLogger(InvtOrderItemBuilder22.class);
	
	private static MfDataService mfDataService = DataServiceFactory.getInstance().getService(MfDataService.class);
	/* base */
	private WorkorderMaterialInfo wom;

	/* data */
	private List<MbsbStmtBuilderByWom> mbsbStmtBuilderList;

	@Override
	protected InvtOrderItemBuilder22 appendBase() {
		/* base */
		wom = (WorkorderMaterialInfo) args[0];

		/* data */
		appendMmUid(wom.getMmUid());
		appendIoType(InvtOrderType.O2);
		// orderQty和orderValue必須依賴從Mbsb挑完才能決定
		mbsbStmtBuilderList = new ArrayList<>();

		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public MbsbStmtBuilderByWom addMbsbStmtBuilder(String _mbsbUid, double _stmtQty, double _stmtValue) {
		MbsbStmtBuilderByWom b = new MbsbStmtBuilderByWom();
		b.init();
		b.appendMbsbUid(_mbsbUid);
		b.appendStmtQty(_stmtQty).appendStmtValue(_stmtValue);
		mbsbStmtBuilderList.add(b);
		return b;
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

	public WorkorderMaterialInfo getWom() {
		return wom;
	}

	public List<MbsbStmtBuilderByWom> getMbsbStmtBuilderList() {
		return mbsbStmtBuilderList;
	}

	public double getSumMbsbStmtBuilderQty() {
		return getMbsbStmtBuilderList().stream().mapToDouble(b -> b.getStmtQty()).sum();
	}

	public double getSumMbsbStmtBuilderValue() {
		return getMbsbStmtBuilderList().stream().mapToDouble(b -> b.getStmtValue()).sum();
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

		/* wom的數量必須被mbsbStmt滿足 */
//		log.debug("getWom().getQty0(): {}", getWom().getQty0());
//		log.debug("getSumMbsbStmtBuilderQty(): {}", getSumMbsbStmtBuilderQty());
		if (getWom().getQty0() != getSumMbsbStmtBuilderQty()) {
			_msg.append("工令料表的應領數量和欲領數量不同。").append(System.lineSeparator());
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
		for (MbsbStmtBuilderByWom mbsbStmtBuilder : getMbsbStmtBuilderList()) {
			mbsbStmtBuilder.appendIoi(ioi);
			StringBuilder msg = new StringBuilder();
			MbsbStmtInfo mbsbStmt = mbsbStmtBuilder.build(msg, tt);
			if (mbsbStmt == null) {
				tt.travel();
				log.error("mbsbStmtBuilder.build return null. {}", msg.toString());
				return null;
			} // copy sites inside
		}

		/* womQty0to1 */
		double qty = getWom().getQty0();
		if (!mfDataService.womQty0to1(getWom().getUid(), qty)) {
			tt.travel();
			log.error("mfDataService.womQty0to1 return false.");
			return null;
		}
		tt.addSite("revert womQty0to1", () -> mfDataService.womQty0to1(getWom().getUid(), -qty));
		wom = wom.reload();
		log.info("mfDataService.womQty0to1 {}\t{}\t{}\t{}", getWom().getWoNo(), getWom().getMmMano(),
				getWom().getQty0(), getWom().getQty1());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return ioi.reload();
	}

}
