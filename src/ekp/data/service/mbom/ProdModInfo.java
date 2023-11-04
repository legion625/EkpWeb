package ekp.data.service.mbom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import legion.ObjectModelInfo;

public interface ProdModInfo extends ObjectModelInfo{

	String getProdUid();

	String getId();

	String getName();

	String getDesp();
	
	// -------------------------------------------------------------------------------
	ProdModInfo reload();
	
	ProdInfo getProd();
	
	List<ProdModItemInfo> getProdModItemList();
	
	default List<ProdModItemInfo> getProdModItemListLv1(){
		return getProdModItemList().stream().filter(pmi->pmi.getProdCtl().getLv()==1).collect(Collectors.toList());
	}
	
//	default ProdModItemInfo getProdModItem(String _partUid) {
//		return getProdModItemList().stream().filter(pmi->pmi.getProdCtl().getPartUid().equalsIgnoreCase(_partUid)).findAny().orElse(null);
//	}
	
	
	// -------------------------------------------------------------------------------
	Map<String, ProdModItemInfo> getProdCtlUidProdModItemMap();
	
	default ProdModItemInfo getProdModItem(String _prodCtlUid) {
		return getProdCtlUidProdModItemMap().get(_prodCtlUid);
	}
	
	@Deprecated
	default ProdModItemInfo getProdModItemByPartUid(String _partUid) {
		return getProdModItemList().stream()
				.filter(pmi -> pmi.isPartAcqCfgAssigned() && pmi.getPartAcq().getPartUid().equals(_partUid)).findAny()
				.orElse(null);
	}
	
	// -------------------------------------------------------------------------------
	default boolean isReady() {
		return getProdModItemList().stream().allMatch(pmi -> !pmi.getProdCtl().isReq() || pmi.isPartAcqCfgAssigned());
	}
	
//	default double getUnitPrice1() {
//		List<ProdCtlInfo> prodCtlListLv1 = getProd().getProdCtlListLv1();
//		
//		Map<String, ProdModItemInfo> prodCtlUidProdModMap = new HashMap<>();
//		for (ProdModItemInfo prodModItem : getProdModItemList()) {
//			prodCtlUidProdModMap.put(prodModItem.getProdCtlUid(), prodModItem);
//		}
//		
////		Map<String, List<>> getProdModItemList().stream().collect(Collectors.toMap(ProdModItemInfo::getProdCtlUid, pmi->pmi));
//		
//	}
//	default double xxx(ProdCtlInfo _prodCtl, ProdModItemInfo _prodModItem) {
//		
//		
//		
//	}
	
	

}