package ekp.web.control.zk.invt.mm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mf.WorkorderInfo;
import legion.util.DateFormatUtil;
import legion.util.DateUtil;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.web.zk.ZkMsgBox;
import legion.web.zk.ZkNotification;
import legion.web.zk.ZkUtil;

public class Mm_woComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(Mm_woComposer.class);

	private final static String SRC = "/invt/mm/mm_wo.zul";

	public final static Mm_woComposer of(Include _icd) {
		return ZkUtil.of(_icd, SRC, "divmm_wo");
	}

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxWo;

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
	
	void init() {
		/**/
		ListitemRenderer<WorkorderInfo> woRenderer = (li, wo, i) -> {
			li.appendChild(new Listcell(wo.getWoNo()));
			li.appendChild(new Listcell(wo.getPartPin()));
			li.appendChild(new Listcell(wo.getPartAcqId()));
			li.appendChild(new Listcell(wo.getPartAcqMmMano()));
			li.appendChild(new Listcell(wo.getPartCfgId()));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(wo.getRqQty(), 2)));
			li.appendChild(new Listcell(DateFormatUtil.transToDate(wo.getStartWorkTime())));
			li.appendChild(new Listcell(DateFormatUtil.transToDate(wo.getFinishWorkTime())));
			li.appendChild(new Listcell(DateFormatUtil.transToDate(wo.getOverTime())));
		};
		lbxWo.setItemRenderer(woRenderer);
	}
	
	@Wire
	private Window wdAddWoWithPost;
	

	@Listen(Events.ON_CLICK + "=#btnAddWoWithPost")
	public void btnAddWoWithPost_clicked() {
		if (mm == null) {
			ZkNotification.warning("必須先選取料件主檔。");
			return;
		}
		
		wdAddWoWithPost_btnResetBlanks_clicked();
		
		//
		// TODO 設定各個元件的值
		
		wdAddWoWithPost.setVisible(true);
	}
	
	@Listen(Events.ON_CLICK + "=#wdAddWoWithPost #btnResetBlanks")
	public void wdAddWoWithPost_btnResetBlanks_clicked() {
		// TODO
	}
	
	@Listen(Events.ON_CLICK + "=#wdAddWoWithPost #btnSubmit")
	public void wdAddWoWithPost_btnSubmit_clicked() {
		if (mm == null) {
			ZkMsgBox.exclamation("No material master selected.");
			return;
		}
		
		
	}
	
}
