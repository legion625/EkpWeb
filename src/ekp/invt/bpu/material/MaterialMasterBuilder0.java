package ekp.invt.bpu.material;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.mbom.type.PartUnit;
import legion.biz.Bpu;

public class MaterialMasterBuilder0 extends MaterialMasterBuilder {

	/* base */
	// none

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected Bpu<MaterialMasterInfo> appendBase() {
		// none
		return this;
	}

	// -------------------------------------------------------------------------------
	@Override
	public MaterialMasterBuilder0 appendMano(String mano) {
		return (MaterialMasterBuilder0) super.appendMano(mano);
	}

	public MaterialMasterBuilder0 appendName(String name) {
		return (MaterialMasterBuilder0) super.appendName(name);
	}

	public MaterialMasterBuilder0 appendSpecification(String specification) {
		return (MaterialMasterBuilder0) super.appendSpecification(specification);
	}

	public MaterialMasterBuilder0 appendStdUnit(PartUnit stdUnit) {
		return (MaterialMasterBuilder0) super.appendStdUnit(stdUnit);
	}
}
