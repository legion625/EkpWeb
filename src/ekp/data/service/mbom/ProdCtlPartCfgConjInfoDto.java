package ekp.data.service.mbom;

import ekp.ObjectModelInfoDto;

public class ProdCtlPartCfgConjInfoDto extends ObjectModelInfoDto implements ProdCtlPartCfgConjInfo {

	protected ProdCtlPartCfgConjInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String prodCtlUid; // 標的產品型錄prodCtl biz key
	private String partCfgUid; // 此產品型錄對應對產品構型PartCfg biz key

	@Override
	public String getProdCtlUid() {
		return prodCtlUid;
	}

	void setProdCtlUid(String prodCtlUid) {
		this.prodCtlUid = prodCtlUid;
	}

	@Override
	public String getPartCfgUid() {
		return partCfgUid;
	}

	void setPartCfgUid(String partCfgUid) {
		this.partCfgUid = partCfgUid;
	}

}
