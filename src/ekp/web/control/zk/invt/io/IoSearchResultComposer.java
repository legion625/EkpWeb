package ekp.web.control.zk.invt.io;

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

import ekp.data.service.invt.InvtOrderInfo;
import legion.util.DateFormatUtil;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;

public class IoSearchResultComposer extends SelectorComposer<Component> {
	public final static String SRC = "/invt/io/ioSearchResult.zul";
	private Logger log = LoggerFactory.getLogger(IoSearchResultComposer.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxIo;

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
		ListitemRenderer<InvtOrderInfo> soRenderer = (li, io, i) -> {
			li.appendChild(new Listcell(io.getIosn()));
			li.appendChild(new Listcell(io.getStatusName()));
			li.appendChild(new Listcell(io.getApplierName()));
			li.appendChild(new Listcell(DateFormatUtil.transToDate(io.getApplyTime())));
			li.appendChild(new Listcell(DateFormatUtil.transToDate(io.getApvTime())));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(io.getSumIoiOrderValue(), 2)));
			li.appendChild(new Listcell(io.getRemark()));
		};
		lbxIo.setItemRenderer(soRenderer);
	}

	public void refreshData(List<InvtOrderInfo> _ioList) {
		ListModelList<InvtOrderInfo> model = new ListModelList<>(_ioList);
		lbxIo.setModel(model);
	}

}
