package ekp.data.service.invt;

public class InvtOrderCreateObj {
	private String applierId;
	private String applierName;
	private long apvTime; // approval time
	private String remark; //
	public String getApplierId() {
		return applierId;
	}
	public void setApplierId(String applierId) {
		this.applierId = applierId;
	}
	public String getApplierName() {
		return applierName;
	}
	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}
	public long getApvTime() {
		return apvTime;
	}
	public void setApvTime(long apvTime) {
		this.apvTime = apvTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
