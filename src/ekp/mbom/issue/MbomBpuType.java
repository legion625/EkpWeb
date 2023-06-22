package ekp.mbom.issue;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgConjInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mbom.PprocInfo;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;
import ekp.mbom.issue.parsPart.ParsPartBuilder0;
import ekp.mbom.issue.parsPart.ParsPartBuilder1;
import ekp.mbom.issue.parsPart.PpartBpuDel0;
import ekp.mbom.issue.parsProc.ParsProcBuilder0;
import ekp.mbom.issue.part.PartBpuDel0;
import ekp.mbom.issue.part.PartBpuPcAssignPa;
import ekp.mbom.issue.part.PartBpuUpdate;
import ekp.mbom.issue.part.PartBuilder0;
import ekp.mbom.issue.partAcq.PaBpuDel0;
import ekp.mbom.issue.partAcq.PaBpuPublish;
import ekp.mbom.issue.partAcq.PaBpuUpdateRefUnitCost;
import ekp.mbom.issue.partAcq.PartAcqBuilder0;
import ekp.mbom.issue.partAcqRoutingStep.ParsBpuDel0;
import ekp.mbom.issue.partAcqRoutingStep.ParsBuilder1;
import ekp.mbom.issue.partCfg.PartCfgBpuEditing;
import ekp.mbom.issue.partCfg.PartCfgBpuPublish;
import ekp.mbom.issue.prod.ProdBuilder0;
import ekp.mbom.issue.prod.ProdBpuEditCtl;
import ekp.mbom.issue.prodCtl.ProdCtlBpuPartCfgConj;
import ekp.mbom.issue.prodCtl.ProdCtlBuilder0;
import ekp.mbom.issue.partCfg.PartCfgBuilder0;
import ekp.mbom.type.PartAcqStatus;
import ekp.mbom.type.PartCfgStatus;
import legion.biz.BpuType;

public enum MbomBpuType implements BpuType {
	/* p */
	PART_0(PartBuilder0.class), //
	PART_$DEL0(PartBpuDel0.class, PartInfo.class), //
	PART_$PC_ASSIGN_PA(PartBpuPcAssignPa.class, PartInfo.class, PartCfgInfo.class), //
	PART_$UPDATE(PartBpuUpdate.class, PartInfo.class), //
	/* pa */
	PART_ACQ_0(PartAcqBuilder0.class), //
	PART_ACQ_$DEL0(PaBpuDel0.class, PartAcqInfo.class), //
	PART_ACQ_$PUBLISH(PaBpuPublish.class, PartAcqInfo.class), //
	PART_ACQ_$UPDATE_REF_UNIT_COST(PaBpuUpdateRefUnitCost.class, PartAcqInfo.class), //
	
	/* pars */
	PARS_1(ParsBuilder1.class, PartAcqInfo.class), //
	PARS_$DEL0(ParsBpuDel0.class, ParsInfo.class), //

	/* ppart */
	PARS_PART_0(ParsPartBuilder0.class), //
	PARS_PART_1(ParsPartBuilder1.class, ParsInfo.class), //
	PPART_$DEL0(PpartBpuDel0.class, PpartInfo.class), //
	/* pproc */
	PARS_PROC_0(ParsProcBuilder0.class), //
	
	/**/
	PART_CFG_0(PartCfgBuilder0.class), //
	PART_CFG_$EDITING(PartCfgBpuEditing.class, PartCfgInfo.class), //
	PART_CFG_$PUBLISH(PartCfgBpuPublish.class, PartCfgInfo.class), //
	/**/
	PROD_0(ProdBuilder0.class), //
	PROD_$EDIT_CTL(ProdBpuEditCtl.class, ProdInfo.class), //
	PROD_CTL_0(ProdCtlBuilder0.class), //
	PROD_CTL_$PART_CFG_CONJ(ProdCtlBpuPartCfgConj.class, ProdCtlInfo.class), //

	;

