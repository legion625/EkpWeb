package ekp.data.service.mbom;

public class ProdModCreateObj {
	private String prodUid; // 對應的產品項 biz key
	//
	private String id; // 識別碼 biz key
	private String name;
	private String desp;

	public String getProdUid() {
		return prodUid;
	}

	public void setProdUid(String prodUid) {
		this.prodUid = prodUid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}
}
