package ekp.sd;

import legion.biz.BpuType;

public enum SdBpuType implements BpuType {
	/* BizPartner */
	BP(BizPartnerBuilder.class), //
	/* SalesOrder */
	SO_1(SalesOrderBuilder1.class), //
	;

	private Class builderClass;
	private Class[] argsClasses;

	private SdBpuType(Class builderClass, Class... argsClasses) {
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
		case BP:
		case SO_1:
			return true;
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}
}
