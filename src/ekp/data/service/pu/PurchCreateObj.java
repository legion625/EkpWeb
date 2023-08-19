package ekp.data.service.pu;

public class PurchCreateObj {
	private String puNo; // 購案案號
	private String title; // 名稱
	private String supplierName;
	private String supplierBan; // 供應商統編（臺灣）

	public String getPuNo() {
		return puNo;
	}

	public void setPuNo(String puNo) {
		this.puNo = puNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierBan() {
		return supplierBan;
	}

	public void setSupplierBan(String supplierBan) {
		this.supplierBan = supplierBan;
	}
}
