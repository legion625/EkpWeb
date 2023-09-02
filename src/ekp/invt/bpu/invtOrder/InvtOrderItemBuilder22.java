package ekp.invt.bpu.invtOrder;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.mf.WorkorderInfo;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public class InvtOrderItemBuilder22 extends InvtOrderItemBuilder {
	/* base */
	private WorkorderInfo wo;

	/* data */
	private String miUid; // optional
	
// TODO

	@Override
	protected InvtOrderItemBuilder22 appendBase() {
		/* base */
		wo = (WorkorderInfo) args[0];
		
		// TODO
		
		/* data */
		// TODO
		
		return this;
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

//			//
//			if (DataFO.isEmptyString(getWrhsBinUid())) {
//				_msg.append("wrhsBinUid should NOT be empty.").append(System.lineSeparator());
//				v = false;
//			}
			// TODO
			
			return v;
		}
		
		// -------------------------------------------------------------------------------
		@Override
		protected InvtOrderItemInfo buildProcess(TimeTraveler _tt) {
			TimeTraveler tt = new TimeTraveler();
			
			// TODO mi?
			
			/* 2 */
			/* 2.InvtOrderItem */
			InvtOrderItemInfo ioi = buildInvtOrderItem(tt);
			if (ioi == null) {
				tt.travel();
				log.error("buildInvtOrderItem return null.");
				return null;
			} // copy sites inside
			

			
			// FIXME
			
			return null;
		}

}
