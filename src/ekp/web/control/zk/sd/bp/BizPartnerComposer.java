package ekp.web.control.zk.sd.bp;

import java.util.List;

import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.sd.BizPartnerInfo;
import ekp.sd.BizPartnerBuilder;
import ekp.sd.SdBpuType;
import ekp.sd.SdService;
import legion.BusinessServiceFactory;
import legion.biz.BpuFacade;
import legion.util.LogUtil;
import legion.util.TimeTraveler;
import legion.web.zk.ZkMsgBox;
import legion.web.zk.ZkNotification;

public class BizPartnerComposer extends SelectorComposer<Component> {
	public final static String SRC = "/sd/bp/bizPartner.zul";

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxBp;

	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			//
			init();
			//
			List<BizPartnerInfo> bpList = BusinessServiceFactory.getInstance().getService(SdService.class)
					.loadBizPartnerList();
			refreshBpList(bpList);

		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	private void init() {
		/**/
		ListitemRenderer<BizPartnerInfo> bpRenderer = (li, bp, i) -> {
			li.appendChild(new Listcell());
			li.appendChild(new Listcell(bp.getBpsn()));
			li.appendChild(new Listcell(bp.getName()));
			li.appendChild(new Listcell(bp.getBan()));
		};
		lbxBp.setItemRenderer(bpRenderer);

	}

	// -------------------------------------------------------------------------------
	private void refreshBpList(List<BizPartnerInfo> _bpList) {
		ListModelList<BizPartnerInfo> model = _bpList == null ? new ListModelList<>() : new ListModelList<>(_bpList);
		lbxBp.setModel(model);
	}
	
	// -------------------------------------------------------------------------------
	@Wire
	private Window wdCreateBp;
	@Wire("#wdCreateBp #txbName")
	private Textbox txbCreateBpName;
	@Wire("#wdCreateBp #txbBan")
	private Textbox txbCreateBpBan;
	
	@Listen(Events.ON_CLICK+"=#btnCreateBp")
	public void btnCreateBp_clicked() {
		wdCreateBp_btnResetBlanks_clicked();
		wdCreateBp.setVisible(true);
	}

	@Listen(Events.ON_CLICK + "=#wdCreateBp #btnResetBlanks")
	public void wdCreateBp_btnResetBlanks_clicked() {
		txbCreateBpName.setValue("");
		txbCreateBpBan.setValue("");
	}

	@Listen(Events.ON_CLICK + "=#wdCreateBp #btnSubmit")
	public void wdCreateBp_btnSubmit_clicked() {
		BizPartnerBuilder b = BpuFacade.getInstance().getBuilder(SdBpuType.BP);
		b.appendName(txbCreateBpName.getValue());
		b.appendBan(txbCreateBpBan.getValue());
		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}

		ZkMsgBox.confirm("Confirm create?", () -> {
			BizPartnerInfo bp = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (bp != null) {
				ZkNotification.info("Create business parter [" + bp.getName() + "][" + bp.getBan() + "] success.");
				ListModelList<BizPartnerInfo> model = (ListModelList) lbxBp.getModel();
				model.add(bp);
				wdCreateBp_closed(new Event("evt"));
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}

	@Listen(Events.ON_CLOSE + "=#wdCreateBp")
	public void wdCreateBp_closed(Event _evt) {
		_evt.stopPropagation();
		wdCreateBp.setVisible(false);
	}

}
