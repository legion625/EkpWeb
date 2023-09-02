package ekp.mf.bpu;

import ekp.data.MfDataService;
import ekp.data.service.mf.WorkorderCreateObj;
import ekp.data.service.mf.WorkorderInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class WoBuilder extends Bpu<WorkorderInfo> {
	protected static MfDataService mfDataService = DataServiceFactory.getInstance().getService(MfDataService.class);

	/* base */
	private String partUid;
	private String partPin;
	private String partMmMano;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected WoBuilder appendPartUid(String partUid) {
		this.partUid = partUid;
		return this;
	}

	protected WoBuilder appendPartPin(String partPin) {
		this.partPin = partPin;
		return this;
	}

	protected WoBuilder appendPartMmMano(String partMmMano) {
		this.partMmMano = partMmMano;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getPartUid() {
		return partUid;
	}

	public String getPartPin() {
		return partPin;
	}

	public String getPartMmMano() {
		return partMmMano;
	}

	// -------------------------------------------------------------------------------
	private WorkorderCreateObj packWorkorderCreateObj() {
		WorkorderCreateObj dto = new WorkorderCreateObj();
		dto.setPartUid(getPartUid());
		dto.setPartPin(getPartPin());
		dto.setPartMmMano(getPartMmMano());
		return dto;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;

		if (DataFO.isEmptyString(getPartUid())) {
			_msg.append("PartUid should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		if (DataFO.isEmptyString(getPartPin())) {
			_msg.append("PartPin should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		if (DataFO.isEmptyString(getPartMmMano())) {
			_msg.append("PartMmName should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected abstract WorkorderInfo buildProcess(TimeTraveler _tt);

	protected final WorkorderInfo buildWoBasic(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		/* 1.Workorder */
		WorkorderInfo wo = mfDataService.createWorkorder(packWorkorderCreateObj());
		if (wo == null) {
			tt.travel();
			log.error("mfDataService.createWorkorder return null.");
			return null;
		}
		tt.addSite("revert createWorkorder", () -> mfDataService.deleteWorkorder(wo.getUid()));
		log.info("mfDataService.createWorkorder [{}][{}][{}][{}]", wo.getUid(), wo.getWoNo(), wo.getPartUid(),
				wo.getPartPin());

		/* 2.工令狀態->待開工 */
		if (!mfDataService.woToStart(wo.getUid())) {
			tt.travel();
			log.error("mfDataService.woToStart return false.");
			return null;
		}
		tt.addSite("revert woToStart", () -> mfDataService.woRevertToStart(wo.getUid()));
		log.info("mfDataService.woToStart [{}]", wo.getUid(), wo.getWoNo(), wo.getStatus());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return wo.reload();
	}

}
