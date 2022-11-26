package ekp.mbom.issue.partAcqRoutingStep;

import ekp.data.service.mbom.ParsInfo;
import legion.util.TimeTraveler;

public class ParsBpuDel0 extends ParsBpu {
	/* base */
	private ParsInfo pars;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected ParsBpuDel0 appendBase() {
		/* base */
		pars = (ParsInfo) args[0];
		appendPars(pars);

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
		if (getPars() == null) {
			v = false;
			_msg.append("Part acquisition routing step null.").append(System.lineSeparator());
		}
		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		return mbomDataService.deletePartAcqRoutingStep(getPars().getUid());
	}

}
