package ekp.web.control.zk.invt;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
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
import legion.BusinessServiceFactory;
import legion.util.DateFormatUtil;
import legion.util.DateUtil;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.web.zk.ZkMsgBox;

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
//				List<WrhsLocInfo> wlList = invtService.loadWrhsLocList();
//				List<MaterialMasterInfo> mmList = invtService.load
//				refreshWlList(wlList);
			// TODO
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
	
	@Listen(Events.ON_CLICK+"=#wdCreateMm #btnSubmit")
	public void wdCreateMm_btnSubmit_clicked() {
		// TODO
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
		// TODO
		
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
		
		// TODO
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
		
		// TODO
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
