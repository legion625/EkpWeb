package ekp.web.control.zk.sd;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.query.InvtOrderItemQueryParam;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.SalesOrderItemInfo;
import ekp.invt.InvtService;
import ekp.invt.type.IoiTargetType;
import ekp.util.DataUtil;
import legion.BusinessServiceFactory;
import legion.util.DateFormatUtil;
import legion.util.DateUtil;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.CompareOp;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;

public class SoSearchResultComposer extends SelectorComposer<Component> {
	public final static String SRC = "/sd/soSearchResult.zul";
	private Logger log = LoggerFactory.getLogger(SoSearchResultComposer.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxSo;
	@Wire
	private Listbox lbxSoi;
	@Wire
	private Listbox lbxIoi;

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
		//
		ListitemRenderer<SalesOrderInfo> soRenderer = (li, so, i) -> {
			li.appendChild(new Listcell(""));
			li.appendChild(new Listcell(so.getSosn()));
			li.appendChild(new Listcell(so.getTitle()));
			li.appendChild(new Listcell(so.getCustomerName()));
			li.appendChild(new Listcell(so.getCustomerBan()));
			li.appendChild(new Listcell(so.getSalerName()));
			li.appendChild(new Listcell(DateFormatUtil.transToDate(so.getSaleDate())));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(so.getSumSoiAmt(), 2)));
			
			//
			li.addEventListener(Events.ON_CLICK,e->refreshSo(so));
		};
		lbxSo.setItemRenderer(soRenderer);

		//
		ListitemRenderer<SalesOrderItemInfo> soiRenderer = (li, soi, i) -> {
			li.appendChild(new Listcell(""));
			li.appendChild(new Listcell(soi.getMmMano()));
			li.appendChild(new Listcell(soi.getMmName()));
			li.appendChild(new Listcell(soi.getMmSpec()));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(soi.getQty(), 2)));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(soi.getValue(), 2)));
			li.appendChild(new Listcell(DataUtil.getStr(soi.isAllDelivered())));
			li.appendChild(new Listcell(DateFormatUtil.transToDate(soi.getFinishDeliveredDate())));
		};
		lbxSoi.setItemRenderer(soiRenderer);
		
		//
		ListitemRenderer<InvtOrderItemInfo> ioiRenderer = (li, ioi, i)->{
			li.appendChild(new Listcell(ioi.getIo().getIosn()));
			li.appendChild(new Listcell(ioi.getIo().getStatusName()));
			li.appendChild(new Listcell(DateFormatUtil.transToDate(ioi.getIo().getApplyTime())));
			li.appendChild(new Listcell(DateFormatUtil.transToDate(ioi.getIo().getApvTime())));
			li.appendChild(new Listcell(ioi.getIo().getRemark()));
			li.appendChild(new Listcell(ioi.getMm().getMano()));
			li.appendChild(new Listcell(ioi.getMm().getName()));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(ioi.getOrderQty(), 2) ));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString (ioi.getOrderValue(),2)));
		} ;
		lbxIoi.setItemRenderer(ioiRenderer);
	}

	public void refreshData(List<SalesOrderInfo> _soList) {
		ListModelList<SalesOrderInfo> model = new ListModelList<>(_soList);
		lbxSo.setModel(model);
	}

	private void refreshSo(SalesOrderInfo _so) {
		//
		ListModelList<SalesOrderItemInfo> soiModel = _so == null ? new ListModelList<>()
				: new ListModelList<>(_so.getSalesOrderItemList());
		lbxSoi.setModel(soiModel);

		//
		InvtService invtService = BusinessServiceFactory.getInstance().getService(InvtService.class);
		QueryOperation<InvtOrderItemQueryParam, InvtOrderItemInfo> ioiParam = new QueryOperation<>();
		ioiParam.appendCondition(QueryOperation.value(InvtOrderItemQueryParam.TARGET_TYPE_IDX, CompareOp.equal,IoiTargetType.SOI.getIdx()));
		ioiParam.appendCondition(QueryOperation.value(InvtOrderItemQueryParam.TARGET_BIZ_KEY, CompareOp.equal,_so.getSosn()));
		ioiParam = invtService.searchInvtOrderItem(ioiParam, null);
		List<InvtOrderItemInfo> ioiList = ioiParam.getQueryResult();
		ListModelList<InvtOrderItemInfo> ioiModel = new ListModelList<>(ioiList);
		lbxIoi.setModel(ioiModel);
	}
}
