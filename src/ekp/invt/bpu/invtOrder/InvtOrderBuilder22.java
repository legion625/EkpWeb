package ekp.invt.bpu.invtOrder;

import java.util.ArrayList;
import java.util.List;

import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.WorkorderMaterialInfo;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public class InvtOrderBuilder22 extends InvtOrderBuilder{
	/* base */
	private WorkorderInfo wo;

	/* data */
	private List<InvtOrderItemBuilder22> ioiBuilderList;
	
	
	// -------------------------------------------------------------------------------
	@Override
	protected InvtOrderBuilder22 appendBase() {
		/* base */
		wo =(WorkorderInfo) args[0];
		
		/* data */
		// to append applierId, applierName, applyTime
		appendRemark("工令" + wo.getWoNo() + "領料");
		
		ioiBuilderList = new ArrayList<>();
		for(WorkorderMaterialInfo wom: wo.getWomList()) {
			InvtOrderItemBuilder22 ioiBuilder = new InvtOrderItemBuilder22();
			ioiBuilder.init(wom);
			ioiBuilderList.add(ioiBuilder);
		}
		
		return this;
	}
	
	// -------------------------------------------------------------------------------
	public WorkorderInfo getWo() {
		return wo;
	}
	
	@Override
	public List<InvtOrderItemBuilder22> getInvtOrderItemBuilderList() {
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
		// 工令沒有狀態控制

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return io;
	}

	
	

}
