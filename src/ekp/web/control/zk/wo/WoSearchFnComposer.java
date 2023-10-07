package ekp.web.control.zk.wo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;

import ekp.DebugLogMark;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.query.WorkorderQueryParam;
import ekp.mf.MfService;
import ekp.web.control.zk.common.SearchConfigType;
import ekp.web.control.zk.common.SearchFnComposer;
import legion.BusinessServiceFactory;
import legion.util.LogUtil;
import legion.util.query.QueryCondition;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryGroup;
import legion.util.query.QueryOperation.QueryValue;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;

public class WoSearchFnComposer extends SelectorComposer<Component> {
	public final static String SRC = "/mf/woSearchFn.zul";

	private Logger log = LoggerFactory.getLogger(WoSearchFnComposer.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Include icdSearchFnPage;
	private SearchFnComposer searchFnPageComposer;

	// -------------------------------------------------------------------------------
	private FnCntProxy fnCntProxy;

	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			fnCntProxy = FnCntProxy.register(this);
			log.debug("fnCntProxy: {}", fnCntProxy);
			init();
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	// -------------------------------------------------------------------------------
	private void init() {
		searchFnPageComposer = SearchFnComposer.of(icdSearchFnPage);
		searchFnPageComposer.init(SearchConfigType.WO, () -> {
			QueryOperation<WorkorderQueryParam, WorkorderInfo> param = searchFnPageComposer.packQueryParam();
//			log.debug("param.getConditions().size(): {}", param.getConditions().size());
//			for(QueryCondition qc: param.getConditions()) {
//				
//				log.debug("qc: {}",qc);
//				if(qc instanceof QueryGroup) {
//					QueryGroup qg = (QueryGroup) qc;
//					log.debug("qg.getConjunctiveOp(): {}", qg.getConjunctiveOp());
//					for(QueryCondition _qv: qg.getValues()) {
//						if(_qv instanceof QueryValue) {
//							QueryValue qv =(QueryValue) _qv;
//							log.debug("qv: {}\t{}\t{}", qv.getCondition(), qv.getCompareOp(), qv.getValue());
//						}
//						
//					}
//				};
//			}
			param = BusinessServiceFactory.getInstance().getService(MfService.class).searchWorkorder(param, null);
			List<WorkorderInfo> woList = param.getQueryResult();
			log.debug("woList.size(): {}", woList.size());
			fnCntProxy.refreshCntUri(WoSearchResultComposer.SRC);
			WoSearchResultComposer c = fnCntProxy.getComposer(WoSearchResultComposer.class);
			
			c.refreshData(woList);
		});
	}

}
