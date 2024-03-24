package ekp.web.control.zk.sd;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import ekp.DebugLogMark;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.query.MaterialMasterQueryParam;
import ekp.data.service.sd.BizPartnerInfo;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.invt.InvtService;
import ekp.sd.SalesOrderBuilder1;
import ekp.sd.SalesOrderItemBuilder1;
import ekp.sd.SdBpuType;
import ekp.sd.SdService;
import ekp.util.DataUtil;
import ekp.web.control.zk.sd.bp.BizPartnerComposer;
import ekp.web.zk.ZkUtil;
import legion.BusinessServiceFactory;
import legion.biz.BpuFacade;
import legion.util.LogUtil;
import legion.util.TimeTraveler;
import legion.util.query.QueryOperation;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;
import legion.web.zk.ZkMsgBox;
import legion.web.zk.ZkNotification;

public class SoFnComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(SoFnComposer.class);
//	private Logger log = LoggerFactory.getLogger(DebugLogMark.class);

	private FnCntProxy fnCntProxy;

	// -------------------------------------------------------------------------------
	private InvtService invtService = BusinessServiceFactory.getInstance().getService(InvtService.class);
	private SdService sdService = BusinessServiceFactory.getInstance().getService(SdService.class);
	
	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			fnCntProxy = FnCntProxy.register(this);
			
			init();
			
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	private void init() {
		
		/* MmQvData */
		ListitemRenderer<MmQvData> mmQvDataRenderer = (li, d, i) -> {
			Listcell lc;
			//
			lc = new Listcell();
			Toolbarbutton btnRemove = new Toolbarbutton();
			btnRemove.setIconSclass("fas fa-trash-alt");
			btnRemove.addEventListener(Events.ON_CLICK, e -> {
				ListModelList<MmQvData> model = (ListModelList) lbxCreateSoMmQvData.getModel();
				model.remove(d);
			});
			lc.appendChild(btnRemove);
			li.appendChild(lc);

			//
			li.appendChild(new Listcell(d.getMmMano()));
			li.appendChild(new Listcell(d.getMmName()));
			li.appendChild(new Listcell(d.getMmSpec()));
			//
			lc = new Listcell();
			Doublebox dbbQty = new Doublebox(d.getQty());
			dbbQty.setHflex("1");
			dbbQty.addEventListener(Events.ON_CHANGE, e -> d.setQty(dbbQty.getValue()==null?0:dbbQty.getValue()));
			lc.appendChild(dbbQty);
			li.appendChild(lc);
			//
			lc = new Listcell();
			Doublebox dbbValue = new Doublebox(d.getValue());
			dbbValue.setHflex("1");
			dbbValue.addEventListener(Events.ON_CHANGE, e -> d.setValue(dbbValue.getValue()==null?0:dbbValue.getValue()));
			lc.appendChild(dbbValue);
			li.appendChild(lc);
			
			//
			li.setDroppable("true");
			li.addEventListener(Events.ON_DROP,(DropEvent e) -> {
				
//				log.debug("li: {}\t{}", li, li.getValue());
				Listitem  target = (Listitem) e.getTarget();
//				log.debug("target: {}\t{}", target, target.getValue());
				Listitem  dragged = (Listitem) e.getDragged();
//				log.debug("dragged: {}\t{}", dragged, dragged.getValue());
				
				MaterialMasterInfo mm = dragged.getValue();
//				log.debug("{}\t{}\t{}", mm.getMano(), mm.getName(), mm.getSpecification());
				d.setMm(mm);
				
				// (要加這段畫面才會刷新)
				ListModelList model = (ListModelList) lbxCreateSoMmQvData.getModel();
				model.notifyChange(d);
			});
			
		};
		lbxCreateSoMmQvData.setItemRenderer(mmQvDataRenderer);
		
		
		/* MmList */
		ListitemRenderer<MaterialMasterInfo> mmRenderer = (li, mm, i)->{
			li.appendChild(new Listcell(mm.getMano()));
			li.appendChild(new Listcell(mm.getName()));
			li.appendChild(new Listcell(mm.getSpecification()));
			
			li.setValue(mm);
			li.setDraggable("true");
		};
		lbxCreateSoMm.setItemRenderer(mmRenderer);
		
		//
		QueryOperation<MaterialMasterQueryParam, MaterialMasterInfo> mmParam = new QueryOperation<>();
		List<MaterialMasterInfo> mmList = invtService.searchMaterialMaster(mmParam).getQueryResult();
		ListModelList<MaterialMasterInfo> mmModel = new ListModelList<>(mmList);
		lbxCreateSoMm.setModel(mmModel);
		
	}
	
	// -------------------------------------------------------------------------------
	@Listen(Events.ON_CLICK + "=#btnGotoSearchFnPage")
	public void btnGotoSearchFnPage_clicked() {
		fnCntProxy.refreshCntUri(SoSearchFnComposer.SRC);
	}

	@Listen(Events.ON_CLICK + "=#btnGotoBizPartnerFnPage")
	public void btnGotoBizPartnerFnPage_clicked() {
		fnCntProxy.refreshCntUri(BizPartnerComposer.SRC);
	}
	
	// -------------------------------------------------------------------------------
	@Wire
	private Window wdCreateSo;
	@Wire("#wdCreateSo #txbTitle")
	private Textbox txbCreateSoTitle;
	@Wire("#wdCreateSo #cbbCustomer")
	private Combobox cbbCreateSoCustomer;
	@Wire("#wdCreateSo #lbxMmQvData")
	private Listbox lbxCreateSoMmQvData;
	@Wire("#wdCreateSo #lbxMm")
	private Listbox lbxCreateSoMm;
	
	@Listen(Events.ON_CLICK + "=#btnCreateSo")
	public void btnCreateSo_clicked() {
		/* Customer */
		ZkUtil.initCbb(cbbCreateSoCustomer, sdService.loadCustomerList().toArray(new BizPartnerInfo[0]), false);
		
		wdCreateSo_btnResetBlanks_clicked();
		wdCreateSo.setVisible(true);
	}
	
	@Listen(Events.ON_CLICK + "=#wdCreateSo #btnAddMmQvData")
	public void wdCreateSo_btnAddMmQvData_clicked() {
		ListModelList<MmQvData> model = (ListModelList) lbxCreateSoMmQvData.getModel();
		model.add(new MmQvData());
	}

	@Listen(Events.ON_CLICK + "=#wdCreateSo #btnSubmit")
	public void wdCreateSo_btnSubmit_clicked() {
		SalesOrderBuilder1 sob = BpuFacade.getInstance().getBuilder(SdBpuType.SO_1);
		sob.appendTitle(txbCreateSoTitle.getValue());
		BizPartnerInfo customer = cbbCreateSoCustomer.getSelectedItem() == null ? null
				: cbbCreateSoCustomer.getSelectedItem().getValue();
		sob.appendCustomer(customer);

		ListModelList model = (ListModelList) lbxCreateSoMmQvData.getModel();
		List<MmQvData> mmQvDataList = model.getInnerList();

		for (MmQvData mmQvData : mmQvDataList) {
			SalesOrderItemBuilder1 soib = sob.addSoiBuilder();
			soib.appendMm(mmQvData.getMm());
			soib.appendQty(mmQvData.getQty()).appendValue(mmQvData.getValue());
		}

		StringBuilder sb = new StringBuilder();
		boolean verify = sob.verify(sb);

		if (!verify) {
			ZkMsgBox.exclamation(sb.toString());
			return;
		}

		ZkMsgBox.confirm("Confirm create?", ()->{
			SalesOrderInfo so = sob.build();
			if (so == null) {
				ZkNotification.error();
				return;
			}
			ZkNotification.info("Create sales order[" + so.getSosn() + "] complete.");
			
			wdCreateSo_closed(new Event("evt"));
		});
		
	}
	
	
	@Listen(Events.ON_CLICK + "=#wdCreateSo #btnResetBlanks")
	public void wdCreateSo_btnResetBlanks_clicked() {
		txbCreateSoTitle.setValue("");
		cbbCreateSoCustomer.setValue("");
		refreshLbxCreateSoSoib(null);
	}

	private void refreshLbxCreateSoSoib(List<MmQvData> _dataList) {
		ListModelList<MmQvData> model = _dataList == null ? new ListModelList<>() : new ListModelList<>(_dataList);
		lbxCreateSoMmQvData.setModel(model);
	}
	
	@Listen(Events.ON_CLOSE+"=#wdCreateSo")
	public void wdCreateSo_closed(Event _evt) {
		wdCreateSo.setVisible(false);
		_evt.stopPropagation();
	}
	
	private class MmQvData {
		private MaterialMasterInfo mm;
		private double qty, value;

		public MaterialMasterInfo getMm() {
			return mm;
		}

		public void setMm(MaterialMasterInfo mm) {
			this.mm = mm;
		}

		public double getQty() {
			return qty;
		}

		public void setQty(double qty) {
			this.qty = qty;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}

		// ---------------------------------------------------------------------------
		public String getMmMano() {
			return getMm() == null ? DataUtil.NO_DATA : getMm().getMano();
		}

		public String getMmName() {
			return getMm() == null ? DataUtil.NO_DATA : getMm().getName();
		}

		public String getMmSpec() {
			return getMm() == null ? DataUtil.NO_DATA : getMm().getSpecification();
		}

	}

}