	private Class builderClass;
	private Class[] argsClasses;

	private MbomBpuType(Class builderClass, Class... argsClasses) {
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
		/* part */
		case PART_0:
			return true;
		case PART_$DEL0:
			return matchBizPartDel0((PartInfo) _args[0]);
		case PART_$PC_ASSIGN_PA:
//			return matchBizPartPcAssignPa((PartInfo) _args[0], (PartCfgInfo) _args[1]);
			return true;
		case PART_$UPDATE:
			return true;
		/* part acq */
		case PART_ACQ_0:
			return true;
		case PART_ACQ_$DEL0:
			return matchBizPaDel0((PartAcqInfo) _args[0]);
		case PART_ACQ_$PUBLISH:
			return matchBizPaPublish((PartAcqInfo) _args[0]);
		case PART_ACQ_$UPDATE_REF_UNIT_COST:
			return true;
		/* pars */
		case PARS_1:
			return matchBizPars1((PartAcqInfo) _args[0]);
		case PARS_$DEL0:
			return matchBizParsDel0((ParsInfo) _args[0]);
		/* ppart */
		case PARS_PART_0:
			return true;
		case PARS_PART_1:
			return matchBizParsPart1((ParsInfo) _args[0]);
		case PPART_$DEL0:
			return matchBizPpartDel0((PpartInfo) _args[0]);
		/* pproc */
		case PARS_PROC_0:
			return true;
		/* part cfg */
		case PART_CFG_0:
			return true;
		case PART_CFG_$EDITING:
			return matchBizPartCfgEditing((PartCfgInfo) _args[0]);
		case PART_CFG_$PUBLISH:
			return matchBizPartCfgPublish((PartCfgInfo) _args[0]);
		/* prod */
		case PROD_0:
			return true;
		case PROD_$EDIT_CTL:
			return matchBizProdEditCtl((ProdInfo) _args[0]);
		case PROD_CTL_0:
			return true;
		case PROD_CTL_$PART_CFG_CONJ:
			return matchBizProdCtlPartCfgConj((ProdCtlInfo) _args[0]);
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}

	// -------------------------------------------------------------------------------
	private Logger log = LoggerFactory.getLogger(MbomBpuType.class);

	// -------------------------------------------------------------------------------
	// -------------------------------------part--------------------------------------
	private boolean matchBizPartDel0(PartInfo _p) {
		if (_p == null) {
			log.warn("_p null.");
			return false;
		}

		List<PartAcqInfo> paList = _p.getPaList(true);
		if (!paList.isEmpty()) {
			log.info("paList should be empty.");
			return false;
		}

		List<PpartInfo> ppartList = _p.getPpartList(true);
		if (!ppartList.isEmpty()) {
			log.info("ppartList should be empty.");
			return false;
		}

		List<PartCfgInfo> partCfgList = _p.getRootPartCfgList(true);
		if (!partCfgList.isEmpty()) {
			log.info("partCfgList should be empty.");
			return false;
		}

		return true;
	}

//	private boolean matchBizPartPcAssignPa(PartInfo _p, PartCfgInfo _pc) {
//		if (_p == null) {
//			log.warn("_p null.");
//			return false;
//		}
//		if (_pc == null) {
//			log.warn("_pc null.");
//			return false;
//		}
//		
//		if(_pc.getRootPart().equals(_p)) {
//			log.info("The root part of configuration pc null.");
//			return false;
//		}
//		
//		return true;
//	}

	// -------------------------------------------------------------------------------
	// ------------------------------------partAcq------------------------------------
	// TODO

	private boolean matchBizPaDel0(PartAcqInfo _pa) {
		if (_pa == null) {
			log.warn("_pa null.");
			return false;
		}

		if (PartAcqStatus.EDITING != _pa.getStatus()) {
			log.trace("PartAcqStatus should be EDITING. [{}][{}]", _pa.getUid(), _pa.getStatus());
			return false;
		}

		List<ParsInfo> parsList = _pa.getParsList(true);
		if (!parsList.isEmpty()) {
			log.info("parsList should be empty.");
			return false;
		}

		List<PartCfgConjInfo> partCfgConjList = _pa.getPartCfgConjList(true);
		if (!partCfgConjList.isEmpty()) {
			log.info("partCfgConjList should be empty.");
			return false;
		}

		return true;
	}
	
