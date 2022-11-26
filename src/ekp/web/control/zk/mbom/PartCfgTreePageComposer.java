package ekp.web.control.zk.mbom;

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
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.mbom.MbomService;
import legion.BusinessServiceFactory;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;
import legion.web.zk.ZkUtil;

public class PartCfgTreePageComposer extends SelectorComposer<Component> {
	public final static String URI = "/mbom/partCfgTreePage.zul";

//	private Logger log = LoggerFactory.getLogger(PartCfgTreePageComposer.class);
	private Logger log = LoggerFactory.getLogger(DebugLogMark.class);
	
	public static PartCfgTreePageComposer of(Include _icd) {
		return ZkUtil.of(_icd, URI, "pnPargCfgTreePage");
	}

	// -------------------------------------------------------------------------------
	@Wire
	private Tree treePartCfg;

	// -------------------------------------------------------------------------------
	private MbomService mbomService = BusinessServiceFactory.getInstance().getService(MbomService.class);

	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			log.debug("PartCfgTreePageComposer::doAfterCompose");
			PartCfgInfo partCfg = mbomService.loadPartCfgById("MTW");
			log.debug("partCfg: {}\t{}\t{}", partCfg.getId(), partCfg.getRootPartPin(), partCfg.getRootPart().getName());
			init(partCfg);
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}
	
//	public boolean testCallback() {
//		log.debug("testCallback");
//		return true;
//	}
	
	public void init(PartCfgInfo _partCfg) {
		TreeitemRenderer<DefaultTreeNode> renderer = (ti, node, i) -> {
//			log.debug("obj.getClass(): {}\t  obj.toString(): {}", node.getClass(), node.toString());
//			log.debug("{}\t{}\t{}", node instanceof PartInfo, (node instanceof DefaultTreeNode));
			
			Treerow tr = ti.getTreerow();
			if (tr == null) { // tree row not create yet.
				log.debug("create tree row");
				tr = new Treerow();
				ti.appendChild(tr);
			}
			
			if(node.getData() instanceof PartInfo) {
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
			}
			else if(node.getData() instanceof PpartInfo) {
				log.debug("render ppart");
				PpartInfo ppart =(PpartInfo) node.getData();
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
		List<DefaultTreeNode<PpartInfo>>  children = ppartChildern.stream().map(ppart->{
			
			List<PpartInfo> grandChildrenPpartList = ppart.getPart().getPpartChildren(_partCfg);
			List<DefaultTreeNode<PpartInfo>> grandChildrenList = grandChildrenPpartList.stream().map(pp->new DefaultTreeNode<>(pp)).collect(Collectors.toList());
			
			DefaultTreeNode<PpartInfo> thisTn = new DefaultTreeNode<PpartInfo>(ppart, grandChildrenList);
			
			return thisTn;	
		}).collect(Collectors.toList());
		log.debug("children.size(): {}", children.size());
		
		DefaultTreeNode<Object> tn = new DefaultTreeNode(rootPart,children);
		
		DefaultTreeModel<Object> model = new DefaultTreeModel(tn);
		treePartCfg.setModel(model);
		log.debug("model.getRoot().getData(): {}", model.getRoot().getData());
	}
	
	
//	public class PartCfgTreeRenderer implements TreeitemRenderer{
//
//		@Override
//		public void render(Treeitem item, Object data, int index) throws Exception {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}

}
