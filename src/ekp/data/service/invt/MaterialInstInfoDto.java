package ekp.data.service.invt;

import ekp.invt.type.MaterialInstAcqChannel;
import ekp.invt.type.MaterialInstSrcStatus;
import legion.ObjectModelInfoDto;

public class MaterialInstInfoDto extends ObjectModelInfoDto implements MaterialInstInfo{

	protected MaterialInstInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String mmUid;

	private String misn; // material instance serial number
	private MaterialInstAcqChannel miac;
	private String miacSrcNo;
	private double qty; // 數量
	private double value; // 帳值
	private long effDate; // 生效日期
	private long expDate; // 失效日期
	
	private MaterialInstSrcStatus srcStatus;
	
	
	@Override
	public String getMmUid() {
		return mmUid;
	}
	void setMmUid(String mmUid) {
		this.mmUid = mmUid;
	}
	@Override
	public String getMisn() {
		return misn;
	}
	void setMisn(String misn) {
		this.misn = misn;
	}
	@Override
	public MaterialInstAcqChannel getMiac() {
		return miac;
	}
	void setMiac(MaterialInstAcqChannel miac) {
		this.miac = miac;
	}
	@Override
	public String getMiacSrcNo() {
		return miacSrcNo;
	}
	void setMiacSrcNo(String miacSrcNo) {
		this.miacSrcNo = miacSrcNo;
	}
	@Override
	public double getQty() {
		return qty;
	}
	void setQty(double qty) {
		this.qty = qty;
	}
	@Override
	public double getValue() {
		return value;
	}
	void setValue(double value) {
		this.value = value;
	}
	@Override
	public long getEffDate() {
		return effDate;
	}
	void setEffDate(long effDate) {
		this.effDate = effDate;
	}
	@Override
	public long getExpDate() {
		return expDate;
	}
	void setExpDate(long expDate) {
		this.expDate = expDate;
	}
	@Override
	public MaterialInstSrcStatus getSrcStatus() {
		return srcStatus;
	}
	 void setSrcStatus(MaterialInstSrcStatus srcStatus) {
		this.srcStatus = srcStatus;
	}
	
	
}
