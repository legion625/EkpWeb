package ekp.web.control.zk.invt.mm;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

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
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.invt.InvtService;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.material.MaterialMasterBpuDel0;
import ekp.invt.bpu.material.MaterialMasterBuilder0;
import ekp.mbom.type.PartUnit;
import ekp.sd.SdService;
import legion.BusinessServiceFactory;
import legion.biz.BpuFacade;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.util.TimeTraveler;
import legion.util.query.QueryOperation;
import legion.web.zk.ZkMsgBox;
import legion.web.zk.ZkNotification;
import legion.web.zk.ZkUtil;

public class MmiComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(MmiComposer.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxMaterialMaster;

	@Wire
	private Include icdMi;
	private MiComposer miComposer;

	// -------------------------------------------------------------------------------
	@Wire
	private Include icdMm_mbs;
	private Mm_mbsComposer mm_mbsComposer;

	// -------------------------------------------------------------------------------
	@Wire
	private Include icdMm_pu;
	private Mm_puComposer mm_puComposer;

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
			List<MaterialMasterInfo> mmList = invtService.searchMaterialMaster(new QueryOperation<>()).getQueryResult();
			refreshMmList(mmList);

		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	private void init() {
		/**/
		ListitemRenderer<MaterialMasterInfo> mmRenderer = (li, mm, i) -> {
			li.appendChild(new Listcell());
			li.appendChild(new Listcell(mm.getMano()));
			li.appendChild(new Listcell(mm.getName()));
			li.appendChild(new Listcell(mm.getStdUnitChtName()));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(mm.getSumStockQty(), 2)));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(mm.getSumStockValue(), 2)));
			li.appendChild(new Listcell(mm.getSpecification()));
			//
			li.addEventListener(Events.ON_CLICK, evt -> {
				miComposer.refreshMiList(mm);
				mm_mbsComposer.refreshMbsList(mm);
				mm_puComposer.refreshPiList(mm);
			});
		};
		lbxMaterialMaster.setItemRenderer(mmRenderer);

		ZkUtil.initCbb(cbbCreateMmStdUnit, PartUnit.values(), false);

		/**/
		miComposer = MiComposer.of(icdMi);
		Consumer<MaterialInstInfo> miRunAfterSubmit = mi -> {
			MaterialMasterInfo mmM = getSelectedMmFromModel(mi.getMmUid()); // 從model中找到mi的parent，若有的話，reload其miList。
			if (mmM != null) {
				mmM.getMiList(true); // reload
			}

		};
		miComposer.init(miRunAfterSubmit);

		/**/
		mm_mbsComposer = Mm_mbsComposer.of(icdMm_mbs);
		mm_mbsComposer.init();

		/**/
		mm_puComposer = Mm_puComposer.of(icdMm_pu);
		Consumer<PurchInfo> runAfterPurchBuildAll = pu->{
			for(PurchItemInfo pi: pu.getPurchItemList()) {
				pi.getMmUid();
				ListModelList<MaterialMasterInfo> mmModel = (ListModelList) lbxMaterialMaster.getListModel();
				MaterialMasterInfo mmM = mmModel.getInnerList().stream()
						.filter(_mm -> _mm.getUid().equalsIgnoreCase(pi.getMmUid())).findAny().orElse(null);
				if (mmM != null) {
					MaterialMasterInfo mmReload = mmM.reload();
					// 更新左側的Material Master
					mmModel.set(mmModel.indexOf(mmM), mmReload);
					mmM.getMiList(true); // reload

					// 更新mi
					miComposer.refreshMiList(mmReload);
					// 更新倉庫管理
					mm_mbsComposer.refreshMbsList(mmReload);
				}
			}
		};
		mm_puComposer.init(runAfterPurchBuildAll); // TODO
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
	private void refreshMmList(List<MaterialMasterInfo> _mmList) {
		ListModelList<MaterialMasterInfo> model = _mmList == null ? new ListModelList<>()
				: new ListModelList<>(_mmList);
		lbxMaterialMaster.setModel(model);
	}
}
