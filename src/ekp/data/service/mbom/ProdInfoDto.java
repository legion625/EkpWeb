package ekp.data.service.mbom;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.MbomDataService;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class ProdInfoDto extends ObjectModelInfoDto implements ProdInfo {

	protected ProdInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String id; // 型號 biz key
	private String name; // 名稱

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

	// -------------------------------------------------------------------------------
	@Override
	public ProdInfo reload() {
		return DataServiceFactory.getInstance().getService(MbomDataService.class).loadProd(getUid());
	}

	private BizObjLoader<List<ProdCtlInfo>> prodCtlListLoader = BizObjLoader.of(
			() -> DataServiceFactory.getInstance().getService(MbomDataService.class).loadProdCtlListByProd(getUid()));
	
	@Override
	public List<ProdCtlInfo> getProdCtlList(){
		return prodCtlListLoader.getObj();
	}
	
	private BizObjLoader<List<ProdCtlInfo>> prodCtlListLv1Loader = BizObjLoader
			.of(() -> DataServiceFactory.getInstance().getService(MbomDataService.class).loadProdCtlListLv1(getUid()));

	@Override
	public List<ProdCtlInfo> getProdCtlListLv1() {
		return prodCtlListLv1Loader.getObj();
	}
	
	private BizObjLoader<List<ProdModInfo>> prodModListLoader = BizObjLoader
			.of(() -> DataServiceFactory.getInstance().getService(MbomDataService.class).loadProdModList(getUid()));
	
	@Override
	public List<ProdModInfo> getProdModList(){
		return prodModListLoader.getObj();
	}

}
