package ekp.invt.bpu.invtOrder;

import java.util.ArrayList;
import java.util.List;

import ekp.data.service.invt.InvtOrderInfo;
import legion.util.TimeTraveler;

public class InvtOrderBuilder0 extends InvtOrderBuilder {
	/* base */

	/* data */
	private List<InvtOrderItemBuilder21> ioiBuilderList;

	// -------------------------------------------------------------------------------
	@Override
	protected InvtOrderBuilder0 appendBase() {
		// data
		ioiBuilderList = new ArrayList<>();
		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	@Override
	public InvtOrderBuilder0 appendRemark(String remark) {
		return (InvtOrderBuilder0) super.appendRemark(remark);
	}

	public InvtOrderItemBuilder21 addIoiBuilder() {
		InvtOrderItemBuilder21 ioib = new InvtOrderItemBuilder21();
		ioiBuilderList.add(ioib);
		return ioib;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public List<InvtOrderItemBuilder21> getInvtOrderItemBuilderList() {
		return ioiBuilderList;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	protected InvtOrderInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		InvtOrderInfo io = buildInvtOrderBasic(tt);
		if (io == null) {
			tt.travel();
			log.error("buildInvtOrderBasic return null.");
			return null;
		} // copy sites inside

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return io;
	}
	
}