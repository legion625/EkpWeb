package ekp.mbom.issue.prod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;
import legion.util.TimeTraveler;

public class ProdBpuEditCtl extends ProdBpu {
	/* base */
	private ProdInfo prod;

	/* data */
	private Map<String, ProdCtlInfo> prodCtlMap;
	private Map<String, String> prodCtlParentUidMap;

	// -------------------------------------------------------------------------------
	@Override
	protected ProdBpuEditCtl appendBase() {
		/* base */
		prod = (ProdInfo) args[0];
		super.appendProd(prod);

		/* data */
		prodCtlMap = new HashMap<>();
		prodCtlParentUidMap = new HashMap<>();

		return this;
	}

	// -------------------------------------------------------------------------------
	public ProdBpuEditCtl appendProdCtl(ProdCtlInfo _prodCtl) {
		prodCtlMap.putIfAbsent(_prodCtl.getUid(), _prodCtl);
		return this;
	}

	public ProdBpuEditCtl appendProdCtl(ProdCtlInfo _prodCtl, ProdCtlInfo _parentProdCtl) {
		appendProdCtl(_prodCtl).appendProdCtl(_parentProdCtl);
		prodCtlParentUidMap.put(_prodCtl.getUid(), _parentProdCtl.getUid());
		return this;
	}

	public ProdBpuEditCtl removeProdCtl(ProdCtlInfo _prodCtl) {
		String _prodCtlUid = _prodCtl.getUid();
		prodCtlMap.remove(_prodCtlUid);
		// check parentMap
		List<String> removeKeyList = new ArrayList<>();
		for (Entry<String, String> e : prodCtlParentUidMap.entrySet())
			if (_prodCtlUid.equals(e.getKey()) || _prodCtlUid.equals(e.getValue()))
				removeKeyList.add(e.getKey());
		for (String removeKey : removeKeyList)
			prodCtlParentUidMap.remove(removeKey);
		return this;
	}

	// -------------------------------------------------------------------------------
	private Map<String, ProdCtlInfo> getProdCtlMap() {
		return prodCtlMap;
	}

	public List<ProdCtlInfo> getProdCtlList() {
		return new ArrayList<>(getProdCtlMap().values());
	}

	public Map<String, String> getProdCtlParentUidMap() {
		return prodCtlParentUidMap;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;

		if (getProd() == null) {
			_msg.append("Prod null.").append(System.lineSeparator());
			v = false;
		}

		// check the relation between child and parent
		for (Entry<String, String> e : prodCtlParentUidMap.entrySet()) {
			ProdCtlInfo pcChild = getProdCtlMap().get(e.getKey());
			ProdCtlInfo pcParent = getProdCtlMap().get(e.getValue());
			//
			if (pcChild == null) {
				_msg.append("ProdCtl null. [" + e.getKey() + "]").append(System.lineSeparator());
				v = false;
			}
			if (pcParent == null) {
				_msg.append("ProdCtl null. [" + e.getValue() + "]").append(System.lineSeparator());
				v = false;
			}

			//
//			if (!(pcChild.getLv() > pcParent.getLv())) {
			if (!(pcChild.getLv() == pcParent.getLv() + 1)) {
				_msg.append("The lv of child [" + pcChild.getPartPin() + "," + pcChild.getLv()
						+ "] should be greater than the lv of parent [" + pcParent.getPartPin() + "," + pcParent.getLv()
						+ "].").append(System.lineSeparator());
				v = false;
			}
		}

		return v;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		// assign prod
		for (String prodCtlUid : getProdCtlMap().keySet()) {
			ProdCtlInfo prodCtl = getProdCtlMap().get(prodCtlUid);
			String origProdUid = prodCtl.getProdUid();
			if (!mbomDataService.prodCtlAssignProd(prodCtlUid, getProd().getUid())) {
				tt.travel();
				log.error("mbomDataSerivce.prodCtlAssignProd return false. [{}][{}]", prodCtlUid, getProd().getUid());
				return false;
			}
			tt.addSite("revert prodCtlAssignProd", () -> mbomDataService.prodCtlAssignProd(prodCtlUid, origProdUid));
			log.info("mbomDataService.prodCtlAssignProd [{}][{}]", prodCtlUid, getProd().getUid());
		}

		// assign parent
		for (Entry<String, String> e : prodCtlParentUidMap.entrySet()) {
			ProdCtlInfo pcChild = getProdCtlMap().get(e.getKey());
			String origParentUid = pcChild.getParentUid();
			ProdCtlInfo pcParent = getProdCtlMap().get(e.getValue());

			if (!mbomDataService.prodCtlAssignParent(pcChild.getUid(), pcParent.getUid(), "")) {
				tt.travel();
				log.error("mbomDataSerivce.prodCtlAssignParent return false. [{}][{}][{}]", pcChild.getUid(),
						pcParent.getUid(), pcParent.getPartUid());
				return false;
			}
			tt.addSite("revert prodCtlAssignParent",
					() -> mbomDataService.prodCtlAssignParent(pcChild.getUid(), origParentUid, ""));
			log.info("mbomDataService.prodCtlAssignParent [{}][{}][{}]", pcChild.getUid(), pcParent.getUid(),
					pcParent.getPartUid());
		}

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return true;
	}
}
