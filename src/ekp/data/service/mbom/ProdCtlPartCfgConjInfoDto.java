package ekp.data.service.mbom;

import ekp.data.BizObjLoader;
import legion.ObjectModelInfoDto;

public class ProdCtlPartCfgConjInfoDto extends ObjectModelInfoDto implements ProdCtlPartCfgConjInfo {

	protected ProdCtlPartCfgConjInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String prodCtlUid; // 標的產品型錄prodCtl biz key
	private String partCfgUid; // 此產品型錄對應對產品構型PartCfg biz key
	private String partAcqUid; // 此產品型錄對應產品PartAcq biz

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
	
	@Override
	public String getPartAcqUid() {
		return partAcqUid;
	}

	void setPartAcqUid(String partAcqUid) {
		this.partAcqUid = partAcqUid;
	}

	// -------------------------------------------------------------------------------
	private BizObjLoader<PartCfgInfo> partCfgLoader = BizObjLoader.PART_CFG.get();

	@Override
	public PartCfgInfo getPartCfg() {
		return partCfgLoader.getObj(getPartCfgUid());
	}
	private BizObjLoader<PartAcqInfo> partAcqLoader = BizObjLoader.PART_ACQ.get();
	
	@Override
	public PartAcqInfo getPartAcq() {
		return partAcqLoader.getObj(getPartAcqUid());
	}

}
