package ekp.invt.bpu.invtOrder;

import java.util.ArrayList;
import java.util.List;

import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.invt.bpu.material.MbsbStmtBuilder2;
import ekp.invt.type.InvtOrderType;
import ekp.invt.type.IoiTargetType;
import legion.util.TimeTraveler;

public class InvtOrderBuilder21 extends InvtOrderBuilder {
	/* base */
//	private PurchInfo purch;
	private PurchItemInfo purchItem;
	private PartAcqInfo partAcq;
	private PartCfgInfo partCfg;
	private double qty;

	/* data */
	private List<InvtOrderItemBuilder21> ioiBuilderList;

	// -------------------------------------------------------------------------------
	@Override
	protected InvtOrderBuilder21 appendBase() {
		// base
		purchItem= (PurchItemInfo) args[0];
		partAcq = (PartAcqInfo) args[1];
		partCfg = (PartCfgInfo) args[2];
		qty = (double) args[3];

		// data
		ioiBuilderList = new ArrayList<>();
		for (PpartInfo ppart : partAcq.getPpartList()) {
			PartAcqInfo childPa = ppart.getPart().getPa(partCfg);
			InvtOrderItemBuilder21 ioib = new InvtOrderItemBuilder21();
			ioib.init();
			ioib.appendMmUid(childPa.getMmUid());
			ioib.appendIoType(InvtOrderType.O1);
//			ioib.appendTargetType(IoiTargetType.PURCH).appendTargetUid(purchItem.getUid())
			ioib.appendTargetType(IoiTargetType.PURCH_ITEM).appendTargetUid(purchItem.getUid())
					.appendTargetBizKey(purchItem.getPurch().getPuNo());
			ioib.appendOrderQty(qty * ppart.getPartReqQty()); // 乘上單階配賦量
			// orderValue必須依賴從Mbsb挑完才能決定
			ioiBuilderList.add(ioib);
		}
		
		return this;
	}

	// -------------------------------------------------------------------------------
//	public PurchInfo getPurch() {
//		return purch;
//	}
	public PurchItemInfo getPurchItem() {
		return purchItem;
	}

	public PartAcqInfo getPartAcq() {
		return partAcq;
	}

	public PartCfgInfo getPartCfg() {
		return partCfg;
	}

	@Override
	public List<InvtOrderItemBuilder21> getInvtOrderItemBuilderList() {
		return ioiBuilderList;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected InvtOrderInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		/* 1. buildInvtOrderBasic */
		InvtOrderInfo io = buildInvtOrderBasic(tt);
		if (io == null) {
			tt.travel();
			log.error("buildInvtOrderBasic return null.");
			return null;
		}

		/**/
		// 購案沒有供料狀態控制

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return io;
	}

}
