package ekp.web.control.zk.mbom.partCfg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import ekp.DebugLogMark;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.mbom.issue.MbomBpuType;
import ekp.mbom.issue.part.PartBpuPcAssignPa;
import ekp.mbom.issue.partAcq.PaBpuPublish;
import ekp.mbom.issue.partAcq.PaBpuUpdateRefUnitCost;
import ekp.web.control.zk.mbom.PartCfgTreePageComposer;
import ekp.web.control.zk.mbom.dto.PartCfgTreeDto;
import legion.biz.BpuFacade;
import legion.util.LogUtil;
import legion.util.TimeTraveler;
import legion.web.zk.ZkMsgBox;
import legion.web.zk.ZkNotification;
import legion.web.zk.ZkUtil;

public class PartCfgTreeComposer extends SelectorComposer<Component> {
	public final static String URI = "/mbom/partCfg/partCfgTree.zul";

	private static Logger log = LoggerFactory.getLogger(DebugLogMark.class);

	public static PartCfgTreeComposer of(Include _icd) {
		return ZkUtil.of(_icd, URI, "divPartCfgTree");
	}

	// -------------------------------------------------------------------------------
	@Wire
	private Tree treePartCfg;
	
	@Wire
	private Radiogroup rg;

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
		/* tree */
		TreeitemRenderer<DefaultTreeNode<PartCfgTreeDto>> renderer = (ti, tn, i) -> {
			Treerow tr = ti.getTreerow();
			if (tr == null) { // tree row not create yet.
				tr = new Treerow();
				ti.appendChild(tr);
			}
			PartCfgTreeDto data = tn.getData();
			Treecell tc;

			// checkmark
			ti.getTreerow().appendChild(new Treecell(""));
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
			
			ti.getTreerow().appendChild(new Treecell(data.getPa().getMmMano()));
			
			// qty
			ti.getTreerow().appendChild(new Treecell(data.getPpartReqQtyDisplay()));
			//
			tc = new Treecell();
			Doublebox dbbPaRefUnitCost = new Doublebox(data.getPaRefUnitCost());
			dbbPaRefUnitCost.setInplace(true);
			dbbPaRefUnitCost.setFormat("#,###.00");
			dbbPaRefUnitCost.setStyle("text-align:right");
			dbbPaRefUnitCost.addEventListener(Events.ON_CHANGE, evt->{
				if(dbbPaRefUnitCost.getValue()==null)
					return;
				
				PartAcqInfo pa = data.getPa();
				if(pa==null) {
					ZkNotification.info("尚未指定獲取方式(PartAcq)。");
					return;
				}
				PaBpuUpdateRefUnitCost bpu = BpuFacade.getInstance().getBuilder(MbomBpuType.PART_ACQ_$UPDATE_REF_UNIT_COST, pa);
						if(bpu==null) {
							ZkNotification.error();
							return;
						}
						
						bpu.appendRefUnitCost(dbbPaRefUnitCost.getValue());
						boolean b = bpu.build(new StringBuilder(), new TimeTraveler());
				if(b)
					ZkNotification.info("更新參考單價成功。");
				else ZkNotification.error("更新參考單價失敗。");
			});
			tc.appendChild(dbbPaRefUnitCost);
			ti.getTreerow().appendChild(tc);
//			ti.getTreerow().appendChild(new Treecell(data.getPaRefUnitCostDisplay()));
			//
			ti.getTreerow().appendChild(new Treecell(data.getPaMmAvgStockValueDisplay()));
			ti.getTreerow().appendChild(new Treecell(data.getPaMmIoiAvgOrderValueDisplayI1Display()));
			ti.getTreerow().appendChild(new Treecell(data.getPaMmIoiAvgOrderValueDisplayI2Display()));
			ti.getTreerow().appendChild(new Treecell(data.getPaMmIoiAvgOrderValueDisplayO2Display()));
			ti.getTreerow().appendChild(new Treecell(data.getPaMmIoiAvgOrderValueDisplayO9Display()));
			ti.getTreerow().appendChild(new Treecell(data.getPaMmSoiAvgValueDisplay()));
			

		};
		treePartCfg.setItemRenderer(renderer);
	}

	// -------------------------------------------------------------------------------
	@Listen(Events.ON_CHECK+"=#rg")
	public void rg_checked() {
		List<Treecol> tcList = treePartCfg.getTreecols().getChildren();

		// 全部顯示
		if (rg.getSelectedIndex() == 0) {
			for (int i = 0; i < tcList.size(); i++)
				tcList.get(i).setVisible(true);
		}
		// 僅顯示金額
		else if (rg.getSelectedIndex() == 1) {
			int[] idx1 = new int[] { 0, 1, 5,7, 8, 9, 10, 11, 12, 13, 14,15 };
			int j = 0;
			for (int i = 0; i < tcList.size(); i++) {
				if (idx1[j] == i) {
					tcList.get(i).setVisible(true);
					j++;
				} else {
					tcList.get(i).setVisible(false);
				}
			}
		}
	}
	
	// -------------------------------------------------------------------------------
	public TreeNode<PartCfgTreeDto> getSelectedPartCfgTreeNode() {
		DefaultTreeModel<PartCfgTreeDto> model = (DefaultTreeModel) treePartCfg.getModel();
		if (model == null)
			return null;
		Iterator<TreeNode<PartCfgTreeDto>> it = model.getSelection().iterator();
		return it.hasNext() ? it.next() : null;
	}

	public void refreshPartCfgTree(PartCfgInfo _partCfg) {
		if (_partCfg == null) {
			treePartCfg.setModel(null);
			return;
		}

		PartCfgTreeDto partCfgTreeDto = PartCfgTreeDto.of(_partCfg);
		DefaultTreeNode<PartCfgTreeDto> rootTn = partCfgTreeDto.packRootTreeNode4Model();
		DefaultTreeModel<PartCfgTreeDto> model = new DefaultTreeModel<>(rootTn);
		treePartCfg.setModel(model);

		// 全展開
		List<TreeNode<PartCfgTreeDto>> list = new ArrayList<>();
		appendAllBranchTreeNodes(list, rootTn);

		// 預設展第1階(root的children才是我們認定的第1階，實際上是model的第2階。)
		for (TreeNode<PartCfgTreeDto> tn : list) {
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

	
	
}
