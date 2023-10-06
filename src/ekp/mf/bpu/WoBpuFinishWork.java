package ekp.mf.bpu;

import ekp.data.service.mf.WorkorderInfo;
import ekp.invt.type.InvtOrderStatus;
import ekp.mf.type.WorkorderStatus;
import legion.util.TimeTraveler;

public class WoBpuFinishWork extends WoBpu {
	/* base */
	private WorkorderInfo wo;

	/* data */
	private long finishWorkTime;

	// -------------------------------------------------------------------------------
	@Override
	protected WoBpuFinishWork appendBase() {
		/* base */
		wo = (WorkorderInfo) args[0];
		appendWo(wo);

		/* data */
		// none
		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public WoBpuFinishWork appendFinishWorkTime(long finishWorkTime) {
		this.finishWorkTime = finishWorkTime;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public long getFinishWorkTime() {
		return finishWorkTime;
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
		if (WorkorderStatus.WORKING != getWo().getStatus()) {
			_msg.append("WorkorderStatus error.").append(System.lineSeparator());
			v = false;
		}

		if (getFinishWorkTime() <= 0) {
			_msg.append("getFinishWorkTime error.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		/**/
		if (!mfDataSerivce.woFinishWork(getWo().getUid(), getFinishWorkTime())) {
			tt.travel();
			log.error("mfDataSerivce.woFinishWork return false.");
			return false;
		}
		tt.addSite("revert woFinishWork", () -> mfDataSerivce.woRevertFinishWork(getWo().getUid()));
		log.info("woFinishWork [{}][{}]", getWo().getUid(), getWo().getWoNo());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return true;
	}

}
