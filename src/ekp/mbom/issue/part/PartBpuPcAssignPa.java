package ekp.mbom.issue.part;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgConjInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import legion.util.TimeTraveler;

public class PartBpuPcAssignPa extends PartBpu {
	protected Logger log = LoggerFactory.getLogger(PartBpuPcAssignPa.class);
	/* base */
	private PartInfo part;
	private PartCfgInfo pc;

	/* data */
	private PartAcqInfo inPa; // exsiting pa;
	private PartAcqInfo pa;

	// -------------------------------------------------------------------------------
	@Override
	protected PartBpuPcAssignPa appendBase() {
		/* base */
		part = (PartInfo) args[0];
		pc = (PartCfgInfo) args[1];
		appendPart(part);

		/* data */
		inPa = part.getPa(pc);

		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public PartBpuPcAssignPa appendPa(PartAcqInfo pa) {
		this.pa = pa;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	
	public PartCfgInfo getPc() {
		return pc;
	}
	
	public PartAcqInfo getInPa() {
		return inPa;
	}
	
	public PartAcqInfo getPa() {
		return pa;
	}

	// -------------------------------------------------------------------------------
	public List<PartAcqInfo> getAllPaList() {
		List<PartAcqInfo> list = part.getPaList(false);
		return list;
	}
	
	public boolean isPaAvaible(PartAcqInfo _pa) {
		return _pa == null ? false : inPa == null || !inPa.equals(_pa);
	}

	@Deprecated
	public List<PartAcqInfo> getAvailablePaList() {
		List<PartAcqInfo> list = part.getPaList(false);
		if (inPa != null)
			list.remove(inPa);
		return list;
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
			_msg.append("Part null.").append(System.lineSeparator());
		}

		//
		if (getPa() == null) {
			v = false;
			_msg.append("Part acquisition is not assigned.").append(System.lineSeparator());
		} else if (getInPa()!=null && getPa().equals(getInPa())) {
			v = false;
			_msg.append("Part acquisition does not change.").append(System.lineSeparator());
		}

		return v;
	}

	
	
	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		
		// 
		if (getInPa() != null) {
			PartCfgConjInfo inPcc =  getInPa().getPartCfgConj(getPc().getUid(), true);
			if (!mbomDataService.deletePartCfgConj(inPcc.getUid())) {
				log.error("mbomDataService.deletePartCfgConj return false. [{}][{}][{}]", inPcc.getUid(),
						inPcc.getPartCfgUid(), inPcc.getPartAcqUid());
				tt.travel();
				return false;
			}
			tt.addSite("revert deletePartCfgConj",
					() -> mbomDataService.createPartCfgConj(inPcc.getPartCfgUid(), inPcc.getPartAcqUid()) != null);
			log.info("mbomDataService.deletePartCfgConj [{}][{}][{}]", inPcc.getUid(), inPcc.getPartCfgUid(),
					inPcc.getPartAcqUid());
		}
		
		// 
		PartCfgConjInfo pcc = mbomDataService.createPartCfgConj(getPc().getUid(), getPa().getUid());
		if (pcc == null) {
			log.error("mbomDataService.createPartCfgConj return false. [{}][{}]", getPc().getUid(), getPa().getUid());
			tt.travel();
			return false;
		}
		tt.addSite("revert mbomDataService.createPartCfgConj", () -> mbomDataService.deletePartCfgConj(pcc.getUid()));
		log.info("mbomDataService.createPartCfgConj [{}][{}]", getPc().getUid(), getPa().getUid());

		//
		if(_tt!=null)
			_tt.copySitesFrom(tt);

		return true;
	}
}
