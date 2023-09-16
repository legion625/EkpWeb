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
	private String partAcqUid;
	private String partAcqId;
	private String partAcqMmMano;
	private String partCfgUid;
	private String partCfgId;
	private double rqQty; // 需求數量

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

	protected WoBuilder appendPartAcqUid(String partAcqUid) {
		this.partAcqUid = partAcqUid;
		return this;
	}

	protected WoBuilder appendPartAcqId(String partAcqId) {
		this.partAcqId = partAcqId;
		return this;
	}

	protected WoBuilder appendPartAcqMmMano(String partAcqMmMano) {
		this.partAcqMmMano = partAcqMmMano;
		return this;
	}
	
	protected WoBuilder appendPartCfgUid(String partCfgUid) {
		this.partCfgUid = partCfgUid;
		return this;
	}

	protected WoBuilder appendPartCfgId(String partCfgId) {
		this.partCfgId = partCfgId;
		return this;
	}

	protected WoBuilder appendRqQty(double rqQty) {
		this.rqQty = rqQty;
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

	public String getPartAcqUid() {
		return partAcqUid;
	}

	public String getPartAcqId() {
		return partAcqId;
	}
	
	public String getPartAcqMmMano() {
		return partAcqMmMano;
	}
	
	public String getPartCfgUid() {
		return partCfgUid;
	}

	public String getPartCfgId() {
		return partCfgId;
	}

	public double getRqQty() {
		return rqQty;
	}

	// -------------------------------------------------------------------------------
	private WorkorderCreateObj packWorkorderCreateObj() {
		WorkorderCreateObj dto = new WorkorderCreateObj();
		dto.setPartUid(getPartUid());
		dto.setPartPin(getPartPin());
		dto.setPartAcqUid(getPartAcqUid());
		dto.setPartAcqId(getPartAcqId());
		dto.setPartAcqMmMano(getPartAcqMmMano());
		dto.setPartCfgUid(getPartCfgUid());
		dto.setPartCfgId(getPartCfgId());
		dto.setRqQty(getRqQty());
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
		if (DataFO.isEmptyString(getPartAcqUid())) {
			_msg.append("PartAcqUid should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		if (DataFO.isEmptyString(getPartAcqId())) {
			_msg.append("PartAcqId should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		if (DataFO.isEmptyString(getPartAcqMmMano())) {
			_msg.append("PartAcqMmName should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		if (DataFO.isEmptyString(getPartCfgUid())) {
			_msg.append("PartCfgUid should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		if (DataFO.isEmptyString(getPartCfgId())) {
			_msg.append("PartCfgId should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		
		if(getRqQty()<=0) {
			_msg.append("RqQty should be GREATER than 0.").append(System.lineSeparator());
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
