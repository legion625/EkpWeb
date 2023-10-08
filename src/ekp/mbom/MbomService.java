package ekp.mbom;

import java.util.List;
import java.util.Map;

import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartSkewer;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.data.service.mbom.query.PartQueryParam;
import ekp.data.service.mbom.query.PpartSkewerQueryParam;
import legion.BusinessService;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public interface MbomService extends BusinessService{
	
	// -------------------------------------------------------------------------------
	// -------------------------------------Part--------------------------------------
	public PartInfo loadPartByPin(String _pin);
	
	default List<PartInfo> loadPartList() {
		QueryOperation<PartQueryParam, PartInfo> param = new QueryOperation<>();
		return searchPart(param).getQueryResult();
	}

	public QueryOperation<PartQueryParam, PartInfo> searchPart(QueryOperation<PartQueryParam, PartInfo> _param);
	
	// -------------------------------------------------------------------------------
	// ----------------------------------PpartSkewer----------------------------------
	public QueryOperation<PpartSkewerQueryParam, PpartSkewer> searchPpartSkewer(
			QueryOperation<PpartSkewerQueryParam, PpartSkewer> _param,
			Map<PpartSkewerQueryParam, QueryValue[]> _existsQvMap);
	
	// -------------------------------------------------------------------------------
	// ------------------------------------PartCfg------------------------------------
	public PartCfgInfo loadPartCfgById(String _id);
	
	public List<PartCfgInfo> loadPartCfgList();
	
	public QueryOperation<PartCfgQueryParam, PartCfgInfo> searchPartCfg(QueryOperation<PartCfgQueryParam, PartCfgInfo> _param);
}
