package ekp.data.service.sd;

import legion.ObjectModelInfoDto;

public class SalesOrderItemInfoDto extends ObjectModelInfoDto implements SalesOrderItemInfo {
	protected SalesOrderItemInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String soUid;
	private String mmUid;
	private String mmMano;
	private String mmName;
	private String mmSpec;
	private double qty;
	private double value;

	private boolean allDelivered; // 是否已交貨
	private long finishDeliveredDate;

	@Override
	public String getSoUid() {
		return soUid;
	}

	void setSoUid(String soUid) {
		this.soUid = soUid;
	}

	@Override
	public String getMmUid() {
		return mmUid;
	}

	void setMmUid(String mmUid) {
		this.mmUid = mmUid;
	}

	@Override
	public String getMmMano() {
		return mmMano;
	}

	void setMmMano(String mmMano) {
		this.mmMano = mmMano;
	}

	@Override
	public String getMmName() {
		return mmName;
	}

	void setMmName(String mmName) {
		this.mmName = mmName;
	}

	@Override
	public String getMmSpec() {
		return mmSpec;
	}

	void setMmSpec(String mmSpec) {
		this.mmSpec = mmSpec;
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
	public boolean isAllDelivered() {
		return allDelivered;
	}

	void setAllDelivered(boolean allDelivered) {
		this.allDelivered = allDelivered;
	}

	@Override
	public long getFinishDeliveredDate() {
		return finishDeliveredDate;
	}

	void setFinishDeliveredDate(long finishDeliveredDate) {
		this.finishDeliveredDate = finishDeliveredDate;
	}

}
