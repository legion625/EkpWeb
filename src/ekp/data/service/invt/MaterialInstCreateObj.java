package ekp.data.service.invt;

import ekp.invt.type.MaterialInstAcqChannel;

public class MaterialInstCreateObj {
	private String mmUid;

	private MaterialInstAcqChannel miac;
	private double qty; // 數量
	private double value; // 帳值
	private long effDate; // 生效日期
	private long expDate; // 失效日期

	public String getMmUid() {
		return mmUid;
	}

	public void setMmUid(String mmUid) {
		this.mmUid = mmUid;
	}

	public MaterialInstAcqChannel getMiac() {
		return miac;
	}

	public void setMiac(MaterialInstAcqChannel miac) {
		this.miac = miac;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public long getEffDate() {
		return effDate;
	}

	public void setEffDate(long effDate) {
		this.effDate = effDate;
	}

	public long getExpDate() {
		return expDate;
	}

	public void setExpDate(long expDate) {
		this.expDate = expDate;
	}
}
