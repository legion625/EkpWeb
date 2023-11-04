package ekp.web.control.zk.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.RowRenderer;

import ekp.web.control.zk.pu.PuSearchFnComposer;
import legion.util.LogUtil;
import legion.util.query.QueryOperation;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;
import legion.web.zk.ZkUtil;

public class SearchFnComposer extends SelectorComposer<Component> {
	private final static String SRC = "/common/searchFn.zul";
	private Logger log = LoggerFactory.getLogger(SearchFnComposer.class);

	// -------------------------------------------------------------------------------
	public static SearchFnComposer of(Include _icd) {
		return ZkUtil.of(_icd, SRC, "divSearchFnMain");
	}
	
	// -------------------------------------------------------------------------------
	@Wire
	private Button btnSearch;
	
	@Wire
	private Grid gridNormalSearch;
	
	private SearchConfig searchConfig;
	
	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	// -------------------------------------------------------------------------------
	public void init(SearchConfigType _searchConfigType, Runnable _runSearch) {
		searchConfig = _searchConfigType.getSearchConfigInstance();

		//
		RowRenderer<NormalSearchLine> renderer = (r, nsl, i) -> {
			r.appendChild(nsl.getLbName());
			r.appendChild(nsl.getInputElem());
		};
		gridNormalSearch.setRowRenderer(renderer);

		ListModelList<NormalSearchLine> model = new ListModelList<>(searchConfig.getNormalSearchLines());
		gridNormalSearch.setModel(model);

		//
		btnSearch.addEventListener(Events.ON_CLICK, evt -> _runSearch.run());
	}
	
	public QueryOperation packQueryParam() {
		return searchConfig.packQueryParam();
	}
	
	@Listen(Events.ON_CLICK + "=#btnClear")
	public void btnClear_clicked() {
		for (NormalSearchLine nsl : searchConfig.getNormalSearchLines()) {
			nsl.getInputElem().setRawValue(null);
		}
	}
	
	
}
