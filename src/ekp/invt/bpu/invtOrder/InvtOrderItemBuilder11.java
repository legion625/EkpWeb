package ekp.invt.bpu.invtOrder;

import java.time.LocalDate;
import java.util.Date;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.material.MaterialInstBuilder0;
import ekp.invt.type.InvtOrderType;
import ekp.invt.type.MaterialInstAcqChannel;
import ekp.mbom.issue.MbomBpuType;
import legion.biz.Bpu;
import legion.biz.BpuFacade;
import legion.util.DateFormatUtil;
import legion.util.TimeTraveler;

public class InvtOrderItemBuilder11 extends InvtOrderItemBuilder {
	/* base */
	private PurchItemInfo pi;
	
	/* data */
	private MaterialInstBuilder0 miBuilder;

	@Override
	protected InvtOrderItemBuilder11 appendBase() {
		/* base */
		pi = (PurchItemInfo) args[0];
		
		appendMmUid(pi.getMmUid()).appendMiUid("");
		appendIoType(InvtOrderType.I1);
		appendOrderQty(pi.getQty()).appendOrderValue(pi.getValue());
		
		/* data */
		miBuilder = BpuFacade.getInstance().getBuilder(InvtBpuType.MI_0);
//		miBuilder = new MaterialInstBuilder0();
		miBuilder.appendMmUid(pi.getMmUid());
		miBuilder.appendMiac(MaterialInstAcqChannel.PURCHASING);
		miBuilder.appendQty(pi.getQty()).appendValue(pi.getValue());
		Date dateEff = DateFormatUtil.getEarliestTimeInDate(new Date(System.currentTimeMillis()));
		miBuilder.appendEffDate(dateEff.getTime());
		LocalDate ldEff = DateFormatUtil.parseLocalDate(dateEff);
		LocalDate ldExp = ldEff.plusYears(2); // 預設帶2年
		miBuilder.appendExpDate(DateFormatUtil.parseLong(ldExp));
		
		return this;
	}
	
	
	
	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		// none
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = true;
		if (!verifyThis(_msg))
			v = false;
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
		appendMiUid(mi.getUid()); //
		InvtOrderItemInfo ioi = buildInvtOrderItem(tt);
		if (ioi == null) {
			tt.travel();
			log.error("buildInvtOrderItem return null.");
			return null;
		} // copy sites inside

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return ioi;
	}

	

	

}
