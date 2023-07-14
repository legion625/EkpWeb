package ekp.invt.bpu.wrhsLoc;

import java.util.List;

import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.invt.WrhsLocInfo;
import legion.util.TimeTraveler;

public class WrhsLocBpuDel0 extends WrhsLocBpu {

	/* base */
	private WrhsLocInfo wrhsLoc;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected WrhsLocBpuDel0 appendBase() {
		/* base */
		wrhsLoc = (WrhsLocInfo) args[0];
		appendWrhsLoc(wrhsLoc);

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
		boolean v = true;
		if(getWrhsLoc()==null) {
			v = false;
			_msg.append("WrhsLoc null.").append(System.lineSeparator());
		}
		
		List<WrhsBinInfo> wbList = getWrhsLoc().getWrhsBinList();
		if (!wbList.isEmpty()) {
			v = false;
			_msg.append("wbList should be empty.").append(System.lineSeparator());
		}
		
		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		if(!invtDataService.deleteWrhsLoc(getWrhsLoc().getUid())) {
			log.error("invtDataService.deleteWrhsLoc return false. [{}][{}][{}]", getWrhsLoc().getUid(), getWrhsLoc().getId(), getWrhsLoc().getName());
			return false;
		}
		log.info("invtDataService.deleteWrhsLoc. [{}][{}][{}]", getWrhsLoc().getUid(), getWrhsLoc().getId(), getWrhsLoc().getName());

		return true;
	}


}
