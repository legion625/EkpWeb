package ekp.mf.bpu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.mbom.type.PartAcqStatus;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mf.type.WorkorderStatus;
import legion.biz.BpuType;

public enum MfBpuType implements BpuType {
	/* Workorder */
	WO_1(WoBuilder1.class, PartAcqInfo.class, PartCfgInfo.class), //
	WO_$START(WoBpuStart.class, WorkorderInfo.class), //
	WO_$FINISH_WORK(WoBpuFinishWork.class, WorkorderInfo.class), //
	
	;

	private Class builderClass;
	private Class[] argsClasses;

	private MfBpuType(Class builderClass, Class... argsClasses) {
		this.builderClass = builderClass;
		this.argsClasses = argsClasses;
	}

	@Override
	public Class getBuilderClass() {
		return builderClass;
	}

	@Override
	public Class[] getArgsClasses() {
		return argsClasses;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean matchBiz(Object... _args) {
		switch (this) {
		case WO_1:
			return matchBizWo1((PartAcqInfo) _args[0], (PartCfgInfo) _args[1]);
		case WO_$START:
			return matchBizWoStart((WorkorderInfo)_args[0]);
		case WO_$FINISH_WORK:
			return matchBizWoFinishWork((WorkorderInfo)_args[0]);
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}

	// -------------------------------------------------------------------------------
	private Logger log = LoggerFactory.getLogger(MfBpuType.class);
	
	// -------------------------------------------------------------------------------
	// -----------------------------------Workorder-----------------------------------
//	private boolean matchBizWo1(PartInfo _p) {
	private boolean matchBizWo1(PartAcqInfo _pa, PartCfgInfo _pc) {
		if (_pa == null) {
			log.warn("_pa null.");
			return false;
		} else {
			// 已指定料件基本檔
			if (!_pa.isMmAssigned()) {
				log.debug("PartAcq should have assigned mm. [{}][{}][{}]", _pa.getUid(), _pa.getPartPin(), _pa.getId());
				return false;
			}
			
			if (PartAcqStatus.PUBLISHED != _pa.getStatus()) {
				log.debug("PartAcqStatus should be PUBLISHED. [{}][{}][{}][{}]", _pa.getUid(), _pa.getPartPin(), _pa.getId(), _pa.getStatus());
				return false;
			}
			
			// 必須要有已經發佈的PartAcq
//			if (!_p.getPaList(PartAcquisitionType.SELF_PRODUCING).stream()
//					.anyMatch(pa -> PartAcqStatus.PUBLISHED == pa.getStatus())) {
//				log.debug("There must exist some SELF_PRODUCING and PUBLISHED PartAcqs. [{}][{}]", _p.getUid(),
//						_p.getPin());
//				return false;
//			}
			
		}
		
		if (_pc == null) {
			log.warn("_pc null.");
			return false;
		} else {
				
		}
		
		
		return true;
	}

	private boolean matchBizWoStart(WorkorderInfo _wo) {
		if (_wo == null) {
			log.warn("_wo null.");
			return false;
		} 
		
		if(WorkorderStatus.TO_START != _wo.getStatus()) {
			log.debug("WorkorderStatus should be TO_START. [{}][{}][{}]", _wo.getUid(), _wo.getWoNo(), _wo.getStatusName());
			return false;
		}

		return true;
	}

	private boolean matchBizWoFinishWork(WorkorderInfo _wo) {
		if (_wo == null) {
			log.warn("_wo null.");
			return false;
		}

		if (WorkorderStatus.WORKING != _wo.getStatus()) {
			log.debug("WorkorderStatus should be FINISH_WORK. [{}][{}][{}]", _wo.getUid(), _wo.getWoNo(),
					_wo.getStatusName());
			return false;
		}

		return true;
	}
	
}
