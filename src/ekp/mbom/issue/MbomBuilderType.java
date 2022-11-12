package ekp.mbom.issue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.service.mbom.PartCfgInfo;
import ekp.mbom.issue.parsPart.ParsPartBuilder0;
import ekp.mbom.issue.parsProc.ParsProcBuilder0;
import ekp.mbom.issue.part.PartBuilder0;
import ekp.mbom.issue.partAcq.PartAcqBuilder0;
import ekp.mbom.issue.partAcqRoutingStep.PartAcqRoutingStepBuilder0;
import ekp.mbom.issue.partCfg.PartCfgEditingBpu;
import ekp.mbom.issue.prod.ProdBuilder0;
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
	/**/
	PART_CFG_0(PartCfgBuilder0.class), //
	PART_CFG_EDITING(PartCfgEditingBpu.class, PartCfgInfo.class), //
	/**/
	PROD_0(ProdBuilder0.class), //
	PROD_CTL_0(ProdCtlBuilder0.class), //

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
		/**/
		case PART_CFG_0:
			return true;
		case PART_CFG_EDITING:
			return matchBizPartCfgAssignPartAcq((PartCfgInfo) _args[0]);
		/**/
		case PROD_0:
		case PROD_CTL_0:
			return true;
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}

	// -------------------------------------------------------------------------------
	private Logger log = LoggerFactory.getLogger(MbomBuilderType.class);

	private boolean matchBizPartCfgAssignPartAcq(PartCfgInfo _pc) {
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
}
