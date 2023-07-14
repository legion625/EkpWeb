package ekp.data.service.invt;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.InvtDataService;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class WrhsLocInfoDto extends ObjectModelInfoDto implements WrhsLocInfo {

	protected WrhsLocInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------attribute-----------------------------------
	private String id;
	private String name;

	// -------------------------------------------------------------------------------
	// ---------------------------------getter&setter---------------------------------
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
	private BizObjLoader<List<WrhsBinInfo>> wrhsBinListLoader = BizObjLoader
			.of(() -> DataServiceFactory.getInstance().getService(InvtDataService.class).loadWrhsBinList(getUid()));

	@Override
	public List<WrhsBinInfo> getWrhsBinList() {
		return wrhsBinListLoader.getObj();
	}

}
