package ekp.data.service.mbom;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.MbomDataService;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class ProdCtlInfoDto extends ObjectModelInfoDto implements ProdCtlInfo{

	protected ProdCtlInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}
	
	private String id; // 型號 biz key
	private int lv; // 1:系統;2:次系統;3:模組 預設先展到第3階
	private String name; // 名稱
	private boolean req; // 是否為必要的

	private String parentUid;
	private String parentId;

	//
	private String prodUid;

	@Override
	public String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	@Override
	public int getLv() {
		return lv;
	}

	void setLv(int lv) {
		this.lv = lv;
	}

	@Override
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean isReq() {
		return req;
	}

	void setReq(boolean req) {
		this.req = req;
	}

	@Override
	public String getParentUid() {
		return parentUid;
	}

	void setParentUid(String parentUid) {
		this.parentUid = parentUid;
	}

	@Override
	public String getParentId() {
		return parentId;
	}

	void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String getProdUid() {
		return prodUid;
	}

	void setProdUid(String prodUid) {
		this.prodUid = prodUid;
	}

	// -------------------------------------------------------------------------------
	@Override
	public ProdCtlInfo reload() {
		return DataServiceFactory.getInstance().getService(MbomDataService.class).loadProdCtl(getUid());
	}

	private BizObjLoader<List<ProdCtlInfo>> childrenListLoader = BizObjLoader
			.of(() -> DataServiceFactory.getInstance().getService(MbomDataService.class).loadProdCtlList(getUid()));

	@Override
	public List<ProdCtlInfo> getChildrenList() {
		return childrenListLoader.getObj();
	}
	
	
	private BizObjLoader<ProdInfo> prodLoader = BizObjLoader.PROD.get();

	@Override
	public ProdInfo getProd() {
		return prodLoader.getObj(getProdUid());
	}

	private BizObjLoader<List<ProdCtlPartCfgConjInfo>> pcpccListLoader = BizObjLoader.of(() -> DataServiceFactory
			.getInstance().getService(MbomDataService.class).loadProdCtlPartCfgConjList1(getUid()));

	@Override
	public List<ProdCtlPartCfgConjInfo> getPcpccList() {
		return pcpccListLoader.getObj();
	}
	
	
}
