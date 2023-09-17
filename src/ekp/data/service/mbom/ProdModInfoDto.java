package ekp.data.service.mbom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ekp.data.BizObjLoader;
import ekp.data.MbomDataService;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

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

	// -------------------------------------------------------------------------------
	@Override
	public ProdModInfo reload() {
		return DataServiceFactory.getInstance().getService(MbomDataService.class).loadProdMod(getUid());
	}

	private BizObjLoader<ProdInfo> prodLoader = BizObjLoader.PROD.get();

	@Override
	public ProdInfo getProd() {
		return prodLoader.getObj(getProdUid());
	}

	// -------------------------------------------------------------------------------
	private BizObjLoader<List<ProdModItemInfo>> prodModItemListLoader = BizObjLoader
			.of(() -> DataServiceFactory.getInstance().getService(MbomDataService.class).loadProdModItemList(getUid()));

	@Override
	public List<ProdModItemInfo> getProdModItemList() {
		return prodModItemListLoader.getObj();
	}
	
	private BizObjLoader<Map<String, ProdModItemInfo>> prodCtlUidProdModItemMapLoader = BizObjLoader.of(() -> {
		Map<String, ProdModItemInfo> map = new HashMap<>();
		for (ProdModItemInfo prodModItem : getProdModItemList())
			map.put(prodModItem.getProdCtlUid(), prodModItem);
		return map;
	});

	@Override
	public Map<String, ProdModItemInfo> getProdCtlUidProdModItemMap() {
		return prodCtlUidProdModItemMapLoader.getObj();
	}
}
