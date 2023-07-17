package ekp.web.control.zk.invt;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ekp.DebugLogMark;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.invt.WrhsLocInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.invt.InvtService;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.wrhsLoc.WrhsBinBpuDel0;
import ekp.invt.bpu.wrhsLoc.WrhsBinBuilder1;
import ekp.invt.bpu.wrhsLoc.WrhsLocBpuDel0;
import ekp.invt.bpu.wrhsLoc.WrhsLocBuilder0;
import ekp.mbom.MbomService;
import legion.BusinessServiceFactory;
import legion.biz.BpuFacade;
import legion.util.LogUtil;
import legion.util.TimeTraveler;
import legion.web.zk.ZkMsgBox;
import legion.web.zk.ZkNotification;

public class WrhsLocBinComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(DebugLogMark.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxWrhsLoc;

	@Wire
	private Listbox lbxWrhsBin;
	
	// -------------------------------------------------------------------------------
	private InvtService invtService = BusinessServiceFactory.getInstance().getService(InvtService.class);

	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			//
			init();
			//
			List<WrhsLocInfo> wlList = invtService.loadWrhsLocList();
			refreshWlList(wlList);
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}
	
	// -------------------------------------------------------------------------------
	private void init() {
		ListitemRenderer<WrhsLocInfo> wrhsLocRenderer = (li, wl, i) -> {
			li.appendChild(new Listcell());
			li.appendChild(new Listcell(wl.getId()));
			li.appendChild(new Listcell(wl.getName()));
			//
			li.addEventListener(Events.ON_CLICK, evt -> refreshWbList(wl));
		};
		lbxWrhsLoc.setItemRenderer(wrhsLocRenderer);

		ListitemRenderer<WrhsBinInfo> wrhsBinRenderer = (li, wb, i) -> {
			li.appendChild(new Listcell());
			li.appendChild(new Listcell(wb.getId()));
			li.appendChild(new Listcell(wb.getName()));
		};
		lbxWrhsBin.setItemRenderer(wrhsBinRenderer);
	}
	
	// -------------------------------------------------------------------------------
	@Wire
	private Window wdCreateWl;
	@Wire("#wdCreateWl #txbId")
	private Textbox txbCreateWrhLocId;
	@Wire("#wdCreateWl #txbName")
	private Textbox txbCreateWrhLocName;
	
	@Listen(Events.ON_CLICK+"=#btnAddWrhsLoc")
	public void btnAddWrhsLoc_clicked() {
		resetWdCreateWlBlanks();
		wdCreateWl.setVisible(true);
	}
	
	@Listen(Events.ON_CLICK+"=#wdCreateWl #btnResetBlanks")
	public void resetWdCreateWlBlanks() {
		txbCreateWrhLocId.setValue("");
		txbCreateWrhLocName.setValue("");
	}
	
	@Listen(Events.ON_CLICK + "=#wdCreateWl #btnSubmit")
	public void wdCreateWl_btnSumbit_clicked() {
		WrhsLocBuilder0 b = BpuFacade.getInstance().getBuilder(InvtBpuType.WL_0);
		b.appendId(txbCreateWrhLocId.getValue());
		b.appendName(txbCreateWrhLocName.getValue());
		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}
		
		ZkMsgBox.confirm("Confirm create?", () -> {
			WrhsLocInfo wl = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (wl != null) {
				ZkNotification.info("Create WrhsLoc [" + wl.getId() + "][" + wl.getName() + "] success.");
				ListModelList<WrhsLocInfo> model = (ListModelList) lbxWrhsLoc.getModel();
				model.add(wl);
				wdCreateWl_closed(new Event("evt"));
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}
	
	@Listen(Events.ON_CLOSE + "=#wdCreateWl")
	public void wdCreateWl_closed(Event _evt) {
		_evt.stopPropagation();
		wdCreateWl.setVisible(false);
	}
	
	// -------------------------------------------------------------------------------
	private WrhsLocInfo getSelectedWrhsLoc() {
		ListModelList<WrhsLocInfo> model = (ListModelList) lbxWrhsLoc.getListModel();
		Set<WrhsLocInfo> wlSet = model.getSelection(); // 目前只開放單選
		if (wlSet.isEmpty())
			return null;

		// 目前只開放單選，先挑一筆。
		WrhsLocInfo wl = wlSet.iterator().next();
		return wl;
	}
	
	private WrhsLocInfo getSelectedWrhsLocFromModel(String _wlUid) {
		ListModelList<WrhsLocInfo> wlModel = (ListModelList) lbxWrhsLoc.getModel();
		return wlModel.getInnerList().stream().filter(_wl->_wl.getUid().equalsIgnoreCase(_wlUid)).findAny().orElse(null);
	}
	
	@Listen(Events.ON_CLICK+"=#btnDeleteWrhsLoc")
	public void btnDeleteWrhsLoc_clicked() {
		// 目前只開放單選，先挑一筆。
		WrhsLocInfo wl = getSelectedWrhsLoc();
		if (wl == null) {
			ZkMsgBox.exclamation("No warehouse location selected.");
			return;
		}
			
		WrhsLocBpuDel0 b = BpuFacade.getInstance().getBuilder(InvtBpuType.WL_$DEL0, wl);
		if (b == null) {
			log.warn("getBuilder return null.");
			ZkNotification.error();
			return;
		}
		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}
		
		ZkMsgBox.confirm("Confirm delete?", () -> {
			Boolean result = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (result != null) {
				
				ZkNotification.info("Delete warehouse location [" + b.getWrhsLoc().getId()+ "][" + b.getWrhsLoc().getName() + "] success.");
				ListModelList<WrhsLocInfo> model = (ListModelList) lbxWrhsLoc.getListModel();
				model.remove(wl);
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}
	
	// -------------------------------------------------------------------------------
	@Wire
	private Window wdCreateWb;
	@Wire("#wdCreateWb #txbId")
	private Textbox txbCreateWrhBinId;
	@Wire("#wdCreateWb #txbName")
	private Textbox txbCreateWrhBinName;
	
	@Listen(Events.ON_CLICK + "=#btnAddWrhsBin")
	public void btnAddWrhsBin_clicked() {
		WrhsLocInfo wl = getSelectedWrhsLoc();
		if (wl == null) {
			ZkMsgBox.exclamation("No warehouse location selected.");
			return;
		}

		resetWdCreateWbBlanks();
		wdCreateWb.setVisible(true);
	}

	@Listen(Events.ON_CLICK + "=#wdCreateWb #btnResetBlanks")
	public void resetWdCreateWbBlanks() {
		txbCreateWrhBinId.setValue("");
		txbCreateWrhBinName.setValue("");
	}
	
	@Listen(Events.ON_CLICK + "=#wdCreateWb #btnSubmit")
	public void wdCreateWb_btnSumbit_clicked() {
		WrhsLocInfo wl = getSelectedWrhsLoc();
		if (wl == null) {
			ZkMsgBox.exclamation("No warehouse location selected.");
			return;
		}
		
		WrhsBinBuilder1 b = BpuFacade.getInstance().getBuilder(InvtBpuType.WB_1, wl);
		b.appendId(txbCreateWrhBinId.getValue());
		b.appendName(txbCreateWrhBinName.getValue());
		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}
		
		ZkMsgBox.confirm("Confirm create?", () -> {
			WrhsBinInfo wb = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (wb != null) {
				ZkNotification.info("Create WrhsBin [" + wb.getId() + "][" + wb.getName() + "] success.");
				//
				ListModelList<WrhsBinInfo> model = (ListModelList) lbxWrhsBin.getModel();
				model.add(wb);
				//
				// TODO
				WrhsLocInfo wlM = getSelectedWrhsLocFromModel(wb.getWlUid()); // 從model中找到wb的parent，若有的話，reload其wbList。
				if (wlM != null)
					wlM.getWrhsBinList(true); // reload

				wdCreateWb_closed(new Event("evt"));
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}
	
	
	@Listen(Events.ON_CLOSE + "=#wdCreateWb")
	public void wdCreateWb_closed(Event _evt) {
		_evt.stopPropagation();
		wdCreateWb.setVisible(false);
	}
	
	// -------------------------------------------------------------------------------
	private WrhsBinInfo getSelectedWrhsBin() {
		ListModelList<WrhsBinInfo> model = (ListModelList) lbxWrhsBin.getListModel();
		Set<WrhsBinInfo> wbSet = model.getSelection(); // 目前只開放單選
		if (wbSet.isEmpty())
			return null;

		// 目前只開放單選，先挑一筆。
		WrhsBinInfo wb = wbSet.iterator().next();
		return wb;
	}
	
	@Listen(Events.ON_CLICK + "=#btnDeleteWrhsBin")
	public void btnDeleteWrhsBin_clicked() {
		// 目前只開放單選，先挑一筆。
		WrhsBinInfo wb = getSelectedWrhsBin();
		if (wb == null) {
			ZkMsgBox.exclamation("No warehouse bin selected.");
			return;
		}

		WrhsBinBpuDel0 b = BpuFacade.getInstance().getBuilder(InvtBpuType.WB_$DEL0, wb);
		if (b == null) {
			log.warn("getBuilder return null.");
			ZkNotification.error();
			return;
		}
		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}

		ZkMsgBox.confirm("Confirm delete?", () -> {
			Boolean result = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (result != null) {
				ZkNotification.info("Delete warehouse bin [" + b.getWrhsBin().getId() + "][" + b.getWrhsBin().getName()
						+ "] success.");
				//
				ListModelList<WrhsBinInfo> model = (ListModelList) lbxWrhsBin.getListModel();
				model.remove(wb);
				// TODO
				WrhsLocInfo wlM = getSelectedWrhsLocFromModel(wb.getWlUid()); // 從model中找到wb的parent，若有的話，reload其wbList。
				log.debug("wlM: {}", wlM);
				if (wlM != null)
					wlM.getWrhsBinList(true); // reload
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}
	
	
	
	
	
	// -------------------------------------------------------------------------------
	private void refreshWlList(List<WrhsLocInfo> _wlList) {
		ListModelList<WrhsLocInfo> model = _wlList == null ? new ListModelList<>() : new ListModelList<>(_wlList);
		lbxWrhsLoc.setModel(model);
	}
	
	private void refreshWbList(WrhsLocInfo _wl) {
		List<WrhsBinInfo> wbList =_wl.getWrhsBinList(); 
		ListModelList<WrhsBinInfo> model = wbList==null? new ListModelList<>(): new ListModelList<>(wbList);
		lbxWrhsBin.setModel(model);
	}
}
