package ekp.web.control.zk.mbom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.Part;
import javax.swing.tree.TreeModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.bind.impl.BindTreeitemRenderer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import ekp.DebugLogMark;
import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mbom.PpartSkewer;
import ekp.mbom.MbomService;
import ekp.mbom.issue.MbomBpuType;
import ekp.mbom.issue.part.PartBpuDel0;
import ekp.mbom.issue.part.PartBpuPcAssignPa;
import ekp.mbom.issue.part.PartBuilder0;
import ekp.mbom.issue.partAcq.PaBpuDel0;
import ekp.mbom.issue.partAcq.PaBpuPublish;
import ekp.mbom.issue.partCfg.PartCfgBpuEditing;
import ekp.mbom.issue.partCfg.PartCfgBpuPublish;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartUnit;
import ekp.web.control.zk.mbom.dto.PartCfgTreeDto;
import legion.BusinessServiceFactory;
import legion.biz.BpuFacade;
import legion.biz.BpuType;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.util.TimeTraveler;
import legion.web.zk.ZkMsgBox;
import legion.web.zk.ZkNotification;
import legion.web.zk.ZkUtil;

public class PartCfgTreePageComposer extends SelectorComposer<Component> {
	public final static String URI = "/mbom/partCfgTreePage.zul";

//	private Logger log = LoggerFactory.getLogger(PartCfgTreePageComposer.class);
	private static Logger log = LoggerFactory.getLogger(DebugLogMark.class);

	public static PartCfgTreePageComposer of(Include _icd) {
		return ZkUtil.of(_icd, URI, "pnPartCfgTreePage");
	}

	// -------------------------------------------------------------------------------
	/* Part configuration list */
	@Wire
	private Button btnPcPublish;
	@Wire
	private Listbox lbxPartCfg;

	/* Part configuration tree */
	/* borderlayout-center */
	@Wire
	private Label lbSelectedPc;
	@Wire
	private Button btnAssignPartAcq, btnPaPublish;

	@Wire
	private Tree treePartCfg;

	/* borderlayout-east */
	@Wire
	private Button btnUnassignPartCfg;
	@Wire
	private Listbox lbxPpartSkewer;

	// -------------------------------------------------------------------------------
	private MbomService mbomService = BusinessServiceFactory.getInstance().getService(MbomService.class);

