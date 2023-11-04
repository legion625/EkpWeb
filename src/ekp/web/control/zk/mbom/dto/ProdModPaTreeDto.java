package ekp.web.control.zk.mbom.dto;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.logging.Log;
import org.zkoss.zul.DefaultTreeNode;

import ekp.DebugLogMark;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mbom.PpartSkewer;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdCtlPartCfgConjInfo;
import ekp.data.service.mbom.ProdModInfo;
import ekp.data.service.mbom.ProdModItemInfo;

public class ProdModPaTreeDto {
	private static Logger log = LoggerFactory.getLogger(ProdModPaTreeDto.class);
	
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
		return new DefaultTreeNode(new ProdModPaTreeDto(null, null, null, null, treeDtoList), rootTreeNodes);
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
			
			ProdModPaTreeDto pmp = ofNew(pmi.getProdCtl(), _prodMod, partAcq, partCfg, pmi, null);
			pmpList.add(pmp);
		}
		return pmpList;
	}

	private static ProdModPaTreeDto ofNew(ProdCtlInfo _prodCtl, ProdModInfo _prodMod, PartAcqInfo _partAcq, PartCfgInfo _partCfg,
			ProdModItemInfo _prodModItem, PpartInfo _parentPpart) {
		log.debug("============================================================");
		log.debug("{}\t{}\t{}\t{}\t{}\t{}",
				_prodCtl==null?"null": _prodCtl.getName(),
				_partCfg.getId(), 
				_prodModItem==null?"null":_prodModItem.getPartCfgId(),
				_prodModItem==null?"null":_prodModItem.getPartAcqId(), 
				
				_parentPpart==null?"null":_parentPpart.getPartPin(), _parentPpart==null?"null":_parentPpart.getPartReqQty()
				);
		
		
		List<ProdModPaTreeDto> childrenList = new ArrayList<>();

		// _prodCtl仍有子階，從這邊長子階。
		if (_prodCtl!=null && _prodCtl.getChildrenList().size() > 0) {
			log.debug("test 1 {}", _partAcq.getId());
			for (ProdCtlInfo _childProdCtl : _prodCtl.getChildrenList()) {
				log.debug("test 1 {}", _childProdCtl.getName());
				// 找PMI
				ProdModItemInfo childPmi = _prodMod.getProdModItem(_childProdCtl.getUid());
				PartCfgInfo childPartCfg = null;
				PartAcqInfo childPartAcq = null;
				if (childPmi.isPartAcqCfgAssigned()) {
					log.debug("test 1a");
					childPartCfg = childPmi.getPartCfg();
					childPartAcq = childPmi.getPartAcq();
				} else {
					log.debug("test 1b");
					childPartCfg = _partCfg;
					log.debug("_partCfg: {}\t{}", _partCfg.getUid(), _partCfg.getId());
					log.debug("_childProdCtl.getPcpccList().size(): {}", _childProdCtl.getPcpccList().size());
					for(ProdCtlPartCfgConjInfo pcpcc: _childProdCtl.getPcpccList()) {
						log.debug("{}\t{}\t{}\t{}", pcpcc.getPartAcqUid(),pcpcc.getPartAcq().getId(), pcpcc.getPartCfgUid(), pcpcc.getPartCfg().getId());
					}
					
					// 沒有指定->從_partAcq和childPartCfg找出下階的所有_childPartAcq
					List<ProdCtlPartCfgConjInfo> pcpccList = _childProdCtl.getPcpccList().stream()
//							.filter(pcpcc -> pcpcc.getPartCfgUid().equalsIgnoreCase(_partCfg.getUid()))
							.collect(Collectors.toList());
					log.debug("pcpccList.size(): {}", pcpccList.size());
					for(ProdCtlPartCfgConjInfo pcpcc: pcpccList) {
						log.debug("{}\t{}", pcpcc.getPartAcqUid(), pcpcc.getPartCfgUid());
					}
					List<PartAcqInfo> beforeFilter = _partAcq.getChildrenList(childPartCfg);
					log.debug("beforeFilter.size(): {}", beforeFilter.size());
					for(PartAcqInfo pa: beforeFilter) {
						log.debug("{}\t{}\t{}",pa.getUid(),pa.getId(),  pa.getPartPin());
					}
					
					childPartAcq = _partAcq.getChildrenList(childPartCfg).stream()
							.filter(childPa -> pcpccList.stream()
									.anyMatch(pcpcc -> pcpcc.getPartAcqUid().equalsIgnoreCase(childPa.getUid())))
							.findAny().orElse(null);
//					ppart= childPartAcq.getPart(false).getSrcPpart(childPartCfg);

				}
				log.debug("childPartCfg: {}\tchildPartAcq: {}",childPartCfg, childPartAcq );
				
				
				if(childPartCfg!=null && childPartAcq!=null) {
					log.debug("test 1c");
					PpartInfo ppart = childPartAcq.getPart(false).getSrcPpart(childPartCfg);
					
					
//					log.debug("{}\t{}",ppart.getPartPin(), ppart.getPartReqQty());
					
					ProdModPaTreeDto childPmp = ofNew(_childProdCtl, _prodMod, childPartAcq, childPartCfg, childPmi, ppart);
					childrenList.add(childPmp);	
				}
			}
		}
		// 沒有子階，從構型長。
		else {
			log.debug("test 2 {}", _partAcq.getId());
			for (PpartInfo ppart : _partAcq.getPpartList()) {
				
				ProdModPaTreeDto childPmp = ofNew(null, _prodMod, _partCfg.getPartAcqByPart(ppart.getPartUid()), _partCfg, null, ppart);
				childrenList.add(childPmp);
			}
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
	public double getSumChildrenRefUnitCost() {
//		getChildrenList().stream().map(c->c.getPartAcq().getRefUnitCost())
		if (getChildrenList().size() <= 0) {
			return getPartAcq().getRefUnitCost();
		}
		else 
			return getChildrenList().stream().mapToDouble(c->c.getQty()* c.getSumChildrenRefUnitCost()).sum();
		
	}

}
