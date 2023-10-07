package ekp.web.control.zk.pu;

import java.awt.CardLayout;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;

import ekp.DebugLogMark;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.query.PurchQueryParam;
import ekp.pu.PuService;
import ekp.web.control.zk.common.SearchConfigType;
import ekp.web.control.zk.common.SearchFnComposer;
import legion.BusinessServiceFactory;
import legion.util.LogUtil;
import legion.util.query.QueryCondition;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryGroup;
import legion.util.query.QueryOperation.QueryValue;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;

public class PuSearchFnComposer extends SelectorComposer<Component> {
	public final static String SRC = "/pu/puSearchFn.zul";
	
	private Logger log = LoggerFactory.getLogger(PuSearchFnComposer.class);
	
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
		searchFnPageComposer =SearchFnComposer.of(icdSearchFnPage); 
		searchFnPageComposer.init(SearchConfigType.PU, () -> {
			QueryOperation<PurchQueryParam, PurchInfo> param = searchFnPageComposer.packQueryParam();
			log.debug("param.getConditions().size(): {}", param.getConditions().size());
			for(QueryCondition qc: param.getConditions()) {
				
				log.debug("qc: {}",qc);
				if(qc instanceof QueryGroup) {
					QueryGroup qg = (QueryGroup) qc;
					log.debug("qg.getConjunctiveOp(): {}", qg.getConjunctiveOp());
					for(QueryCondition _qv: qg.getValues()) {
						if(_qv instanceof QueryValue) {
							QueryValue qv =(QueryValue) _qv;
							log.debug("qv: {}\t{}\t{}", qv.getCondition(), qv.getCompareOp(), qv.getValue());
						}
						
					}
				};
			}
			
			param = BusinessServiceFactory.getInstance().getService(PuService.class).searchPurch(param, null);
			List<PurchInfo> puList = param.getQueryResult();

			fnCntProxy.refreshCntUri(PuSearchResultComposer.SRC);
			PuSearchResultComposer c = fnCntProxy.getComposer(PuSearchResultComposer.class);
			c.refreshData(puList);
		});
	}

}
