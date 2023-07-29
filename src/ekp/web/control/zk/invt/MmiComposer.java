package ekp.web.control.zk.invt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.bind.init.ZKBinderPhaseListeners;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ekp.DebugLogMark;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.invt.WrhsLocInfo;
import ekp.invt.InvtService;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.material.MaterialInstBpuDel0;
import ekp.invt.bpu.material.MaterialInstBuilder0;
import ekp.invt.bpu.material.MaterialMasterBpuDel0;
import ekp.invt.bpu.material.MaterialMasterBuilder0;
import ekp.invt.type.MaterialInstAcqChannel;
import ekp.mbom.type.PartUnit;
import legion.BusinessServiceFactory;
import legion.biz.BpuFacade;
import legion.util.DateFormatUtil;
import legion.util.DateUtil;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.util.TimeTraveler;
import legion.web.zk.ZkMsgBox;
import legion.web.zk.ZkNotification;
import legion.web.zk.ZkUtil;

public class MmiComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(DebugLogMark.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxMaterialMaster;

	@Wire
	private Listbox lbxMaterialInst;

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
			List<MaterialMasterInfo> mmList = new ArrayList<>();
//				List<MaterialMasterInfo> mmList = invtService.load // TODO
			refreshMmList(mmList);

		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	private void init() {
		
		
		
		
		ListitemRenderer<MaterialMasterInfo> mmRenderer = (li, mm, i)->{
			li.appendChild(new Listcell());
			li.appendChild(new Listcell(mm.getMano()));
			li.appendChild(new Listcell(mm.getName()));
			li.appendChild(new Listcell(mm.getSpecification()));
			li.appendChild(new Listcell(mm.getStdUnitChtName()));
			li.appendChild(new Listcell( NumberFormatUtil.getDecimalString(mm.getSumStockQty(), 2) ));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(mm.getSumStockValue(),2)));
			//
			li.addEventListener(Events.ON_CLICK, evt->refreshMiList(mm));
		};
		lbxMaterialMaster.setItemRenderer(mmRenderer);
		
		ListitemRenderer<MaterialInstInfo> miRenderer = (li, mi, i)->{
			li.appendChild(new Listcell());
			li.appendChild(new Listcell(mi.getMisn()));
			li.appendChild(new Listcell(mi.getMiacName()));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(mi.getQty(),2)));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(mi.getValue(),2)));
			li.appendChild(
					new Listcell(DateFormatUtil.transToDate(mi.getEffDate() <= 0 ? null : new Date(mi.getEffDate()))));
			li.appendChild(
					new Listcell(DateFormatUtil.transToDate(mi.getEffDate() <= 0 ? null : new Date(mi.getExpDate()))));
		};
		lbxMaterialInst.setItemRenderer(miRenderer);
		
		
		ZkUtil.initCbb(cbbCreateMmStdUnit, PartUnit.values(), false);
		ZkUtil.initCbb(cbbCreateMiMiac, MaterialInstAcqChannel.values(), false);
	}
	
	// -------------------------------------------------------------------------------
	@Wire
	private Window wdCreateMm;
	@Wire("#wdCreateMm #txbMano")
	private Textbox txbCreateMmMano;
	@Wire("#wdCreateMm #txbName")
	private Textbox txbCreateMmName;
	@Wire("#wdCreateMm #txbSpec")
	private Textbox txbCreateMmSpec;
	@Wire("#wdCreateMm #cbbStdUnit")
	private Combobox cbbCreateMmStdUnit;
	
	@Listen(Events.ON_CLICK+"=#btnAddMm")
	public void btnAddMm_clicked() {
		resetWdCreateMmBlanks();
		wdCreateMm.setVisible(true);
	}
	
	@Listen(Events.ON_CLICK + "=#wdCreateMm #btnResetBlanks")
	public void resetWdCreateMmBlanks() {
		txbCreateMmMano.setValue("");
		txbCreateMmName.setValue("");
		txbCreateMmSpec.setValue("");
		cbbCreateMmStdUnit.setValue("");
	}
	
	@Listen(Events.ON_CLICK + "=#wdCreateMm #btnSubmit")
	public void wdCreateMm_btnSubmit_clicked() {
		MaterialMasterBuilder0 b = BpuFacade.getInstance().getBuilder(InvtBpuType.MM_0);
		b.appendMano(txbCreateMmMano.getValue());
		b.appendName(txbCreateMmName.getValue());
		b.appendSpecification(txbCreateMmSpec.getValue());
		b.appendStdUnit(
				cbbCreateMmStdUnit.getSelectedItem() == null ? null : cbbCreateMmStdUnit.getSelectedItem().getValue());

		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}

		ZkMsgBox.confirm("Confirm create?", () -> {
			MaterialMasterInfo mm = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (mm != null) {
				ZkNotification.info("Create MaterialMaster [" + mm.getMano() + "][" + mm.getName() + "] success.");
				ListModelList<MaterialMasterInfo> model = (ListModelList) lbxMaterialMaster.getModel();
				model.add(mm);
				wdCreateMm_closed(new Event("evt"));
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}
	
	@Listen(Events.ON_CLOSE + "=#wdCreateMm")
	public void wdCreateMm_closed(Event _evt) {
		_evt.stopPropagation();
		wdCreateMm.setVisible(false);
	}
	
	// -------------------------------------------------------------------------------
	private MaterialMasterInfo getSelectedMm() {
		ListModelList<MaterialMasterInfo> model = (ListModelList) lbxMaterialMaster.getListModel();
		Set<MaterialMasterInfo> mmSet = model.getSelection(); // 目前只開放單選
		if (mmSet.isEmpty())
			return null;

		// 目前只開放單選，先挑一筆。
		MaterialMasterInfo mm = mmSet.iterator().next();
		return mm;
	}
	
	private MaterialMasterInfo getSelectedMmFromModel(String _mmUid) {
		ListModelList<MaterialMasterInfo> mmModel = (ListModelList) lbxMaterialMaster.getListModel();
		return mmModel.getInnerList().stream().filter(_mm -> _mm.getUid().equalsIgnoreCase(_mmUid)).findAny()
				.orElse(null);
	}
	
	@Listen(Events.ON_CLICK+"=#btnDeleteMm")
	public void btnDeleteMm_clicked() {
		// 目前只開放單選，先挑一筆。
		MaterialMasterInfo mm = getSelectedMm();
		if (mm == null) {
			ZkMsgBox.exclamation("No material master selected.");
			return;
		}
		
		MaterialMasterBpuDel0 b = BpuFacade.getInstance().getBuilder(InvtBpuType.MM_$DEL0, mm);
		if(b==null) {
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
				ZkNotification.info("Delete masterial master [" + b.getMm().getMano()+ "][" + b.getMm().getName() + "] success.");
				ListModelList<MaterialMasterInfo> model = (ListModelList) lbxMaterialMaster.getListModel();
				model.remove(mm);
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}
	
	// -------------------------------------------------------------------------------
	@Wire
	private Window wdCreateMi;
	@Wire("#wdCreateMi #cbbMiac")
	private Combobox cbbCreateMiMiac;
	@Wire("#wdCreateMi #dbbQty")
	private Doublebox dbbCreateMiQty;
	@Wire("#wdCreateMi #dbbValue")
	private Doublebox dbbCreateMiValue;
	@Wire("#wdCreateMi #dtbEffDate")
	private Datebox dtbCreateMiEffDate;
	@Wire("#wdCreateMi #dtbExpDate")
	private Datebox dtbCreateMiExpDate;
	
	@Listen(Events.ON_CLICK + "=#btnAddMi")
	public void btnAddMi_clicked() {
		MaterialMasterInfo mm = getSelectedMm();
		if (mm == null) {
			ZkMsgBox.exclamation("No material master selected.");
			return;
		}
		
		resetWdCreateMiBlanks();
		wdCreateMi.setVisible(true);
	}
	
	@Listen(Events.ON_CLICK+"=#wdCreateMi #btnResetBlanks")
	public void resetWdCreateMiBlanks() {
		cbbCreateMiMiac.setValue("");
		dbbCreateMiQty.setValue(null);
		dbbCreateMiValue.setValue(null);
		dtbCreateMiEffDate.setValue(null);
		dtbCreateMiExpDate.setValue(null);
	}
	
	@Listen(Events.ON_CLICK + "=#wdCreateMi #btnSubmit")
	public void wdCreateMi_btnsSubmit_clicked() {
		MaterialMasterInfo mm = getSelectedMm();
		if (mm == null) {
			ZkMsgBox.exclamation("No material master selected.");
			return;
		}
		
		MaterialInstBuilder0 b = BpuFacade.getInstance().getBuilder(InvtBpuType.MI_0);
		b.appendMmUid(mm.getUid());
		b.appendMiac(cbbCreateMiMiac.getSelectedItem()==null?null:cbbCreateMiMiac.getSelectedItem().getValue());
		b.appendQty(dbbCreateMiQty.getValue());
		b.appendValue(dbbCreateMiValue.getValue());
		b.appendEffDate(dtbCreateMiEffDate.getValue() == null ? 0 : dtbCreateMiEffDate.getValue().getTime());
		b.appendExpDate(dtbCreateMiExpDate.getValue() == null ? 0 : dtbCreateMiExpDate.getValue().getTime());
		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}
		
		ZkMsgBox.confirm("Confirm create?", () -> {
			MaterialInstInfo mi = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (mi != null) {
				ZkNotification.info("Create MaterialInst [" + mi.getMisn() + "][" +mi.getMiacName() + "]["+mi.getQty()+"]["+mi.getValue()+"] success.");
				//
				ListModelList<MaterialInstInfo> model = (ListModelList) lbxMaterialInst.getModel();
				model.add(mi);
				//
				// TODO
				MaterialMasterInfo mmM = getSelectedMmFromModel(mi.getMmUid()); // 從model中找到mi的parent，若有的話，reload其miList。
				if (mmM != null)
					mmM.getMiList(true); // reload

				wdCreateMi_closed(new Event("evt"));
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}
	
	@Listen(Events.ON_CLOSE + "=#wdCreateMi")
	public void wdCreateMi_closed(Event _evt) {
		_evt.stopPropagation();
		wdCreateMi.setVisible(false);
	}
	
	// -------------------------------------------------------------------------------
	private MaterialInstInfo getSelectedMi() {
		ListModelList<MaterialInstInfo> model = (ListModelList) lbxMaterialInst.getListModel();
		Set<MaterialInstInfo> miSet = model.getSelection(); // 目前只開放單選
		if (miSet.isEmpty())
			return null;
		
		// 目前只開放單選，先挑一筆。
		MaterialInstInfo mi = miSet.iterator().next();
		return mi;
	}
	
	@Listen(Events.ON_CLICK + "=#btnDeleteMi")
	public void btnDeleteMi_clicked() {
		MaterialInstInfo mi = getSelectedMi();
		if (mi == null) {
			ZkMsgBox.exclamation("No material instance selected.");
			return;
		}
		
		MaterialInstBpuDel0 b = BpuFacade.getInstance().getBuilder(InvtBpuType.MI_$DEL0, mi);
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
				ZkNotification.info("Delete masterial instance [" + b.getMi().getMisn() + "][" + b.getMi().getMiacName()
						+ "]["+b.getMi().getQty()+"]["+b.getMi().getValue()+"] success.");
				//
				ListModelList<MaterialInstInfo> model = (ListModelList) lbxMaterialInst.getListModel();
				model.remove(mi);
				MaterialMasterInfo mmM = getSelectedMmFromModel(mi.getMmUid()); // 從model中找到mi的parent，若有的話，reload其wbList。
				log.debug("mmM: {}", mmM);
				if (mmM != null)
					mmM.getMiList(true); // reload
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}
	
	// -------------------------------------------------------------------------------
	private void refreshMmList(List<MaterialMasterInfo> _mmList) {
		ListModelList<MaterialMasterInfo> model = _mmList == null ? new ListModelList<>()
				: new ListModelList<>(_mmList);
		lbxMaterialMaster.setModel(model);
	}

	private void refreshMiList(MaterialMasterInfo _mm) {
		List<MaterialInstInfo> miList = _mm.getMiList();
		ListModelList<MaterialInstInfo> model = miList == null ? new ListModelList<>() : new ListModelList<>(miList);
		lbxMaterialInst.setModel(model);
	}
}
