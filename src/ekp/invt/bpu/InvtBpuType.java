package ekp.invt.bpu;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.invt.WrhsLocInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.invt.bpu.invtOrder.InvtOrderBuilder11;
import ekp.invt.bpu.invtOrder.InvtOrderItemBuilder11;
import ekp.invt.bpu.invtOrder.IoBpuApprove;
import ekp.invt.bpu.material.MaterialInstBpuDel0;
import ekp.invt.bpu.material.MaterialInstBuilder;
import ekp.invt.bpu.material.MaterialInstBuilder0;
import ekp.invt.bpu.material.MaterialMasterBpuDel0;
import ekp.invt.bpu.material.MaterialMasterBuilder0;
import ekp.invt.bpu.material.MbsbStmtBuilderByIoi;
import ekp.invt.bpu.wrhsLoc.WrhsBinBpuDel0;
import ekp.invt.bpu.wrhsLoc.WrhsBinBuilder1;
import ekp.invt.bpu.wrhsLoc.WrhsLocBpuDel0;
import ekp.invt.bpu.wrhsLoc.WrhsLocBuilder0;
import ekp.invt.type.InvtOrderStatus;
import ekp.mbom.issue.MbomBpuType;
import ekp.pu.type.PurchPerfStatus;
import legion.biz.BpuType;

public enum InvtBpuType implements BpuType {
	/* wrhsLoc */
	WL_0(WrhsLocBuilder0.class), //
	WL_$DEL0(WrhsLocBpuDel0.class, WrhsLocInfo.class), //
	/* wrhsBin */
	WB_1(WrhsBinBuilder1.class, WrhsLocInfo.class), //
	WB_$DEL0(WrhsBinBpuDel0.class, WrhsBinInfo.class), //
	/* InvtOrder */
	IO_11(InvtOrderBuilder11.class,PurchInfo.class), //
	IO_$APPROVE(IoBpuApprove.class, InvtOrderInfo.class ), //
	/* InvtOrderItem */
//	IOI_11(InvtOrderItemBuilder11.class, PurchItemInfo.class), //
	/* MaterialMaster */
	MM_0(MaterialMasterBuilder0.class), //
	MM_$DEL0(MaterialMasterBpuDel0.class, MaterialMasterInfo.class), //
	/* MaterialInst */
	MI_0(MaterialInstBuilder0.class), //
	MI_$DEL0(MaterialInstBpuDel0.class,MaterialInstInfo.class ), //
	
	/* MbsbStmt */
	MBSB_STMT_BY_IOI(MbsbStmtBuilderByIoi.class, InvtOrderItemInfo.class), //

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
//			return matchBizWlDel0((WrhsLocInfo)_args[0]);
			return true;
		case WB_1:
		case WB_$DEL0:
			return true;
		case IO_11:
			return matchBizIo11((PurchInfo) _args[0]);
		case IO_$APPROVE:
			return matchBizIoApprove((InvtOrderInfo)_args[0]);
//			return true;
		case MM_0:
		case MM_$DEL0:
			return true;
		case MI_0:
		case MI_$DEL0:
			return true;
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}
	
	// -------------------------------------------------------------------------------
//	private Logger log = LoggerFactory.getLogger(InvtBpuType.class);
	private Logger log = LoggerFactory.getLogger(DebugLogMark.class);

	// -------------------------------------------------------------------------------
	private boolean matchBizIo11(PurchInfo _p) {
		if (_p == null) {
			log.warn("_p null.");
			return false;
		}
		
		if (PurchPerfStatus.TO_PERF != _p.getPerfStatus()) {
			log.debug("PurchPerfStatus should be [{}], but is [{}]", PurchPerfStatus.TO_PERF, _p.getPerfStatus());
			return false;
		}
		
		return true;
	}

	private boolean matchBizIoApprove(InvtOrderInfo _io) {
		if (_io == null) {
			log.warn("_io null.");
			return false;
		}

		//
		if (InvtOrderStatus.TO_APV != _io.getStatus()) {
			log.debug("InvtOrderStatus should be [{}], but is [{}]", InvtOrderStatus.TO_APV, _io.getStatus());
			return false;
		}

		//

		return true;
	}
	
//	private boolean matchBizWlDel0(WrhsLocInfo _wl) {
//		if(_wl==null) {
//			log.warn("_wl null.");
//			return false;
//		}
//		
////		List<WrhsBinInfo> wbList = _wl.getWrhsBinList();
////		if(!wbList.isEmpty()) {
////			log.trace("wbList should be empty.");
////			return false;
////		}
//		
//		return true;
//	}
}
