package ekp.mbom.issue.part;

import ekp.mbom.type.PartUnit;

public class PartBuilder0 extends PartBuilder {
	/* base */
	// none

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected PartBuilder0 appendBase() {
		// none
		return this;
	}

	// -------------------------------------------------------------------------------
	@Override
	public PartBuilder0 appendPin(String pin) {
		return (PartBuilder0) super.appendPin(pin);
	}

	@Override
	public PartBuilder0 appendName(String name) {
		return (PartBuilder0) super.appendName(name);
	}
	
	@Override
	public PartBuilder0 appendUnit(PartUnit unit) {
		return (PartBuilder0) super.appendUnit(unit);
	}

}
