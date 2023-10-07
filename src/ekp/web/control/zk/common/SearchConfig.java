package ekp.web.control.zk.common;

import java.util.ArrayList;
import java.util.List;

import legion.util.query.QueryCondition;
import legion.util.query.QueryOperation;
import legion.util.query.QueryParam;
import legion.util.query.QueryOperation.ConjunctiveOp;
import legion.util.query.QueryOperation.QueryValue;

public abstract class SearchConfig<P extends QueryParam, T> {

	private NormalSearchLine<P, T>[] normalSearchLines;
	
	protected SearchConfig() {
		normalSearchLines = initNormalSearchLines();
	}
	
	protected abstract NormalSearchLine<P, T>[] initNormalSearchLines();

	// -------------------------------------------------------------------------------
	public NormalSearchLine<P, T>[] getNormalSearchLines() {
		return normalSearchLines;
	}
	
	// -------------------------------------------------------------------------------
	public QueryOperation<P, T> packQueryParam() {
		List<QueryCondition<P>> qcList = new ArrayList<>();
		for (NormalSearchLine<P, T> nsl : normalSearchLines) {
			QueryValue<P, T> qv = nsl.packQueryValue();
			if (qv != null)
				qcList.add(qv);
		}

		QueryOperation<P, T> qop = new QueryOperation<>();

		if (qcList != null && qcList.size() > 0) {
			QueryCondition[] qcs = qcList.toArray(new QueryCondition[0]);
			qop.appendCondition(QueryOperation.group(ConjunctiveOp.and, qcs));
		}
		return qop;
	}

	
}
