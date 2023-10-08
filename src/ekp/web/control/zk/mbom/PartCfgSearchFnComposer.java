package ekp.web.control.zk.mbom;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;

import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.mbom.MbomService;
import ekp.web.control.zk.common.SearchConfigType;
import ekp.web.control.zk.common.SearchFnComposer;
import legion.BusinessServiceFactory;
import legion.util.LogUtil;
import legion.util.query.QueryOperation;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;

public class PartCfgSearchFnComposer extends SelectorComposer<Component> {
	public final static String SRC = "/mbom/partCfgSearchFn.zul";
	private Logger log = LoggerFactory.getLogger(PartCfgSearchFnComposer.class);

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
		searchFnPageComposer.init(SearchConfigType.PART_CFG, () -> {
			QueryOperation<PartCfgQueryParam, PartCfgInfo> param = searchFnPageComposer.packQueryParam();
			param = BusinessServiceFactory.getInstance().getService(MbomService.class).searchPartCfg(param);
			List<PartCfgInfo> partCfgList = param.getQueryResult();
			fnCntProxy.refreshCntUri(PartCfgSearchResultComposer.SRC);
			PartCfgSearchResultComposer c = fnCntProxy.getComposer(PartCfgSearchResultComposer.class);
			c.refreshData(partCfgList);
		});
	}

}
