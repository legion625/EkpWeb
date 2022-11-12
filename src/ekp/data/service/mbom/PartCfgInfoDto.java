package ekp.data.service.mbom;

import ekp.mbom.type.PartCfgStatus;
import legion.ObjectModelInfoDto;

public class PartCfgInfoDto extends ObjectModelInfoDto implements PartCfgInfo {

	protected PartCfgInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String rootPartUid; // ref data key
	private String rootPartPin; // ref biz key

	private PartCfgStatus status;

	private String id; // biz key
	private String name;
	private String desp;

	@Override
	public String getRootPartUid() {
		return rootPartUid;
	}

	void setRootPartUid(String rootPartUid) {
		this.rootPartUid = rootPartUid;
	}

	@Override
	public String getRootPartPin() {
		return rootPartPin;
	}

	void setRootPartPin(String rootPartPin) {
		this.rootPartPin = rootPartPin;
	}

	@Override
	public PartCfgStatus getStatus() {
		return status;
	}

	void setStatus(PartCfgStatus status) {
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
	public String getDesp() {
		return desp;
	}

	void setDesp(String desp) {
		this.desp = desp;
	}

}
