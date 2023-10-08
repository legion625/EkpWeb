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

import ekp.data.service.mbom.PpartSkewer;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;

public class PpartSearchResultComposer extends SelectorComposer<Component> {
	public final static String SRC = "/mbom/ppartSearchResult.zul";
	private Logger log = LoggerFactory.getLogger(PpartSearchResultComposer.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxPpart;

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
		ListitemRenderer<PpartSkewer> ppartSkewerRenderer = (li, ppartSkewer, i) -> {
			li.appendChild(new Listcell(ppartSkewer.getpPin()));
			li.appendChild(new Listcell(ppartSkewer.getpName()));
			li.appendChild(new Listcell(ppartSkewer.getPaId()));
			li.appendChild(new Listcell(ppartSkewer.getPaName()));
			li.appendChild(new Listcell(ppartSkewer.getParsSeq()));
			li.appendChild(new Listcell(ppartSkewer.getParsName()));
			li.appendChild(new Listcell(ppartSkewer.getParsDesp()));
			li.appendChild(new Listcell(ppartSkewer.getPartPin()));
			li.appendChild(new Listcell(ppartSkewer.getPartName()));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(ppartSkewer.getPartReqQty(), 2)));
		};
		lbxPpart.setItemRenderer(ppartSkewerRenderer);
	}
	
	public void refreshData(List<PpartSkewer> _ppartSkewerList) {
		ListModelList<PpartSkewer> model = new ListModelList<>(_ppartSkewerList);
		lbxPpart.setModel(model);
	}

}
