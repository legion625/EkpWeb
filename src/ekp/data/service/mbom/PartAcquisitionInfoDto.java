package ekp.data.service.mbom;

import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.MbomDataService;
import ekp.mbom.type.PartAcquisitionType;

public class PartAcquisitionInfoDto extends ObjectModelInfoDto implements PartAcqInfo {

	protected PartAcquisitionInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------attribute-----------------------------------
	private String partUid; // ref data key
	private String partPin; // ref biz key

	private String id; // biz key
	private String name;
	private PartAcquisitionType type;

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
	
	// -------------------------------------------------------------------------------
	private BizObjLoader<List<ParsInfo>> parsListLoader = BizObjLoader.of(() -> DataServiceFactory.getInstance()
			.getService(MbomDataService.class).loadPartAcqRoutingStepList(getUid()));
	
	@Override
	public List<ParsInfo> getParsList(boolean _reload){
		return parsListLoader.getObj(_reload);
	}

}
