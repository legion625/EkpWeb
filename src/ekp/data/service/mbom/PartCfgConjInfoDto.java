package ekp.data.service.mbom;

import ekp.ObjectModelInfoDto;

public class PartCfgConjInfoDto extends ObjectModelInfoDto implements PartCfgConjInfo {

	protected PartCfgConjInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------attribute-----------------------------------
	private String partCfgUid;

	private String partAcqUid;

	// -------------------------------------------------------------------------------
	// ---------------------------------getter&setter---------------------------------
	@Override
	public String getPartCfgUid() {
		return partCfgUid;
	}

	void setPartCfgUid(String partCfgUid) {
		this.partCfgUid = partCfgUid;
	}

	@Override
	public String getPartAcqUid() {
		return partAcqUid;
	}

	void setPartAcqUid(String partAcqUid) {
		this.partAcqUid = partAcqUid;
	}

}