	private PartInfo part;

	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			init();
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	// -------------------------------------------------------------------------------
	private void init() {
		/* lbx */
		ListitemRenderer<PartCfgInfo> partRenderer = (li, pc, i) -> {
			Listcell lc;
			// delete
			lc = new Listcell();
			// TODO
//			Toolbarbutton btn = new Toolbarbutton();
//			btn.setIconSclass("fa fa-minus");
//			btn.addEventListener(Events.ON_CLICK, e -> {
//				boolean match = MbomBpuType.PART_$DEL0.match(p);
//				if (!match) {
//					ZkNotification.warning("This part cannot be deleted.");
//					return;
//				}
//				
//				PartBpuDel0 b = BpuFacade.getInstance().getBuilder(MbomBpuType.PART_$DEL0, p);
//				if (b == null) {
//					ZkNotification.error();
//					return;
//				}
//
//				ZkMsgBox.confirm("Confirm delete?", () -> {
//					boolean d = b.build(new StringBuilder(), new TimeTraveler());
//					if (d) {
//						ZkNotification.info("Delete part [" + p.getPin() + "][" + p.getName() + "] success.");
//						ListModelList<PartInfo> model = (ListModelList) lbxPart.getModel();
//						model.remove(p);
//					} else {
//						ZkNotification.error();
//					}
//
//				});
//			});
//			lc.appendChild(btn);
			li.appendChild(lc);
			//
			li.appendChild(new Listcell(pc.getId()));
			//
			li.appendChild(new Listcell(pc.getName()));
			// root part pin
			li.appendChild(new Listcell(pc.getRootPartPin()));
			//
			li.appendChild(new Listcell(pc.getStatusName()));

			//
			li.appendChild(new Listcell(pc.getDesp()));

//			// click event -> show part
//			li.addEventListener(Events.ON_CLICK, e -> {
//				fnCntProxy.refreshCntUri(PartInfoComposer.URI);
//				PartInfoComposer partComposer = fnCntProxy.getComposer(PartInfoComposer.class);
//				partComposer.refreshPartInfo(p);
//			});
		};
		lbxPartCfg.setItemRenderer(partRenderer);

		/* wdPcAssignPa */
		ListitemRenderer<PartAcqInfo> pcAssignPaPaRenderer = (li, pa, i) -> {
			Listcell lc;
			// delete
			lc = new Listcell();
			li.appendChild(lc);

			// id
			li.appendChild(new Listcell(pa.getId()));
			// name
			li.appendChild(new Listcell(pa.getName()));
			// type
			li.appendChild(new Listcell(pa.getTypeName()));
			// partCfgList
			lc = new Listcell();
			List<PartCfgInfo> partCfgList = pa.getPartCfgList(false);
			Hlayout hlayout = new Hlayout();
			for (PartCfgInfo partCfg : partCfgList) {
				A a = new A(partCfg.getId());
				hlayout.appendChild(a);
			}
			lc.appendChild(hlayout);
			li.appendChild(lc);

			//
			li.setDisabled(b == null || !b.isPaAvaible(pa));
		};
		lbxPcAssignPaPa.setItemRenderer(pcAssignPaPaRenderer);

		/* tree */
		TreeitemRenderer<DefaultTreeNode<PartCfgTreeDto>> renderer = (ti, tn, i) -> {
			Treerow tr = ti.getTreerow();
			if (tr == null) { // tree row not create yet.
				tr = new Treerow();
				ti.appendChild(tr);
			}
			PartCfgTreeDto data = tn.getData();

			// part pin
			ti.getTreerow().appendChild(new Treecell(data.getPartPin()));
			// part name
			ti.getTreerow().appendChild(new Treecell(data.getPartName()));
			// part unit
			ti.getTreerow().appendChild(new Treecell(data.getPartUnitName()));
			//
			ti.getTreerow().appendChild(new Treecell(data.getPaId()));
			ti.getTreerow().appendChild(new Treecell(data.getPaName()));
			ti.getTreerow().appendChild(new Treecell(data.getPaTypeName()));
			ti.getTreerow().appendChild(new Treecell(data.getPaStatusName()));
			// qty
			ti.getTreerow().appendChild(new Treecell(data.getPpartReqQtyDisplay()));

		};
		treePartCfg.setItemRenderer(renderer);

		/* east: ppartSkewer */
		ListitemRenderer<PpartSkewer> ppartSkewerRenderer = (li, ppart, i) -> {
			Listcell lc;
			// check
			lc = new Listcell();
			li.appendChild(lc);
			// mark
			lc = new Listcell();
			if (ppart.isRoot(getSelectedPc()))
				lc.setLabel("Root");
			else if (ppart.isOrphan(getSelectedPc()))
				lc.setLabel("Orphan");
			else
				lc.setLabel(" - ");
			li.appendChild(lc);
			// pPin
			li.appendChild(new Listcell(ppart.getpPin()));
			// pName
			li.appendChild(new Listcell(ppart.getpName()));
			// paId
			li.appendChild(new Listcell(ppart.getPaId()));
			// paName
			li.appendChild(new Listcell(ppart.getPaName()));
			// parsSeq
			li.appendChild(new Listcell(ppart.getParsSeq()));
			// parsName
			li.appendChild(new Listcell(ppart.getParsName()));
			// partPin
			li.appendChild(new Listcell(ppart.getPartPin()));
			// partName
			li.appendChild(new Listcell(ppart.getPartName()));
			// partReqQty
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(ppart.getPartReqQty(), 3)));
		};
		lbxPpartSkewer.setItemRenderer(ppartSkewerRenderer);
	}

	private PartCfgInfo getSelectedPc() {
		ListModelList<PartCfgInfo> model = (ListModelList) lbxPartCfg.getModel();
		Iterator<PartCfgInfo> it = model.getSelection().iterator();
		PartCfgInfo selectedPartCfg = it.hasNext() ? it.next() : null;
		return selectedPartCfg;
	}

	// -------------------------------------------------------------------------------
	// ----------------------------------pa_toolbar-----------------------------------
	@Listen(Events.ON_SELECT + "=#lbxPartCfg")
	public void lbxPartCfg_selected() {
		PartCfgInfo pc = getSelectedPc();
		if (pc == null) {
			ZkNotification.warning("Please select Part configuration.");
			return;
		}

		/* toggle buttons */
		togglePcToolbarButtons(pc);

		/* refresh pc */
		refreshPartCfg(pc);
	}

