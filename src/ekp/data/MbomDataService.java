package ekp.data;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mbom.PpartSkewer;
import ekp.data.service.mbom.PprocCreateObj;
import ekp.data.service.mbom.PprocInfo;
import ekp.data.service.mbom.ParsCreateObj;
import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqCreateObj;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgConjInfo;
import ekp.data.service.mbom.PartCfgCreateObj;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartCreateObj;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.ProdCreateObj;
import ekp.data.service.mbom.ProdCtlCreateObj;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdCtlPartCfgConjInfo;
import ekp.data.service.mbom.ProdInfo;
import ekp.data.service.mbom.ProdModCreateObj;
import ekp.data.service.mbom.ProdModInfo;
import ekp.data.service.mbom.ProdModItemInfo;
import ekp.data.service.mbom.query.PartAcquisitionQueryParam;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.data.service.mbom.query.PartQueryParam;
import ekp.data.service.mbom.query.PpartSkewerQueryParam;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartUnit;
import ekp.serviceFacade.rmi.mbom.PartAcquisitionRemote;
import ekp.serviceFacade.rmi.mbom.PartCfgRemote;
import ekp.serviceFacade.rmi.mbom.PpartSkewerRemote;
import legion.IntegrationService;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public interface MbomDataService extends IntegrationService {
	public boolean testEkpKernelServiceRemoteCallBack();

	// -------------------------------------------------------------------------------
	// -------------------------------------Part--------------------------------------
	public PartInfo createPart(PartCreateObj _dto);

	public boolean deletePart(String _uid);

	public PartInfo loadPart(String _uid);

	public PartInfo loadPartByPin(String _pin);
	
	public QueryOperation<PartQueryParam, PartInfo> searchPart(QueryOperation<PartQueryParam, PartInfo> _param);
	
	public boolean partUpdate(String _uid, String _pin, String _name, PartUnit _unit);
	
//	public boolean partAssignMm(String _uid, String _mmUid, String _mmMano);
//
//	public boolean partRevertAssignMm(String _uid);

	// -------------------------------------------------------------------------------
	// --------------------------------PartAcquisition--------------------------------
	public PartAcqInfo createPartAcquisition(PartAcqCreateObj _dto);

	public boolean deletePartAcquisition(String _uid);

	public PartAcqInfo loadPartAcquisition(String _uid);

	public PartAcqInfo loadPartAcquisition(String _partPin, String _id);

	public List<PartAcqInfo> loadPartAcquisitionList(String _partUid);

	public QueryOperation<PartAcquisitionQueryParam, PartAcqInfo> searchPartAcquisition(
			QueryOperation<PartAcquisitionQueryParam, PartAcqInfo> _param);
	
	public boolean partAcqStartEditing(String _uid);

	public boolean partAcqRevertStartEditing(String _uid);

	public boolean partAcqAssignMm(String _uid, String _mmUid, String _mmMano);

	public boolean partAcqRevertAssignMm(String _uid);
	
	public boolean partAcqPublish(String _uid, long _publishTime);

	public boolean partAcqRevertPublish(String _uid);
	
	public boolean partAcqUpdateInfo(String _uid, String _id, String _name, PartAcquisitionType _type);
	
	public boolean partAcqUpdateRefUnitCost(String _uid, double _refUnitCost);
	
	

	// -------------------------------------------------------------------------------
	// ------------------------------PartAcqRoutingStep-------------------------------
	public ParsInfo createPartAcqRoutingStep(ParsCreateObj _dto);

	public boolean deletePartAcqRoutingStep(String _uid);

	public ParsInfo loadPartAcqRoutingStep(String _uid);

	public ParsInfo loadPartAcqRoutingStep(String _partAcqUid, String _id);

	public List<ParsInfo> loadPartAcqRoutingStepList(String _partAcqUid);

	// -------------------------------------------------------------------------------
	// -----------------------------------ParsProc------------------------------------
	public PprocInfo createParsProc(PprocCreateObj _dto);

	public boolean deleteParsProc(String _uid);

	public PprocInfo loadParsProc(String _uid);

	public List<PprocInfo> loadParsProcList(String _parsUid);

	public List<PprocInfo> loadParsProcListByProc(String _procUid);

	public boolean parsProcAssignProc(String _uid, String _procUid, String _procId);

	public boolean parsProcRevertAssignProc(String _uid);

	// -------------------------------------------------------------------------------
	// -----------------------------------ParsPart------------------------------------
	public PpartInfo createParsPart(String _parsUid);

	public boolean deleteParsPart(String _uid);

	public PpartInfo loadParsPart(String _uid);
	
	public PpartInfo loadParsPart(String _parsUid, String _partuid);

	public List<PpartInfo> loadParsPartList(String _parsUid);

	public List<PpartInfo> loadParsPartListByPart(String _partUid);

	public boolean parsPartAssignPart(String _uid, String _partUid, String _partPin, double _partReqQty);

	public boolean parsPartRevertAssignPart(String _uid);

	// -------------------------------------------------------------------------------
	// ----------------------------------PpartSkewer----------------------------------
	public PpartSkewer loadPpartSkewer(String _uid);
	
	public QueryOperation<PpartSkewerQueryParam, PpartSkewer> searchPpartSkewer(
			QueryOperation<PpartSkewerQueryParam, PpartSkewer> _param,
			Map<PpartSkewerQueryParam, QueryValue[]> _existsQvMap);
//	
//	default public QueryOperation<PpartSkewerQueryParam, PpartSkewer> searchPpartSkewer(
//			QueryOperation<PpartSkewerQueryParam, PpartSkewer> _param) {
//		return searchPpartSkewer(_param, null);
//	}
	
	// -------------------------------------------------------------------------------
	// ------------------------------------PartCfg------------------------------------
	public PartCfgInfo createPartCfg(PartCfgCreateObj _dto);

	public boolean deletePartCfg(String _uid);

	public PartCfgInfo loadPartCfg(String _uid);
	
	public PartCfgInfo loadPartCfgById(String _id);

	public List<PartCfgInfo> loadPartCfgList(String _rootPartUid);
	
	public QueryOperation<PartCfgQueryParam, PartCfgInfo> searchPartCfg(QueryOperation<PartCfgQueryParam, PartCfgInfo> _param);

	public boolean partCfgStartEditing(String _uid);

	public boolean partCfgRevertStartEditing(String _uid);

	public boolean partCfgPublish(String _uid, long _publishTime);

	public boolean partCfgRevertPublish(String _uid);

	// -------------------------------------------------------------------------------
	// ----------------------------------PartCfgConj----------------------------------
	public PartCfgConjInfo createPartCfgConj(String _partCfgUid, String _partAcqUid);

	public boolean deletePartCfgConj(String _uid);

	public PartCfgConjInfo loadPartCfgConj(String _uid);
	
	public PartCfgConjInfo loadPartCfgConj(String _partCfgUid, String _partAcqUid);

	public List<PartCfgConjInfo> loadPartCfgConjList(String _partCfgUid);
	
	public List<PartCfgConjInfo> loadPartCfgConjListByPartAcq(String _partAcqUid);

	// -------------------------------------------------------------------------------
	// -------------------------------------Prod--------------------------------------
	public ProdInfo createProd(ProdCreateObj _dto);

	public boolean deleteProd(String _uid);

	public ProdInfo loadProd(String _uid);
	
	public ProdInfo loadProdById(String _id);

	public List<ProdInfo> loadProdList();

	// -------------------------------------------------------------------------------
	// ------------------------------------ProdCtl------------------------------------
	public ProdCtlInfo createProdCtl(ProdCtlCreateObj _dto);

	public boolean deleteProdCtl(String _uid);

	public ProdCtlInfo loadProdCtl(String _uid);

	public List<ProdCtlInfo> loadProdCtlList(String _parentUid);

	public List<ProdCtlInfo> loadProdCtlListLv1(String _prodUid);

	public boolean prodCtlAssignParent(String _uid, String _parentUid);

	public boolean prodCtlUnassignParent(String _uid);

	public boolean prodCtlAssignProd(String _uid, String _prodUid);

	public boolean prodCtlUnassignProd(String _uid);

	// -------------------------------------------------------------------------------
	// ------------------------------ProdCtlPartCfgConj-------------------------------
	public ProdCtlPartCfgConjInfo createProdCtlPartCfgConj(String _prodCtlUid, String _partCfgUid, String _partAcqUid);

	public boolean deleteProdCtlPartCfgConj(String _uid);

	public ProdCtlPartCfgConjInfo loadProdCtlPartCfgConj(String _uid);
	
	public ProdCtlPartCfgConjInfo loadProdCtlPartCfgConj(String _prodCtlUid, String _partCfgUid, String _partAcqUid);
	
	public List<ProdCtlPartCfgConjInfo> loadProdCtlPartCfgConjList1(String _prodCtlUid);

	public List<ProdCtlPartCfgConjInfo> loadProdCtlPartCfgConjList2(String _partCfgUid);
	
	public List<ProdCtlPartCfgConjInfo> loadProdCtlPartCfgConjList3(String _partAcqUid);

	// -------------------------------------------------------------------------------
	// ------------------------------------ProdMod------------------------------------
	public ProdModInfo createProdMod(ProdModCreateObj _dto);

	public boolean deleteProdMod(String _uid);

	public ProdModInfo loadProdMod(String _uid);
	
	public ProdModInfo loadProdModById(String _id);

	public List<ProdModInfo> loadProdModList(String _prodUid);

	// -------------------------------------------------------------------------------
	// ----------------------------------ProdModItem----------------------------------
	public ProdModItemInfo createProdModItem(String prodModUid, String prodCtlUid);

	public boolean deleteProdModItem(String _uid);

	public ProdModItemInfo loadProdModItem(String _uid);
	public ProdModItemInfo loadProdModItem(String _prodModUid, String _prodCtlUid);
	public ProdModItemInfo loadProdModItem(String _prodModUid, String _prodCtlUid, String _partCfgUid, String _partAcqUid);

	public List<ProdModItemInfo> loadProdModItemList(String _prodModUid);

	public boolean prodModItemAssignPartAcqCfg(String _uid, String _partCfgUid, String _partAcqUid);

	public boolean prodModItemUnassignPartAcqCfg(String _uid);

}
