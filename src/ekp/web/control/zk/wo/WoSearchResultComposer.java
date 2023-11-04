package ekp.web.control.zk.wo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;

import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.pu.PurchInfo;
import legion.util.DateFormatUtil;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;
import legion.web.zk.ZkUtil;

public class WoSearchResultComposer extends SelectorComposer<Component> {
	public final static String SRC = "/mf/woSearchResult.zul";
	private Logger log = LoggerFactory.getLogger(WoSearchResultComposer.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxWo;

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

	private void init() {
		ListitemRenderer<WorkorderInfo> woRenderer = (li, wo, i) -> {
			li.appendChild(new Listcell(wo.getWoNo()));
			li.appendChild(new Listcell(wo.getStatusName()));
			li.appendChild(new Listcell(wo.getPartPin()));
			li.appendChild(new Listcell(wo.getPartAcqId()));
			li.appendChild(new Listcell(wo.getPartAcqMmMano()));
			li.appendChild(new Listcell(wo.getPartCfgId()));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(wo.getRqQty(), 2)));
			li.appendChild(new Listcell(DateFormatUtil.transToDate(wo.getStartWorkTime())));
			li.appendChild(new Listcell(DateFormatUtil.transToDate(wo.getFinishWorkTime())));
			li.appendChild(new Listcell(DateFormatUtil.transToDate(wo.getOverTime())));
		};
		lbxWo.setItemRenderer(woRenderer);
	}

	public void refreshData(List<WorkorderInfo> _woList) {
		ListModelList<WorkorderInfo> model = new ListModelList<>(_woList);
		lbxWo.setModel(model);
	}

}
