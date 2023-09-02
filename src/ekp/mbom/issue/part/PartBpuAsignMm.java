package ekp.mbom.issue.part;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mbom.PartInfo;
import legion.util.TimeTraveler;

public class PartBpuAsignMm extends PartBpu{
	/* base */
	private PartInfo part;
	
	/* data */
	private MaterialMasterInfo mm;
	
	// -------------------------------------------------------------------------------
	@Override
	protected PartBpuAsignMm appendBase() {
		/* base */
		part = (PartInfo) args[0];
		appendPart(part);

		/* data */
		// none

		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public PartBpuAsignMm appendMm(MaterialMasterInfo mm) {
		this.mm = mm;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public MaterialMasterInfo getMm() {
		return mm;
	}
	
	// -------------------------------------------------------------------------------
	public String getMmUid() {
		return getMm().getUid();
	}
	public String getMmMano() {
		return getMm().getMano();
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}
	
	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;
		if (getPart() == null) {
			v = false;
			_msg.append("Part null").append(System.lineSeparator());
		} else {
			if (getPart().isMmAssigned()) {
				v = false;
				_msg.append("Mm has been assigned.").append(System.lineSeparator());
			}
		}

		if (getMm() == null) {
			v = false;
			_msg.append("MaterialMaster null").append(System.lineSeparator());
		}

		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		//
		if(!mbomDataService.partAssignMm(getPart().getUid(), getMmUid(), getMmMano())) {
			tt.travel();
			log.error("mbomDataService.partAssignMm return false. [{}][{}][{}]", getPart().getUid(),getMmUid(), getMmMano());
			return false;
		}
		tt.addSite("revert partAssignMm", () -> mbomDataService.partRevertAssignMm(getPart().getUid()));
		log.info("mbomDataService.partAssignMm [{}][{}][{}]", getPart().getUid(), getMmUid(), getMmMano());
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);
		
		return true;
	}

}
