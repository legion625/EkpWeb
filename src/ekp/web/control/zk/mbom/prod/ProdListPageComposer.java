package ekp.web.control.zk.mbom.prod;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import ekp.DebugLogMark;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;
import ekp.data.service.mbom.ProdModInfo;
import ekp.data.service.mbom.ProdModItemInfo;
import ekp.invt.type.InvtOrderType;
import ekp.util.DataUtil;
import ekp.web.control.zk.mbom.dto.PartCfgTreeDto;
import ekp.web.control.zk.mbom.dto.ProdCtlTreeDto;
import ekp.web.control.zk.mbom.dto.ProdModPaTreeDto;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.util.query.QueryOperation;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;

public class ProdListPageComposer extends SelectorComposer<Component> {
	public final static String URI = "/mbom/prod/prodListPage.zul";

	private Logger log = LoggerFactory.getLogger(DebugLogMark.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxProd;
	
	@Wire
	private Listbox lbxProdMod;

	@Wire
	private Tree treeProdCtl;
	
	@Wire
	private Listbox lbxProdModItem;
	@Wire
	private Tree treeProdModPa;

	// -------------------------------------------------------------------------------
	private FnCntProxy fnCntProxy;

	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			fnCntProxy = FnCntProxy.register(this);
			//
			init();

		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	private void init() {
		/* Prod */
		ListitemRenderer<ProdInfo> prodRenderer = (li, prod, i) -> {
			li.appendChild(new Listcell());
			li.appendChild(new Listcell(prod.getId()));
			li.appendChild(new Listcell(prod.getName()));
			//
			li.addEventListener(Events.ON_CLICK, evt -> refreshProdInfo(prod));
		};
		lbxProd.setItemRenderer(prodRenderer);

		/* ProdMod */
		ListitemRenderer<ProdModInfo> prodModRenderer = (li, pm, i) -> {
			li.appendChild(new Listcell());
			li.appendChild(new Listcell(pm.getId()));
			li.appendChild(new Listcell(pm.getName()));
			li.appendChild(new Listcell(pm.getDesp()));
			//
			li.addEventListener(Events.ON_CLICK, evt -> refreshProdModInfo(pm));

		};
		lbxProdMod.setItemRenderer(prodModRenderer);
		
		/* treeProdCtl */
		TreeitemRenderer<DefaultTreeNode<ProdCtlTreeDto>> prodCtlTreeRenderer = (ti, tn, i) -> {
			Treerow tr = ti.getTreerow();
			if (tr == null) { // tree row not create yet.
				tr = new Treerow();
				ti.appendChild(tr);
			}
			ProdCtlTreeDto data = tn.getData();
			// name
			ti.getTreerow().appendChild(new Treecell(data.getName()));
			// req
			ti.getTreerow().appendChild(new Treecell(data.getReqDisplay()));
			// partAcqs
			ti.getTreerow().appendChild(new Treecell(data.getPcpccDisplay()));
		};
		treeProdCtl.setItemRenderer(prodCtlTreeRenderer);
		
		/* ProdModItem */
		ListitemRenderer<ProdModItemInfo> prodModItemRenderer = (li, pmi, i) -> {
			li.appendChild(new Listcell());
			li.appendChild(new Listcell(pmi.getProdCtl().getName()));
			li.appendChild(new Listcell(DataUtil.getStr(pmi.isPartAcqCfgAssigned())));
			li.appendChild(new Listcell(pmi.getPartCfgId()));
			li.appendChild(new Listcell(pmi.getPartAcqId()));
		};
		lbxProdModItem.setItemRenderer(prodModItemRenderer);
		
		/* treeProdModPa */
		TreeitemRenderer<DefaultTreeNode<ProdModPaTreeDto>> prodModPaTreeRenderer = (ti, tn, i) -> {
			Treerow tr = ti.getTreerow();
			if (tr == null) { // tree row not create yet.
				tr = new Treerow();
				ti.appendChild(tr);
			}
			ProdModPaTreeDto data = tn.getData();
			
			// part pin
			ti.getTreerow().appendChild(new Treecell(data.getPartAcq().getPartPin()));
			ti.getTreerow().appendChild(new Treecell(NumberFormatUtil.getDecimalString(data.getQty(), 2) ));
			ti.getTreerow().appendChild(new Treecell(data.getPartCfg().getId()));
//			ti.getTreerow().appendChild(new Treecell(data.getPartAcq().getId()));
			ti.getTreerow().appendChild(new Treecell(data.getPartAcq().getName()));
			ti.getTreerow().appendChild(new Treecell(data.getPartAcq().getMmMano()));
			
			ti.getTreerow().appendChild(new Treecell(NumberFormatUtil.getDecimalString(data.getPartAcq().getRefUnitCost(), 2) ));
			ti.getTreerow().appendChild(new Treecell(NumberFormatUtil.getDecimalString(data.getPartAcq().getMm().getAvgStockValue(), 2) ));
			ti.getTreerow().appendChild(new Treecell(NumberFormatUtil.getDecimalString(data.getPartAcq().getMm().getIoiAvgOrderValue(InvtOrderType.I1), 2) ));
			ti.getTreerow().appendChild(new Treecell(NumberFormatUtil.getDecimalString(data.getPartAcq().getMm().getIoiAvgOrderValue(InvtOrderType.I2), 2) ));
			ti.getTreerow().appendChild(new Treecell(NumberFormatUtil.getDecimalString(data.getPartAcq().getMm().getIoiAvgOrderValue(InvtOrderType.O2), 2) ));
			ti.getTreerow().appendChild(new Treecell(NumberFormatUtil.getDecimalString(data.getPartAcq().getMm().getIoiAvgOrderValue(InvtOrderType.O9), 2) ));
			ti.getTreerow().appendChild(new Treecell(NumberFormatUtil.getDecimalString(data.getPartAcq().getMm().getSoiAvgValue(), 2) ));
		};
		treeProdModPa.setItemRenderer(prodModPaTreeRenderer);
		
		
	}

	// -------------------------------------------------------------------------------
	public void refreshProdList(List<ProdInfo> _prodList) {
		ListModelList<ProdInfo> model = new ListModelList<>(_prodList);
		lbxProd.setModel(model);

	}
	
	// -------------------------------------------------------------------------------
	private void refreshProdInfo(ProdInfo _prod) {
		refreshProdMod(_prod);
		refreshProdCtlTree(_prod);
	}
	
	private void refreshProdMod(ProdInfo _prod) {
		if (_prod == null)
			return;
		List<ProdModInfo> pmList = _prod.getProdModList();
		ListModelList<ProdModInfo> model =pmList==null? new ListModelList<>():new ListModelList<>(pmList);
		lbxProdMod.setModel(model);
	}
	
	private void refreshProdCtlTree(ProdInfo _prod) {
		if (_prod == null)
			return;
		List<ProdCtlInfo> _prodCtlList = _prod.getProdCtlListLv1();
		if (_prodCtlList == null || _prodCtlList.isEmpty()) {
			treeProdCtl.setModel(null);
			return;
		}

		DefaultTreeNode rootTn = ProdCtlTreeDto.packRootTreeNode4Model(_prod);
		DefaultTreeModel<ProdCtlTreeDto> model = new DefaultTreeModel<>(rootTn);
		treeProdCtl.setModel(model);

		// 全展開
		List<TreeNode<ProdCtlTreeDto>> list = new ArrayList<>();
		appendAllBranchTreeNodes(list, rootTn);

		for (TreeNode<ProdCtlTreeDto> tn : list) {
			model.addOpenObject(tn);
		}
	}

	private void appendAllBranchTreeNodes(List<TreeNode<ProdCtlTreeDto>> _list, TreeNode<ProdCtlTreeDto> _tn) {
		if (_tn.getChildCount() > 0) {
			_list.add(_tn);
			for (TreeNode<ProdCtlTreeDto> _childTn : _tn.getChildren())
				appendAllBranchTreeNodes(_list, _childTn);
		}
	}

	// -------------------------------------------------------------------------------
	private void refreshProdModInfo(ProdModInfo _pm) {
		if(_pm==null)
			return;
		ListModelList<ProdModItemInfo> model = new ListModelList<>(_pm.getProdModItemList());
		lbxProdModItem.setModel(model);
		
		/* prodMod */
//		List<ProdCtlInfo> _prodCtlList = _pm.getProdCtlListLv1();
//		if (_prodCtlList == null || _prodCtlList.isEmpty()) {
//			treeProdCtl.setModel(null);
//			return;
//		}

		DefaultTreeNode rootTn = ProdModPaTreeDto.packRootTreeNode4Model(_pm);
		DefaultTreeModel<ProdModPaTreeDto> treeModel = new DefaultTreeModel<>(rootTn);
		treeProdModPa.setModel(treeModel);

		// 全展開
		List<TreeNode<ProdModPaTreeDto>> list = new ArrayList<>();
		appendProdModPaAllBranchTreeNodes(list, rootTn);

		for (TreeNode<ProdModPaTreeDto> tn : list) {
			treeModel.addOpenObject(tn);
		}
		
	}
	
	private void appendProdModPaAllBranchTreeNodes(List<TreeNode<ProdModPaTreeDto>> _list, TreeNode<ProdModPaTreeDto> _tn) {
		if (_tn.getChildCount() > 0) {
			_list.add(_tn);
			for (TreeNode<ProdModPaTreeDto> _childTn : _tn.getChildren())
				appendProdModPaAllBranchTreeNodes(_list, _childTn);
		}
	}
}
