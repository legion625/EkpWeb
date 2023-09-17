package ekp.data.service.mbom;

import ekp.data.BizObjLoader;
import legion.ObjectModelInfoDto;

public class ProdModItemInfoDto extends ObjectModelInfoDto implements ProdModItemInfo{

	protected ProdModItemInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String prodModUid; // 產品model識別碼 biz key
	private String prodCtlUid; // prodCtl識別碼 biz key
	//
	private boolean partCfgAssigned;
	private String partCfgUid; // PartCfg識別碼，此model對應的prodCtl所選用的構型

	@Override
	public String getProdModUid() {
		return prodModUid;
	}

	void setProdModUid(String prodModUid) {
		this.prodModUid = prodModUid;
	}

	@Override
	public String getProdCtlUid() {
		return prodCtlUid;
	}

	void setProdCtlUid(String prodCtlUid) {
		this.prodCtlUid = prodCtlUid;
	}

	@Override
	public boolean isPartCfgAssigned() {
		return partCfgAssigned;
	}

	void setPartCfgAssigned(boolean partCfgAssigned) {
		this.partCfgAssigned = partCfgAssigned;
	}

	@Override
	public String getPartCfgUid() {
		return partCfgUid;
	}

	void setPartCfgUid(String partCfgUid) {
		this.partCfgUid = partCfgUid;
	}

	// -------------------------------------------------------------------------------
	private BizObjLoader<ProdCtlInfo> prodCtlLoader = BizObjLoader.PROD_CTL.get();

	@Override
	public ProdCtlInfo getProdCtl() {
		return prodCtlLoader.getObj(getProdCtlUid());
	}

	private BizObjLoader<PartCfgInfo> partCfgLoader = BizObjLoader.PART_CFG.get();

	@Override
	public PartCfgInfo getPartCfg() {
		return partCfgLoader.getObj(getPartCfgUid());
	}
}
