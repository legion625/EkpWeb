package ekp.web.control.zk.mbom;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;

import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mf.WorkorderInfo;
import legion.util.LogUtil;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;

public class PartCfgSearchResultComposer extends SelectorComposer<Component> {
	public final static String SRC = "/mbom/partCfgSearchResult.zul";
	private Logger log = LoggerFactory.getLogger(PartCfgSearchResultComposer.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxPartCfg;

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
		ListitemRenderer<PartCfgInfo> partCfgRenderer = (li, partCfg, i) -> {
			li.appendChild(new Listcell(partCfg.getId()));
			li.appendChild(new Listcell(partCfg.getName()));
			li.appendChild(new Listcell(partCfg.getRootPartPin()));
			li.appendChild(new Listcell(partCfg.getStatusName()));
			li.appendChild(new Listcell(partCfg.getDesp()));
		};
		lbxPartCfg.setItemRenderer(partCfgRenderer);
	}

	public void refreshData(List<PartCfgInfo> _partCfgList) {
		ListModelList<PartCfgInfo> model = new ListModelList<>(_partCfgList);
		lbxPartCfg.setModel(model);
	}
}
