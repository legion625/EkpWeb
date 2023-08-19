package ekp.data.service.pu;

import ekp.mbom.type.PartUnit;
import legion.ObjectModelInfoDto;

public class PurchItemInfoDto extends ObjectModelInfoDto implements PurchItemInfo {
	protected PurchItemInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------attribute-----------------------------------
	/* purchUid+mmUid作為biz key */
	private String purchUid;
	private String mmUid;

	/* 快照了物料基本檔當下的各欄位 */
	private String mmMano; // 物料基本檔料號
	private String mmName; // 品名
	private String mmSpecification;
	private PartUnit mmStdUnit;
	// 依物料基本檔輸入採購的數量和總價
	private double qty;
	private double value;
	//
	private String remark; // 備註（補充說明）

	// -------------------------------------------------------------------------------
	// ---------------------------------getter&setter---------------------------------
	@Override
	public String getPurchUid() {
		return purchUid;
	}

	void setPurchUid(String purchUid) {
		this.purchUid = purchUid;
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
	public String getMmSpecification() {
		return mmSpecification;
	}

	void setMmSpecification(String mmSpecification) {
		this.mmSpecification = mmSpecification;
	}

	@Override
	public PartUnit getMmStdUnit() {
		return mmStdUnit;
	}

	void setMmStdUnit(PartUnit mmStdUnit) {
		this.mmStdUnit = mmStdUnit;
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
	public String getRemark() {
		return remark;
	}

	void setRemark(String remark) {
		this.remark = remark;
	}

}
