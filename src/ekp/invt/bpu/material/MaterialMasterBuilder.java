package ekp.invt.bpu.material;

import ekp.data.InvtDataService;
import ekp.data.service.invt.MaterialMasterCreateObj;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.mbom.type.PartUnit;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class MaterialMasterBuilder extends Bpu<MaterialMasterInfo> {
	private static InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);

	/* base */
	private String mano;
	private String name;
	private String specification;
	private PartUnit stdUnit;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected MaterialMasterBuilder appendMano(String mano) {
		this.mano = mano;
		return this;
	}

	protected MaterialMasterBuilder appendName(String name) {
		this.name = name;
		return this;
	}

	protected MaterialMasterBuilder appendSpecification(String specification) {
		this.specification = specification;
		return this;
	}

	protected MaterialMasterBuilder appendStdUnit(PartUnit stdUnit) {
		this.stdUnit = stdUnit;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getMano() {
		return mano;
	}

	public String getName() {
		return name;
	}

	public String getSpecification() {
		return specification;
	}

	public PartUnit getStdUnit() {
		return stdUnit;
	}

	// -------------------------------------------------------------------------------
	private MaterialMasterCreateObj packMaterialMasterCreateObj() {
		MaterialMasterCreateObj dto = new MaterialMasterCreateObj();
		dto.setMano(getMano());
		dto.setName(getName());
		dto.setSpecification(getSpecification());
		dto.setStdUnit(getStdUnit());
		return dto;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = true;

		if (DataFO.isEmptyString(getMano())) {
			_msg.append("Mano should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		if (DataFO.isEmptyString(getName())) {
			_msg.append("Name should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		//
		if (invtDataService.loadMaterialMasterByMano(getMano()) != null) {
			_msg.append("Duplicated mano.").append(System.lineSeparator());
			v = false;
		}

		//
		if (getStdUnit() == null || PartUnit.UNDEFINED == getStdUnit()) {
			_msg.append("StdUnit error.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	@Override
	protected MaterialMasterInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		//
		MaterialMasterInfo mm = invtDataService.createMaterialMaster(packMaterialMasterCreateObj());
		if (mm == null) {
			tt.travel();
			log.error("invtDataService.createMaterialMaster return null.");
			return null;
		}
		tt.addSite("revert createMaterialMaster", () -> invtDataService.deleteMaterialMaster(mm.getUid()));
		log.info("invtDataService.createMaterialMaster [{}][{}][{}]", mm.getUid(), mm.getMano(), mm.getName());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return mm;
	}

}
