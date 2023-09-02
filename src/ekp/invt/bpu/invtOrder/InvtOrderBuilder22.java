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
			
		}
		
		
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	protected List<? extends InvtOrderItemBuilder> getInvtOrderItemBuilderList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected InvtOrderInfo buildProcess(TimeTraveler _tt) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
