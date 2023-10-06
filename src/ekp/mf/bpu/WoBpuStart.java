package ekp.mf.bpu;

import ekp.data.service.mf.WorkorderInfo;
import ekp.invt.type.InvtOrderStatus;
import ekp.mf.type.WorkorderStatus;
import legion.util.TimeTraveler;

public class WoBpuStart extends WoBpu {
	/* base */
	private WorkorderInfo wo;

	/* data */
	private long startWorkTime;
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected WoBpuStart appendBase() {
		/* base */
		wo = (WorkorderInfo) args[0];
		appendWo(wo);

		/* data */
		// none
		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public WoBpuStart appendStartWorkTime(long startWorkTime) {
		this.startWorkTime = startWorkTime;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public long getStartWorkTime() {
		return startWorkTime;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;

		/*  */
		if (WorkorderStatus.TO_START != getWo().getStatus()) {
			_msg.append("WorkorderStatus error.").append(System.lineSeparator());
			v = false;
		}

		if (getStartWorkTime() <= 0) {
			_msg.append("getStartWorkTime error.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		/**/
		if (!mfDataSerivce.woStartWork(getWo().getUid(), getStartWorkTime())) {
			tt.travel();
			log.error("mfDataSerivce.woStartWork return false.");
			return false;
		}
		tt.addSite("revert woStartWork", () -> mfDataSerivce.woRevertStartWork(getWo().getUid()));
		log.info("woStartWork [{}][{}]", getWo().getUid(), getWo().getWoNo());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return true;
	}

}
