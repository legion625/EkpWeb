package ekp.web.control.zk.pu;

import java.util.function.Consumer;
import java.util.stream.Collectors;

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
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.data.service.sd.BizPartnerInfo;
import ekp.invt.InvtService;
import ekp.mbom.type.PartAcquisitionType;
import ekp.pu.bpu.PuBpuType;
import ekp.pu.bpu.PurchBuilderAll;
import ekp.sd.SdService;
import legion.BusinessServiceFactory;
import legion.biz.BpuFacade;
import legion.util.LogUtil;
import legion.web.zk.ZkMsgBox;
import legion.web.zk.ZkNotification;
import legion.web.zk.ZkUtil;

public class WdAddPurchWithPostComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(WdAddPurchWithPostComposer.class);
	private final static String SRC = "/pu/wdAddPurchWithPost.zul";

	public final static WdAddPurchWithPostComposer of(Include _icd) {
		return ZkUtil.of(_icd, SRC, "wdAddPurchWithPost");
	}

	// -------------------------------------------------------------------------------
	@Wire
	private Window wdAddPurchWithPost;
	@Wire("#wdAddPurchWithPost #lbMano")
	private Label lbAddPurchWithPostMano;
	@Wire("#wdAddPurchWithPost #cbbRefPa")
	private Combobox cbbAddPurchWithPostRefPa;
	@Wire("#wdAddPurchWithPost #cbbSupplier")
	private Combobox cbbAddPurchWithPostSupplier;
	@Wire("#wdAddPurchWithPost #cbbWb")
	private Combobox cbbAddPurchWithPostWb;
	@Wire("#wdAddPurchWithPost #dbbQty")
	private Doublebox dbbAddPurchWithPostQty;
	@Wire("#wdAddPurchWithPost #dbbValue")
	private Doublebox dbbAddPurchWithPostValue;

	// -------------------------------------------------------------------------------
	private MaterialMasterInfo mm;
	private Consumer<PurchInfo> runAfterPurchBuildAll;

	// -------------------------------------------------------------------------------
	private InvtService invtService = BusinessServiceFactory.getInstance().getService(InvtService.class);
	private SdService sdService = BusinessServiceFactory.getInstance().getService(SdService.class);

	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	// -------------------------------------------------------------------------------
	public void init(Consumer<PurchInfo> runAfterPurchBuildAll) {
		this.runAfterPurchBuildAll = runAfterPurchBuildAll;
	}
	
	// -------------------------------------------------------------------------------
	public void showWindow(MaterialMasterInfo _mm) {
		if (_mm == null) {
			ZkNotification.warning("必須先選取料件主檔。");
			return;
		}
		this.mm = _mm;

		resetWdAddPurchWithPostBlanks();

		//
		lbAddPurchWithPostMano.setValue(_mm.getMano());
		//
		ZkUtil.initCbb(cbbAddPurchWithPostRefPa, _mm.getPaList(PartAcquisitionType.PU), PartAcqInfo::getPartPinWithId,
				PartAcqInfo::getName, false);
		if (cbbAddPurchWithPostRefPa.getItemCount() > 0)
			cbbAddPurchWithPostRefPa.setSelectedIndex(0);
		//
		ZkUtil.initCbb(cbbAddPurchWithPostSupplier, sdService.loadSupplierList(), BizPartnerInfo::getName,
				BizPartnerInfo::getBan, false);
		if (cbbAddPurchWithPostSupplier.getItemCount() > 0)
			cbbAddPurchWithPostSupplier.setSelectedIndex(0);
		//

		ZkUtil.initCbb(
				cbbAddPurchWithPostWb, invtService.loadWrhsLocList().stream()
						.flatMap(wl -> wl.getWrhsBinList().stream()).collect(Collectors.toList()),
				WrhsBinInfo::getId, WrhsBinInfo::getName, false);
		if (cbbAddPurchWithPostWb.getItemCount() > 0)
			cbbAddPurchWithPostWb.setSelectedIndex(0);

		wdAddPurchWithPost.setVisible(true);
	}

	@Listen(Events.ON_CLICK + "=#wdAddPurchWithPost #btnSubmit")
	public void wdAddPurchWithPost_btnSubmit_clicked() {
		if (mm == null) {
			ZkMsgBox.exclamation("No material master selected.");
			return;
		}

		PurchBuilderAll b = BpuFacade.getInstance().getBuilder(PuBpuType.P_ALL);
		PartAcqInfo pa = cbbAddPurchWithPostRefPa.getSelectedItem() == null ? null
				: cbbAddPurchWithPostRefPa.getSelectedItem().getValue();
		BizPartnerInfo supplier = cbbAddPurchWithPostSupplier.getSelectedItem() == null ? null
				: cbbAddPurchWithPostSupplier.getSelectedItem().getValue();
		WrhsBinInfo wb = cbbAddPurchWithPostWb.getSelectedItem() == null ? null
				: cbbAddPurchWithPostWb.getSelectedItem().getValue();
		double qty = dbbAddPurchWithPostQty.getValue() == null ? 0 : dbbAddPurchWithPostQty.getValue();
		double value = dbbAddPurchWithPostValue.getValue() == null ? 0 : dbbAddPurchWithPostValue.getValue();
		String title = "採購" + mm.getName();
		b.appendTitle(title);
		b.appendSupplier(supplier).appendWb(wb);
		b.addPiBuilder().appendMm(mm).appendPa(pa).appendQty(qty).appendValue(value);

		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}

		ZkMsgBox.confirm("Confirm add purch with post?", () -> {
			PurchInfo p = b.build();
			// 成功
			if (p != null) {
				ZkNotification.info("Add purch with post [" + p.getPuNo() + "][" + mm.getMano() + "][" + mm.getName()
						+ "] success.");

//				ListModelList<PurchItemInfo> model = (ListModelList) lbxPurchItem.getModel();
//				model.addAll(p.getPurchItemList());
				wdAddPurchWithPost_closed(new Event("evt"));

				//
				if (runAfterPurchBuildAll != null)
					runAfterPurchBuildAll.accept(p);

			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}

	@Listen(Events.ON_CLICK + "=#wdAddPurchWithPost #btnResetBlanks")
	public void resetWdAddPurchWithPostBlanks() {
		lbAddPurchWithPostMano.setValue("");
		cbbAddPurchWithPostRefPa.setValue("");
		cbbAddPurchWithPostSupplier.setValue("");
		cbbAddPurchWithPostWb.setValue("");
		dbbAddPurchWithPostQty.setValue(null);
		dbbAddPurchWithPostValue.setValue(null);
	}

	@Listen(Events.ON_CLOSE + "=#wdAddPurchWithPost")
	public void wdAddPurchWithPost_closed(Event _evt) {
		_evt.stopPropagation();
		wdAddPurchWithPost.setVisible(false);
	}

}
