package ekp.mbom.issue.prodCtl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
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
	private Map<String, PartCfgInfo> partCfgMap;

	// -------------------------------------------------------------------------------
	@Override
	protected ProdCtlBpuPartCfgConj appendBase() {
		/* base */
		prodCtl = (ProdCtlInfo) args[0];
		appendProdCtl(prodCtl);
		
		/* data */
		partCfgMap = new HashMap<>();
		
		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public ProdCtlBpuPartCfgConj appendPartCfg(PartCfgInfo _partCfg) {
		if(_partCfg!=null)
			partCfgMap.put(_partCfg.getUid(), _partCfg);
		else
			log.warn("_partCfg null.");
		return this;
	}

	public ProdCtlBpuPartCfgConj removePartCfg(PartCfgInfo _partCfg) {
		if (_partCfg != null)
			partCfgMap.remove(_partCfg.getUid());
		else
			log.warn("_partAcq null.");
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	private Map<String, PartCfgInfo> getPartCfgMap() {
		return partCfgMap;
	}
	
	public List<PartCfgInfo> getPartCfgList(){
		return new ArrayList<>(getPartCfgMap().values());
	}

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
		List<PartCfgInfo> partCfgList = getPartCfgList();
		if (partCfgList == null || partCfgList.size() <= 0) {
			_msg.append("partCfgList should not be empty.").append(System.lineSeparator());
			v = false;
		} else {
			for (PartCfgInfo partCfg : partCfgList) {
				if (mbomDataService.loadProdCtlPartCfgConj(getProdCtl().getUid(), partCfg.getUid()) != null) {
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
		List<PartCfgInfo> partCfgList = getPartCfgList();
		for (PartCfgInfo partCfg : partCfgList) {
			ProdCtlPartCfgConjInfo pcpcc = mbomDataService.createProdCtlPartCfgConj(getProdCtl().getUid(),
					partCfg.getUid());
			if (pcpcc == null) {
				tt.travel();
				log.error("mbomDataSerivce.createProdCtlPartCfgConj return null.");
				return false;
			}
			tt.addSite("revert createProdCtlPartCfgConj",
					() -> mbomDataService.deleteProdCtlPartCfgConj(pcpcc.getUid()));
			log.info("mbomDataService.createProdCtlPartCfgConj [{}][{}][{}]", pcpcc.getUid(), pcpcc.getProdCtlUid(),
					pcpcc.getPartCfgUid());
		}
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return true;
	}

}
