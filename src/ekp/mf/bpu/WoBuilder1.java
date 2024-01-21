package ekp.mf.bpu;

import java.util.ArrayList;
import java.util.List;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.WorkorderMaterialInfo;
import legion.util.TimeTraveler;

public class WoBuilder1 extends WoBuilder {
	/* base */
	private PartAcqInfo pa;
	private PartCfgInfo pc;

	/* data */
	private List<WomBuilder1> womBuilderList;

	// -------------------------------------------------------------------------------
	@Override
	protected WoBuilder1 appendBase() {
		/* base */
		pa = (PartAcqInfo) args[0];
		pc = (PartCfgInfo) args[1];
		PartInfo p = pa.getPart(false);
		appendPartUid(p.getUid()).appendPartPin(p.getPin());
		appendPartAcqUid(pa.getUid()).appendPartAcqId(pa.getId()).appendPartAcqMmMano(pa.getMmMano());
		appendPartCfgUid(pc.getUid()).appendPartCfgId(pc.getId());
		

		/* data */
		womBuilderList = new ArrayList<>();
		for (PpartInfo ppart : pa.getPpartList()) {
			WomBuilder1 womBuilder = new WomBuilder1();
//			MaterialMasterInfo mm = ppart.getPart().getMm();
			PartAcqInfo womPa = ppart.getPart().getPa(pc.getUid());
			womBuilder.appendMm(womPa.getMm()).appendMultiplier(ppart.getPartReqQty());
//			.appendQty(rqQty * ppart.getPartReqQty());
			womBuilderList.add(womBuilder);
		}

		return this;
	}

	// -------------------------------------------------------------------------------
	// XXX
//	public WoBuilder1 appendPa(PartAcqInfo pa, double rqQty) {
	public WoBuilder1 appendRqQty( double rqQty) {
//		this.pa = pa;
//		appendPartAcqUid(pa.getUid()).appendPartAcqId(pa.getId()).appendRqQty(rqQty);
		super.appendRqQty(rqQty);

		for(WomBuilder1 womBuilder: womBuilderList) {
//			womBuilder	.appendQty(rqQty * ppart.getPartReqQty());
			womBuilder.appendWoRqQty(rqQty);
		}
		
//		womBuilderList = new ArrayList<>();
//		for (PpartInfo ppart : pa.getPpartList()) {
//			WomBuilder1 womBuilder = new WomBuilder1();
//			MaterialMasterInfo mm = ppart.getPart().getMm();
//			womBuilder.appendMm(mm).appendQty(rqQty * ppart.getPartReqQty());
//			womBuilderList.add(womBuilder);
//		}
		return this;
	}
	

//	// -------------------------------------------------------------------------------
//	public PartInfo getP() {
//		return p;
//	}

	public PartAcqInfo getPa() {
		return pa;
	}
	
	public List<WomBuilder1> getWomBuilderList() {
		return womBuilderList;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;
		if (!super.verify(_msg, _full))
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
			WorkorderMaterialInfo wom = womBuilder.build(new StringBuilder(), tt);
			if (wom == null) {
				tt.travel();
				log.error("womBuilder.build return null.");
				return null;
			} // tt copied inside
		}

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);
		return wo;
	}
	

}
