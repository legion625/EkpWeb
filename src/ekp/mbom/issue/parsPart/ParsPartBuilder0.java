package ekp.mbom.issue.parsPart;

import ekp.data.service.mbom.PpartInfo;
import ekp.mbom.issue.parsProc.ParsProcBuilder0;
import legion.util.TimeTraveler;

public class ParsPartBuilder0 extends ParsPartBuilder {
	/* base */
	// none

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected ParsPartBuilder0 appendBase() {
		// none
		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	@Override
	public ParsPartBuilder0 appendParsUid(String parsUid) {
		return (ParsPartBuilder0) super.appendParsUid(parsUid);
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------build-------------------------------------
	@Override
	protected PpartInfo buildProcess(TimeTraveler _tt) {
		return buildPpart(_tt);
		
	}

}