package ekp.mbom.issue.partCfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ekp.data.service.mbom.PartAcquisitionInfo;
import ekp.data.service.mbom.PartCfgConjInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.mbom.type.PartCfgStatus;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public class PartCfgBpuEditing extends PartCfgBpu{

	/* base */
	private PartCfgInfo partCfg;

	/* data */
	private Map<String, PartAcquisitionInfo> partAcqMap;

	// -------------------------------------------------------------------------------
	@Override
	protected PartCfgBpuEditing appendBase() {
		/* base */
		partCfg = (PartCfgInfo) args[0];
		appendPartCfg(partCfg);

		/* data */
		partAcqMap = new HashMap<>();

		return this;
	}
	
	// -------------------------------------------------------------------------------
	public PartCfgBpuEditing appendPartAcq(PartAcquisitionInfo _partAcq) {
		if (_partAcq != null)
			partAcqMap.put(_partAcq.getUid(), _partAcq);
		else
			log.warn("_partAcq null.");
		return this;
	}

	public PartCfgBpuEditing removePartAcq(PartAcquisitionInfo _partAcq) {
		if (_partAcq != null)
			partAcqMap.remove(_partAcq.getUid());
		else
			log.warn("_partAcq null.");
		return this;
	}
	
	// -------------------------------------------------------------------------------
	private Map<String, PartAcquisitionInfo> getPartAcqMap() {
		return partAcqMap;
	}

	public List<PartAcquisitionInfo> getPartAcqList() {
		return new ArrayList<>(getPartAcqMap().values());
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		boolean v = true;

		/* PartAcq */
		List<PartAcquisitionInfo> partAcqList = getPartAcqList();
		if (partAcqList == null || getPartAcqList().size() <= 0) {
			// none
		} else {
			// 檢查不同的PartAcq對應到相同的Part
			boolean b = false;
			for (int i = 0; i < partAcqList.size() - 1; i++) {
				for (int j = i + 1; i < partAcqList.size(); j++) {
					PartAcquisitionInfo pai = partAcqList.get(i);
					PartAcquisitionInfo paj = partAcqList.get(j);
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
	public boolean verify(StringBuilder _msg) {
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
		List<PartAcquisitionInfo> partAcqList = getPartAcqList();
		if (partAcqList == null || partAcqList.size() <= 0) {
			_msg.append("partAcqList should not be empty.").append(System.lineSeparator());
			v = false;
		} else {
//			for (PartAcquisitionInfo pa : partAcqList) {
//				if (mbomDataService.loadPartCfgConj(getPartCfg().getUid(), pa.getUid()) != null) { // FIXME
//					_msg.append("Some conjunctions exist.").append(System.lineSeparator());
//					v = false;
//					break;
//				}
//			}
		}
		
		return v;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		// createPartCfgConj
		List<PartAcquisitionInfo> partAcqList = getPartAcqList();
		for (PartAcquisitionInfo pa : partAcqList) {
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

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return true;
	}

}
