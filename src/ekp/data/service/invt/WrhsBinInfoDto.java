package ekp.data.service.invt;

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

}
