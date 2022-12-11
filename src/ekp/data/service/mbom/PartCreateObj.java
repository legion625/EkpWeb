package ekp.data.service.mbom;

import ekp.mbom.type.PartUnit;

public class PartCreateObj {
	private String pin;
	private String name;
	private PartUnit unit;

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PartUnit getUnit() {
		return unit;
	}

	public void setUnit(PartUnit unit) {
		this.unit = unit;
	}

}
