package ekp.web.control.zk.filter.biz;

import legion.util.filter.FilterFunction;
import legion.util.filter.FilterParam;
import legion.util.filter.handler.EnumEqualFilter;
import legion.util.filter.handler.StringFilter;
import legion.util.filter.FilterOperation.FilterCompareOp;

public enum PartFp implements FilterParam<Object> {
	PIN("pin","PIN", StringFilter.getInstance()), //
	NAME("name","NAME", StringFilter.getInstance()), //
	UNIT("unit","UNIT", EnumEqualFilter.getInstance()), //
	;
	private String id;
	private String desp;
	private FilterFunction ff;

	// -------------------------------------------------------------------------------

	private PartFp(String id, String desp, FilterFunction ff) {
		this.id = id;
		this.desp = desp;
		this.ff = ff;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getDescription() {
		return desp;
	}

	@Override
	public boolean filter(Object _targetVal, FilterCompareOp _filterCompareOp, Object _filterVal) throws Exception {
		return (boolean) ff.apply(_targetVal, _filterCompareOp, _filterVal);
	}

}
