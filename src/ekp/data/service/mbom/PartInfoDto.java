package ekp.data.service.mbom;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.InvtDataService;
import ekp.data.MbomDataService;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.mbom.type.PartUnit;
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
	private PartUnit unit;

//	// mm
//	private boolean mmAssigned;
//	private String mmUid;
//	private String mmMano;

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

	@Override
	public PartUnit getUnit() {
		return unit;
	}

	void setUnit(PartUnit unit) {
		this.unit = unit;
	}

//	@Override
//	public boolean isMmAssigned() {
//		return mmAssigned;
//	}
//
//	void setMmAssigned(boolean mmAssigned) {
//		this.mmAssigned = mmAssigned;
//	}
//
//	@Override
//	public String getMmUid() {
//		return mmUid;
//	}
//
//	void setMmUid(String mmUid) {
//		this.mmUid = mmUid;
//	}
//
//	@Override
//	public String getMmMano() {
//		return mmMano;
//	}
//
//	void setMmMano(String mmMano) {
//		this.mmMano = mmMano;
//	}

	// -------------------------------------------------------------------------------
	@Override
	public PartInfo reload() {
		return DataServiceFactory.getInstance().getService(MbomDataService.class).loadPart(getUid());
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

	private BizObjLoader<List<PartCfgInfo>> partCfgListLoader = BizObjLoader
			.of(() -> DataServiceFactory.getInstance().getService(MbomDataService.class).loadPartCfgList(getUid()));

	@Override
	public List<PartCfgInfo> getRootPartCfgList(boolean _reload) {
		return partCfgListLoader.getObj(_reload);
	}
	
//	// -------------------------------------------------------------------------------
//	private BizObjLoader<MaterialMasterInfo> mmLoader = BizObjLoader.of(() -> isMmAssigned() ?
//
//			DataServiceFactory.getInstance().getService(InvtDataService.class).loadMaterialMaster(getMmUid()) : null);
//
//	@Override
//	public MaterialMasterInfo getMm() {
//		return mmLoader.getObj();
//	}
}
