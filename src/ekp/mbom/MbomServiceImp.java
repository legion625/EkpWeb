package ekp.mbom;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartSkewer;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.data.service.mbom.query.PartQueryParam;
import ekp.data.service.mbom.query.PpartSkewerQueryParam;
import legion.DataServiceFactory;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public class MbomServiceImp implements MbomService{

	private Logger log = LoggerFactory.getLogger(MbomServiceImp.class);
	
	private static MbomDataService dataService;
	
	@Override
	public void register(Map<String, String> _params) {
		log.debug("MbomServiceImp.register");
		dataService = DataServiceFactory.getInstance().getService(MbomDataService.class);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	
	// -------------------------------------------------------------------------------
	// -------------------------------------Part--------------------------------------
	@Override
	public PartInfo loadPartByPin(String _pin) {
		return dataService.loadPartByPin(_pin);
	}
	
	@Override
	public QueryOperation<PartQueryParam, PartInfo> searchPart(QueryOperation<PartQueryParam, PartInfo> _param){
		return dataService.searchPart(_param);
	}
	
	// -------------------------------------------------------------------------------
	// ----------------------------------PpartSkewer----------------------------------
	@Override
	public QueryOperation<PpartSkewerQueryParam, PpartSkewer> searchPpartSkewer(
			QueryOperation<PpartSkewerQueryParam, PpartSkewer> _param,
			Map<PpartSkewerQueryParam, QueryValue[]> _existsQvMap){
		return dataService.searchPpartSkewer(_param, _existsQvMap);
	}
	
	// -------------------------------------------------------------------------------
	// ------------------------------------PartCfg------------------------------------
	@Override
	public PartCfgInfo loadPartCfgById(String _id) {
		return dataService.loadPartCfgById(_id);
	}
	
	@Override
	public List<PartCfgInfo> loadPartCfgList() {
		QueryOperation<PartCfgQueryParam, PartCfgInfo> param = new QueryOperation<>();
		return dataService.searchPartCfg(param).getQueryResult();
	}
	
	@Override
	public QueryOperation<PartCfgQueryParam, PartCfgInfo> searchPartCfg(QueryOperation<PartCfgQueryParam, PartCfgInfo> _param){
		return dataService.searchPartCfg(_param);
	}

}
