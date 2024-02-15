package ekp.web.control.zk.mbom;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.mbom.MbomService;
import ekp.mbom.issue.MbomBpuType;
import ekp.mbom.issue.part.PartBpuDel0;
import ekp.mbom.issue.part.PartBpuUpdate;
import ekp.mbom.issue.part.PartBuilder0;
import ekp.mbom.type.PartUnit;
import ekp.web.control.zk.filter.LbxFltrCtrl;
import ekp.web.control.zk.filter.biz.PartFp;
import legion.BusinessServiceFactory;
import legion.biz.BpuFacade;
import legion.util.DataFO;
import legion.util.LogUtil;
import legion.util.TimeTraveler;
import legion.util.filter.FilterOperation;
import legion.util.filter.FilterOperation.FilterCompareOp;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;
import legion.web.zk.ZkMsgBox;
import legion.web.zk.ZkNotification;
import legion.web.zk.ZkUtil;

public class PartListPageComposer extends SelectorComposer<Component> {
	public final static String SRC = "/mbom/partListPage.zul";
	private Logger log = LoggerFactory.getLogger(PartListPageComposer.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxPart;
	@Wire
	private Textbox txbFltrPin, txbFltrName;
	@Wire
	private Combobox cbbFltrUnit;
	
	private LbxFltrCtrl<PartInfo> fltrCtrlLbxPart;
	
	/**/
	@Wire
	private Include icdPartInfo;
	private PartInfoComposer partInfoComposer;

	// -------------------------------------------------------------------------------
	private MbomService mbomService = BusinessServiceFactory.getInstance().getService(MbomService.class);

	private FnCntProxy fnCntProxy;
	
	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			fnCntProxy = FnCntProxy.register(this);
			log.debug("fnCntProxy: {}", fnCntProxy);

			init();
			List<PartInfo> partList = mbomService.loadPartList();
			setPartList(partList);
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	// -------------------------------------------------------------------------------
	private void init() {
		ListitemRenderer<PartInfo> partRenderer = (li, p, i) -> {
			Listcell lc;
			// delete
			lc = new Listcell();
			Toolbarbutton btn = new Toolbarbutton();
			btn.setIconSclass("fa fa-minus");
			btn.addEventListener(Events.ON_CLICK, e -> {
				boolean match = MbomBpuType.PART_$DEL0.match(p);
				if (!match) {
					ZkNotification.warning("This part cannot be deleted.");
					return;
				}
				
				PartBpuDel0 b = BpuFacade.getInstance().getBuilder(MbomBpuType.PART_$DEL0, p);
				if (b == null) {
					ZkNotification.error();
					return;
				}

				ZkMsgBox.confirm("Confirm delete?", () -> {
					boolean d = b.build(new StringBuilder(), new TimeTraveler());
					if (d) {
						ZkNotification.info("Delete part [" + p.getPin() + "][" + p.getName() + "] success.");
						ListModelList<PartInfo> model = (ListModelList) lbxPart.getModel();
						model.remove(p);
					} else {
						ZkNotification.error();
					}

				});
			});
			lc.appendChild(btn);
			li.appendChild(lc);
			//
			li.appendChild(new Listcell(p.getPin()));
			//
			lc = new Listcell();
			Textbox txbName = new Textbox(p.getName());
			txbName.setInplace(true);
			txbName.addEventListener(Events.ON_CHANGE, e->{
				PartBpuUpdate b = BpuFacade.getInstance().getBuilder(MbomBpuType.PART_$UPDATE, p);
				String newName = txbName.getValue()==null?"":txbName.getValue();
				b.appendName(newName);
				StringBuilder msg = new StringBuilder();
				if (!b.verify(msg)) {
					ZkMsgBox.exclamation(msg.toString());
					return;
				}
				
				if (b.build(new StringBuilder(), null)) {
					ZkNotification.info("Update part name   [" + p.getPin() + "][" +newName + "] success.");
					ListModelList<PartInfo> model = (ListModelList) lbxPart.getModel();

					model.add(model.indexOf(p), p.reload());
					model.remove(p);
				} else {
					ZkNotification.error();
				}
			});
			lc.appendChild(txbName);
			li.appendChild(lc);
			//
			li.appendChild(new Listcell(p.getUnit().name()));

			// click event -> show part
			li.addEventListener(Events.ON_CLICK, e -> {
				// TODO
				partInfoComposer.refreshPartInfo(p.reload());
			});
		};
		fltrCtrlLbxPart = LbxFltrCtrl.of(lbxPart, partRenderer);
		Textbox[] txbsFltrPart = new Textbox[] {txbFltrPin, txbFltrName};
		Combobox[] cbbsFltrPart = new Combobox[] {cbbFltrUnit};
		fltrCtrlLbxPart.initFilter(txbsFltrPart, cbbsFltrPart, null, null, this::filterPart);
		
		ZkUtil.initCbb(cbbFltrUnit, PartUnit.values(), true);
		
		/**/
		partInfoComposer = PartInfoComposer.of(icdPartInfo);
	}

	private List<PartInfo> filterPart(List<PartInfo> _list){
		/**/
		String fltrPin = txbFltrPin.getValue();
		String fltrName = txbFltrName.getValue();
		PartUnit fltrUnit = cbbFltrUnit.getSelectedItem()==null?null:
			cbbFltrUnit.getSelectedItem().getValue();
			
		/* filter */
		FilterOperation<PartFp, ?> fop  =new  FilterOperation<>();
		// pin
		if(!DataFO.isEmptyString(fltrPin))
			fop.addCondition(FilterOperation.value(PartFp.PIN, FilterCompareOp.like, "%" + fltrPin + "%"));
		// name
		if(!DataFO.isEmptyString(fltrName))
			fop.addCondition(FilterOperation.value(PartFp.NAME, FilterCompareOp.like, "%" + fltrName + "%"));
		// partUnit
		if(fltrUnit!=null)
			fop.addCondition(FilterOperation.value(PartFp.UNIT, FilterCompareOp.equal,fltrUnit));
			
		try {
			List<PartInfo> filteredList = fop.filterConditions(_list);
			return filteredList;
		} catch (Throwable e) {
			LogUtil.log(e);
			return new ArrayList<>();
		}
	}
	
	
	// -------------------------------------------------------------------------------
	// ---------------------------------wdCreatePart----------------------------------
	@Wire
	private Window wdCreatePart;
	@Wire("#wdCreatePart #txbPin")
	private Textbox txbCreatePartPin;
	@Wire("#wdCreatePart #txbName")
	private Textbox txbCreatePartName;
	@Wire("#wdCreatePart #cbbUnit")
	private Combobox cbbCreatePartUnit;

	@Listen(Events.ON_CLICK + "=#btnAddPart")
	public void btnAddPart_clicked() {
		showWdCreatePart();
	}

	private void showWdCreatePart() {
		resetWdCreatePartBlanks();
		wdCreatePart.setVisible(true);
	}

	private void resetWdCreatePartBlanks() {
		txbCreatePartPin.setValue("");
		txbCreatePartName.setValue("");
		
		ZkUtil.initCbb(cbbCreatePartUnit, PartUnit.values(), false);
	}

	@Listen(Events.ON_CLICK + "=#wdCreatePart #btnSubmit")
	public void wdCreatePart_btnSubmit_clicked() {
		PartBuilder0 b = BpuFacade.getInstance().getBuilder(MbomBpuType.PART_0);
		b.appendPin(txbCreatePartPin.getValue()).appendName(txbCreatePartName.getValue());
		b.appendUnit(
				cbbCreatePartUnit.getSelectedItem() == null ? null : cbbCreatePartUnit.getSelectedItem().getValue());
		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}

		ZkMsgBox.confirm("Confirm create?", () -> {
			PartInfo p = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (p != null) {
				ZkNotification.info("Create part [" + p.getPin() + "][" + p.getName() + "] success.");
				ListModelList<PartInfo> model = (ListModelList) lbxPart.getModel();
				model.add(p);
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});

	}

	@Listen(Events.ON_CLICK + "=#wdCreatePart #btnResetBlanks")
	public void wdCreatePart_btnResetBlanks_clicked() {
		resetWdCreatePartBlanks();
	}

	@Listen(Events.ON_CLOSE + "=#wdCreatePart")
	public void wdCreatePart_closed(Event _evt) {
		_evt.stopPropagation();
		wdCreatePart.setVisible(false);
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------
	public void setPartList(List<PartInfo> _partList) {
		fltrCtrlLbxPart.refresh(_partList);
	}

}
