package ekp.web.control.zk.sd;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;

import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.query.SalesOrderQueryParam;
import ekp.sd.SdService;
import ekp.web.control.zk.common.SearchConfigType;
import ekp.web.control.zk.common.SearchFnComposer;
import legion.BusinessServiceFactory;
import legion.util.LogUtil;
import legion.util.query.QueryOperation;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;

public class SoSearchFnComposer extends SelectorComposer<Component> {
	public final static String SRC = "/sd/soSearchFn.zul";

	private Logger log = LoggerFactory.getLogger(SoSearchFnComposer.class);

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
		searchFnPageComposer.init(SearchConfigType.SO,  () ->{
			QueryOperation<SalesOrderQueryParam, SalesOrderInfo> param = searchFnPageComposer.packQueryParam();
			param= BusinessServiceFactory.getInstance().getService(SdService.class).searchSalesOrder(param, null);
			List<SalesOrderInfo> soList = param.getQueryResult();
			fnCntProxy.refreshCntUri(SoSearchResultComposer.SRC);
			SoSearchResultComposer c = fnCntProxy.getComposer(SoSearchResultComposer.class);
			c.refreshData(soList);
		});
	}
}
