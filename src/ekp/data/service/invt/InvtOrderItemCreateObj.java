package ekp.data.service.invt;

import ekp.invt.type.InvtOrderType;

public class InvtOrderItemCreateObj {
	/* biz key */
	private String ioUid; // invt order uid
	private String mbsUid; // BaterialBinStock uid (biz key) 指定「料項+儲位」

	private InvtOrderType ioType;
	private double orderQty; // 記錄異動的數量
	private double orderValue; // 記錄異動的金額

	public String getIoUid() {
		return ioUid;
	}

	public void setIoUid(String ioUid) {
		this.ioUid = ioUid;
	}

	public String getMbsUid() {
		return mbsUid;
	}

	public void setMbsUid(String mbsUid) {
		this.mbsUid = mbsUid;
	}

	public InvtOrderType getIoType() {
		return ioType;
	}

	public void setIoType(InvtOrderType ioType) {
		this.ioType = ioType;
	}

	public double getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(double orderQty) {
		this.orderQty = orderQty;
	}

	public double getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(double orderValue) {
		this.orderValue = orderValue;
	}
}
