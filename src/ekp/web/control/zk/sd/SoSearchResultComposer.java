package ekp.web.control.zk.sd;

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

import ekp.data.service.sd.SalesOrderInfo;
import legion.util.DateFormatUtil;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;

public class SoSearchResultComposer extends SelectorComposer<Component> {
	public final static String SRC = "/sd/soSearchResult.zul";
	private Logger log = LoggerFactory.getLogger(SoSearchResultComposer.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxSo;

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
		ListitemRenderer<SalesOrderInfo> soRenderer = (li, so, i) -> {
			li.appendChild(new Listcell(so.getSosn()));
			li.appendChild(new Listcell(so.getTitle()));
			li.appendChild(new Listcell(so.getCustomerName()));
			li.appendChild(new Listcell(so.getCustomerBan()));
			li.appendChild(new Listcell(so.getSalerName()));
			li.appendChild(new Listcell(DateFormatUtil.transToDate(so.getSaleDate())));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(so.getSumSoiAmt(), 2)));
		};
		lbxSo.setItemRenderer(soRenderer);
	}

	public void refreshData(List<SalesOrderInfo> _soList) {
		ListModelList<SalesOrderInfo> model = new ListModelList<>(_soList);
		lbxSo.setModel(model);
	}
}
