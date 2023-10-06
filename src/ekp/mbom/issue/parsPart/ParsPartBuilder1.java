package ekp.mbom.issue.parsPart;

import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.mbom.issue.prod.ProdBuilder0;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public class ParsPartBuilder1 extends ParsPartBuilder {
	/* base */
	private ParsInfo pars;

	/* data */
	private PartInfo part;
	private double partReqQty = 0;

	// -------------------------------------------------------------------------------
	@Override
	protected ParsPartBuilder1 appendBase() {
		/* base */
		pars = (ParsInfo) args[0];
		appendParsUid(pars.getUid());

		/* data */
		// none

		return this;
	}
	
	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public ParsPartBuilder1 appendPart(PartInfo part) {
		this.part = part;
		return this;
	}

	public ParsPartBuilder1 appendPartReqQty(double partReqQty) {
		this.partReqQty = partReqQty;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public ParsInfo getPars() {
		return pars;
	}

	public PartInfo getPart() {
		return part;
	}

	public double getPartReqQty() {
		return partReqQty;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = super.verify(_msg, _full);

		if (getPars() == null) {
			_msg.append("pars null.").append(System.lineSeparator());
			v = false;
		}

		if (getPart() == null) {
			_msg.append("Part should be assigned.").append(System.lineSeparator());
			v = false;
		} else {
			if (mbomDataService.loadParsPart(getParsUid(), getPart().getUid()) != null) {
				_msg.append("Duplicate assign part.").append(System.lineSeparator());
				v = false;
			}
		}

		if (getPartReqQty() == 0) {
			_msg.append("PartReqQty should not be 0.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------build-------------------------------------
	@Override
	protected PpartInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		// 1. build ppart
		PpartInfo ppart = buildPpart(tt);
		if (ppart == null) {
			tt.travel();
			log.error("buildPpart return null.");
			return null;
		} // copy sites

		// 2. assign part
		if (!mbomDataService.parsPartAssignPart(ppart.getUid(), getPart().getUid(), getPart().getPin(),
				getPartReqQty())) {
			tt.travel();
			log.error("mbomDataService.parsPartAssignPart return false.");
			return null;
		}
		tt.addSite("revert parsPartAssignPart", () -> mbomDataService.parsPartRevertAssignPart(ppart.getUid()));
		log.info("mbomDataService.parsPartAssignPart [{}][{}]", ppart.getUid(), getPart().getUid(), getPart().getPin(),
				getPartReqQty());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return ppart.reload();
	}

	// -------------------------------------------------------------------------------

	// TODO
}
