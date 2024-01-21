package ekp.mf.bpu;

import ekp.data.service.mf.WorkorderInfo;
import ekp.mf.type.WorkorderStatus;
import legion.util.TimeTraveler;

public class WoBpuOver extends WoBpu {
	/* base */
	private WorkorderInfo wo;

	/* data */
	private long overTime;

	// -------------------------------------------------------------------------------
	@Override
	protected WoBpuOver appendBase() {
		/* base */
		wo = (WorkorderInfo) args[0];
		appendWo(wo);

		/* data */
		// none
		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public WoBpuOver appendOverTime(long overTime) {
		this.overTime = overTime;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public long getOverTime() {
		return overTime;
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
		if (WorkorderStatus.FINISH_WORK != getWo().getStatus()) {
			_msg.append("WorkorderStatus error.").append(System.lineSeparator());
			v = false;
		}

		if (getOverTime() <= 0) {
			_msg.append("getOverTime error.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	// -------------------------------------------------------------------------------
	@Override
	public Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		/**/
		if (!mfDataSerivce.woOver(getWo().getUid(), getOverTime())) {
			tt.travel();
			log.error("mfDataSerivce.woOver return false.");
			return false;
		}
		tt.addSite("revert woOver", () -> mfDataSerivce.woRevertOver(getWo().getUid()));
		log.info("woFinishWork [{}][{}]", getWo().getUid(), getWo().getWoNo());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return true;
	}

}
