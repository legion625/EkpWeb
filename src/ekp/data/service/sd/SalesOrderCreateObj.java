package ekp.data.service.sd;

public class SalesOrderCreateObj {
	private String title;
	private String customerUid;
	private String customerName;
	private String customerBan;

	private String salerId;
	private String salerName;
	private long saleDate;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCustomerUid() {
		return customerUid;
	}

	public void setCustomerUid(String customerUid) {
		this.customerUid = customerUid;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerBan() {
		return customerBan;
	}

	public void setCustomerBan(String customerBan) {
		this.customerBan = customerBan;
	}

	public String getSalerId() {
		return salerId;
	}

	public void setSalerId(String salerId) {
		this.salerId = salerId;
	}

	public String getSalerName() {
		return salerName;
	}

	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}

	public long getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(long saleDate) {
		this.saleDate = saleDate;
	}
}
