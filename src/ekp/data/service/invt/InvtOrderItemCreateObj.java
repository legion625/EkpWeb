package ekp.data.service.invt;

import ekp.invt.type.InvtOrderType;

public class InvtOrderItemCreateObj {
	/* biz key */
	private String ioUid; // invt order uid

	private String mmUid;
	private String miUid;
	private String wrhsBinUid;

	private InvtOrderType ioType;
	private double orderQty; // 記錄異動的數量
	private double orderValue; // 記錄異動的金額

	public String getIoUid() {
		return ioUid;
	}

	public void setIoUid(String ioUid) {
		this.ioUid = ioUid;
	}

	public String getMmUid() {
		return mmUid;
	}

	public void setMmUid(String mmUid) {
		this.mmUid = mmUid;
	}

	public String getMiUid() {
		return miUid;
	}

	public void setMiUid(String miUid) {
		this.miUid = miUid;
	}

	public String getWrhsBinUid() {
		return wrhsBinUid;
	}

	public void setWrhsBinUid(String wrhsBinUid) {
		this.wrhsBinUid = wrhsBinUid;
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
