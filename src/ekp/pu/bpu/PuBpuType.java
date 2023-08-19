package ekp.pu.bpu;

import legion.biz.BpuType;

public enum PuBpuType implements BpuType {
	/* Purch */
	P_0(PurchBuilder1.class), //
	;

	private Class builderClass;
	private Class[] argsClasses;
	
	private PuBpuType(Class builderClass, Class... argsClasses) {
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
		case P_0:
			return true;
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}

}
