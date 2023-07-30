package ekp.data.service.invt;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.InvtDataService;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class WrhsBinInfoDto extends ObjectModelInfoDto implements WrhsBinInfo {

	protected WrhsBinInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}
	
	private String wlUid;
	private String id;
	private String name;

	@Override
	public String getWlUid() {
		return wlUid;
	}

	void setWlUid(String wlUid) {
		this.wlUid = wlUid;
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
	
	// -------------------------------------------------------------------------------
	private BizObjLoader<WrhsLocInfo> wrhsLocLoader = BizObjLoader
			.of(() -> DataServiceFactory.getInstance().getService(InvtDataService.class).loadWrhsLoc(getWlUid()));

	@Override
	public WrhsLocInfo getWrhsLoc() {
		return wrhsLocLoader.getObj();
	}

	private BizObjLoader<List<MaterialBinStockInfo>> mbsListLoader = BizObjLoader.of(() -> DataServiceFactory
			.getInstance().getService(InvtDataService.class).loadMaterialBinStockListByWrhsBin(getUid()));

	@Override
	public List<MaterialBinStockInfo> getMbsList() {
		return mbsListLoader.getObj();
	}

}
