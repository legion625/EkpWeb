package ekp.mbom.issue.partCfg;

import static legion.util.query.QueryOperation.CompareOp.equal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PpartSkewer;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.data.service.mbom.query.PpartSkewerQueryParam;
import ekp.mbom.type.PartAcqStatus;
import ekp.mbom.type.PartCfgStatus;
import legion.biz.Bpu;
import legion.util.TimeTraveler;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public class PartCfgBpuPublish extends PartCfgBpu{
	/* base */
	private PartCfgInfo partCfg;
	

	/* data */
	private long publishTime;
	
	private List<PartAcqInfo> paList;
	
	
	
	
	// -------------------------------------------------------------------------------
	@Override
	protected PartCfgBpuPublish appendBase() {
		/* base */
		partCfg = (PartCfgInfo) args[0];
		appendPartCfg(partCfg);

		/* data */
		publishTime = System.currentTimeMillis(); // default now
		paList = partCfg.getPccList(true).stream().map(pcc -> pcc.getPartAcq()).collect(Collectors.toList());

		return this;
	}
	
	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public PartCfgBpuPublish appendPublishTime(long publishTime) {
		this.publishTime = publishTime;
		return this;
	}
	
	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public long getPublishTime() {
		return publishTime;
	}
		
	
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = true;
		if(getPartCfg()==null) {
			v = false;
			_msg.append("Part configuration null.").append(System.lineSeparator());
		}
		
		if(PartCfgStatus.EDITING != getPartCfg().getStatus()) {
			v = false;
			_msg.append("Part configuration status error.").append(System.lineSeparator());
		}
		
		if (getPublishTime() <= 0) {
			v = false;
			_msg.append("publishTime error.").append(System.lineSeparator());
		}
		
		/* check no orphan */
		QueryOperation<PpartSkewerQueryParam, PpartSkewer> p = new QueryOperation<>();
		Map<PpartSkewerQueryParam, QueryValue[]> existsQvMap = new HashMap<>();
		p.appendCondition(QueryOperation.value(PpartSkewerQueryParam.B_OF_PC$_PA_EXISTS, equal, true));
		existsQvMap.put(PpartSkewerQueryParam.B_OF_PC$_PA_EXISTS,
				new QueryValue[] { QueryOperation.value(PartCfgQueryParam.ID, equal,getPartCfg().getId()) });
		p.appendCondition(QueryOperation.value(PpartSkewerQueryParam.B_OF_PC_ROOT_PART, equal, false)); // 排除root
		p.appendCondition(QueryOperation.value(PpartSkewerQueryParam.B_OF_PC$_PARENT_PART_EXISTS, equal, false)); // 沒上階(孤兒)
		existsQvMap.put(PpartSkewerQueryParam.B_OF_PC$_PARENT_PART_EXISTS,
				new QueryValue[] { QueryOperation.value(PartCfgQueryParam.ID, equal,getPartCfg().getId()) });
		p  =	mbomDataService.searchPpartSkewer(p, existsQvMap);
		if (p.getTotal() > 0) {
			v = false;
			for (PpartSkewer ppartSkewer : p.getQueryResult()) {
				_msg.append("Orphan ppart: [" + ppartSkewer.getpPin() + "][" + ppartSkewer.getPaId() + "]["
						+ ppartSkewer.getPartPin() + "]").append(System.lineSeparator());
			}
		}
		
		/* check all paList */
		for (PartAcqInfo pa : paList) {
			if (PartAcqStatus.PUBLISHED != pa.getStatus()) {
				v = false;
				_msg.append("PartAcqStatus should be PUBLISHED, but [" + pa.getPartPin() + "][" + pa.getId() + "] is ["
						+ pa.getStatusName() + "].").append(System.lineSeparator());
			}
		}
		
		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		
		// 
		if(!mbomDataService.partCfgPublish(getPartCfg().getUid(), getPublishTime())) {
			tt.travel();
			log.error("mbomDataService.partCfgPublish return false. [{}][{}]", getPartCfg().getUid(), getPublishTime());
			return false;
		}
		tt.addSite("revert partCfgPublish",  () -> mbomDataService.partCfgRevertPublish(getPartCfg().getUid()));
		log.info("mbomDataService.partCfgPublish [{}][{}]", getPartCfg().getUid(), getPublishTime());
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return true;
	}


}
