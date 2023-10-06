package ekp.mbom.issue.partAcq;

import ekp.data.service.mbom.PartAcqInfo;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public class PaBpuUpdateRefUnitCost extends PaBpu{
	/* base */
	private PartAcqInfo pa;

	/* data */
	private double refUnitCost;

	// -------------------------------------------------------------------------------
	@Override
	protected PaBpuUpdateRefUnitCost appendBase() {
		/* base */
		pa = (PartAcqInfo) args[0];
		appendPa(pa);

		/* data */

		return this;
	}
	
	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public PaBpuUpdateRefUnitCost appendRefUnitCost(double refUnitCost) {
		this.refUnitCost = refUnitCost;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public double getRefUnitCost() {
		return refUnitCost;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;
		if (getPa() == null) {
			v = false;
			_msg.append("Part acquisition null.").append(System.lineSeparator());
		}
		
		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		
		//
		double origRefUnitCost = getPa().getRefUnitCost();
		if (!mbomDataService.partAcqUpdateRefUnitCost(getPa().getUid(), getRefUnitCost())) {
			tt.travel();
			log.error("mbomDataService.partAcqUpdateRefUnitCost return false. [{}][{}]", getPa().getUid(),
					getRefUnitCost());
			return false;
		}
		tt.addSite("rvert partAcqUpdateRefUnitCost",
				() -> mbomDataService.partAcqUpdateRefUnitCost(getPa().getUid(), origRefUnitCost));
		log.info("mbomDataService.partAcqUpdateRefUnitCost [{}][{}]", getPa().getUid(), getRefUnitCost());
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);
		
		return true;
	}

}
