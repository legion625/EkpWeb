package ekp.web.control.zk.mbom;

import java.util.Iterator;
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
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import ekp.DebugLogMark;
import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mbom.PprocInfo;
import ekp.mbom.MbomService;
import ekp.mbom.issue.MbomBpuType;
import ekp.mbom.issue.parsPart.ParsPartBuilder1;
import ekp.mbom.issue.parsPart.PpartBpuDel0;
import ekp.mbom.issue.part.PartBpuDel0;
import ekp.mbom.issue.part.PartBpuPcAssignPa;
import ekp.mbom.issue.partAcq.PaBpuDel0;
import ekp.mbom.issue.partAcq.PaBpuPublish;
import ekp.mbom.issue.partAcq.PaBpuUpdateRefUnitCost;
import ekp.mbom.issue.partAcq.PartAcqBuilder0;
import ekp.mbom.issue.partAcqRoutingStep.ParsBpuDel0;
import ekp.mbom.issue.partAcqRoutingStep.ParsBuilder1;
import ekp.mbom.type.PartAcqStatus;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartCfgStatus;
import legion.BusinessServiceFactory;
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
//	private Logger log = LoggerFactory.getLogger(DebugLogMark.class);

	// -------------------------------------------------------------------------------
	/* Part */
	@Wire
	private Label lbPin, lbName, lbUnit;
	
	/* PartAcq */
	@Wire
	private Button btnPaPublish, btnPaDelete;

	@Wire
	private Listbox lbxPartAcq;

	/* Pars */
	@Wire
	private Button btnParsNew, btnParsDelete;
	@Wire
	private Listbox lbxPars;

	/* Ppart */
	@Wire
	private Button btnPpartNew, btnPpartDelete;
	@Wire
	private Listbox lbxPpart;

	/* Pproc */
	@Wire
	private Listbox lbxPproc;
	
	@Wire
	private Include icdPartAcqPage;
	private PartCfgTreePageComposer partCfgTreePageComposer;
	
	// -------------------------------------------------------------------------------
	private FnCntProxy fnCntProxy;

	private MbomService mbomService = BusinessServiceFactory.getInstance().getService(MbomService.class);
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
			// checkmark
			li.appendChild(new Listcell());
			// id
			li.appendChild(new Listcell(pa.getId()));
			// name
			li.appendChild(new Listcell(pa.getName()));
			// type
			li.appendChild(new Listcell(pa.getTypeName()));
			// status
			li.appendChild(new Listcell(pa.getStatusName()));
			// ref unit cost
			lc = new Listcell();
			Doublebox dbbRefUnitCost = new Doublebox(pa.getRefUnitCost());
			dbbRefUnitCost.setInplace(true);
			dbbRefUnitCost.addEventListener(Events.ON_CHANGE, e -> {
				PaBpuUpdateRefUnitCost b = BpuFacade.getInstance()
						.getBuilder(MbomBpuType.PART_ACQ_$UPDATE_REF_UNIT_COST, pa);
				double newRefUnitCost = dbbRefUnitCost.getValue() == null ? 0 : dbbRefUnitCost.getValue();
				b.appendRefUnitCost(newRefUnitCost);
				StringBuilder msg = new StringBuilder();
				if (!b.verify(msg)) {
					ZkMsgBox.exclamation(msg.toString());
					return;
				}

				if (b.build(new StringBuilder(), null)) {
					ZkNotification.info("Update part acquition referenced unit cost  [" + pa.getId() + "]["
							+ pa.getName() + "][" + newRefUnitCost + "] success.");
					ListModelList<PartAcqInfo> model = (ListModelList) lbxPartAcq.getModel();

					model.add(model.indexOf(pa), pa.reload());
					model.remove(pa);
					part.getPaList(true); // reload
				} else {
					ZkNotification.error();
				}
			});
			lc.appendChild(dbbRefUnitCost);
			li.appendChild(lc);
			//  partCfgList
			lc = new Listcell();
			List<PartCfgInfo> partCfgList = pa.getPartCfgList(false);
			Hlayout hlayout = new Hlayout();
			for (PartCfgInfo partCfg : partCfgList) {
				A a = new A(partCfg.getId());
				hlayout.appendChild(a);
			}
			lc.appendChild(hlayout);
			li.appendChild(lc);
		};
		lbxPartAcq.setItemRenderer(paRenderer);

		/* pa-referenced pc */
		ListitemRenderer<PartCfgInfo> referencedPcRenderer = (li, pc, i)->{
			//
			li.appendChild(new Listcell());
			// id
			li.appendChild(new Listcell(pc.getId()));
			// name
			li.appendChild(new Listcell(pc.getName()));
			// root part pin
			li.appendChild(new Listcell(pc.getRootPartPin()));
			// status
			li.appendChild(new Listcell(pc.getStatusName()));
			// description
			li.appendChild(new Listcell(pc.getDesp()));

			/**/
			if (PartCfgStatus.PUBLISHED == pc.getStatus())
				li.setDisabled(true);
			else if (getSelectedPa().getPartCfgConj(pc.getUid(), false) != null) {
				li.setDisabled(true);
			} else
				li.setDisabled(false);
		};
		lbxAssignPcPc.setItemRenderer(referencedPcRenderer);

		/* Pars */
		ListitemRenderer<ParsInfo> parsRenderer = (li, pars, i) -> {
			// checkmark
			li.appendChild(new Listcell());
			// id
			li.appendChild(new Listcell(pars.getSeq()));
			// name
			li.appendChild(new Listcell(pars.getName()));
			// desp
			li.appendChild(new Listcell(pars.getDesp()));
		};
		lbxPars.setItemRenderer(parsRenderer);
		
		/* Ppart */
		ListitemRenderer<PpartInfo> ppartRowRenderer = (li, ppart, i) -> {
			// delete
			li.appendChild(new Listcell());
			// part pin
			li.appendChild(new Listcell(ppart.getPartPin()));
			// part name
			li.appendChild(new Listcell(ppart.getPartName()));
			// part req qty
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(ppart.getPartReqQty(), 3)));
		};
		lbxPpart.setItemRenderer(ppartRowRenderer);

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
		
		/* partCfgTreePage */
		partCfgTreePageComposer = PartCfgTreePageComposer.of(icdPartAcqPage);
	}

	// -------------------------------------------------------------------------------
	// ----------------------------------pa_toolbar-----------------------------------
	@Listen(Events.ON_SELECT + "=#lbxPartAcq")
	public void lbxPartAcq_selected() {
		PartAcqInfo pa = getSelectedPa();

		/* toggle buttons */
		togglePaToolbarButtons(pa);

		/* refresh pars */
		ListModelList<ParsInfo> parsModel = new ListModelList<>(pa.getParsList());
		lbxPars.setModel(parsModel);
	}

	private void togglePaToolbarButtons(PartAcqInfo _pa) {
		if (_pa == null) {
			btnPaPublish.setDisabled(true);
			btnPaDelete.setDisabled(true);
			btnParsNew.setDisabled(true);
			return;
		}

		btnPaPublish.setDisabled(!MbomBpuType.PART_ACQ_$PUBLISH.match(_pa));
		btnPaDelete.setDisabled(!MbomBpuType.PART_ACQ_$DEL0.match(_pa));

		// ->pars
		btnParsNew.setDisabled(!MbomBpuType.PARS_1.match(_pa));
	}
	
	private PartAcqInfo getSelectedPa() {
		ListModelList<PartAcqInfo> model = (ListModelList) lbxPartAcq.getModel();
		Iterator<PartAcqInfo> it = model.getSelection().iterator();
		return it.hasNext() ? it.next() : null;
	}
	
	private PartCfgInfo geAssignPcSelected() {
		ListModelList<PartCfgInfo> model =(ListModelList) lbxAssignPcPc.getModel();
		Iterator<PartCfgInfo> it = model.getSelection().iterator();
		return it.hasNext() ? it.next() : null;
	}


	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	/* new pa */
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
				part.getPaList(true); // reload
				wdCreatePa_closed(new Event("evt"));
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
	/* Assign part configuration */
	@Wire
	private Window wdAssignPc;
	@Wire("#wdAssignPc #lbxPc")
	private Listbox lbxAssignPcPc;
	
	@Listen(Events.ON_CLICK + "=#btnAssignPc")
	public void btnAssignPc_clicked() {
		PartAcqInfo pa = getSelectedPa();
		if (pa == null) {
			ZkNotification.warning("Please select a part acquisition.");
			return;
		}
		
		showWdAssignPc(pa);
	}

	private void showWdAssignPc(PartAcqInfo _pa) {
		resetWdAssignPc(_pa);
		wdAssignPc.setVisible(true);
	}

	private void resetWdAssignPc(PartAcqInfo _pa) {
		List<PartCfgInfo> pcList = mbomService.loadPartCfgList();
		ListModelList<PartCfgInfo> model = new ListModelList<>(pcList);
		lbxAssignPcPc.setModel(model);
	}
	
	@Listen(Events.ON_CLICK + "=#wdAssignPc #btnSubmit")
	public void wdAssignPc_btnSubmit_clicked() {
		PartCfgInfo pc = geAssignPcSelected();
		PartBpuPcAssignPa b = BpuFacade.getInstance().getBuilder(MbomBpuType.PART_$PC_ASSIGN_PA, part, pc);
		PartAcqInfo pa =getSelectedPa() ;
		b.appendPa(pa);
		
		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}

		ZkMsgBox.confirm("Confirm assign?", () -> {
			boolean result = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (result) {
				ZkNotification.info(
						"Assign part acquition [" + pa.getId() + "][" + pa.getName() + "] to part configuration ["+pc.getId()+"]["+pc.getName()+"] success.");
				log.debug("pa.getPartCfgConjList(false).size(): {}", pa.getPartCfgConjList(false).size());
				log.debug("part: {}", part);
				log.debug("pa.getPartCfgConjList(false).size(): {}", pa.getPartCfgConjList(false).size());
				refreshPartInfo(part.reload());
				wdAssignPc_closed(new Event("evt"));
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}
	

	@Listen(Events.ON_CLOSE + "=#wdAssignPc")
	public void wdAssignPc_closed(Event _evt) {
		_evt.stopPropagation();
		wdAssignPc.setVisible(false);
	}

	// -------------------------------------------------------------------------------
	@Listen(Events.ON_CLICK + "=#btnPaPublish")
	public void btnPaPublish_clicked() {
		PartAcqInfo pa = getSelectedPa();
		if (pa == null) {
			ZkNotification.warning("Please select a part acquisition.");
			return;
		}

		boolean match = MbomBpuType.PART_ACQ_$PUBLISH.match(pa);
		if (!match) {
			ZkNotification.warning("This part acquisition cannot be published.");
			return;
		}

		PaBpuPublish b = BpuFacade.getInstance().getBuilder(MbomBpuType.PART_ACQ_$PUBLISH, pa);
		if (b == null) {
			ZkNotification.error();
			return;
		}

		ZkMsgBox.confirm("Confirm publish?", () -> {
			boolean d = b.build(new StringBuilder(), new TimeTraveler());
			if (d) {
				ZkNotification.info("Publish part acuisition [" + pa.getId() + "][" + pa.getName() + "] success.");
				refreshPartInfo(part.reload()); // reload
				togglePaToolbarButtons(null);
				toggleParsToolbarButtons(null);
				togglePpartToolbarButtons(null);
			} else {
				ZkNotification.error();
			}
		});
	}

	// -------------------------------------------------------------------------------
	@Listen(Events.ON_CLICK + "=#btnPaDelete")
	public void btnPaDelete_clicked() {
		PartAcqInfo pa = getSelectedPa();
		if (pa == null) {
			ZkNotification.warning("Please select a part acquisition.");
			return;
		}
		
		boolean match = MbomBpuType.PART_ACQ_$DEL0.match(pa);
		if (!match) {
			ZkNotification.warning("This part acquisition cannot be deleted.");
			return;
		}

		PaBpuDel0 b = BpuFacade.getInstance().getBuilder(MbomBpuType.PART_ACQ_$DEL0, pa);
		if (b == null) {
			ZkNotification.error();
			return;
		}
		
		StringBuilder msg = new StringBuilder();
		if(!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}

		ZkMsgBox.confirm("Confirm delete?", () -> {
			boolean d = b.build(new StringBuilder(), new TimeTraveler());
			if (d) {
				ZkNotification
						.info("Delete part acuisition [" + pa.getId() + "][" + pa.getName() + "] success.");
				ListModelList<PartAcqInfo> model = (ListModelList) lbxPartAcq.getModel();
				model.remove(pa);
				part.getPaList(true); // reload
			} else {
				ZkNotification.error();
			}
		});
	}
	
	
	// -------------------------------------------------------------------------------
	// ---------------------------------pars_toolbar----------------------------------
	@Listen(Events.ON_SELECT + "=#lbxPars")
	public void lbxPars_selected() {
		ParsInfo pars = getSelectedPars();

		/* toggle buttons */
		toggleParsToolbarButtons(pars);

		/* refresh ppart */
		ListModelList<PpartInfo> ppartModel = new ListModelList<>(pars.getPpartList());
		lbxPpart.setModel(ppartModel);

		/* refresh pproc */
		ListModelList<PprocInfo> pprocModel = new ListModelList<>(pars.getPprocList());
		lbxPproc.setModel(pprocModel);
	}

	private void toggleParsToolbarButtons(ParsInfo _pars) {
		if (_pars == null) {
			btnParsDelete.setDisabled(true);
			btnPpartNew.setDisabled(true);
			return;
		}
		
//		btnParsNew // TODO
		btnParsDelete.setDisabled(!MbomBpuType.PARS_$DEL0.match(_pars));
		
		// ->ppart
		btnPpartNew.setDisabled(!MbomBpuType.PARS_PART_1.match(_pars));
	}

	private ParsInfo getSelectedPars() {
		ListModelList<ParsInfo> model = (ListModelList) lbxPars.getModel();
		Iterator<ParsInfo> it = model.getSelection().iterator();
		return it.hasNext() ? it.next() : null;
	}
	
	// -------------------------------------------------------------------------------
	/* new pars */
	@Wire
	private Window wdCreatePars;
	@Wire("#wdCreatePars #txbId")
	private Textbox txbCreateParsId;
	@Wire("#wdCreatePars #txbName")
	private Textbox txbCreateParsName;
	@Wire("#wdCreatePars #txbDesp")
	private Textbox txbCreateParsDesp;

	@Listen(Events.ON_CLICK + "=#btnParsNew")
	public void btnParsNew_clicked() {
		PartAcqInfo pa = getSelectedPa();
		if (pa == null) {
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

//		ParsBuilder0 b = BpuFacade.getInstance().getBuilder(MbomBpuType.PARS_0);
		ParsBuilder1 b = BpuFacade.getInstance().getBuilder(MbomBpuType.PARS_1, pa);
//		b.appendPartAcqUid(pa.getUid());
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
						"Create part acquition routing step [" + pars.getSeq() + "][" + pars.getName() + "] success.");
				ListModelList<ParsInfo> model = (ListModelList) lbxPars.getModel();
				model.add(pars);
				pa.getParsList(true); // reload
				wdCreatePars_closed(new Event("evt"));
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
	@Listen(Events.ON_CLICK + "=#btnParsDelete")
	public void btnParsDelete_clicked() {
		ParsInfo pars = getSelectedPars();
		if (pars == null) {
			ZkNotification.warning("Please select a routing step.");
			return;
		}

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
				ZkNotification.info(
						"Delete part acuisition routing step [" + pars.getSeq() + "][" + pars.getName() + "] success.");
				ListModelList<ParsInfo> model = (ListModelList) lbxPars.getModel();
				model.remove(pars);
				getSelectedPa().getParsList(true); // reload
			} else {
				ZkNotification.error();
			}
		});
	}
	
	
	// -------------------------------------------------------------------------------
	// ---------------------------------ppart_toolbar---------------------------------
	@Listen(Events.ON_SELECT + "=#lbxPpart")
	public void lbxPpart_selected() {
		PpartInfo ppart = getSelectedPpart();

		/* toggle buttons */
		togglePpartToolbarButtons(ppart);
	}

	private void togglePpartToolbarButtons(PpartInfo _ppart) {
		if (_ppart == null) {
			btnPpartDelete.setDisabled(true);
			return;
		}
		
		btnPpartDelete.setDisabled(!MbomBpuType.PPART_$DEL0.match(_ppart));
	}

	private PpartInfo getSelectedPpart() {
		ListModelList<PpartInfo> model = (ListModelList) lbxPpart.getModel();
		Iterator<PpartInfo> it = model.getSelection().iterator();
		return it.hasNext() ? it.next() : null;
	}
	
	
	
	// -------------------------------------------------------------------------------
	/* new ppart */
	@Wire
	private Window wdCreatePpart;
	@Wire("#wdCreatePpart #lbxPart")
	private Listbox lbxCreatePpartPart;
	@Wire("#wdCreatePpart #lbSelPart")
	private Label lbCreatePpartSelPart;
	@Wire("#wdCreatePpart #dbbReqQty")
	private Doublebox dbbCreatePpartReqQty;

	@Listen(Events.ON_CLICK + "=#btnPpartNew")
	public void btnPpartNew_clicked() {
		if (getSelectedPars() == null) {
			ZkNotification.warning("Please select part acquisition routing step.");
			return;
		}
		showWdCreatePpart();
	}

	private void showWdCreatePpart() {
		resetWdCreatePpartBlanks();
		wdCreatePpart.setVisible(true);
	}

	private void resetWdCreatePpartBlanks() {
		/**/
		ParsInfo pars = getSelectedPars();
		List<PpartInfo> ppartList = pars.getPpartList(true);

		ListitemRenderer<PartInfo> partRowRenderer = (li, part, i) -> {
			// pin
			li.appendChild(new Listcell(part.getPin()));
			// name
			li.appendChild(new Listcell(part.getName()));

			//
			boolean match = ppartList.stream().anyMatch(ppart -> ppart.getPartUid().equals(part.getUid()));
			li.setDisabled(match);
		};
		lbxCreatePpartPart.setItemRenderer(partRowRenderer);

		//
		ListModelList<PartInfo> model = new ListModelList<>(mbomService.loadPartList());
		lbxCreatePpartPart.setModel(model);

		/**/
		lbCreatePpartSelPart.setValue(null);
		dbbCreatePpartReqQty.setValue(null);
	}

	private PartInfo getCreatePpartSelectedPart() {
		ListModelList<PartInfo> model = (ListModelList) lbxCreatePpartPart.getListModel();
		Iterator<PartInfo> it = model.getSelection().iterator();
		return it.hasNext() ? it.next() : null;
	}
	
	@Listen(Events.ON_SELECT + "=#wdCreatePpart #lbxPart")
	public void wdCreatePpart_lbxPart_selected() {
		PartInfo selPart = getCreatePpartSelectedPart();
		lbCreatePpartSelPart.setValue(selPart.getPin() + "\t" + selPart.getName()+"\t"+selPart.getUnitName());
	}

	@Listen(Events.ON_CLICK + "=#wdCreatePpart #btnSubmit")
	public void wdCreatePpart_btnSubmit_clicked() {
		ParsInfo pars = getSelectedPars();

		if (pars == null) {
			ZkNotification.warning("Please select target part acquisition routing step.");
			return;
		}

		ParsPartBuilder1 b = BpuFacade.getInstance().getBuilder(MbomBpuType.PARS_PART_1, getSelectedPars());
		PartInfo selPart = getCreatePpartSelectedPart();
		if (selPart != null)
			log.debug("selPart: {}\t{}", selPart.getPin(), selPart.getName());
		else
			log.debug("selPart: null");
		b.appendPart(selPart);
		Double reqQty = dbbCreatePpartReqQty.getValue();
		b.appendPartReqQty(reqQty == null ? 0 : reqQty);

		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}

		ZkMsgBox.confirm("Confirm create?", () -> {
			PpartInfo ppart = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (ppart != null) {
				ZkNotification.info("Create routing step part [" + ppart.getPartPin() + "][" + ppart.getPartName()
						+ "][" + ppart.getPartReqQty() + "] success.");
				ListModelList<PpartInfo> model = (ListModelList) lbxPpart.getModel();
				model.add(ppart);
				pars.getPpartList(true); // reload
				wdCreatePpart_closed(new Event("evt"));
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}

	@Listen(Events.ON_CLICK + "=#wdCreatePpart #btnResetBlanks")
	public void wdCreatePpart_btnResetBlanks_clicked() {
		resetWdCreatePpartBlanks();
	}

	@Listen(Events.ON_CLOSE + "=#wdCreatePpart")
	public void wdCreatePpart_closed(Event _evt) {
		_evt.stopPropagation();
		wdCreatePpart.setVisible(false);
	}

	// -------------------------------------------------------------------------------
	@Listen(Events.ON_CLICK + "=#btnPpartDelete")
	public void btnPpartDelete_clicked() {
		PpartInfo ppart = getSelectedPpart();
		if (ppart == null) {
			ZkNotification.warning("Please select a routing step part.");
			return;
		}
		
		boolean match = MbomBpuType.PPART_$DEL0.match(ppart);
		if (!match) {
			ZkNotification.warning("This routing step part cannot be deleted.");
			return;
		}
		PpartBpuDel0 b = BpuFacade.getInstance().getBuilder(MbomBpuType.PPART_$DEL0, ppart);
		if (b == null) {
			ZkNotification.error();
			return;
		}

		ZkMsgBox.confirm("Confirm delete?", () -> {
			boolean d = b.build(new StringBuilder(), new TimeTraveler());
			if (d) {
				ZkNotification.info("Delete routing step part [" + ppart.getPartPin() + "]["
						+ ppart.getPartName() + "] success.");
				ListModelList<PpartInfo> model = (ListModelList) lbxPpart.getModel();
				model.remove(ppart);
				getSelectedPars().getPpartList(true); // reload
			} else {
				ZkNotification.error();
			}
		});
	}
	
	
	// -------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------
	void refreshPartInfo(PartInfo _part) {
		if (_part == null)
			return;

		this.part = _part;

		/* part */
		lbPin.setValue(_part.getPin());
		lbName.setValue(_part.getName());
		lbUnit.setValue(_part.getUnitName());

		/* part acq */
		refreshPa(_part.getPaList(false));

		/* Part cfg */
		partCfgTreePageComposer.refreshPart(_part);
	}

	private void refreshPa(List<PartAcqInfo> _paList) {
		ListModelList<PartAcqInfo> paModel = new ListModelList<>(_paList);
		lbxPartAcq.setModel(paModel);
	} 

}
