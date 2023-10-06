package ekp.mbom.issue.parsPart;

import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.mbom.issue.part.PartBpuDel0;
import legion.util.TimeTraveler;

public class PpartBpuDel0 extends PpartBpu {
	/* base */
	private PpartInfo ppart;

	/* data */
	// none
	
	// -------------------------------------------------------------------------------
	@Override
	protected PpartBpuDel0 appendBase() {
		/* base */
		ppart = (PpartInfo) args[0];
		appendPpart(ppart);

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
		if (getPpart() == null) {
			v = false;
			_msg.append("Ppart null.").append(System.lineSeparator());
		}
		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		if (!mbomDataService.deleteParsPart(getPpart().getUid())) {
			log.error("mbomDataService.deleteParsPart return false. [{}][{}][{}]", ppart.getUid(), ppart.getPartPin(),
					ppart.getPartName());
			return false;
		}
		log.info("mbomDataService.deleteParsPart. [{}][{}][{}]", ppart.getUid(), ppart.getPartPin(),
				ppart.getPartName());

		return true;
	}

}
