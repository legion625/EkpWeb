package ekp.data.service.mf;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.MfDataService;
import ekp.mf.type.WorkorderStatus;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class WorkorderInfoDto extends ObjectModelInfoDto implements WorkorderInfo {
	protected WorkorderInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String woNo;
	private WorkorderStatus status;
	// 欲製造的part
	private String partUid;
	private String partPin;
	private String partMmMano;

	private String partAcqUid;
	private String partAcqId;
	private double rqQty; // 需求數量
	
	private long startWorkTime;
	private long finishWorkTime;
	private long overTime;

	@Override
	public String getWoNo() {
		return woNo;
	}

	void setWoNo(String woNo) {
		this.woNo = woNo;
	}

	@Override
	public WorkorderStatus getStatus() {
		return status;
	}

	void setStatus(WorkorderStatus status) {
		this.status = status;
	}

	@Override
	public String getPartUid() {
		return partUid;
	}

	void setPartUid(String partUid) {
		this.partUid = partUid;
	}

	@Override
	public String getPartPin() {
		return partPin;
	}

	void setPartPin(String partPin) {
		this.partPin = partPin;
	}

	@Override
	public String getPartMmMano() {
		return partMmMano;
	}

	void setPartMmMano(String partMmMano) {
		this.partMmMano = partMmMano;
	}

	
	@Override
	public String getPartAcqUid() {
		return partAcqUid;
	}

	void setPartAcqUid(String partAcqUid) {
		this.partAcqUid = partAcqUid;
	}
	@Override
	public String getPartAcqId() {
		return partAcqId;
	}

	void setPartAcqId(String partAcqId) {
		this.partAcqId = partAcqId;
	}
	@Override
	public double getRqQty() {
		return rqQty;
	}

	void setRqQty(double rqQty) {
		this.rqQty = rqQty;
	}

	@Override
	public long getStartWorkTime() {
		return startWorkTime;
	}

	void setStartWorkTime(long startWorkTime) {
		this.startWorkTime = startWorkTime;
	}

	@Override
	public long getFinishWorkTime() {
		return finishWorkTime;
	}

	void setFinishWorkTime(long finishWorkTime) {
		this.finishWorkTime = finishWorkTime;
	}

	@Override
	public long getOverTime() {
		return overTime;
	}

	void setOverTime(long overTime) {
		this.overTime = overTime;
	}

	// -------------------------------------------------------------------------------
	@Override
	public WorkorderInfo reload() {
		return DataServiceFactory.getInstance().getService(MfDataService.class).loadWorkorder(getUid());
	}

	// -------------------------------------------------------------------------------
	private BizObjLoader<List<WorkorderMaterialInfo>> womListLoader = BizObjLoader.of(
			() -> DataServiceFactory.getInstance().getService(MfDataService.class).loadWorkorderMaterialList(getUid()));

	@Override
	public List<WorkorderMaterialInfo> getWomList() {
		return womListLoader.getObj();
	}

}
