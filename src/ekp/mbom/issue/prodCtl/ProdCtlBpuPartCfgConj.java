package ekp.mbom.issue.prodCtl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdCtlPartCfgConjInfo;
import ekp.data.service.mbom.ProdInfo;
import ekp.mbom.issue.prod.ProdBpuEditCtl;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public class ProdCtlBpuPartCfgConj extends ProdCtlBpu {
	/* base */
	private ProdCtlInfo prodCtl;

	/* data */
	private Map<String, Object[]> partAcqCfgMap; // key: partAcqUid, value, PartAcqInfo, PartCfgInfo

	// -------------------------------------------------------------------------------
	@Override
	protected ProdCtlBpuPartCfgConj appendBase() {
		/* base */
		prodCtl = (ProdCtlInfo) args[0];
		appendProdCtl(prodCtl);
		
		/* data */
		partAcqCfgMap = new HashMap<>();
		
		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public ProdCtlBpuPartCfgConj appendPartAcqCfg(PartAcqInfo _partAcq, PartCfgInfo _partCfg) {
		if (_partCfg == null || _partAcq == null) {
			log.warn("_partCfg null or _partAcq null.");
		} else {
			partAcqCfgMap.put(_partAcq.getUid()+"@!@"+_partCfg.getUid(), new Object[] { _partAcq, _partCfg });
		}
		return this;
	}

	public ProdCtlBpuPartCfgConj removePartAcqCfg(PartAcqInfo _partAcq) {
		if (_partAcq != null)
			partAcqCfgMap.remove(_partAcq.getUid());
		else
			log.warn("_partAcq null.");
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	private Map<String, Object[]> getPartAcqCfgMap() {
		return partAcqCfgMap;
	}

//	public List<PartCfgInfo> getPartCfgList(){
//		return new ArrayList<>(getPartCfgMap().values());
//	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;
		
		/* ProdCtl */
		if(getProdCtl()==null) {
			_msg.append("ProdCtl null.").append(System.lineSeparator());
			v = false;
		}
		
		/* PartCfg */
		Map<String, Object[]> map = getPartAcqCfgMap();
		if (map == null || map.size() <= 0) {
			_msg.append("partAcqCfgMap should not be empty.").append(System.lineSeparator());
			v = false;
		} else {
			for (String key : map.keySet()) {
				Object[] value = map.get(key);
				PartAcqInfo pa = (PartAcqInfo) value[0];
				PartCfgInfo pc = (PartCfgInfo) value[1];
				if (mbomDataService.loadProdCtlPartCfgConj(getProdCtl().getUid(), pc.getUid(), pa.getUid()) != null) {
					_msg.append("Some conjunctions exist.").append(System.lineSeparator());
					v = false;
				}
			}

		}
		
		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		
		// createProdCtlPartCfgConj
//		List<PartCfgInfo> partCfgList = getPartCfgList();
		Map<String, Object[]> map = getPartAcqCfgMap();
//		for (PartCfgInfo partCfg : partCfgList) {
		for (String key : map.keySet()) {
			Object[] value = map.get(key);
			PartAcqInfo pa = (PartAcqInfo) value[0];
			PartCfgInfo pc = (PartCfgInfo) value[1];
			ProdCtlPartCfgConjInfo pcpcc = mbomDataService.createProdCtlPartCfgConj(getProdCtl().getUid(),
					pc.getUid(), pa.getUid());
			if (pcpcc == null) {
				tt.travel();
				log.error("mbomDataSerivce.createProdCtlPartCfgConj return null.");
				return false;
			}
			tt.addSite("revert createProdCtlPartCfgConj",
					() -> mbomDataService.deleteProdCtlPartCfgConj(pcpcc.getUid()));
			log.info("mbomDataService.createProdCtlPartCfgConj [{}][{}][{}][{}]", pcpcc.getUid(), pcpcc.getProdCtlUid(),
					pcpcc.getPartCfgUid(), pcpcc.getPartAcqUid());
		}
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return true;
	}

}
