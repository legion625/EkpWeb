package ekp.data.service.sd;

public class SalesOrderItemCreateObj {
	private String mmUid;
	private String mmMano;
	private String mmName;
	private String mmSpec;
	private double qty;
	private double value;

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

	public String getMmSpec() {
		return mmSpec;
	}

	public void setMmSpec(String mmSpec) {
		this.mmSpec = mmSpec;
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
}
