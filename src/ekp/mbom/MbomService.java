package ekp.mbom;

import java.util.List;

import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.query.PartQueryParam;
import legion.BusinessService;
import legion.util.query.QueryOperation;

public interface MbomService extends BusinessService{
	
	// -------------------------------------------------------------------------------
	// -------------------------------------Part--------------------------------------
	public PartInfo loadPartByPin(String _pin);
	
	default List<PartInfo> loadPartList() {
		QueryOperation<PartQueryParam, PartInfo> param = new QueryOperation<>();
		return searchPart(param).getQueryResult();
	}

	public QueryOperation<PartQueryParam, PartInfo> searchPart(QueryOperation<PartQueryParam, PartInfo> _param);
}
