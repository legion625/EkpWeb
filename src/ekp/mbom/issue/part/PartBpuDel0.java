package ekp.mbom.issue.part;

import ekp.data.service.mbom.PartInfo;
import legion.util.TimeTraveler;

public class PartBpuDel0 extends PartBpu {
	/* base */
	private PartInfo part;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected PartBpuDel0 appendBase() {
		/* base */
		part = (PartInfo) args[0];
		appendPart(part);

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
	public boolean verify(StringBuilder _msg) {
		boolean v = true;
		if (getPart() == null) {
			v = false;
			_msg.append("Part null.").append(System.lineSeparator());
		}
		return v;
	}
	
	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		return mbomDataService.deletePart(getPart().getUid());
	}
}
