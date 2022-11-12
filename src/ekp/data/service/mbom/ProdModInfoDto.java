package ekp.data.service.mbom;

import ekp.ObjectModelInfoDto;

public class ProdModInfoDto extends ObjectModelInfoDto implements ProdModInfo{

	protected ProdModInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String prodUid; // 對應的產品項 biz key
	//
	private String id; // 識別碼 biz key
	private String name;
	private String desp;

	@Override
	public String getProdUid() {
		return prodUid;
	}

	void setProdUid(String prodUid) {
		this.prodUid = prodUid;
	}

	@Override
	public String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDesp() {
		return desp;
	}

	void setDesp(String desp) {
		this.desp = desp;
	}
}
