package ekp.web.control.zk.mbom;

import java.util.Iterator;

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
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mbom.PprocInfo;
import ekp.mbom.issue.MbomBpuType;
import ekp.mbom.issue.part.PartBpuDel0;
import ekp.mbom.issue.part.PartBuilder0;
import ekp.mbom.issue.partAcq.PaBpuDel0;
import ekp.mbom.issue.partAcq.PartAcqBuilder0;
import ekp.mbom.issue.partAcqRoutingStep.ParsBpuDel0;
import ekp.mbom.issue.partAcqRoutingStep.ParsBuilder0;
import ekp.mbom.type.PartAcquisitionType;
import legion.biz.BpuFacade;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.util.TimeTraveler;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;
import legion.web.zk.ZkMsgBox;
import legion.web.zk.ZkNotification;
import legion.web.zk.ZkUtil;

public class PartInfoComposer extends SelectorComposer<Component> {
	public final static String URI = "/mbom/partInfo.zul";

	private Logger log = LoggerFactory.getLogger(PartInfoComposer.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Label lbPin, lbName;

	@Wire
	private Listbox lbxPartAcq, lbxPars, lbxPproc, lbxPpart;

	// -------------------------------------------------------------------------------
	private FnCntProxy fnCntProxy;
	
	private PartInfo part;

	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			fnCntProxy = FnCntProxy.register(this);
			log.debug("fnCntProxy: {}", fnCntProxy);

			init();
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	// -------------------------------------------------------------------------------
	private void init() {
		/* PartAcq */
		ListitemRenderer<PartAcqInfo> paRenderer = (li, pa, i) -> {
			Listcell lc;
			// delete
			lc = new Listcell();
			Toolbarbutton btn = new Toolbarbutton();
			btn.setIconSclass("fa fa-minus");
			btn.addEventListener(Events.ON_CLICK, e -> {
				boolean match = MbomBpuType.PART_ACQ_$DEL0.match(pa);
				if (!match) {
					ZkNotification.warning("This part acquisition cannot be deleted.");
					return;
				}

				PaBpuDel0 b =  BpuFacade.getInstance().getBuilder(MbomBpuType.PART_ACQ_$DEL0, pa);
				if (b == null) {
					ZkNotification.error();
					return;
				}
				
				ZkMsgBox.confirm("Confirm delete?", () -> {
					boolean d = b.build(new StringBuilder(), new TimeTraveler());
					if (d) {
						ZkNotification.info("Delete part acuisition [" + pa.getId() + "]["
								+ pa.getName() + "] success.");
						ListModelList<PartAcqInfo> model = (ListModelList) lbxPartAcq.getModel();
						model.remove(pa);
					} else {
						ZkNotification.error();
					}
				});
			});
			lc.appendChild(btn);
			li.appendChild(lc);
			
			// id
			li.appendChild(new Listcell(pa.getId()));
			// name
			li.appendChild(new Listcell(pa.getName()));
			// type
			li.appendChild(new Listcell(pa.getTypeName()));

			// click event -> show pars
			li.addEventListener(Events.ON_CLICK, e -> {
				ListModelList<ParsInfo> parsModel = new ListModelList<>(pa.getParsList());
				lbxPars.setModel(parsModel);
			});
		};
		lbxPartAcq.setItemRenderer(paRenderer);

		/* Pars */
		ListitemRenderer<ParsInfo> parsRenderer = (li, pars, i) -> {
			Listcell lc;

			// delete
			lc = new Listcell();
			Toolbarbutton btn = new Toolbarbutton();
			btn.setIconSclass("fa fa-minus");
			btn.addEventListener(Events.ON_CLICK, e -> {
				boolean match = MbomBpuType.PARS_$DEL0.match(pars);
				if (!match) {
					ZkNotification.warning("This part acquisition routing step cannot be deleted.");
					return;
				}

				ParsBpuDel0 b = BpuFacade.getInstance().getBuilder(MbomBpuType.PARS_$DEL0, pars);
				if (b == null) {
					ZkNotification.error();
					return;
				}

				ZkMsgBox.confirm("Confirm delete?", () -> {
					boolean d = b.build(new StringBuilder(), new TimeTraveler());
					if (d) {
						ZkNotification.info("Delete part acuisition routing step [" + pars.getId() + "]["
								+ pars.getName() + "] success.");
						ListModelList<ParsInfo> model = (ListModelList) lbxPars.getModel();
						model.remove(pars);
					} else {
						ZkNotification.error();
					}
				});
			});
			lc.appendChild(btn);
			li.appendChild(lc);
			
			// id
			li.appendChild(new Listcell(pars.getId()));
			// name
			li.appendChild(new Listcell(pars.getName()));
			// desp
			li.appendChild(new Listcell(pars.getDesp()));

			// click event -> show pars
			li.addEventListener(Events.ON_CLICK, e -> {
				ListModelList<PprocInfo> pprocModel = new ListModelList<>(pars.getPprocList());
				lbxPproc.setModel(pprocModel);

				ListModelList<PpartInfo> ppartModel = new ListModelList<>(pars.getPpartList());
				lbxPpart.setModel(ppartModel);
			});

		};
		lbxPars.setItemRenderer(parsRenderer);

		/* Pproc */
		ListitemRenderer<PprocInfo> pprocRenderer = (li, pproc, i) -> {
			// seq
			li.appendChild(new Listcell(pproc.getSeq()));
			// name
			li.appendChild(new Listcell(pproc.getName()));
			// desp
			li.appendChild(new Listcell(pproc.getDesp()));
		};
		lbxPproc.setItemRenderer(pprocRenderer);

		/* Ppart */
		ListitemRenderer<PpartInfo> ppartRowRenderer = (li, ppart, i) -> {
			// part pin
			li.appendChild(new Listcell(ppart.getPartPin()));
			// part name
			li.appendChild(new Listcell(ppart.getPartName()));
			// part req qty
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(ppart.getPartReqQty(), 0)));
		};
		lbxPpart.setItemRenderer(ppartRowRenderer);

	}
	
	// -------------------------------------------------------------------------------
	private PartAcqInfo getSelectedPa() {
		ListModelList<PartAcqInfo> model = (ListModelList) lbxPartAcq.getModel();
		Iterator<PartAcqInfo> it = model.getSelection().iterator();
		return it.hasNext() ? it.next() : null;
	}

	
	// -------------------------------------------------------------------------------
	// ----------------------------------wdCreatePa-----------------------------------
	@Wire
	private Window wdCreatePa;
	@Wire("#wdCreatePa #txbId")
	private Textbox txbCreatePaId;
	@Wire("#wdCreatePa #txbName")
	private Textbox txbCreatePaName;
	@Wire("#wdCreatePa #cbbType")
	private Combobox cbbCreatePaType;

	@Listen(Events.ON_CLICK + "=#btnAddPa")
	public void btnAddPa_clicked() {
		if (part == null) {
			ZkNotification.error("Part null.");
			return;
		}
		showWdCreatePa();
	}

	private void showWdCreatePa() {
		resetWdCreatePaBlanks();
		wdCreatePa.setVisible(true);
	}

	private void resetWdCreatePaBlanks() {
		txbCreatePaId.setValue("");
		txbCreatePaName.setValue("");
		ZkUtil.initCbb(cbbCreatePaType, PartAcquisitionType.values(), false);
		cbbCreatePaType.setValue("");
	}

	@Listen(Events.ON_CLICK + "=#wdCreatePa #btnSubmit")
	public void wdCreatePa_btnSubmit_clicked() {
		if (part == null) {
			ZkNotification.warning("Please select part acquisition.");
			return;
		}

		PartAcqBuilder0 b = BpuFacade.getInstance().getBuilder(MbomBpuType.PART_ACQ_0);
		b.appendPartUid(part.getUid()).appendPartPin(part.getPin());
		b.appendId(txbCreatePaId.getValue()).appendName(txbCreatePaName.getValue());
		PartAcquisitionType paType = cbbCreatePaType.getSelectedItem() == null ? PartAcquisitionType.UNDEFINED
				: cbbCreatePaType.getSelectedItem().getValue();
		b.appendType(paType);
		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}

		ZkMsgBox.confirm("Confirm create?", () -> {
			PartAcqInfo pa = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (pa != null) {
				ZkNotification.info("Create part acquition [" + pa.getId() + "][" + pa.getName() + "]["
						+ pa.getTypeName() + "] success.");
				ListModelList<PartAcqInfo> model = (ListModelList) lbxPartAcq.getModel();
				model.add(pa);
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}

	@Listen(Events.ON_CLICK + "=#wdCreatePa #btnResetBlanks")
	public void wdCreatePa_btnResetBlanks_clicked() {
		resetWdCreatePaBlanks();
	}

	@Listen(Events.ON_CLOSE + "=#wdCreatePa")
	public void wdCreatePa_closed(Event _evt) {
		_evt.stopPropagation();
		wdCreatePa.setVisible(false);
	}

	// -------------------------------------------------------------------------------
	// ---------------------------------wdCreatePars----------------------------------
	@Wire
	private Window wdCreatePars;
	@Wire("#wdCreatePars #txbId")
	private Textbox txbCreateParsId;
	@Wire("#wdCreatePars #txbName")
	private Textbox txbCreateParsName;
	@Wire("#wdCreatePars #txbDesp")
	private Textbox txbCreateParsDesp;

	@Listen(Events.ON_CLICK + "=#btnAddPars")
	public void btnAddPars_clicked() {
		PartAcqInfo pa = getSelectedPa();
		if(pa==null) {
			ZkNotification.warning("Please select part acquisition.");
			return;
		}
		showWdCreatePars();
	}

	private void showWdCreatePars() {
		resetWdCreateParsBlanks();
		wdCreatePars.setVisible(true);
	}

	private void resetWdCreateParsBlanks() {
		txbCreateParsId.setValue("");
		txbCreateParsName.setValue("");
		txbCreateParsDesp.setValue("");
	}

	@Listen(Events.ON_CLICK + "=#wdCreatePars #btnSubmit")
	public void wdCreatePars_btnSubmit_clicked() {
		PartAcqInfo pa = getSelectedPa();
		if (pa == null) {
			ZkNotification.warning("Please select part acquisition.");
			return;
		}

		ParsBuilder0 b = BpuFacade.getInstance().getBuilder(MbomBpuType.PARS_0);
		b.appendPartAcqUid(pa.getUid());
		b.appendId(txbCreateParsId.getValue()).appendName(txbCreateParsName.getValue())
				.appendDesp(txbCreateParsDesp.getValue());
		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}

		ZkMsgBox.confirm("Confirm create?", () -> {
			ParsInfo pars = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (pars != null) {
				ZkNotification.info(
						"Create part acquition routing step [" + pars.getId() + "][" + pars.getName() + "] success.");
				ListModelList<ParsInfo> model = (ListModelList) lbxPars.getModel();
				model.add(pars);
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});

	}

	@Listen(Events.ON_CLICK + "=#wdCreatePars #btnResetBlanks")
	public void wdCreatePars_btnResetBlanks_clicked() {
		resetWdCreateParsBlanks();
	}

	@Listen(Events.ON_CLOSE + "=#wdCreatePars")
	public void wdCreatePars_closed(Event _evt) {
		_evt.stopPropagation();
		wdCreatePars.setVisible(false);
	}

	// -------------------------------------------------------------------------------
	void refreshPartInfo(PartInfo _part) {
		if (_part == null)
			return;
		
		this.part = _part;

		/* part */
		lbPin.setValue(_part.getPin());
		lbName.setValue(_part.getName());

		/* part acq */
		ListModelList<PartAcqInfo> paModel = new ListModelList<>(_part.getPaList(false));
		lbxPartAcq.setModel(paModel);
	}

}
