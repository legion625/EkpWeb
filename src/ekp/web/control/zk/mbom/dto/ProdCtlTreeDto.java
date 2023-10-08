package ekp.web.control.zk.mbom.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.zkoss.zul.DefaultTreeNode;

import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;
import ekp.util.DataUtil;
import legion.util.DataFO;

public class ProdCtlTreeDto {
	private ProdCtlInfo prodCtl;

	public List<ProdCtlTreeDto> childrenList;

	private ProdCtlTreeDto(ProdCtlInfo prodCtl, List<ProdCtlTreeDto> childrenList) {
		this.prodCtl = prodCtl;
		this.childrenList = childrenList;
	}
	
	public static ProdCtlTreeDto of(ProdCtlInfo _prodCtl) {
		ProdCtlInfo prodCtl = _prodCtl;
		List<ProdCtlTreeDto> childrenList = new ArrayList<>();
		for (ProdCtlInfo childProdCtl : prodCtl.getChildrenList()) {
			childrenList.add(of(childProdCtl));
		}
		return new ProdCtlTreeDto(prodCtl, childrenList);
	}

	// ---------------------------------------------------------------------------
	public ProdCtlInfo getProdCtl() {
		return prodCtl;
	}

	public List<ProdCtlTreeDto> getChildrenList() {
		return childrenList;
	}
	
	// ---------------------------------------------------------------------------
	public String getLvDisplay() {
		return DataUtil.nodataIfEmpty(getProdCtl(), pc->""+pc.getLv());
	}
	public String getName() {
		return DataUtil.nodataIfEmpty(getProdCtl(), ProdCtlInfo::getName);
	}
	public String getReqDisplay() {
		return DataUtil.nodataIfEmpty(getProdCtl(), pc->DataUtil.getStr(pc.isReq()));
	}

	public String getPartAcqsStr() {
		return DataUtil.nodataIfEmpty(getProdCtl(),
				pc -> DataUtil.getStrJ(pc.getPcpccList().stream().filter(pcpcc -> pcpcc.getPartAcq() != null)
						.map(pcpcc -> pcpcc.getPartAcq().getId()).distinct().collect(Collectors.toList())));
	}
	
	public String getPcpccDisplay() {
		return DataUtil.nodataIfEmpty(getProdCtl(),
				pc -> DataUtil.getStrJ(pc.getPcpccList().stream()
						.map(pcpcc -> pcpcc.getDisplay()).distinct().collect(Collectors.toList())));
	}
	
	
	
	// ---------------------------------------------------------------------------
	public static DefaultTreeNode packRootTreeNode4Model(ProdInfo _prod) {
		List<ProdCtlInfo> prodCtlList = _prod.getProdCtlListLv1();
		List<ProdCtlTreeDto> treeDtoList = 
				prodCtlList.stream().map(pc->of(pc)).collect(Collectors.toList());
		DefaultTreeNode[] rootTreeNodes = new DefaultTreeNode[prodCtlList.size()];
		for (int i = 0; i < treeDtoList.size(); i++) {
			rootTreeNodes[i] = treeDtoList.get(i).packTreeNode();
		}
		return new DefaultTreeNode(new ProdCtlTreeDto(null, treeDtoList), rootTreeNodes);
	}
	
	public DefaultTreeNode<ProdCtlTreeDto> packTreeNode() {
		if (getChildrenList() == null || getChildrenList().isEmpty()) {
			return new DefaultTreeNode<ProdCtlTreeDto>(this);
		} else {
			List<DefaultTreeNode<ProdCtlTreeDto>> childrenTreeNodeList = new ArrayList<>();
			for (ProdCtlTreeDto childDto : getChildrenList())
				childrenTreeNodeList.add(childDto.packTreeNode());
			return new DefaultTreeNode<ProdCtlTreeDto>(this, childrenTreeNodeList);
		}
	}

}
