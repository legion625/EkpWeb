package ekp.data.service.invt;

import ekp.invt.type.InvtOrderStatus;
import legion.ObjectModelInfoDto;

public class InvtOrderInfoDto extends ObjectModelInfoDto implements InvtOrderInfo {

	protected InvtOrderInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String iosn; // invt order serial number
	private InvtOrderStatus status;
	private String applierId;
	private String applierName;
	private long applyTime; // apply time;
	private String remark; //
	private long apvTime; // approval time

	@Override
	public String getIosn() {
		return iosn;
	}

	void setIosn(String iosn) {
		this.iosn = iosn;
	}
	
	@Override
	public InvtOrderStatus getStatus() {
		return status;
	}

	void setStatus(InvtOrderStatus status) {
		this.status = status;
	}
	
	@Override
	public String getApplierId() {
		return applierId;
	}

	void setApplierId(String applierId) {
		this.applierId = applierId;
	}

	@Override
	public String getApplierName() {
		return applierName;
	}

	void setApplierName(String applierName) {
		this.applierName = applierName;
	}
	
	@Override
	public long getApplyTime() {
		return applyTime;
	}

	void setApplyTime(long applyTime) {
		this.applyTime = applyTime;
	}

	@Override
	public String getRemark() {
		return remark;
	}

	void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Override
	public long getApvTime() {
		return apvTime;
	}

	void setApvTime(long apvTime) {
		this.apvTime = apvTime;
	}

}
