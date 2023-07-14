package ekp.invt.bpu;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.invt.WrhsLocInfo;
import ekp.invt.bpu.wrhsLoc.WrhsLocBpuDel0;
import ekp.invt.bpu.wrhsLoc.WrhsLocBuilder0;
import ekp.mbom.issue.MbomBpuType;
import legion.biz.BpuType;

public enum InvtBpuType implements BpuType {
	/* wrhsLoc */
	WL_0(WrhsLocBuilder0.class), //
	WL_$DEL0(WrhsLocBpuDel0.class, WrhsLocInfo.class), //

	;

	private Class builderClass;
	private Class[] argsClasses;

	private InvtBpuType(Class builderClass, Class... argsClasses) {
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
		case WL_0:
			return true;
		case WL_$DEL0:
			return matchBizWlDel0((WrhsLocInfo)_args[0]);
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}
	
	// -------------------------------------------------------------------------------
	private Logger log = LoggerFactory.getLogger(InvtBpuType.class);

	// -------------------------------------------------------------------------------
	private boolean matchBizWlDel0(WrhsLocInfo _wl) {
		if(_wl==null) {
			log.warn("_wl null.");
			return false;
		}
		
//		List<WrhsBinInfo> wbList = _wl.getWrhsBinList();
//		if(!wbList.isEmpty()) {
//			log.trace("wbList should be empty.");
//			return false;
//		}
		
		return true;
	}
}
