package ekp.invt.bpu.wrhsLoc;

import java.util.List;

import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.invt.WrhsLocInfo;
import legion.util.TimeTraveler;

public class WrhsBinBpuDel0 extends WrhsBinBpu {
	/* base */
	private WrhsBinInfo wrhsBin;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected WrhsBinBpuDel0 appendBase() {
		/* base */
		wrhsBin = (WrhsBinInfo) args[0];
		appendWrhsBin(wrhsBin);

		/* data */
		// none

		return this;
	}
	
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		return true;
	}
	
	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		if(!invtDataService.deleteWrhsBin(getWrhsBin().getUid())) {
			log.error("invtDataService.deleteWrhsBin return false. [{}][{}][{}]", getWrhsBin().getUid(), getWrhsBin().getId(),getWrhsBin().getName());
			return false;
		}
		log.info("invtDataService.deleteWrhsBin. [{}][{}][{}], getWrhsBin().getUid(), getWrhsBin().getId(),getWrhsBin().getName()");
		return true;
	}

}
