package ekp.mbom;

import legion.biz.BizObjBuilderType;

public enum PartBuilderType implements BizObjBuilderType {
	TYPE0(PartBuilder0.class), //
	;

	private Class builderClass;
	private Class[] argsClasses;

	private PartBuilderType(Class builderClass, Class... argsClasses) {
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
		case TYPE0: {
			return true;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}

}
