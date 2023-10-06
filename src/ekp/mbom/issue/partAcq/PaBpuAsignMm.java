package ekp.mbom.issue.partAcq;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.mbom.issue.part.PartBpu;
import legion.util.TimeTraveler;

public class PaBpuAsignMm extends PaBpu{
	/* base */
//	private PartInfo part;
	private PartAcqInfo pa;
	
	/* data */
	private MaterialMasterInfo mm;
	
	// -------------------------------------------------------------------------------
	@Override
	protected PaBpuAsignMm appendBase() {
		/* base */
//		part = (PartInfo) args[0];
//		appendPart(part);
		pa = (PartAcqInfo)args[0];
		appendPa(pa);

		/* data */
		// none

		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public PaBpuAsignMm appendMm(MaterialMasterInfo mm) {
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
//		if (getPart() == null) {
		if (getPa() == null) {
			v = false;
//			_msg.append("Part null").append(System.lineSeparator());
			_msg.append("Pa null").append(System.lineSeparator());
		} else {
//			if (getPart().isMmAssigned()) {
			if (getPa().isMmAssigned()) {
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
//		if(!mbomDataService.partAssignMm(getPart().getUid(), getMmUid(), getMmMano())) {
		if(!mbomDataService.partAcqAssignMm(getPa().getUid(), getMmUid(), getMmMano())) {
			tt.travel();
			log.error("mbomDataService.partAcqAssignMm return false. [{}][{}][{}]", getPa().getUid(),getMmUid(), getMmMano());
			return false;
		}
		tt.addSite("revert partAcqAssignMm", () -> mbomDataService.partAcqRevertAssignMm(getPa().getUid()));
		log.info("mbomDataService.partAcqAssignMm [{}][{}][{}]", getPa().getUid(), getMmUid(), getMmMano());
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);
		
		return true;
	}

}
