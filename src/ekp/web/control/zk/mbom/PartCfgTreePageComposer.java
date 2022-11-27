package ekp.web.control.zk.mbom;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.bind.impl.BindTreeitemRenderer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import ekp.DebugLogMark;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.mbom.MbomService;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartUnit;
import legion.BusinessServiceFactory;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.web.zk.ZkUtil;

public class PartCfgTreePageComposer extends SelectorComposer<Component> {
	public final static String URI = "/mbom/partCfgTreePage.zul";

//	private Logger log = LoggerFactory.getLogger(PartCfgTreePageComposer.class);
	private static Logger log = LoggerFactory.getLogger(DebugLogMark.class);

	public static PartCfgTreePageComposer of(Include _icd) {
		return ZkUtil.of(_icd, URI, "pnPargCfgTreePage");
	}

	// -------------------------------------------------------------------------------
	@Wire
	private Tree treePartCfg;

	@Wire
	private Tree tree2;
	
	@Wire
	private Tree tree3;

	// -------------------------------------------------------------------------------
	private MbomService mbomService = BusinessServiceFactory.getInstance().getService(MbomService.class);

	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			log.debug("PartCfgTreePageComposer::doAfterCompose");
			PartCfgInfo partCfg = mbomService.loadPartCfgById("MTW");
			log.debug("partCfg: {}\t{}\t{}", partCfg.getId(), partCfg.getRootPartPin(),
					partCfg.getRootPart().getName());
			init1(partCfg);
			init2(partCfg);
			init3(partCfg);
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	// -------------------------------------------------------------------------------
	public void init1(PartCfgInfo _partCfg) {
		TreeitemRenderer<DefaultTreeNode> renderer = (ti, node, i) -> {
			Treerow tr = ti.getTreerow();
			if (tr == null) { // tree row not create yet.
				log.debug("create tree row");
				tr = new Treerow();
				ti.appendChild(tr);
			}

			if (node.getData() instanceof PartInfo) {
				log.debug("render part");
				PartInfo p = (PartInfo) node.getData();
				// pin
				ti.getTreerow().appendChild(new Treecell(p.getPin()));
				// name
				ti.getTreerow().appendChild(new Treecell(p.getName()));
				// unit
				ti.getTreerow().appendChild(new Treecell(p.getUnitName()));
				// qty
				ti.getTreerow().appendChild(new Treecell());
			} else if (node.getData() instanceof PpartInfo) {
				log.debug("render ppart");
				PpartInfo ppart = (PpartInfo) node.getData();
				// pin
				ti.getTreerow().appendChild(new Treecell(ppart.getPartPin()));
				// name
				ti.getTreerow().appendChild(new Treecell(ppart.getPartName()));
				// unit
				ti.getTreerow().appendChild(new Treecell(ppart.getPart().getUnitName()));
				// qty
				ti.getTreerow().appendChild(new Treecell(NumberFormatUtil.getDecimalString(ppart.getPartReqQty(), 3)));
			}

		};
		treePartCfg.setItemRenderer(renderer);

//		TreeNode< PartInfo> tn = new TreeNode<PartInfo>() {
//		};

		PartInfo rootPart = _partCfg.getRootPart();
		List<PpartInfo> ppartChildern = rootPart.getPpartChildren(_partCfg);
		log.debug("ppartChildern.size(): {}", ppartChildern.size());
		List<DefaultTreeNode<PpartInfo>> children = ppartChildern.stream().map(ppart -> {

			List<PpartInfo> grandChildrenPpartList = ppart.getPart().getPpartChildren(_partCfg);
			List<DefaultTreeNode<PpartInfo>> grandChildrenList = grandChildrenPpartList.stream()
					.map(pp -> new DefaultTreeNode<>(pp)).collect(Collectors.toList());

			DefaultTreeNode<PpartInfo> thisTn = new DefaultTreeNode<PpartInfo>(ppart, grandChildrenList);

			return thisTn;
		}).collect(Collectors.toList());
		log.debug("children.size(): {}", children.size());

		DefaultTreeNode<Object> tn = new DefaultTreeNode(rootPart, children);

		DefaultTreeModel<Object> model = new DefaultTreeModel(tn);
		treePartCfg.setModel(model);
		log.debug("model.getRoot().getData(): {}", model.getRoot().getData());
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------init2-------------------------------------
	public void init2(PartCfgInfo _partCfg) {
		TreeitemRenderer<DefaultTreeNode<PartAcqInfo>> renderer = (ti, tn, i) -> {
			Treerow tr = ti.getTreerow();
			if (tr == null) { // tree row not create yet.
				tr = new Treerow();
				ti.appendChild(tr);
			}
			PartAcqInfo pa = tn.getData();
			PartInfo p = pa.getPart(false);
			// part pin
			ti.getTreerow().appendChild(new Treecell(p.getPin()));
			// part name
			ti.getTreerow().appendChild(new Treecell(p.getName()));
			// part unit
			ti.getTreerow().appendChild(new Treecell(p.getUnitName()));
			//
			ti.getTreerow().appendChild(new Treecell(pa.getId()));
			ti.getTreerow().appendChild(new Treecell(pa.getName()));
			ti.getTreerow().appendChild(new Treecell(pa.getTypeName()));
			// qty
			ti.getTreerow().appendChild(new Treecell()); // FIXME

		};
		tree2.setItemRenderer(renderer);

		PartAcqInfo rootPa = _partCfg.getRootPart().getPa(_partCfg);
		DefaultTreeNode<PartAcqInfo> rootNode = parseTreeNode(_partCfg, rootPa);
		List<DefaultTreeNode<PartAcqInfo>> list = new ArrayList<>();
		list.add(rootNode);
		DefaultTreeNode<PartAcqInfo> rootNode4Model = new DefaultTreeNode<PartAcqInfo>(rootPa, list);

//		DefaultTreeModel<PartAcqInfo> model = new DefaultTreeModel<>(rootNode);
		DefaultTreeModel<PartAcqInfo> model = new DefaultTreeModel<>(rootNode4Model);
		tree2.setModel(model);

//		DefaultTreeNode<PartAcqInfo> node = rootNode;
		logNode(rootNode, model);
//		while (node.getChildCount() > 0) {
//
//		}
//		do {
//			
//
//			if(node.getChildCount()>0) {
//				
//			}
//		}
	}

	private void logNode(TreeNode<PartAcqInfo> _node, DefaultTreeModel<PartAcqInfo> model) {
		log.debug("model.isLeaf(node): {}\t{}", _node.getData().getPartPin(), model.isLeaf(_node));

		if (_node.getChildCount() > 0) {
			_node.getChildren().forEach(cNode -> logNode(cNode, model));
		}
	}

	private DefaultTreeNode<PartAcqInfo> parseTreeNode(PartCfgInfo _partCfg, PartAcqInfo _pa) {
		List<PartAcqInfo> childrenList = _pa.getChildrenList(_partCfg);
		if (childrenList == null || childrenList.isEmpty()) {
			log.debug("add leaf {}", _pa.getPartPin());
			return new DefaultTreeNode<PartAcqInfo>(_pa);
		} else {
			List<DefaultTreeNode<PartAcqInfo>> childrenTreeNodeList = new ArrayList<>();
			for (PartAcqInfo childPa : childrenList) {
				childrenTreeNodeList.add(parseTreeNode(_partCfg, childPa));
			}
			log.debug("add branch {}", _pa.getPartPin());
			return new DefaultTreeNode<PartAcqInfo>(_pa, childrenTreeNodeList);
		}

		//
//		DefaultTreeNode<PartAcqInfo> tn = new DefaultTreeNode<PartAcqInfo>(_pa, new ArrayList<>());
//		List<PartAcqInfo> childrenList = _pa.getChildrenList(_partCfg);
//		if(childrenList!=null && !childrenList.isEmpty()) {
//			for (PartAcqInfo childPa : childrenList) {
////				childrenTreeNodeList.add(parseTreeNode(_partCfg, childPa));
//				tn.add(parseTreeNode(_partCfg, childPa));
//			}
//		}
//		return tn;
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------init3-------------------------------------
	public void init3(PartCfgInfo _partCfg) {
		
		
		TreeitemRenderer<DefaultTreeNode<PartCfgTreeDto>> renderer = (ti, tn, i) -> {
			Treerow tr = ti.getTreerow();
			if (tr == null) { // tree row not create yet.
				tr = new Treerow();
				ti.appendChild(tr);
			}
//			PartAcqInfo pa = tn.getData();
			PartCfgTreeDto data = tn.getData();
			PartInfo p = data.getP();
			PartAcqInfo pa = data.getPa();
			PpartInfo ppart = data.getPpart();
			
//			PartInfo p = pa.getPart(false);
			// part pin
			ti.getTreerow().appendChild(new Treecell(p==null?"(No data)":p.getPin()));
			// part name
			ti.getTreerow().appendChild(new Treecell(p==null?"(No data)":p.getName()));
			// part unit
			ti.getTreerow().appendChild(new Treecell(p==null?"(No data)":p.getUnitName()));
			//
			ti.getTreerow().appendChild(new Treecell(pa==null?"(No data)":pa.getId()));
			ti.getTreerow().appendChild(new Treecell(pa==null?"(No data)":pa.getName()));
			ti.getTreerow().appendChild(new Treecell(pa==null?"(No data)":pa.getTypeName()));
			// qty
			ti.getTreerow().appendChild(new Treecell(ppart==null?"(No data)": NumberFormatUtil.getDecimalString(ppart.getPartReqQty(), 3) ));

		};
		tree3.setItemRenderer(renderer);

//		PartAcqInfo rootPa = _partCfg.getRootPart().getPa(_partCfg);
		PartCfgTreeDto partCfgTreeDto = PartCfgTreeDto.of(_partCfg);
//		DefaultTreeNode<PartCfgTreeDto> tn = partCfgTreeDto.packTreeNode();
		DefaultTreeNode<PartCfgTreeDto> tn =partCfgTreeDto.packRootTreeNode4Model();
		
//		DefaultTreeNode<PartCfgTreeDto> tn4Model = new DefaultTreeNode<PartCfgTreePageComposer.PartCfgTreeDto>(partCfgTreeDto, new DefaultTreeNode[] {tn});
		DefaultTreeModel<PartCfgTreeDto> model = new DefaultTreeModel<>(tn);
		tree3.setModel(model);
		
//		DefaultTreeNode<PartAcqInfo> rootNode = parseTreeNode(_partCfg, rootPa);
		
		
//		List<DefaultTreeNode<PartAcqInfo>> list = new ArrayList<>();
//		list.add(rootNode);
//		DefaultTreeNode<PartAcqInfo> rootNode4Model = new DefaultTreeNode<PartAcqInfo>(rootPa, list);
//
////		DefaultTreeModel<PartAcqInfo> model = new DefaultTreeModel<>(rootNode);
//		DefaultTreeModel<PartAcqInfo> model = new DefaultTreeModel<>(rootNode4Model);
//		tree2.setModel(model);

//		DefaultTreeNode<PartAcqInfo> node = rootNode;
//		logNode(rootNode, model);
//		while (node.getChildCount() > 0) {
//
//		}
//		do {
//			
//
//			if(node.getChildCount()>0) {
//				
//			}
//		}
	}
	
	

	
	public static class PartCfgTreeDto {
		private PpartInfo ppart; // root: no ppart
		private PartInfo p;
		private PartAcqInfo pa;
		private List<PartCfgTreeDto> childrenList;
		
		private PartCfgTreeDto( PpartInfo ppart,PartInfo p, PartAcqInfo pa, List<PartCfgTreeDto> childrenList) {
			this.p = p;
			this.pa = pa;
			this.ppart = ppart;
			this.childrenList = childrenList;
		}

		private static PartCfgTreeDto of(PartCfgInfo _partCfg) {
			PartInfo rootPart = _partCfg.getRootPart();
			PartAcqInfo rootPa = rootPart.getPa(_partCfg);
			PpartInfo ppart = null;
			List<PartCfgTreeDto> childrenList = new ArrayList<>();

			for (PpartInfo childPpart : rootPa.getPpartList()) {
				childrenList.add(of(_partCfg, childPpart));
			}

			return new PartCfgTreeDto(ppart, rootPart, rootPa, childrenList);
		}

		private static PartCfgTreeDto of(PartCfgInfo _partCfg, PpartInfo _ppart) {
			PpartInfo ppart = _ppart;
			PartInfo p = ppart.getPart();
			PartAcqInfo pa = p == null ? null : p.getPa(_partCfg);
			List<PartCfgTreeDto> childrenList = new ArrayList<>();
			if (pa != null)
				for (PpartInfo childPpart : pa.getPpartList()) {
					childrenList.add(of(_partCfg, childPpart));
				}
			return new PartCfgTreeDto(ppart, p, pa, childrenList);
		}
		
		public PartInfo getP() {
			return p;
		}

		public PartAcqInfo getPa() {
			return pa;
		}

		public PpartInfo getPpart() {
			return ppart;
		}

		public List<PartCfgTreeDto> getChildrenList() {
			return childrenList;
		}
		
		// ---------------------------------------------------------------------------
		public DefaultTreeNode<PartCfgTreeDto> packRootTreeNode4Model(){
			return  new DefaultTreeNode<PartCfgTreePageComposer.PartCfgTreeDto>(this, new DefaultTreeNode[] {packTreeNode()});
		}
		
		public DefaultTreeNode<PartCfgTreeDto> packTreeNode() {
			if (getChildrenList() == null || getChildrenList().isEmpty()) {
				return new DefaultTreeNode<PartCfgTreePageComposer.PartCfgTreeDto>(this);
			} else {
				List<DefaultTreeNode<PartCfgTreeDto>> childrenTreeNodeList = new ArrayList<>();
				for (PartCfgTreeDto childDto : getChildrenList())
					childrenTreeNodeList.add(childDto.packTreeNode());
				return new DefaultTreeNode<PartCfgTreePageComposer.PartCfgTreeDto>(this, childrenTreeNodeList);
			}
		}
		
		
		


	}

}
