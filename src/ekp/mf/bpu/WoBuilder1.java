package ekp.mf.bpu;

import java.util.ArrayList;
import java.util.List;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mf.WorkorderInfo;
import legion.util.TimeTraveler;

public class WoBuilder1 extends WoBuilder {
	/* base */
	private PartInfo p;

	/* data */
	private PartAcqInfo pa;
	private List<WomBuilder1> womBuilderList;
//	private PartAcqInfo pa; XXX

	// -------------------------------------------------------------------------------
	@Override
	protected WoBuilder1 appendBase() {
		/* base */
		p = (PartInfo) args[0];
		appendPartUid(p.getUid()).appendPartPin(p.getPin()).appendPartMmMano(p.getMmMano());

		/* data */
		// none

		return this;
	}

	// -------------------------------------------------------------------------------
	// XXX
	public WoBuilder1 appendPa(PartAcqInfo pa) {
		this.pa = pa;
		womBuilderList = new ArrayList<>();
		for (PpartInfo ppart : pa.getPpartList()) {
			WomBuilder1 womBuilder = new WomBuilder1();
			MaterialMasterInfo mm = ppart.getPart().getMm();
			womBuilder.appendMm(mm).appendQty(ppart.getPartReqQty());
			womBuilderList.add(womBuilder);
		}
		return this;
	}
	
	// -------------------------------------------------------------------------------
	public PartInfo getP() {
		return p;
	}

	public PartAcqInfo getPa() {
		return pa;
	}
	
	public List<WomBuilder1> getWomBuilderList() {
		return womBuilderList;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = true;
		if (!super.verify(_msg))
			v = false;

		for (WomBuilder1 womBuilder : getWomBuilderList()) {
			if (!womBuilder.verify(_msg)) {
				v = false;
				break;
			}
		}

		return v;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected WorkorderInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		
		//
		WorkorderInfo wo = buildWoBasic(tt);
		if (wo == null) {
			tt.travel();
			log.error("buildWoBasic return null.");
			return null;
		} // tt copied inside
		
		
		// 料表
		for (WomBuilder1 womBuilder : getWomBuilderList()) {
			womBuilder.appendWo(wo);
		}
		
		// TODO
		
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);
		return wo;
	}
	

}
