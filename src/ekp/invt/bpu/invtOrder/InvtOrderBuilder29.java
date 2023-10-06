package ekp.invt.bpu.invtOrder;

import java.util.ArrayList;
import java.util.List;

import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.SalesOrderItemInfo;
import legion.util.TimeTraveler;

public class InvtOrderBuilder29 extends InvtOrderBuilder {
	/* base */
	private SalesOrderInfo so;

	/* data */
	private List<InvtOrderItemBuilder29> ioiBuilderList;

	// -------------------------------------------------------------------------------
	@Override
	protected InvtOrderBuilder29 appendBase() {
		/* base */
		so = (SalesOrderInfo) args[0];

		/* data */
		// to append applierId, applierName, applyTime
		appendRemark("銷售訂單" + so.getSosn() + "成品出庫");

		ioiBuilderList = new ArrayList<>();
		for (SalesOrderItemInfo soi : so.getSalesOrderItemList()) {
			InvtOrderItemBuilder29 ioiBuilder = new InvtOrderItemBuilder29();
			ioiBuilder.init(soi);
			ioiBuilderList.add(ioiBuilder);
		}

		return this;
	}

	// -------------------------------------------------------------------------------
	public SalesOrderInfo getSo() {
		return so;
	}

	@Override
	public List<InvtOrderItemBuilder29> getInvtOrderItemBuilderList() {
		return ioiBuilderList;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected InvtOrderInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		/* 1.buildInvtOrderBasic */
		InvtOrderInfo io = buildInvtOrderBasic(tt);
		if (io == null) {
			tt.travel();
			log.error("buildInvtOrderBasic return null.");
			return null;
		}

		/**/
		// 銷售訂單沒有狀態控制

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return io;
	}

}