	private boolean matchBizPaPublish(PartAcqInfo _pa) {
		if (_pa == null) {
			log.warn("_pa null.");
			return false;
		}

		if (PartAcqStatus.EDITING != _pa.getStatus()) {
			log.trace("PartAcqStatus should be EDITING. [{}][{}]", _pa.getUid(), _pa.getStatus());
			return false;
		}

		return true;
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------pars--------------------------------------
	private boolean matchBizPars1(PartAcqInfo _pa) {
		if (_pa == null) {
			log.warn("_pars null.");
			return false;
		}

		if (PartAcqStatus.EDITING != _pa.getStatus()) {
			log.trace("PartAcqStatus should be EDITING. [{}][{}]", _pa.getUid(), _pa.getStatus());
			return false;
		}

		return true;
	}
	
	private boolean matchBizParsDel0(ParsInfo _pars) {
		if (_pars == null) {
			log.warn("_pars null.");
			return false;
		}

		List<PprocInfo> pprocList = _pars.getPprocList(true);
		if (!pprocList.isEmpty()) {
			log.trace("pprocList should be empty.");
			return false;
		}

		List<PpartInfo> ppartList = _pars.getPpartList(true);
		if (!ppartList.isEmpty()) {
			log.trace("ppartList should be empty.");
			return false;
		}

		return true;
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------ppart-------------------------------------
	private boolean matchBizParsPart1(ParsInfo _pars) {
		if (_pars == null) {
			log.warn("_pars null.");
			return false;
		}

		PartAcqInfo pa = _pars.getPa();
		if (PartAcqStatus.EDITING != pa.getStatus()) {
			log.trace("The Status of Pa should be EDITING.");
			return false;
		}

		return true;
	}

	private boolean matchBizPpartDel0(PpartInfo _ppart) {
		if (_ppart == null) {
			log.warn("_ppart null.");
			return false;
		}

		// 檢查是否有相關構型，且有任何一個構型已經凍結
		boolean somePartCfgPublished = _ppart.getPars().getPa().getPartCfgConjList(true).stream()
				.map(PartCfgConjInfo::getPartCfg).anyMatch(partCfg -> PartCfgStatus.PUBLISHED == partCfg.getStatus());
		if (somePartCfgPublished) {
			log.trace("Some part configuration published.");
			return false;
		}

		return true;
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------pproc-------------------------------------
	// TODO

	// -------------------------------------------------------------------------------
	// ------------------------------------partCfg------------------------------------
	private boolean matchBizPartCfgEditing(PartCfgInfo _pc) {
		if (_pc == null) {
			log.warn("_pc null.");
			return false;
		}

		if (PartCfgStatus.EDITING != _pc.getStatus()) {
			log.trace("_pc.getStatus should be EDITING.");
			return false;
		}

		return true;
	}
	
	private boolean matchBizPartCfgPublish(PartCfgInfo _pc) {
		if (_pc == null) {
			log.warn("_pc null.");
			return false;
		}

		if (PartCfgStatus.EDITING != _pc.getStatus()) {
			log.trace("_pc.getStatus should be EDITING.");
			return false;
		}

		return true;
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------prod--------------------------------------
	private boolean matchBizProdEditCtl(ProdInfo _p) {
		if (_p == null) {
			log.warn("_p null");
			return false;
		}

		return true;
	}

	private boolean matchBizProdCtlPartCfgConj(ProdCtlInfo _prodCtl) {
		if (_prodCtl == null) {
			log.warn("_prodCtl null");
			return false;
		}

		return true;
	}

}
