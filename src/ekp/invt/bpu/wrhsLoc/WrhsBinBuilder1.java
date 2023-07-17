package ekp.invt.bpu.wrhsLoc;

import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.invt.WrhsLocInfo;
import legion.biz.Bpu;

public class WrhsBinBuilder1 extends WrhsBinBuilder{

	/* base */
	private WrhsLocInfo wl;
	
	/* data */
	// none
	
	// -------------------------------------------------------------------------------
	@Override
	protected Bpu<WrhsBinInfo> appendBase() {
		/* base */
		wl = (WrhsLocInfo) args[0];

		/* data */
		appendWlUid(wl.getUid());

		return this;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public WrhsBinBuilder1 appendId(String id) {
		return (WrhsBinBuilder1) super.appendId(id);
	}
	
	@Override
	public WrhsBinBuilder1 appendName(String name) {
		return (WrhsBinBuilder1) super.appendName(name);
	}


}
