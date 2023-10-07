package ekp.web.control.zk.pu;

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

import ekp.data.service.pu.PurchInfo;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;
import legion.web.zk.ZkUtil;

public class PuSearchResultComposer extends SelectorComposer<Component> {
	public final static String SRC = "/pu/puSearchResult.zul";

	private Logger log = LoggerFactory.getLogger(PuSearchResultComposer.class);

	public static PuSearchResultComposer of(Include _icd) {
		return ZkUtil.of(_icd, SRC, "divPuSearchResult");
	}

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxPu;

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
		ListitemRenderer<PurchInfo> puRenderer = (li, pu, i) -> {
			li.appendChild(new Listcell(pu.getPuNo()));
			li.appendChild(new Listcell(pu.getTitle()));
			li.appendChild(new Listcell(pu.getSupplierBan()));
			li.appendChild(new Listcell(pu.getSupplierName()));
			li.appendChild(new Listcell(pu.getPerfStatus().getName()));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(pu.getSumPurchItemAmt(), 2)));
		};
		lbxPu.setItemRenderer(puRenderer);
	}

	public void refreshData(List<PurchInfo> _puList) {
		ListModelList<PurchInfo> model = new ListModelList<>(_puList);
		lbxPu.setModel(model);
	}
}
