package ekp.web.control.zk.mbom.dto;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.DefaultTreeNode;

import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mbom.ProdModInfo;
import ekp.data.service.mbom.ProdModItemInfo;

public class ProdModPaTreeDto {
	private PartAcqInfo partAcq; 
	private PartCfgInfo partCfg;
	
	private ProdModItemInfo pmi; // 若沒指定到，就是NULL。
	
	private PpartInfo parentPpart; // 若有parentPpart，才能知道「配賦量」。根節點的parentPpart是NULL。
	
	private List<ProdModPaTreeDto> childrenList;

	// -------------------------------------------------------------------------------
	private ProdModPaTreeDto(PartAcqInfo partAcq, PartCfgInfo partCfg, ProdModItemInfo pmi, PpartInfo parentPpart,
			List<ProdModPaTreeDto> childrenList) {
		this.partAcq = partAcq;
		this.partCfg = partCfg;
		this.pmi = pmi;
		this.parentPpart = parentPpart;
		this.childrenList = childrenList;
	}
	
	// -------------------------------------------------------------------------------
	public static DefaultTreeNode packRootTreeNode4Model(ProdModInfo _prodMod) {
		List<ProdModPaTreeDto> treeDtoList = of(_prodMod);
		DefaultTreeNode[] rootTreeNodes = new DefaultTreeNode[treeDtoList.size()];
		for (int i = 0; i < treeDtoList.size(); i++) {
			rootTreeNodes[i] = treeDtoList.get(i).packTreeNode();
		}
		return new DefaultTreeNode(new ProdModPaTreeDto(null, null, null, null, treeDtoList)
				, rootTreeNodes);
	}
	
	private DefaultTreeNode<ProdModPaTreeDto> packTreeNode() {
		if (getChildrenList() == null || getChildrenList().isEmpty()) {
			return new DefaultTreeNode<ProdModPaTreeDto>(this);
		} else {
			List<DefaultTreeNode<ProdModPaTreeDto>> childrenTreeNodeList = new ArrayList<>();
			for (ProdModPaTreeDto childDto : getChildrenList())
				childrenTreeNodeList.add(childDto.packTreeNode());
			return new DefaultTreeNode<ProdModPaTreeDto>(this, childrenTreeNodeList);
		}
	}
	
	public static List<ProdModPaTreeDto> of(ProdModInfo _prodMod) {
		List<ProdModItemInfo> lv1PmiList = _prodMod.getProdModItemListLv1();

		List<ProdModPaTreeDto> pmpList = new ArrayList<>();
		for (ProdModItemInfo pmi : lv1PmiList) {
			PartAcqInfo partAcq = pmi.getPartAcq(); // lv1一定要有指定
			PartCfgInfo partCfg = pmi.getPartCfg(); // lv1一定要有指定

			ProdModPaTreeDto pmp = of(_prodMod, partAcq, partCfg, pmi, null);
			pmpList.add(pmp);
		}
		return pmpList;
	}
	
	private static ProdModPaTreeDto of (ProdModInfo _prodMod, PartAcqInfo _partAcq, PartCfgInfo _partCfg,
			ProdModItemInfo _prodModItem, PpartInfo _parentPpart) {
		List<ProdModPaTreeDto> childrenList = new ArrayList<>();
		for (PpartInfo ppart : _partAcq.getPpartList()) {
			ProdModItemInfo childPmi = _prodMod.getProdModItemByPartUid(ppart.getPartUid());
			PartCfgInfo childPartCfg = childPmi == null ? _partCfg : childPmi.getPartCfg();
//			assertNotNull(childPartCfg);
			PartAcqInfo childPartAcq = childPartCfg.getPartAcqByPart(ppart.getPartUid());
//			assertNotNull(childPartAcq);
			
			ProdModPaTreeDto childPmp = of(_prodMod, childPartAcq, childPartCfg, childPmi, ppart);
			childrenList.add(childPmp);
		}
		ProdModPaTreeDto pmp = new ProdModPaTreeDto(_partAcq, _partCfg, _prodModItem, _parentPpart, childrenList);
		return pmp;
	}
	
	// -------------------------------------------------------------------------------
	public PartAcqInfo getPartAcq() {
		return partAcq;
	}

	public PartCfgInfo getPartCfg() {
		return partCfg;
	}

	public ProdModItemInfo getPmi() {
		return pmi;
	}

	public PpartInfo getParentPpart() {
		return parentPpart;
	}

	public List<ProdModPaTreeDto> getChildrenList() {
		return childrenList;
	}

	// ---------------------------------------------------------------------------
	public double getQty() {
		return getParentPpart() == null ? 1 : getParentPpart().getPartReqQty();
	}
	
	// ---------------------------------------------------------------------------
	
	
	
}
