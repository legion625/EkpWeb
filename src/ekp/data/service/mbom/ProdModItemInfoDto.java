package ekp.data.service.mbom;

import ekp.data.BizObjLoader;
import ekp.data.MbomDataService;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class ProdModItemInfoDto extends ObjectModelInfoDto implements ProdModItemInfo{

	protected ProdModItemInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String prodModUid; // 產品model識別碼 biz key
	private String prodCtlUid; // prodCtl識別碼 biz key
	//
	private boolean partAcqCfgAssigned;
	private String partCfgUid; // PartCfg識別碼，此model對應的prodCtl所選用的構型
	private String partAcqUid; // PartAcq識別碼，此model對應的prodCtl所選用的獲取方式

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
	public boolean isPartAcqCfgAssigned() {
		return partAcqCfgAssigned;
	}

	void setPartAcqCfgAssigned(boolean partAcqCfgAssigned) {
		this.partAcqCfgAssigned = partAcqCfgAssigned;
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
	@Override
		public ProdModItemInfo reload() {
		return DataServiceFactory.getInstance().getService(MbomDataService.class).loadProdModItem(getUid());
	}
	
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

	private BizObjLoader<PartAcqInfo> partAcqLoader = BizObjLoader.PART_ACQ.get();

	@Override
	public PartAcqInfo getPartAcq() {
		return partAcqLoader.getObj(getPartAcqUid());
	}
}
