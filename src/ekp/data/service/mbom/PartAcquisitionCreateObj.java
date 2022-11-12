package ekp.data.service.mbom;

import ekp.mbom.type.PartAcquisitionType;

public class PartAcquisitionCreateObj {
	private String partUid; // ref data key
	private String partPin; // ref biz key

	private String id; // biz key
	private String name;
	private PartAcquisitionType type;

	public String getPartUid() {
		return partUid;
	}

	public void setPartUid(String partUid) {
		this.partUid = partUid;
	}

	public String getPartPin() {
		return partPin;
	}

	public void setPartPin(String partPin) {
		this.partPin = partPin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PartAcquisitionType getType() {
		return type;
	}

	public void setType(PartAcquisitionType type) {
		this.type = type;
	}
}
