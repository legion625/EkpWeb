package ekp.invt.bpu.invtOrder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MaterialInstSrcConjCreateObj;
import ekp.data.service.invt.MaterialInstSrcConjInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.MbsbStmtInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.material.MaterialInstBuilder0;
import ekp.invt.bpu.material.MbsbStmtBuilderByWo;
import ekp.invt.type.InvtOrderType;
import ekp.invt.type.IoiTargetType;
import ekp.invt.type.MaterialInstAcqChannel;
import legion.biz.Bpu;
import legion.biz.BpuFacade;
import legion.util.DateFormatUtil;
import legion.util.TimeTraveler;

public class InvtOrderItemBuilder12 extends InvtOrderItemBuilder {
	
	/* base */
	private WorkorderInfo wo;

	/* data */
	private MaterialInstBuilder0 miBuilder;
	private Map<String, List<MbsbStmtInfo>> srcMiUidMbsbStmtListMap;
	private MbsbStmtBuilderByWo mbsbStmtBuilder;
	private WrhsBinInfo wb;

	@Override
	protected InvtOrderItemBuilder12 appendBase() {
		/* base */
		wo = (WorkorderInfo) args[0];

		String mmUid = wo.getPartAcq().getMmUid();
		double qty = wo.getRqQty();
		appendMmUid(mmUid);
		appendIoType(InvtOrderType.I2);
		appendTargetType(IoiTargetType.WO).appendTargetUid(wo.getUid()).appendTargetBizKey(wo.getWoNo());
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

		//
		srcMiUidMbsbStmtListMap = wo.getWomList().stream().flatMap(wom -> wom.getQty1MbsbStmtList().stream())
				.collect(Collectors.groupingBy(s -> s.getMbsb().getMiUid()));
		log.debug("srcMiUidMbsbStmtListMap: {}", srcMiUidMbsbStmtListMap);
		log.debug("srcMiUidMbsbStmtListMap.size(): {}", srcMiUidMbsbStmtListMap.size());
		for (String srcMiUid : srcMiUidMbsbStmtListMap.keySet()) {
			List<MbsbStmtInfo> miSrcMbsbStmtList = srcMiUidMbsbStmtListMap.get(srcMiUid);
			log.debug("srcMiUid: {}", srcMiUid);
			for (MbsbStmtInfo miSrcMbsbStmt : miSrcMbsbStmtList) {
				MaterialInstInfo miSrcMi = miSrcMbsbStmt.getMbsb().getMi();
				log.debug("miSrcMi: {}", miSrcMi);
				log.debug("miSrcMi.getMmUid(): {}", miSrcMi.getMmUid());
				
				log.debug("{}\t{}\t{}\t{}\t{}", miSrcMi.getUid(), miSrcMi.getMisn(), miSrcMi.getMiacName(),
					miSrcMbsbStmt.getStmtQty(), miSrcMbsbStmt.getStmtValue());
				MaterialMasterInfo mm = miSrcMi.getMm();
				log.debug("mm: {}", mm);
			}
			log.debug("srcMiUid: {}\t miSrcMbsbStmt.size(): {}", srcMiUid, miSrcMbsbStmtList.size());
		}
		
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
	public Map<String, List<MbsbStmtInfo>> getSrcMiUidMbsbStmtListMap() {
		return srcMiUidMbsbStmtListMap;
	}
	
	public double getSumSrcMiValue() {
		return getSrcMiUidMbsbStmtListMap().values().stream().flatMap(l->l.stream()).mapToDouble(MbsbStmtInfo::getStmtValue).sum();
	}
	
	
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
		if (getSrcMiUidMbsbStmtListMap() == null || getSrcMiUidMbsbStmtListMap().size() <= 0) {
			_msg.append("getMiUidMbsbStmtListMap error.").append(System.lineSeparator());
			v = false;
		}
		
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
		
		/**/
		for (String srcMiUid : getSrcMiUidMbsbStmtListMap().keySet()) {
			MaterialInstSrcConjCreateObj miscCreateObj = new MaterialInstSrcConjCreateObj();
			miscCreateObj.setMiUid(mi.getUid());
			miscCreateObj.setSrcMiUid(srcMiUid);

			List<MbsbStmtInfo> mbsbStmtList = getSrcMiUidMbsbStmtListMap().get(srcMiUid);
			miscCreateObj.setSrcMiQty(mbsbStmtList.stream().mapToDouble(s -> s.getStmtQty()).sum());
			miscCreateObj.setSrcMiValue(mbsbStmtList.stream().mapToDouble(s -> s.getStmtValue()).sum());

			MaterialInstSrcConjInfo misc = invtDataService.createMaterialInstSrcConj(miscCreateObj);
			if (misc == null) {
				tt.travel();
				log.error("invtDataService.createMaterialInstSrcConj return null.");
				return null;
			}
			tt.addSite("revert createMaterialInstSrcConj",
					() -> invtDataService.deleteMaterialInstSrcConj(misc.getUid()));
		}
		
		

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
