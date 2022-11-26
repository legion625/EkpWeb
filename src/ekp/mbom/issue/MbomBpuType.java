package ekp.mbom.issue;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mbom.PprocInfo;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;
import ekp.mbom.issue.parsPart.ParsPartBuilder0;
import ekp.mbom.issue.parsPart.ParsPartBuilder1;
import ekp.mbom.issue.parsProc.ParsProcBuilder0;
import ekp.mbom.issue.part.PartBpuDel0;
import ekp.mbom.issue.part.PartBuilder0;
import ekp.mbom.issue.partAcq.PartAcqBuilder0;
import ekp.mbom.issue.partAcqRoutingStep.ParsBpuDel0;
import ekp.mbom.issue.partAcqRoutingStep.ParsBuilder0;
import ekp.mbom.issue.partCfg.PartCfgBpuEditing;
import ekp.mbom.issue.prod.ProdBuilder0;
import ekp.mbom.issue.prod.ProdBpuEditCtl;
import ekp.mbom.issue.prodCtl.ProdCtlBpuPartCfgConj;
import ekp.mbom.issue.prodCtl.ProdCtlBuilder0;
import ekp.mbom.issue.partCfg.PartCfgBuilder0;
import ekp.mbom.type.PartCfgStatus;
import legion.biz.BpuType;

public enum MbomBpuType implements BpuType {
	/**/
	PART_0(PartBuilder0.class), //
	PART_$DEL0(PartBpuDel0.class, PartInfo.class), //
	PART_ACQ_0(PartAcqBuilder0.class), //
	/* pars */
	PARS_0(ParsBuilder0.class), //
	PARS_$DEL0(ParsBpuDel0.class, ParsInfo.class), //
	/**/
	PARS_PROC_0(ParsProcBuilder0.class), //
	PARS_PART_0(ParsPartBuilder0.class), //
	PARS_PART_1(ParsPartBuilder1.class, ParsInfo.class), //
	/**/
	PART_CFG_0(PartCfgBuilder0.class), //
	PART_CFG_$EDITING(PartCfgBpuEditing.class, PartCfgInfo.class), //
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
		/**/
		case PART_0:
			return true;
		case PART_$DEL0:
			return matchBizPartDel0((PartInfo) _args[0]);
		case PART_ACQ_0:
			return true;
		/**/
		case PARS_0:
			return true;
		case PARS_$DEL0:
			return matchBizParsDel0((ParsInfo) _args[0]);
		/**/
		case PARS_PROC_0:
		case PARS_PART_0:
			return true;
		case PARS_PART_1:
			return matchBizParsPart1((ParsInfo) _args[0]);
		/**/
		case PART_CFG_0:
			return true;
		case PART_CFG_$EDITING:
			return matchBizPartCfgEditing((PartCfgInfo) _args[0]);
		/**/
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

		List<PartCfgInfo> partCfgList = _p.getPartCfgList(true);
		if (!partCfgList.isEmpty()) {
			log.info("partCfgList should be empty.");
			return false;
		}

		return true;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------partAcq------------------------------------
	// TODO

	// -------------------------------------------------------------------------------
	// -------------------------------------pars--------------------------------------
	private boolean	matchBizParsDel0(ParsInfo _pars) {
		if (_pars == null) {
			log.warn("_pars null.");
			return false;
		}

		List<PprocInfo> pprocList = _pars.getPprocList(true);
		if (!pprocList.isEmpty()) {
			log.info("pprocList should be empty.");
			return false;
		}

		List<PpartInfo> ppartList = _pars.getPpartList(true);
		if (!ppartList.isEmpty()) {
			log.info("ppartList should be empty.");
			return false;
		}

		return true;
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------pproc-------------------------------------
	// TODO

	// -------------------------------------------------------------------------------
	// -------------------------------------ppart-------------------------------------

	private boolean matchBizParsPart1(ParsInfo _pars) {
		if (_pars == null) {
			log.warn("_pars null.");
			return false;
		}

		return true;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------partCfg------------------------------------
	private boolean matchBizPartCfgEditing(PartCfgInfo _pc) {
		if (_pc == null) {
			log.warn("_pc null.");
			return false;
		}

		if (PartCfgStatus.EDITING != _pc.getStatus()) {
			log.info("_pc.getStatus should be EDITING.");
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
