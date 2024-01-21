package ekp.mf.bpu;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.MaterialBinStockBatchInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.WorkorderMaterialInfo;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.invtOrder.InvtOrderBuilder12;
import ekp.invt.bpu.invtOrder.InvtOrderBuilder22;
import ekp.invt.bpu.invtOrder.InvtOrderItemBuilder12;
import ekp.invt.bpu.invtOrder.InvtOrderItemBuilder22;
import ekp.invt.bpu.invtOrder.IoBpuApprove;
import legion.biz.BpuFacade;
import legion.util.TimeTraveler;

public class WoBuilderAll extends WoBuilder {
	/* base */
	private PartAcqInfo pa;
	private PartCfgInfo pc;
	
	
	/* data */
	private double rqQty;
	private long startWorkTime;
	private long finishWorkTime;
	private long overTime;
	
	private String userId;
	private String userName;
	
	private WrhsBinInfo wb;
	
	// -------------------------------------------------------------------------------
	private final BpuFacade bpuFacade = BpuFacade.getInstance();
	
	// -------------------------------------------------------------------------------
	protected WoBuilderAll appendBase() {
		/* base */
		pa = (PartAcqInfo) args[0];
		pc = (PartCfgInfo) args[1];

		/* data */
		// none

		return this;
	}

	public WoBuilderAll appendRqQty(double rqQty) {
		this.rqQty = rqQty;
		return this;
	}

	public WoBuilderAll appendStartWorkTime(long startWorkTime) {
		this.startWorkTime = startWorkTime;
		return this;
	}

	public WoBuilderAll appendFinishWorkTime(long finishWorkTime) {
		this.finishWorkTime = finishWorkTime;
		return this;
	}

	public WoBuilderAll appendOverTime(long overTime) {
		this.overTime = overTime;
		return this;
	}
	
	public WoBuilderAll appendUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public WoBuilderAll appendUserName(String userName) {
		this.userName = userName;
		return this;
	}
	
	public WoBuilderAll appendWb(WrhsBinInfo wb) {
		this.wb = wb;
		return this;
	}

	// -------------------------------------------------------------------------------
	public PartAcqInfo getPa() {
		return pa;
	}

	public PartCfgInfo getPc() {
		return pc;
	}
	
	public double getRqQty() {
		return rqQty;
	}

	public long getStartWorkTime() {
		return startWorkTime;
	}

	public long getFinishWorkTime() {
		return finishWorkTime;
	}

