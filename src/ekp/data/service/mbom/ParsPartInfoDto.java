package ekp.data.service.mbom;

import ekp.data.MbomDataService;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class ParsPartInfoDto extends ObjectModelInfoDto implements ParsPartInfo {

	protected ParsPartInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------attribute-----------------------------------
	private String parsUid; // ref data key

	// assign part
	private boolean assignPart;
	private String partUid; // ref data key
	private String partPin; // ref biz key
	private double partReqQty; // required quantity (allow decimal in certain conditions)

	// -------------------------------------------------------------------------------
	// ---------------------------------getter&setter---------------------------------
	@Override
	public String getParsUid() {
		return parsUid;
	}

	void setParsUid(String parsUid) {
		this.parsUid = parsUid;
	}

	@Override
	public boolean isAssignPart() {
		return assignPart;
	}

	void setAssignPart(boolean assignPart) {
		this.assignPart = assignPart;
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
	public double getPartReqQty() {
		return partReqQty;
	}

	void setPartReqQty(double partReqQty) {
		this.partReqQty = partReqQty;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public ParsPartInfo reload() {
		return DataServiceFactory.getInstance().getService(MbomDataService.class).loadParsPart(this.getUid());
	}
		

}
