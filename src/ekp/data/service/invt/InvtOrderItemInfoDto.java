package ekp.data.service.invt;

import ekp.invt.type.InvtOrderType;
import legion.ObjectModelInfoDto;

public class InvtOrderItemInfoDto extends ObjectModelInfoDto implements InvtOrderItemInfo{

	protected InvtOrderItemInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}
	
	private String ioUid; // invt order uid
	private String mbsUid; // BaterialBinStock uid (biz key) 指定「料項+儲位」

	private InvtOrderType ioType;
	private double orderQty; // 記錄異動的數量
	private double orderValue; // 記錄異動的金額
	@Override
	public String getIoUid() {
		return ioUid;
	}
	void setIoUid(String ioUid) {
		this.ioUid = ioUid;
	}
	@Override
	public String getMbsUid() {
		return mbsUid;
	}
	void setMbsUid(String mbsUid) {
		this.mbsUid = mbsUid;
	}
	@Override
	public InvtOrderType getIoType() {
		return ioType;
	}
	void setIoType(InvtOrderType ioType) {
		this.ioType = ioType;
	}
	@Override
	public double getOrderQty() {
		return orderQty;
	}
	void setOrderQty(double orderQty) {
		this.orderQty = orderQty;
	}
	@Override
	public double getOrderValue() {
		return orderValue;
	}
	void setOrderValue(double orderValue) {
		this.orderValue = orderValue;
	}

}
