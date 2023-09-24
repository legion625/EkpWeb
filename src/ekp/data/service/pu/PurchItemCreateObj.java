package ekp.data.service.pu;

import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartUnit;

public class PurchItemCreateObj {
	/* purchUid+mmUid作為biz key */
	private String purchUid;
	private String mmUid;

	/* 快照了物料基本檔當下的各欄位 */
	private String mmMano; // 物料基本檔料號
	private String mmName; // 品名
	private String mmSpecification;
	private PartUnit mmStdUnit;
	/* 快照了當下主要參考的PartAcq */
	private boolean refPa;
	private String refPaUid;
	private PartAcquisitionType refPaType;
	// 依物料基本檔輸入採購的數量和總價
	private double qty;
	private double value;
	//
	private String remark; // 備註（補充說明）

	public String getPurchUid() {
		return purchUid;
	}

	public void setPurchUid(String purchUid) {
		this.purchUid = purchUid;
	}

	public String getMmUid() {
		return mmUid;
	}

	public void setMmUid(String mmUid) {
		this.mmUid = mmUid;
	}

	public String getMmMano() {
		return mmMano;
	}

	public void setMmMano(String mmMano) {
		this.mmMano = mmMano;
	}

	public String getMmName() {
		return mmName;
	}

	public void setMmName(String mmName) {
		this.mmName = mmName;
	}

	public String getMmSpecification() {
		return mmSpecification;
	}

	public void setMmSpecification(String mmSpecification) {
		this.mmSpecification = mmSpecification;
	}

	public PartUnit getMmStdUnit() {
		return mmStdUnit;
	}

	public void setMmStdUnit(PartUnit mmStdUnit) {
		this.mmStdUnit = mmStdUnit;
	}

	
	
	public boolean isRefPa() {
		return refPa;
	}

	public void setRefPa(boolean refPa) {
		this.refPa = refPa;
	}

	public String getRefPaUid() {
		return refPaUid;
	}

	public void setRefPaUid(String refPaUid) {
		this.refPaUid = refPaUid;
	}

	public PartAcquisitionType getRefPaType() {
		return refPaType;
	}

	public void setRefPaType(PartAcquisitionType refPaType) {
		this.refPaType = refPaType;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
