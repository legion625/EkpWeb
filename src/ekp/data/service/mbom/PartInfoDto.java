package ekp.data.service.mbom;

import ekp.ObjectModelInfoDto;

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
}
