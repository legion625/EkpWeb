package ekp.mf.bpu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.service.mbom.PartInfo;
import ekp.mbom.type.PartAcqStatus;
import ekp.mbom.type.PartAcquisitionType;
import legion.biz.BpuType;

public enum MfBpuType implements BpuType {
	/* Workorder */
	WO_1(WoBuilder1.class, PartInfo.class), //
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
			return matchBizWo1((PartInfo) _args[0]);
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}

	// -------------------------------------------------------------------------------
	private Logger log = LoggerFactory.getLogger(MfBpuType.class);
	
	// -------------------------------------------------------------------------------
	// -----------------------------------Workorder-----------------------------------
	private boolean matchBizWo1(PartInfo _p) {
		if (_p == null) {
			log.warn("_p null.");
			return false;
		} else {
			// 已指定料件基本檔
			if (!_p.isMmAssigned()) {
				log.debug("_part should have assigned mm. [{}][{}]", _p.getUid(), _p.getPin());
				return false;
			}
			
			// 必須要有已經發佈的PartAcq
			if (!_p.getPaList(PartAcquisitionType.SELF_PRODUCING).stream()
					.anyMatch(pa -> PartAcqStatus.PUBLISHED == pa.getStatus())) {
				log.debug("There must exist some SELF_PRODUCING and PUBLISHED PartAcqs. [{}][{}]", _p.getUid(),
						_p.getPin());
				return false;
			}
			
		}
		
		return true;
	}

}