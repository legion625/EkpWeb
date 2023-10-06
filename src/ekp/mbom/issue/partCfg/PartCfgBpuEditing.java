package ekp.mbom.issue.partCfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgConjInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.mbom.type.PartCfgStatus;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public class PartCfgBpuEditing extends PartCfgBpu{

	/* base */
	private PartCfgInfo partCfg;

	/* data */
	private Map<String, PartAcqInfo> partAcqMap;
	private Set<PartAcqInfo> unassigningPartAcqSet;

	// -------------------------------------------------------------------------------
	@Override
	protected PartCfgBpuEditing appendBase() {
		/* base */
		partCfg = (PartCfgInfo) args[0];
		appendPartCfg(partCfg);

		/* data */
		partAcqMap = new HashMap<>();
		unassigningPartAcqSet = new HashSet<>();

		return this;
	}
	
	// -------------------------------------------------------------------------------
	public PartCfgBpuEditing appendPartAcq(PartAcqInfo _partAcq) {
		if (_partAcq != null)
			partAcqMap.put(_partAcq.getUid(), _partAcq);
		else
			log.warn("_partAcq null.");
		return this;
	}

	public PartCfgBpuEditing removePartAcq(PartAcqInfo _partAcq) {
		if (_partAcq != null)
			partAcqMap.remove(_partAcq.getUid());
		else
			log.warn("_partAcq null.");
		return this;
	}
	
	public PartCfgBpuEditing appendUnassignedPartAcq(PartAcqInfo _partAcq) {
		unassigningPartAcqSet.add(_partAcq);
		return this;
	}
	public  PartCfgBpuEditing removeUnassignedPartAcq(PartAcqInfo _partAcq) {
		unassigningPartAcqSet.remove(_partAcq);
		return this;
	}
	
	// -------------------------------------------------------------------------------
	private Map<String, PartAcqInfo> getPartAcqMap() {
		return partAcqMap;
	}

	public List<PartAcqInfo> getPartAcqList() {
		return new ArrayList<>(getPartAcqMap().values());
	}
	
	public Set<PartAcqInfo> getUnassigningPartAcqSet() {
		return unassigningPartAcqSet;
	}
	
	// -------------------------------------------------------------------------------
	public List<PartCfgConjInfo> packDeletingPccList() {
		return getUnassigningPartAcqSet().stream().map(pa -> pa.getPartCfgConj(getPartCfg().getUid(), true))
				.collect(Collectors.toList());
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		boolean v = true;

		/* PartAcq */
		List<PartAcqInfo> partAcqList = getPartAcqList();
		if (partAcqList == null || partAcqList.size() <= 0) {
			// none
		} else {
			// 檢查不同的PartAcq對應到相同的Part
			boolean b = false;
			for (int i = 0; i < partAcqList.size() - 1; i++) {
				for (int j = i + 1; j < partAcqList.size(); j++) {
					PartAcqInfo pai = partAcqList.get(i);
					PartAcqInfo paj = partAcqList.get(j);
					if (pai.getPartUid().equals(paj.getPartUid())) {
						_msg.append("Duplicated partAcqs in the same part.").append(System.lineSeparator());
						v = false;
						b = true;
						break;
					}
				}
				if (b)
					break;
			}
		}
		

		return v;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;

		/* PartCfg */
		if (getPartCfg() == null) {
			_msg.append("PartCfg null.").append(System.lineSeparator());
			v = false;
		} else {
			if (PartCfgStatus.EDITING != getPartCfg().getStatus()) {
				_msg.append("PartCfg status error.").append(System.lineSeparator());
				v = false;
			}
		}

		/* PartAcq */
		List<PartAcqInfo> partAcqList = getPartAcqList();
		Set<PartAcqInfo> unassigningPartAcqSet = getUnassigningPartAcqSet();
		if (partAcqList.isEmpty() && unassigningPartAcqSet.isEmpty()) {
			_msg.append("partAcqList should not be empty.").append(System.lineSeparator());
			v = false;
		}else {
			//
			for (PartAcqInfo pa : partAcqList) {
				if (mbomDataService.loadPartCfgConj(getPartCfg().getUid(), pa.getUid()) != null) {
					_msg.append("Some conjunctions exist.").append(System.lineSeparator());
					v = false;
					break;
				}
			}
			
			//
			// 檢查PartAcq對應的Pcc是否存在。
			for (PartAcqInfo pa : unassigningPartAcqSet) {
				PartCfgConjInfo pcc = pa.getPartCfgConj(getPartCfg().getUid(), true);
				if (pcc == null) {
					_msg.append("The PartCfgConj of PartAcq [" + pa.getId() + "] does NOT exist.")
							.append(System.lineSeparator());
					v = false;
				}
			}
		}
		
		
//		
//		/* Unassigning part acq set */
//		
//		if (unassigningPartAcqSet == null || unassigningPartAcqSet.size() <= 0) {
//			// none
//		} else {
//			
//		}
		
		return v;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		// createPartCfgConj
		List<PartAcqInfo> partAcqList = getPartAcqList();
		for (PartAcqInfo pa : partAcqList) {
			PartCfgConjInfo pcc = mbomDataService.createPartCfgConj(getPartCfg().getUid(), pa.getUid());
			if (pcc == null) {
				tt.travel();
				log.error("mbomDataSerivce.createPartCfgConj return null.");
				return false;
			}
			tt.addSite("revert createPartCfgConj", () -> mbomDataService.deletePartCfgConj(pcc.getUid()));
			log.info("mbomDataService.createPartCfgConj [{}][{}][{}]", pcc.getUid(), pcc.getPartCfgUid(),
					pcc.getPartAcqUid());
		}

		// deletePartCfgConj
		for(PartCfgConjInfo pcc: packDeletingPccList()) {
			if(!mbomDataService.deletePartCfgConj(pcc.getUid())) {
				tt.travel();
				log.error("mbomDataSerivce.deletePartCfgConj return null.");
				return false;
			}
			tt.addSite("revert deletePartCfgConj",
					() -> mbomDataService.createPartCfgConj(pcc.getPartCfgUid(), pcc.getPartAcqUid()) != null);
			log.info("mbomDataService.deletePartCfgConj [{}][{}][{}]", pcc.getUid(), pcc.getPartCfgUid(),
					pcc.getPartAcqUid());
		}
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return true;
	}

}
