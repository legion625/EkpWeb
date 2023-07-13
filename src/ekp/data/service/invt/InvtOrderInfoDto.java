package ekp.data.service.invt;

import legion.ObjectModelInfoDto;

public class InvtOrderInfoDto extends ObjectModelInfoDto implements InvtOrderInfo {

	protected InvtOrderInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String iosn; // invt order serial number
	private String applierId;
	private String applierName;
	private long apvTime; // approval time
	private String remark; //

	@Override
	public String getIosn() {
		return iosn;
	}

	void setIosn(String iosn) {
		this.iosn = iosn;
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
	public long getApvTime() {
		return apvTime;
	}

	void setApvTime(long apvTime) {
		this.apvTime = apvTime;
	}

	@Override
	public String getRemark() {
		return remark;
	}

	void setRemark(String remark) {
		this.remark = remark;
	}

}
