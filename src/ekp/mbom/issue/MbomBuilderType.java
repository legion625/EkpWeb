package ekp.mbom.issue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.service.mbom.PartAcqRoutingStepInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;
import ekp.mbom.issue.parsPart.ParsPartBuilder0;
import ekp.mbom.issue.parsPart.ParsPartBuilder1;
import ekp.mbom.issue.parsProc.ParsProcBuilder0;
import ekp.mbom.issue.part.PartBuilder0;
import ekp.mbom.issue.partAcq.PartAcqBuilder0;
import ekp.mbom.issue.partAcqRoutingStep.PartAcqRoutingStepBuilder0;
import ekp.mbom.issue.partCfg.PartCfgBpuEditing;
import ekp.mbom.issue.prod.ProdBuilder0;
import ekp.mbom.issue.prod.ProdBpuEditCtl;
import ekp.mbom.issue.prodCtl.ProdCtlBpuPartCfgConj;
import ekp.mbom.issue.prodCtl.ProdCtlBuilder;
import ekp.mbom.issue.prodCtl.ProdCtlBuilder0;
import ekp.mbom.issue.partCfg.PartCfgBuilder0;
import ekp.mbom.type.PartCfgStatus;
import legion.biz.BizObjBuilderType;

public enum MbomBuilderType implements BizObjBuilderType {
	/**/
	PART_0(PartBuilder0.class), //
	PART_ACQ_0(PartAcqBuilder0.class), //
	PART_ACQ_ROUTING_STEP_0(PartAcqRoutingStepBuilder0.class), //
	PARS_PROC_0(ParsProcBuilder0.class), //
	PARS_PART_0(ParsPartBuilder0.class), //
	PARS_PART_1(ParsPartBuilder1.class, PartAcqRoutingStepInfo.class), //
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

	private MbomBuilderType(Class builderClass, Class... argsClasses) {
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
		case PART_ACQ_0:
		case PART_ACQ_ROUTING_STEP_0:
		case PARS_PROC_0:
		case PARS_PART_0:
			return true;
		case PARS_PART_1:
			return matchBizParsPart1((PartAcqRoutingStepInfo) _args[0]);
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
	private Logger log = LoggerFactory.getLogger(MbomBuilderType.class);

	private boolean matchBizParsPart1(PartAcqRoutingStepInfo _pars) {
		if (_pars == null) {
			log.warn("_pars null.");
			return false;
		}

		return true;
	}
	
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
