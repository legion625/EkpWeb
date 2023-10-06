package ekp.data.service.mbom;

import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.InvtDataService;
import ekp.data.MbomDataService;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.mbom.type.PartAcqStatus;
import ekp.mbom.type.PartAcquisitionType;

public class PartAcquisitionInfoDto extends ObjectModelInfoDto implements PartAcqInfo {

	protected PartAcquisitionInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------attribute-----------------------------------
	private String partUid; // ref data key
	private String partPin; // ref biz key

	private PartAcqStatus status;
	
	private String id; // biz key
	private String name;
	private PartAcquisitionType type;
	
	// mm
	private boolean mmAssigned;
	private String mmUid;
	private String mmMano;
	
	private long publishTime;
	
	private double refUnitCost;
	
	// -------------------------------------------------------------------------------
	// ---------------------------------getter&setter---------------------------------
	@Override
	public String getPartUid() {
		return partUid;
	}

	void setPartUid(String partUid) {
		this.partUid = partUid;
	}

	@Override
	public String getPartPin() {
		return partPin;
	}

	void setPartPin(String partPin) {
		this.partPin = partPin;
	}
	
	@Override
	public PartAcqStatus getStatus() {
		return status;
	}

	void setStatus(PartAcqStatus status) {
		this.status = status;
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
	public PartAcquisitionType getType() {
		return type;
	}

	void setType(PartAcquisitionType type) {
		this.type = type;
	}
	
	@Override
	public boolean isMmAssigned() {
		return mmAssigned;
	}

	void setMmAssigned(boolean mmAssigned) {
		this.mmAssigned = mmAssigned;
	}

	@Override
	public String getMmUid() {
		return mmUid;
	}

	void setMmUid(String mmUid) {
		this.mmUid = mmUid;
	}

	@Override
	public String getMmMano() {
		return mmMano;
	}

	void setMmMano(String mmMano) {
		this.mmMano = mmMano;
	}
	
	@Override
	public long getPublishTime() {
		return publishTime;
	}

	void setPublishTime(long publishTime) {
		this.publishTime = publishTime;
	}
	
	@Override
	public double getRefUnitCost() {
		return refUnitCost;
	}

	void setRefUnitCost(double refUnitCost) {
		this.refUnitCost = refUnitCost;
	}

	
	
	// -------------------------------------------------------------------------------
	@Override
	public PartAcqInfo reload() {
		return DataServiceFactory.getInstance().getService(MbomDataService.class).loadPartAcquisition(getUid());
	}

	private BizObjLoader<PartInfo> partLoader = BizObjLoader.PART.get();

	@Override
	public PartInfo getPart(boolean _reload) {
		return partLoader.getObj(getPartUid());
	}

	private BizObjLoader<List<ParsInfo>> parsListLoader = BizObjLoader.of(() -> DataServiceFactory.getInstance()
			.getService(MbomDataService.class).loadPartAcqRoutingStepList(getUid()));

	@Override
	public List<ParsInfo> getParsList(boolean _reload){
		return parsListLoader.getObj(_reload);
	}

	private BizObjLoader<List<PartCfgConjInfo>> pccListLoader = BizObjLoader.of(() -> DataServiceFactory.getInstance()
			.getService(MbomDataService.class).loadPartCfgConjListByPartAcq(getUid()));

	@Override
	public List<PartCfgConjInfo> getPartCfgConjList(boolean _reload) {
		return pccListLoader.getObj(_reload);
	}
	
	// -------------------------------------------------------------------------------
	private BizObjLoader<MaterialMasterInfo> mmLoader = BizObjLoader.of(() -> isMmAssigned() ?
			DataServiceFactory.getInstance().getService(InvtDataService.class).loadMaterialMaster(getMmUid()) : null);

	@Override
	public MaterialMasterInfo getMm() {
		return mmLoader.getObj();
	}

}