	public long getOverTime() {
		return overTime;
	}
	
	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}
	
	public WrhsBinInfo getWb() {
		return wb;
	}


	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}
	
	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;

		/* 確保料足。 */
		List<PpartInfo> ppartList = getPa().getPpartList();
		for(PpartInfo ppart: ppartList) {
			// 找到子階的Pa
			PartAcqInfo womPa = ppart.getPart().getPa(getPc().getUid());
			// 計算數量
			double qty = getRqQty() * ppart.getPartReqQty();
			
			if(womPa.getMm().getSumStockQty()<qty) {
				_msg.append("[" + womPa.getMmMano() + "] stock qty not enough: required: " + qty + "\t stock: "
						+ womPa.getMm().getSumStockQty());
				v = false;
			}
		}
		
		// TODO
		
		return v;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected WorkorderInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		
		StringBuilder msgBuild = new StringBuilder();
		
		/* a.產生工令 */
		WoBuilder1 wob = bpuFacade.getBuilder(MfBpuType.WO_1, getPa(), getPc());
		if (wob == null) {
			tt.travel();
			log.error("getBuilder MfBpuType.WO_1 return null.");
			return null;
		}
		wob.appendRqQty(getRqQty());
		WorkorderInfo wo = wob.build(msgBuild, tt);
		if (wo == null) {
			tt.travel();
			log.error("WoBuilder1.build return null. {}", msgBuild.toString());
			return null;
		}
		
		/* b.工令領料(依Wo產生InvtOrder、InvtOrderItem、MbsbStmt) */
		wo = wo.reload();
		InvtOrderBuilder22 iob22 = bpuFacade.getBuilder(InvtBpuType.IO_22, wo);
		if(iob22==null) {
			tt.travel();
			log.error("getBuilder InvtBpuType.IO_22 return null.");
			return null;
		}
		iob22.appendApplierId(getUserId()).appendApplierName(getUserName());
		
		List<InvtOrderItemBuilder22> ioibList22 = iob22.getInvtOrderItemBuilderList();
		for (InvtOrderItemBuilder22 ioib22 : ioibList22) {
			WorkorderMaterialInfo wom = ioib22.getWom();
			MaterialMasterInfo mm = wom.getMm();
			List<MaterialBinStockBatchInfo> mbsbList = mm.getMbsbList();
			log.debug("{}\t{}", mm.getMano(), mbsbList.size());
			double a = wom.getQty0();
			double alcQty = 0;
			for (MaterialBinStockBatchInfo mbsb : mbsbList) {
				if(mbsb.getStockQty()<=0)
					continue;
				alcQty = Math.min(a, mbsb.getStockQty()); // 先找出「待分配數量」和「庫存數量」的較小值，作為「分配量」
				double stmtValue = alcQty * mbsb.getStockValue() / mbsb.getStockQty();
				ioib22.addMbsbStmtBuilder(mbsb.getUid(), alcQty, stmtValue);
				a -= alcQty; // 更新「待分配數量」。（mbsb的數量只是參考，要在kernel到時該帳被post時才會生效。）
				if (a <= 0) // 若A已分配滿足，結束for迴圈。
					break;
			}
			assertTrue(a == 0); // a必須要分配完。
			log.debug("{}\t{}", ioib22.getWom().getMmMano(), ioib22.getMbsbStmtBuilderList().size());
		}
		InvtOrderInfo io22 = iob22.build(msgBuild, tt);
		if(io22==null) {
			tt.travel();
			log.error("InvtOrderBuilder22.build return null. {}", msgBuild.toString());
			return null;
		}
		
		/* C.InvtOrder登帳 */
		IoBpuApprove ioBpuApv22 = bpuFacade.getBuilder(InvtBpuType.IO_$APPROVE, io22);
		if (ioBpuApv22 == null) {
			tt.travel();
			log.error("getBuilder InvtBpuType.IO_$APPROVE return null.");
			return null;
		}
		if (!ioBpuApv22.build(msgBuild, tt)) {
			tt.travel();
			log.error("IoBpuApprove.build return false. {}", msgBuild.toString());
			return null;
		}
		
		/* D.工令開工 */
		wo = wo.reload();
		WoBpuStart woBpuStart = bpuFacade.getBuilder(MfBpuType.WO_$START, wo);
		if (woBpuStart == null) {
			tt.travel();
			log.error("getBuilder MfBpuType.WO_$START return null.");
			return null;
		}
		woBpuStart.appendStartWorkTime(getStartWorkTime());
		if(!woBpuStart.build(msgBuild, tt)) {
			tt.travel();
			log.error("WoBpuStart.build return false. {}", msgBuild.toString());
			return null;
		}
		
		/* E.工令完工 */
		wo = wo.reload();
		WoBpuFinishWork woBpuFinishWork = bpuFacade.getBuilder(MfBpuType.WO_$FINISH_WORK, wo);
		if (woBpuFinishWork == null) {
			tt.travel();
			log.error("getBuilder MfBpuType.WO_$FINISH_WORK return null.");
			return null;
		}
		woBpuFinishWork.appendFinishWorkTime(getFinishWorkTime());
		if (!woBpuFinishWork.build(msgBuild, tt)) {
			tt.travel();
			log.error("WoBpuFinishWork.build return false. {}", msgBuild.toString());
			return null;
		}
		
		/* F.工令入庫(依Wo產生InvtOrder、InvtOrderItem、MbsbStmt、MaterialInstConj) */
		wo = wo.reload();
		InvtOrderBuilder12 iob12 = bpuFacade.getBuilder(InvtBpuType.IO_12, wo);
		if (iob12 == null) {
			tt.travel();
			log.error("getBuilder InvtBpuType.IO_12 return null.");
			return null;
		}
		iob12.appendApplierId(getUserId()).appendApplierName(getUserName());
		iob12.appendWb(getWb());
		InvtOrderItemBuilder12 ioib12 = iob12.getIoiBuilder();
		double orderValue = ioib12.getSumSrcMiValue() * 2; // 已料費工費1:1計算
		ioib12.appendOrderValue(orderValue);
		InvtOrderInfo io12 = iob12.build(msgBuild, tt);
		if(io12==null) {
			tt.travel();
			log.error("InvtOrderBuilder12.build return null. {}", msgBuild.toString());
			return null;
		}
		
		/* G.工令銷令 */
		wo = wo.reload();
		WoBpuOver woBpuOver = bpuFacade.getBuilder(MfBpuType.WO_$OVER, wo);
		if (woBpuOver == null) {
			tt.travel();
			log.error("getBuilder MfBpuType.WO_$OVER return null.");
			return null;
		}
		woBpuOver.appendOverTime(getOverTime());
		if (!woBpuOver.build(msgBuild, tt)) {
			tt.travel();
			log.error("WoBpuOver.build return false. {}", msgBuild.toString());
			return null;
		}
		
		//
		if(_tt!=null)
			_tt.copySitesFrom(tt);
		
		return wo.reload();
	}
}
