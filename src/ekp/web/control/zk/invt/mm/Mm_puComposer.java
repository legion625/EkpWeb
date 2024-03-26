package ekp.web.control.zk.invt.mm;

import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.web.control.zk.pu.WdAddPurchWithPostComposer;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.web.zk.ZkNotification;
import legion.web.zk.ZkUtil;

public class Mm_puComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(Mm_puComposer.class);

	private final static String SRC = "/invt/mm/mm_pu.zul";

	public final static Mm_puComposer of(Include _icd) {
		return ZkUtil.of(_icd, SRC, "divMm_pu");
	}

	// -------------------------------------------------------------------------------
	@Wire
	private Include icdWdAddPurchWithpost;
	private WdAddPurchWithPostComposer wdAddPurchWithPostComposer;

	@Wire
	private Listbox lbxPurchItem;

	// -------------------------------------------------------------------------------
	private MaterialMasterInfo mm;

	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	void init(Consumer<PurchInfo> _runAfterPurchBuildAll) {
		/**/

		wdAddPurchWithPostComposer = WdAddPurchWithPostComposer.of(icdWdAddPurchWithpost);
		Consumer<PurchInfo> runAfterPurchBuildAll = p -> {
			//
			ListModelList<PurchItemInfo> model = (ListModelList) lbxPurchItem.getModel();
			model.addAll(p.getPurchItemList());

			//
			_runAfterPurchBuildAll.accept(p);
		};
		wdAddPurchWithPostComposer.init(runAfterPurchBuildAll);

		/**/
		ListitemRenderer<PurchItemInfo> piRenderer = (li, pi, i) -> {
			li.appendChild(new Listcell(pi.getPurch().getPuNo()));
			li.appendChild(new Listcell(pi.getPurch().getSupplierName()));
			li.appendChild(new Listcell(pi.getPurch().getPerfStatus().getName()));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(pi.getQty(), 2)));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(pi.getValue(), 2)));
			li.appendChild(new Listcell(pi.getRefPa().getId()));
			li.appendChild(new Listcell(pi.getRefPa().getName()));
		};
		lbxPurchItem.setItemRenderer(piRenderer);
	}

	@Listen(Events.ON_CLICK + "=#btnAddPurchWithPost")
	public void btnAddPurchWithPost_clicked() {
		if (mm == null) {
			ZkNotification.warning("必須先選取料件主檔。");
			return;
		}

		wdAddPurchWithPostComposer.showWindow(mm);
	}

	// -------------------------------------------------------------------------------
	void refreshPiList(MaterialMasterInfo _mm) {
		this.mm = _mm;

		List<PurchItemInfo> piList = _mm.getPiList();
		ListModelList<PurchItemInfo> model = piList == null ? new ListModelList<>() : new ListModelList<>(piList);
		lbxPurchItem.setModel(model);
	}

}
