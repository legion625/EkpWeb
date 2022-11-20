package ekp.data.service.mbom;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.MbomDataService;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class PartInfoDto extends ObjectModelInfoDto implements PartInfo {
	protected PartInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------attribute-----------------------------------
	private String pin;
	private String name;

	// -------------------------------------------------------------------------------
	// ---------------------------------getter&setter---------------------------------
	@Override
	public String getPin() {
		return pin;
	}

	void setPin(String pin) {
		this.pin = pin;
	}

	@Override
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	// -------------------------------------------------------------------------------
	private BizObjLoader<List<PartAcqInfo>> paListLoader = BizObjLoader.of(
			() -> DataServiceFactory.getInstance().getService(MbomDataService.class).loadPartAcquisitionList(getUid()));

	@Override
	public List<PartAcqInfo> getPaList(boolean _reload) {
		return paListLoader.getObj(_reload);
	}

	private BizObjLoader<List<PpartInfo>> ppartListLoader = BizObjLoader.of(
			() -> DataServiceFactory.getInstance().getService(MbomDataService.class).loadParsPartListByPart(getUid()));

	@Override
	public List<PpartInfo> getPpartList(boolean _reload) {
		return ppartListLoader.getObj(_reload);
	}
	
	private BizObjLoader<List<PartCfgInfo>> partCfgListLoader = BizObjLoader.of(
			() -> DataServiceFactory.getInstance().getService(MbomDataService.class).loadPartCfgList(getUid()));
	
	@Override
	public List<PartCfgInfo> getPartCfgList(boolean _reload){
		return partCfgListLoader.getObj(_reload);
	}
}
