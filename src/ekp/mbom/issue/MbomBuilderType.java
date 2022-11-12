package ekp.mbom.issue;

import ekp.mbom.issue.parsPart.ParsPartBuilder0;
import ekp.mbom.issue.parsProc.ParsProcBuilder0;
import ekp.mbom.issue.part.PartBuilder0;
import ekp.mbom.issue.partAcq.PartAcqBuilder0;
import ekp.mbom.issue.partAcqRoutingStep.PartAcqRoutingStepBuilder0;
import legion.biz.BizObjBuilderType;

public enum MbomBuilderType implements BizObjBuilderType {
	PART_0(PartBuilder0.class), //
	PART_ACQ_0(PartAcqBuilder0.class), //
	PART_ACQ_ROUTING_STEP_0(PartAcqRoutingStepBuilder0.class), //
	PARS_PROC_0(ParsProcBuilder0.class), //
	PARS_PART_0(ParsPartBuilder0.class), //
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

	@Override
	public boolean matchBiz(Object... _args) {
		switch (this) {
		case PART_0:
		case PART_ACQ_0:
		case PART_ACQ_ROUTING_STEP_0:
		case PARS_PROC_0:
		case PARS_PART_0:
			return true;
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}
}
