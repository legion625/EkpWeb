package ekp.invt.bpu.invtOrder;

import java.util.ArrayList;
import java.util.List;

import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.invt.bpu.InvtBpuType;
import legion.biz.BpuFacade;
import legion.util.TimeTraveler;

public class InvtOrderBuilder11 extends InvtOrderBuilder {
	/* base */
	private PurchInfo purch;

	/* data */
	private List<InvtOrderItemBuilder11> ioiBuilderList;
	private WrhsBinInfo wb;
	// TODO

	// -------------------------------------------------------------------------------
	@Override
	protected InvtOrderBuilder11 appendBase() {
		/* base */
		purch = (PurchInfo) args[0];

		/* data */
		// TODO to append applierId, applierName, applyTime
		appendRemark("購案" + purch.getPuNo() + "入庫");

		ioiBuilderList = new ArrayList<>();
		for (PurchItemInfo pi : purch.getPurchItemList()) {
			InvtOrderItemBuilder11 ioiBuilder = new InvtOrderItemBuilder11();
			ioiBuilder.init(pi);
			ioiBuilderList.add(ioiBuilder);
		}

		return this;
	}
	
	// -------------------------------------------------------------------------------
	public InvtOrderBuilder11 appendWb(WrhsBinInfo wb) {
		this.wb = wb;
		if (ioiBuilderList != null)
			ioiBuilderList.forEach(ioib -> ioib.appendWrhsBinUid(wb.getUid()));
		return this;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected List<InvtOrderItemBuilder11> getInvtOrderItemBuilderList() {
		return ioiBuilderList;
	}

	public PurchInfo getPurch() {
		return purch;
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

		/* 2. Purch Perfed 購案已履約 */
		if (!puDataService.purchPerf(getPurch().getUid(), System.currentTimeMillis())) {
			tt.travel();
			log.error("puDataService.purchPerf return false.");
			return null;
		}
		tt.addSite("revert puDataService.purchPerf", () -> puDataService.purchRevertPerf(getPurch().getUid()));

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return io;
	}

}
