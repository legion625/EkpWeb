package ekp.invt.bpu;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.data.MbomDataService;
import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.MbsbStmtInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.invt.WrhsLocInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgConjInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.invt.bpu.invtOrder.InvtOrderBuilder0;
import ekp.invt.bpu.invtOrder.InvtOrderBuilder11;
import ekp.invt.bpu.invtOrder.InvtOrderBuilder12;
import ekp.invt.bpu.invtOrder.InvtOrderBuilder22;
import ekp.invt.bpu.invtOrder.InvtOrderBuilder29;
import ekp.invt.bpu.invtOrder.InvtOrderBuilder4OutsourcingPurch;
import ekp.invt.bpu.invtOrder.InvtOrderItemBuilder11;
import ekp.invt.bpu.invtOrder.IoBpuApprove;
import ekp.invt.bpu.material.MaterialInstBpuDel0;
import ekp.invt.bpu.material.MaterialInstBuilder;
import ekp.invt.bpu.material.MaterialInstBuilder0;
import ekp.invt.bpu.material.MaterialMasterBpuDel0;
import ekp.invt.bpu.material.MaterialMasterBuilder0;
import ekp.invt.bpu.material.MbsbStmtBuilderByPurchItem;
import ekp.invt.bpu.wrhsLoc.WrhsBinBpuDel0;
import ekp.invt.bpu.wrhsLoc.WrhsBinBuilder1;
import ekp.invt.bpu.wrhsLoc.WrhsLocBpuDel0;
import ekp.invt.bpu.wrhsLoc.WrhsLocBuilder0;
import ekp.invt.type.InvtOrderStatus;
import ekp.invt.type.PostingStatus;
import ekp.mbom.issue.MbomBpuType;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mf.type.WorkorderStatus;
import ekp.pu.type.PurchPerfStatus;
import legion.DataServiceFactory;
import legion.biz.BpuType;

public enum InvtBpuType implements BpuType {
	/* wrhsLoc */
	WL_0(WrhsLocBuilder0.class), //
	WL_$DEL0(WrhsLocBpuDel0.class, WrhsLocInfo.class), //
	/* wrhsBin */
	WB_1(WrhsBinBuilder1.class, WrhsLocInfo.class), //
	WB_$DEL0(WrhsBinBpuDel0.class, WrhsBinInfo.class), //
	/* InvtOrder */
//	IO_0(InvtOrderBuilder0.class), //
	IO_11(InvtOrderBuilder11.class,PurchInfo.class), // [採購入庫] io, ioi (mi,mbsbStmt ), io->TO_APV, pu->Perfed
	IO_12(InvtOrderBuilder12.class, WorkorderInfo.class), // [工件入庫] io, ioi (mi,mbsbStmt), io -> TO_APV
	IO_21(InvtOrderBuilder4OutsourcingPurch.class, PurchInfo.class, PartAcqInfo.class, PartCfgInfo.class), //
	IO_22(InvtOrderBuilder22.class,WorkorderInfo.class), // [工令領料] io, ioi (mbsbStmt ), io->TO_APV
	IO_29(InvtOrderBuilder29.class, SalesOrderInfo.class), // [成品出庫](整個訂單一起出庫) O9(29, "成品出庫", ""),io, ioi (mbsbStmt ), io->TO_APV, soi->finishDelivered //
	IO_$APPROVE(IoBpuApprove.class, InvtOrderInfo.class ), //
	/* MaterialMaster */
	MM_0(MaterialMasterBuilder0.class), //
	MM_$DEL0(MaterialMasterBpuDel0.class, MaterialMasterInfo.class), //
	/* MaterialInst */
	MI_0(MaterialInstBuilder0.class), //
	MI_$DEL0(MaterialInstBpuDel0.class,MaterialInstInfo.class ), //
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
			return true;
		case WB_1:
		case WB_$DEL0:
			return true;
		
