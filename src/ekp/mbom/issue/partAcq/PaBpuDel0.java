package ekp.mbom.issue.partAcq;

import ekp.data.service.mbom.PartAcqInfo;
import legion.util.TimeTraveler;

public class PaBpuDel0 extends PaBpu {
	/* base */
	private PartAcqInfo pa;
	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected PaBpuDel0 appendBase() {
		/* base */
		pa = (PartAcqInfo) args[0];
		appendPa(pa);

		/* data */
		// none

		return this;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;
		if (getPa() == null) {
			v = false;
			_msg.append("Part acquisition null.").append(System.lineSeparator());
		}
		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		if (!mbomDataService.deletePartAcquisition(getPa().getUid())) {
			log.error("mbomDataService.deletePartAcquisition return false. [{}][{}][{}][{}]", pa.getUid(),
					pa.getPartUid(), pa.getPartPin(), pa.getId());
			return false;
		}
		log.info("mbomDataService.deletePartAcquisition [{}][{}][{}][{}]", pa.getUid(), pa.getPartUid(),
				pa.getPartPin(), pa.getId());

		return true;
	}

}
