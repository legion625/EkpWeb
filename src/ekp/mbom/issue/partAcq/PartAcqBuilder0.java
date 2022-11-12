package ekp.mbom.issue.partAcq;

import ekp.mbom.type.PartAcquisitionType;

public class PartAcqBuilder0 extends PartAcqBuilder {
	/* base */
	// none

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected PartAcqBuilder0 appendBase() {
		// none
		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	@Override
	public PartAcqBuilder0 appendPartUid(String partUid) {
		return (PartAcqBuilder0) super.appendPartUid(partUid);
	}

	@Override
	public PartAcqBuilder0 appendPartPin(String partPin) {
		return (PartAcqBuilder0) super.appendPartPin(partPin);
	}

	@Override
	public PartAcqBuilder0 appendId(String id) {
		return (PartAcqBuilder0) super.appendId(id);
	}

	@Override
	public PartAcqBuilder0 appendName(String name) {
		return (PartAcqBuilder0) super.appendName(name);
	}

	@Override
	public PartAcqBuilder0 appendType(PartAcquisitionType type) {
		return (PartAcqBuilder0) super.appendType(type);
	}

}