		case IO_11:
			return matchBizIo11((PurchInfo) _args[0]);
		case IO_12:
			return matchBizIo12((WorkorderInfo)_args[0]);
		case IO_21:
			return matchBizIo21((PurchInfo) _args[0], (PartAcqInfo)_args[1], (PartCfgInfo)_args[2]);
		case IO_22:
			return matchBizIo22((WorkorderInfo)_args[0]);
		case IO_29:
			return matchBizIo29((SalesOrderInfo)_args[0]);
		case IO_$APPROVE:
			return matchBizIoApprove((InvtOrderInfo)_args[0]);
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
	private Logger log = LoggerFactory.getLogger(InvtBpuType.class);

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
	
	
	private boolean matchBizIo12(WorkorderInfo _wo) {
		if (_wo == null) {
			log.warn("_wo null.");
			return false;
		}

		if (WorkorderStatus.FINISH_WORK != _wo.getStatus()) {
			log.debug("WorkorderStatus should be [{}], but is [{}]", WorkorderStatus.FINISH_WORK, _wo.getStatus());
			return false;
		}

		return true;
	}
	
	private boolean matchBizIo21(PurchInfo _purch, PartAcqInfo _partAcq, PartCfgInfo _partCfg) {
		if (_purch == null) {
			log.warn("_purch null.");
			return false;
		}
		if (_partAcq == null) {
			log.warn("_partAcq null.");
			return false;
		}
		if (_partCfg == null) {
			log.warn("_partCfg null.");
			return false;
		}

		if (PartAcquisitionType.OUTSOURCING != _partAcq.getType()) {
			log.trace("_partAcq.getType() should be OUTSOURCING but is [{}].", _partAcq.getType());
			return false;
		}

		PartCfgConjInfo pcc = _partAcq.getPartCfgConj(_partCfg.getUid(), false);
		if (pcc == null) {
			log.warn("No PartCfgConjInfo between _partAcq[{}] and _partCfg[{}].", _partAcq.getUid(), _partCfg.getUid());
			return false;
		}

		if(!_partAcq.getPpartList().stream().allMatch(ppart->ppart.getPart().getPa(_partCfg)!=null)) {
			log.warn("There is some ppart of partAcq[{}] such that no childPartAcq of partCfg[{}] .", _partAcq.getUid(), _partCfg.getUid());
			return false;
		}
		
		List<PartAcqInfo> _childPartAcqList = _partAcq.getChildrenList(_partCfg);
		if (_childPartAcqList == null || _childPartAcqList.size() <= 0) {
			log.warn("_childPartAcqList size error.");
			return false;
		}

		return true;
	}
	
	private boolean matchBizIo22(WorkorderInfo _wo) {
		if (_wo == null) {
			log.warn("_wo null.");
			return false;
		}

		// 目前是一次把wo的所有wom領完，所以檢查所有qty0都要>0，且所有qty1<=0
		if (!_wo.getWomList().stream().allMatch(wom -> wom.getQty0() > 0 && wom.getQty1() <= 0)) {
			log.warn("_wom error.");
			return false;
		}

		return true;
	}

	private boolean matchBizIo29(SalesOrderInfo _so) {
		if (_so == null) {
			log.warn("_so null.");
			return false;
		}

		// 目前是一次把SalesOrder所有的項目都出庫，所以全部都不能有領出的紀錄。
		if (!_so.getSalesOrderItemList().stream()
				.allMatch(soi -> !soi.isAllDelivered() && soi.getIoiList().size() <= 0)) {
			log.warn("soi error.");
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
		
		List<MbsbStmtInfo> mbsbStmtList = _io.getMbsbStmtList();
		if (mbsbStmtList.size() <= 0) {
			log.debug("mbsbStmtList.size()<=0 [{}][{}]", _io.getUid(), _io.getIosn());
			return false;
		} else {
			if (!mbsbStmtList.stream().allMatch(s -> PostingStatus.TO_POST == s.getPostingStatus())) {
				log.debug("MbsbStmt's status should be [{}].", PostingStatus.TO_POST);
				return false;
			}
		}
		
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
