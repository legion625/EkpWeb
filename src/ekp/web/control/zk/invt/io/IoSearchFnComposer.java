package ekp.web.control.zk.invt.io;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;

import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.query.InvtOrderQueryParam;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.query.SalesOrderQueryParam;
import ekp.invt.InvtService;
import ekp.sd.SdService;
import ekp.web.control.zk.common.SearchConfigType;
import ekp.web.control.zk.common.SearchFnComposer;
import ekp.web.control.zk.sd.SoSearchFnComposer;
import ekp.web.control.zk.sd.SoSearchResultComposer;
import legion.BusinessServiceFactory;
import legion.util.LogUtil;
import legion.util.query.QueryOperation;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;

public class IoSearchFnComposer extends SelectorComposer<Component> {
	public final static String SRC = "/invt/io/ioSearchFn.zul";
	private Logger log = LoggerFactory.getLogger(IoSearchFnComposer.class);
	
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
		searchFnPageComposer.init(SearchConfigType.IO,  () ->{
			QueryOperation<InvtOrderQueryParam, InvtOrderInfo> param = searchFnPageComposer.packQueryParam();
			param= BusinessServiceFactory.getInstance().getService(InvtService.class).searchInvtOrder(param, null);
			List<InvtOrderInfo> ioList = param.getQueryResult();
			fnCntProxy.refreshCntUri(IoSearchResultComposer.SRC);
			IoSearchResultComposer c = fnCntProxy.getComposer(IoSearchResultComposer.class);
			c.refreshData(ioList);
		});
	}
}
