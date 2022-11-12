package ekp.mbom.issue.partAcq;

import legion.biz.BizObjBuilderType;

public enum PartAcqBuilderType implements BizObjBuilderType{
	T0(PartAcqBuilder0.class), //
	;

	private Class builderClass;
	private Class[] argsClasses;
	
	private PartAcqBuilderType(Class builderClass, Class... argsClasses) {
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
		case T0: {
			return true;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}

}