//	@Listen(Events.ON_SELECT+"=#lbxPpartSkewer")
//	public void lbxPpartSkewer_selected() {
//		PpartSkewer ppartSkewer = getSelectedPpartSkewer();
//		PartCfgInfo selectedPc =getSelectedPc();
//		btnUnassignPartCfg.setDisabled(!ppartSkewer.isOrphan(selectedPc));
//	}

	private void togglePcToolbarButtons(PartCfgInfo _partCfg) {
		if (_partCfg == null) {
			btnPcPublish.setDisabled(true);
		}

		btnPcPublish.setDisabled(!MbomBpuType.PART_CFG_$PUBLISH.match(_partCfg));
	}

	private void refreshPartCfg(PartCfgInfo _partCfg) {
		refreshPartCfgAbstract(_partCfg);
		refreshPartCfgTree(_partCfg);
		refreshPartCfgPpartSkewer(_partCfg);
	}

	@Listen(Events.ON_CLICK + "=#btnPcNew")
	public void btnPcNew_clicked() {
		ZkNotification.warning("working in progress...");
		// TODO
	}

	@Listen(Events.ON_CLICK + "=#btnPcPublish")
	public void btnPcPublish_clicked() {
		PartCfgInfo pc = getSelectedPc();
		if (pc == null) {
			ZkNotification.warning("Please select a part configuration.");
			return;
		}

		boolean match = MbomBpuType.PART_CFG_$PUBLISH.match(pc);
		if (!match) {
			ZkNotification.warning("This part configuration cannot be published.");
			return;
		}

		PartCfgBpuPublish b = BpuFacade.getInstance().getBuilder(MbomBpuType.PART_CFG_$PUBLISH, pc);
		if (b == null) {
			ZkNotification.error();
			return;
		}
		
		StringBuilder msg = new StringBuilder();
		if(!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}
		
		ZkMsgBox.confirm("Confirm publish?", () -> {
			boolean d = b.build(new StringBuilder(), new TimeTraveler());
			if (d) {
				ZkNotification.info("Publish part configuration [" + pc.getId() + "][" + pc.getName() + "] success.");
				refreshPart(part.reload());
				togglePcToolbarButtons(null);
				toggleTreePartCfgToolbar(null);
			} else {
				ZkNotification.error();
			}
		});
	}

	// -------------------------------------------------------------------------------
	private TreeNode<PartCfgTreeDto> getSelectedPartCfgTreeNode() {
		DefaultTreeModel<PartCfgTreeDto> model = (DefaultTreeModel) treePartCfg.getModel();
		if (model == null)
			return null;
		Iterator<TreeNode<PartCfgTreeDto>> it = model.getSelection().iterator();
		return it.hasNext() ? it.next() : null;
	}

	private void toggleTreePartCfgToolbar(PartCfgTreeDto _dto) {
		if (_dto == null) {
			btnAssignPartAcq.setDisabled(true);
			btnPaPublish.setDisabled(true);
			return;
		}

		btnAssignPartAcq.setDisabled(!MbomBpuType.PART_$PC_ASSIGN_PA.match(_dto.getP(), getSelectedPc()));
		btnPaPublish.setDisabled(!MbomBpuType.PART_ACQ_$PUBLISH.match(_dto.getPa()));
	}

	@Listen(Events.ON_SELECT + "=#treePartCfg")
	public void treePartCfg_selected() {
		TreeNode<PartCfgTreeDto> tn = getSelectedPartCfgTreeNode();
		PartCfgTreeDto dto = tn.getData();
		toggleTreePartCfgToolbar(dto);
	}

	@Listen(Events.ON_CLICK + "=#btnAssignPartAcq")
	public void btnAssignPartAcq_clicked() {
		if (getSelectedPartCfgTreeNode() == null) {
			ZkNotification.info("Please select a part pin in tree.");
			return;
		}
		showWdPcAssignPa();
	}

	// -------------------------------------------------------------------------------
	@Wire
	private Window wdPcAssignPa;
	@Wire("#wdPcAssignPa #lbxPa")
	private Listbox lbxPcAssignPaPa;

	private PartBpuPcAssignPa b;

	private void showWdPcAssignPa() {
		log.debug("test showWdPcAssignPa");
		resetWdPcAssignPa();
		wdPcAssignPa.setVisible(true);
	}

	private void resetWdPcAssignPa() {
		TreeNode<PartCfgTreeDto> tn = getSelectedPartCfgTreeNode();
		PartInfo p = tn.getData().getP();
		PartCfgInfo pc = getSelectedPc();
		b = BpuFacade.getInstance().getBuilder(MbomBpuType.PART_$PC_ASSIGN_PA, p, pc);

		//
//		List<PartAcqInfo> paList = b.getAvailablePaList();
		List<PartAcqInfo> paList = b.getAllPaList();
		ListModelList<PartAcqInfo> model = new ListModelList<>(paList);
		lbxPcAssignPaPa.setModel(model);
	}

	@Listen(Events.ON_CLICK + "=#wdPcAssignPa #btnSubmit")
	public void wdPcAssignPa_btnSubmit_clicked() {
		log.debug("wdPcAssignPa_btnSubmit_clicked");
		ListModelList<PartAcqInfo> model = (ListModelList) lbxPcAssignPaPa.getModel();
		Iterator<PartAcqInfo> it = model.getSelection().iterator();
		PartAcqInfo selectedPa = it.hasNext() ? it.next() : null;
		b.appendPa(selectedPa);
		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}

		ZkMsgBox.confirm("Confirm create?", () -> {
			boolean run = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (run) {
				ZkNotification.info("Assign part configuration [" + b.getPc().getId() + "] to acquisition ["
						+ b.getPa().getId() + "] sccuess.");

				TreeNode<PartCfgTreeDto> tn = getSelectedPartCfgTreeNode();
				tn.getData().reloadPa(b.getPc());
				refreshPartCfg(b.getPc());
				wdPcAssignPa_closed(new Event("evt"));
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}

	@Listen(Events.ON_CLOSE + "=#wdPcAssignPa")
	public void wdPcAssignPa_closed(Event _evt) {
		_evt.stopPropagation();
		wdPcAssignPa.setVisible(false);
	}

	// -------------------------------------------------------------------------------
	@Listen(Events.ON_CLICK + "=#btnPaPublish")
	public void btnPaPublish_clicked() {
		if (getSelectedPartCfgTreeNode() == null) {
			ZkNotification.info("Please select a part pin in tree.");
			return;
		}
		
		TreeNode<PartCfgTreeDto>  tn =getSelectedPartCfgTreeNode();
		if(tn==null) {
			ZkNotification.warning("Please select a part configuration tree node.");
			return;
		}
		
		PartAcqInfo pa = tn.getData().getPa();
		if(pa==null) {
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
				tn.getData().reloadPa();
				// DefaultTreeModel<TreeNode<PartCfgTreeDto>> model = (DefaultTreeModel)treePartCfg.getModel();
				// TODO model的狀態還不會及時更新
			} else {
				ZkNotification.error();
			}
		});
	}
	
	// -------------------------------------------------------------------------------
	private PpartSkewer getSelectedPpartSkewer() {
		ListModelList<PpartSkewer> model = (ListModelList) lbxPpartSkewer.getModel();
		Iterator<PpartSkewer> it = model.getSelection().iterator();
		PpartSkewer selectedPpartSkewer = it.hasNext() ? it.next() : null;
		return selectedPpartSkewer;
	}

	@Listen(Events.ON_SELECT + "=#lbxPpartSkewer")
	public void lbxPpartSkewer_selected() {
		PpartSkewer ppartSkewer = getSelectedPpartSkewer();
		PartCfgInfo selectedPc = getSelectedPc();
		btnUnassignPartCfg.setDisabled(!ppartSkewer.isOrphan(selectedPc));
	}

	@Listen(Events.ON_CLICK + "=#btnUnassignPartCfg")
	public void btnUnassignPartCfg_clicked() {

		PartCfgInfo pc = getSelectedPc();
		PartCfgBpuEditing b = BpuFacade.getInstance().getBuilder(MbomBpuType.PART_CFG_$EDITING, pc);
		PpartSkewer ppartSkewer = getSelectedPpartSkewer();
		PartAcqInfo unassingedPa = ppartSkewer.getPa(true);
		b.appendUnassignedPartAcq(unassingedPa);

		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			ZkMsgBox.exclamation(msg.toString());
			return;
		}

		log.debug("getSelectedPartCfgTreeNode(): {}", getSelectedPartCfgTreeNode());

		ZkMsgBox.confirm("Confirm unassign PartAcq [" + unassingedPa.getId() + "]?", () -> {
			boolean run = b.build(new StringBuilder(), new TimeTraveler());
			// 成功
			if (run) {
				ZkNotification.info("Unassign part acquisition [" + unassingedPa.getId() + "] from configuration ["
						+ pc.getId() + "] sccuess.");
				// FIXME
				log.debug("success"); // FIXME

//				TreeNode<PartCfgTreeDto> tn = getSelectedPartCfgTreeNode();
//				log.debug("tn: {}", tn);
				part = part.reload();
				refreshPart(part);
//				PartCfgInfo pcNew = pc.reload();
//				log.debug("pcNew: {}", pcNew);
//				tn.getData().reloadPa(pcNew);
//				refreshPartCfg(pcNew);
//				wdPcAssignPa_closed(new Event("evt"));
			}
			// 失敗
			else {
				ZkNotification.error();
			}
		});
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------
	// TODO
	public void refreshPart(PartInfo _part) {
		this.part = _part;
//		List<PartCfgInfo> partCfgList = part.getRootPartCfgList(false);
		List<PartCfgInfo> partCfgList = part.getReferencedPartCfgList(false);
		ListModelList<PartCfgInfo> model = new ListModelList<>(partCfgList);
		lbxPartCfg.setModel(model);
	}

	private void refreshPartCfgAbstract(PartCfgInfo _partCfg) {
		if (_partCfg == null)
			lbSelectedPc.setValue("");
		else
			lbSelectedPc.setValue(_partCfg.getId() + "\t" + _partCfg.getName());
	}

	private void refreshPartCfgTree(PartCfgInfo _partCfg) {
		toggleTreePartCfgToolbar(null);

		if (_partCfg == null) {
			treePartCfg.setModel(null);
			return;
		}

//		PartCfgTreeDto partCfgTreeDto = PartCfgTreeDto.of(_partCfg);
		PartCfgTreeDto partCfgTreeDto = PartCfgTreeDto.of(_partCfg, part);
		DefaultTreeNode<PartCfgTreeDto> rootTn = partCfgTreeDto.packRootTreeNode4Model();
		DefaultTreeModel<PartCfgTreeDto> model = new DefaultTreeModel<>(rootTn);
		treePartCfg.setModel(model);

		// 全展開
		List<TreeNode<PartCfgTreeDto>> list = new ArrayList<>();
		appendAllBranchTreeNodes(list, rootTn);

		// 預設展第1階(root的children才是我們認定的第1階，實際上是model的第2階。)
//		TreeNode<PartCfgTreeDto> tn  = rootTn;
//		do {
//			model.addOpenObject(tn);
//			tn
//		}
////		while(!tn.isLeaf()) {
//			
////		}
//		
//		model.get
		for (TreeNode<PartCfgTreeDto> tn : list) {
//			log.debug("tn.getData().getP().getPin(): {}", tn.getData().getP().getPin());
			model.addOpenObject(tn);
		}
	}

	private void appendAllBranchTreeNodes(List<TreeNode<PartCfgTreeDto>> _list, TreeNode<PartCfgTreeDto> _tn) {
		if (_tn.getChildCount() > 0) {
			_list.add(_tn);
			for (TreeNode<PartCfgTreeDto> _childTn : _tn.getChildren())
				appendAllBranchTreeNodes(_list, _childTn);
		}
	}

	private void refreshPartCfgPpartSkewer(PartCfgInfo _partCfg) {
		if (_partCfg == null) {
			lbxPpartSkewer.setModel(new ListModelList<>());
			return;
		}

		List<PpartSkewer> ppartSkewerList = _partCfg.getPpartSkewerList(false);
		ListModelList<PpartSkewer> model = new ListModelList<>(ppartSkewerList);
		lbxPpartSkewer.setModel(model);
	}
}
