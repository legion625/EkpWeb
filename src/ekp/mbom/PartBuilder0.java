package ekp.mbom;

import ekp.data.service.mbom.PartInfo;
import legion.biz.BizObjBuilder;

public class PartBuilder0 extends PartBuilder {
	/* base */
	// none

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected BizObjBuilder<PartInfo> appendBase() {
		// none
		return this;
	}

	// -------------------------------------------------------------------------------
	@Override
	public PartBuilder appendPin(String pin) {
		return super.appendPin(pin);
	}

	@Override
	public PartBuilder appendName(String name) {
		return super.appendName(name);
	}

}
