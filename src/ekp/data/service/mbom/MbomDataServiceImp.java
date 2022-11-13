package ekp.data.service.mbom;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import ekp.data.MbomDataService;
import ekp.serviceFacade.rmi.EkpKernelServiceRemote;
import ekp.serviceFacade.rmi.mbom.ParsPartRemote;
import ekp.serviceFacade.rmi.mbom.ParsProcRemote;
import ekp.serviceFacade.rmi.mbom.PartAcqRoutingStepRemote;
import ekp.serviceFacade.rmi.mbom.PartAcquisitionRemote;
import ekp.serviceFacade.rmi.mbom.PartCfgConjRemote;
import ekp.serviceFacade.rmi.mbom.PartCfgRemote;
import ekp.serviceFacade.rmi.mbom.PartCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.PartRemote;
import ekp.serviceFacade.rmi.mbom.ProdCtlPartCfgConjRemote;
import ekp.serviceFacade.rmi.mbom.ProdCtlRemote;
import ekp.serviceFacade.rmi.mbom.ProdModItemRemote;
import ekp.serviceFacade.rmi.mbom.ProdModRemote;
import ekp.serviceFacade.rmi.mbom.ProdRemote;
import legion.datasource.manager.DSManager;
import legion.util.LogUtil;

public class MbomDataServiceImp implements MbomDataService {
	private Logger log = LoggerFactory.getLogger(MbomDataServiceImp.class);

	private String srcEkpKernelRmi;

	@Override
	public void register(Map<String, String> _params) {
		if (_params == null || _params.isEmpty())
			return;

		srcEkpKernelRmi = _params.get("srcEkpKernelRmi");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	// -------------------------------------------------------------------------------
	private EkpKernelServiceRemote getEkpKernelRmi() {
		return (EkpKernelServiceRemote) DSManager.getInstance().getConn(srcEkpKernelRmi);
	}

	@Override
	public boolean testEkpKernelServiceRemoteCallBack() {
		try {
			EkpKernelServiceRemote service = getEkpKernelRmi();
			log.debug("getEkpKernelServiceRemote(): {}", service);
			return service.testCallBack();
		} catch (RemoteException e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------Part--------------------------------------
	@Override
	public PartInfo createPart(PartCreateObj _dto) {
		try {
			PartCreateObjRemote dto = MbomFO.parsePartCreateObjRemote(_dto);
			return MbomFO.parsePart(getEkpKernelRmi().createPart(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deletePart(String _uid) {
		try {
			return getEkpKernelRmi().deletePart(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public PartInfo loadPart(String _uid) {
		try {
			PartRemote remote = getEkpKernelRmi().loadPart(_uid);
			return remote == null ? null : MbomFO.parsePart(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public PartInfo loadPartByPin(String _pin) {
		try {
			PartRemote remote = getEkpKernelRmi().loadPartByPin(_pin);
			return remote == null ? null : MbomFO.parsePart(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// --------------------------------PartAcquisition--------------------------------
	@Override
	public PartAcquisitionInfo createPartAcquisition(PartAcquisitionCreateObj _dto) {
		try {
			return MbomFO.parsePartAcquisition(
					getEkpKernelRmi().createPartAcquisition(MbomFO.parsePartAcquisitionCreateObjRemote(_dto)));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deletePartAcquisition(String _uid) {
		try {
			return getEkpKernelRmi().deletePartAcquisition(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public PartAcquisitionInfo loadPartAcquisition(String _uid) {
		try {
			PartAcquisitionRemote remote = getEkpKernelRmi().loadPartAcquisition(_uid);
			return remote == null ? null : MbomFO.parsePartAcquisition(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public PartAcquisitionInfo loadPartAcquisition(String _partPin, String _id) {
		try {
			PartAcquisitionRemote remote = getEkpKernelRmi().loadPartAcquisition(_partPin, _id);
			return remote == null ? null : MbomFO.parsePartAcquisition(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<PartAcquisitionInfo> loadPartAcquisitionList(String _partUid) {
		try {
			List<PartAcquisitionRemote> remoteList = getEkpKernelRmi().loadPartAcquisitionList(_partUid);
			List<PartAcquisitionInfo> list = remoteList.stream().map(MbomFO::parsePartAcquisition)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// ------------------------------PartAcqRoutingStep-------------------------------
	@Override
	public PartAcqRoutingStepInfo createPartAcqRoutingStep(PartAcqRoutingStepCreateObj _dto) {
		try {
			return MbomFO.parsePartAcqRoutingStep(
					getEkpKernelRmi().createPartAcqRoutingStep(MbomFO.parsePartAcqRoutingStepCreateObjRemote(_dto)));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deletePartAcqRoutingStep(String _uid) {
		try {
			return getEkpKernelRmi().deletePartAcqRoutingStep(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public PartAcqRoutingStepInfo loadPartAcqRoutingStep(String _uid) {
		try {
			PartAcqRoutingStepRemote remote = getEkpKernelRmi().loadPartAcqRoutingStep(_uid);
			return remote == null ? null : MbomFO.parsePartAcqRoutingStep(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public PartAcqRoutingStepInfo loadPartAcqRoutingStep(String _partAcqUid, String _id) {
		try {
			PartAcqRoutingStepRemote remote = getEkpKernelRmi().loadPartAcqRoutingStep(_partAcqUid, _id);
			return remote == null ? null : MbomFO.parsePartAcqRoutingStep(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<PartAcqRoutingStepInfo> loadPartAcqRoutingStepList(String _partAcqUid) {
		try {
			List<PartAcqRoutingStepRemote> remoteList = getEkpKernelRmi().loadPartAcqRoutingStepList(_partAcqUid);
			List<PartAcqRoutingStepInfo> list = remoteList.stream().map(MbomFO::parsePartAcqRoutingStep)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------ParsProc------------------------------------
	@Override
	public ParsProcInfo createParsProc(ParsProcCreateObj _dto) {
		try {
			return MbomFO.parseParsProc(getEkpKernelRmi().createParsProc(MbomFO.parseParsProcCreateObjRemote(_dto)));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteParsProc(String _uid) {
		try {
			return getEkpKernelRmi().deleteParsProc(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public ParsProcInfo loadParsProc(String _uid) {
		try {
			ParsProcRemote remote = getEkpKernelRmi().loadParsProc(_uid);
			return remote == null ? null : MbomFO.parseParsProc(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<ParsProcInfo> loadParsProcList(String _parsUid) {
		try {
			List<ParsProcRemote> remoteList = getEkpKernelRmi().loadParsProcList(_parsUid);
			List<ParsProcInfo> list = remoteList.stream().map(MbomFO::parseParsProc).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<ParsProcInfo> loadParsProcListByProc(String _procUid) {
		try {
			List<ParsProcRemote> remoteList = getEkpKernelRmi().loadParsProcListByProc(_procUid);
			List<ParsProcInfo> list = remoteList.stream().map(MbomFO::parseParsProc).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean parsProcAssignProc(String _uid, String _procUid, String _procId) {
		try {
			return getEkpKernelRmi().parsProcAssignProc(_uid, _procUid, _procId);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean parsProcRevertAssignProc(String _uid) {
		try {
			return getEkpKernelRmi().parsProcRevertAssignProc(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------ParsPart------------------------------------
	@Override
	public ParsPartInfo createParsPart(String _parsUid) {
		try {
			return MbomFO.parseParsPart(getEkpKernelRmi().createParsPart(_parsUid));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteParsPart(String _uid) {
		try {
			return getEkpKernelRmi().deleteParsPart(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public ParsPartInfo loadParsPart(String _uid) {
		try {
			ParsPartRemote remote = getEkpKernelRmi().loadParsPart(_uid);
			return remote == null ? null : MbomFO.parseParsPart(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public ParsPartInfo loadParsPart(String _parsUid, String _partuid) {
		try {
			ParsPartRemote remote = getEkpKernelRmi().loadParsPart(_parsUid, _partuid);
			return remote == null ? null : MbomFO.parseParsPart(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<ParsPartInfo> loadParsPartList(String _parsUid) {
		try {
			List<ParsPartRemote> remoteList = getEkpKernelRmi().loadParsPartList(_parsUid);
			List<ParsPartInfo> list = remoteList.stream().map(MbomFO::parseParsPart).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<ParsPartInfo> loadParsPartListByPart(String _partUid) {
		try {
			List<ParsPartRemote> remoteList = getEkpKernelRmi().loadParsPartListByPart(_partUid);
			List<ParsPartInfo> list = remoteList.stream().map(MbomFO::parseParsPart).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean parsPartAssignPart(String _uid, String _partUid, String _partPin, double _partReqQty) {
		try {
			return getEkpKernelRmi().parsPartAssignPart(_uid, _partUid, _partPin, _partReqQty);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean parsPartRevertAssignPart(String _uid) {
		try {
			return getEkpKernelRmi().parsPartRevertAssignPart(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------PartCfg------------------------------------
	@Override
	public PartCfgInfo createPartCfg(PartCfgCreateObj _dto) {
		try {
			return MbomFO.parsePartCfg(getEkpKernelRmi().createPartCfg(MbomFO.parsePartCfgCreateObjRemote(_dto)));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deletePartCfg(String _uid) {
		try {
			return getEkpKernelRmi().deletePartCfg(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public PartCfgInfo loadPartCfg(String _uid) {
		try {
			PartCfgRemote remote = getEkpKernelRmi().loadPartCfg(_uid);
			return remote == null ? null : MbomFO.parsePartCfg(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public PartCfgInfo loadPartCfgById(String _id) {
		try {
			PartCfgRemote remote = getEkpKernelRmi().loadPartCfgById(_id);
			return remote == null ? null : MbomFO.parsePartCfg(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<PartCfgInfo> loadPartCfgList(String _rootPartUid) {
		try {
			List<PartCfgRemote> remoteList = getEkpKernelRmi().loadPartCfgList(_rootPartUid);
			List<PartCfgInfo> list = remoteList.stream().map(MbomFO::parsePartCfg).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean partCfgStartEditing(String _uid) {
		try {
			return getEkpKernelRmi().partCfgStartEditing(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean partCfgRevertStartEditing(String _uid) {
		try {
			return getEkpKernelRmi().partCfgRevertStartEditing(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean partCfgPublish(String _uid) {
		try {
			return getEkpKernelRmi().partCfgPublish(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean partCfgRevertPublish(String _uid) {
		try {
			return getEkpKernelRmi().partCfgRevertPublish(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	// -------------------------------------------------------------------------------
	// ----------------------------------PartCfgConj----------------------------------
	@Override
	public PartCfgConjInfo createPartCfgConj(String _partCfgUid, String _partAcqUid) {
		try {
			return MbomFO.parsePartCfgConj(getEkpKernelRmi().createPartCfgConj(_partCfgUid, _partAcqUid));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deletePartCfgConj(String _uid) {
		try {
			return getEkpKernelRmi().deletePartCfgConj(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public PartCfgConjInfo loadPartCfgConj(String _uid) {
		try {
			PartCfgConjRemote remote = getEkpKernelRmi().loadPartCfgConj(_uid);
			return remote == null ? null : MbomFO.parsePartCfgConj(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public PartCfgConjInfo loadPartCfgConj(String _partCfgUid, String _partAcqUid) {
		try {
			PartCfgConjRemote remote = getEkpKernelRmi().loadPartCfgConj(_partCfgUid, _partAcqUid);
			return remote == null ? null : MbomFO.parsePartCfgConj(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<PartCfgConjInfo> loadPartCfgConjList(String _partCfgUid) {
		try {
			List<PartCfgConjRemote> remoteList = getEkpKernelRmi().loadPartCfgConjList(_partCfgUid);
			List<PartCfgConjInfo> list = remoteList.stream().map(MbomFO::parsePartCfgConj).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------Prod--------------------------------------
	@Override
	public ProdInfo createProd(ProdCreateObj _dto) {
		try {
			return MbomFO.parseProd(getEkpKernelRmi().createProd(MbomFO.parseProdCreateObjRemote(_dto)));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteProd(String _uid) {
		try {
			return getEkpKernelRmi().deleteProd(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public ProdInfo loadProd(String _uid) {
		try {
			ProdRemote remote = getEkpKernelRmi().loadProd(_uid);
			return remote == null ? null : MbomFO.parseProd(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public ProdInfo loadProdById(String _id) {
		try {
			ProdRemote remote = getEkpKernelRmi().loadProdById(_id);
			return remote == null ? null : MbomFO.parseProd(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<ProdInfo> loadProdList() {
		try {
			List<ProdRemote> remoteList = getEkpKernelRmi().loadProdList();
			List<ProdInfo> list = remoteList.stream().map(MbomFO::parseProd).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------ProdCtl------------------------------------
	@Override
	public ProdCtlInfo createProdCtl(ProdCtlCreateObj _dto) {
		try {
			return MbomFO.parseProdCtl(getEkpKernelRmi().createProdCtl(MbomFO.parseProdCtlCreateObjRemote(_dto)));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteProdCtl(String _uid) {
		try {
			return getEkpKernelRmi().deleteProdCtl(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public ProdCtlInfo loadProdCtl(String _uid) {
		try {
			ProdCtlRemote remote = getEkpKernelRmi().loadProdCtl(_uid);
			return remote == null ? null : MbomFO.parseProdCtl(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public ProdCtlInfo loadProdCtlById(String _id) {
		try {
			ProdCtlRemote remote = getEkpKernelRmi().loadProdCtlById(_id);
			return remote == null ? null : MbomFO.parseProdCtl(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<ProdCtlInfo> loadProdCtlList(String _parentUid) {
		try {
			List<ProdCtlRemote> remoteList = getEkpKernelRmi().loadProdCtlList(_parentUid);
			List<ProdCtlInfo> list = remoteList.stream().map(MbomFO::parseProdCtl).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<ProdCtlInfo> loadProdCtlListLv1(String _prodUid) {
		try {
			List<ProdCtlRemote> remoteList = getEkpKernelRmi().loadProdCtlListLv1(_prodUid);
			List<ProdCtlInfo> list = remoteList.stream().map(MbomFO::parseProdCtl).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean prodCtlAssignParent(String _uid, String _parentUid, String _parentId) {
		try {
			return getEkpKernelRmi().prodCtlAssignParent(_uid, _parentUid, _parentId);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean prodCtlUnassignParent(String _uid) {
		try {
			return getEkpKernelRmi().prodCtlUnassignParent(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean prodCtlAssignProd(String _uid, String _prodUid) {
		try {
			return getEkpKernelRmi().prodCtlAssignProd(_uid, _prodUid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean prodCtlUnassignProd(String _uid) {
		try {
			return getEkpKernelRmi().prodCtlUnassignProd(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	// -------------------------------------------------------------------------------
	// ------------------------------ProdCtlPartCfgConj-------------------------------
	@Override
	public ProdCtlPartCfgConjInfo createProdCtlPartCfgConj(String _prodCtlUid, String _partCfgUid) {
		try {
			return MbomFO.parseProdCtlPartCfgConj(getEkpKernelRmi().createProdCtlPartCfgConj(_prodCtlUid, _partCfgUid));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteProdCtlPartCfgConj(String _uid) {
		try {
			return getEkpKernelRmi().deleteProdCtlPartCfgConj(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public ProdCtlPartCfgConjInfo loadProdCtlPartCfgConj(String _uid) {
		try {
			ProdCtlPartCfgConjRemote remote = getEkpKernelRmi().loadProdCtlPartCfgConj(_uid);
			return remote == null ? null : MbomFO.parseProdCtlPartCfgConj(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public ProdCtlPartCfgConjInfo loadProdCtlPartCfgConj(String _prodCtlUid, String _partCfgUid) {
		try {
			ProdCtlPartCfgConjRemote remote = getEkpKernelRmi().loadProdCtlPartCfgConj(_prodCtlUid, _partCfgUid);
			return remote == null ? null : MbomFO.parseProdCtlPartCfgConj(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<ProdCtlPartCfgConjInfo> loadProdCtlPartCfgConjList1(String _prodCtlUid) {
		try {
			List<ProdCtlPartCfgConjRemote> remoteList = getEkpKernelRmi().loadProdCtlPartCfgConjList1(_prodCtlUid);
			List<ProdCtlPartCfgConjInfo> list = remoteList.stream().map(MbomFO::parseProdCtlPartCfgConj)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<ProdCtlPartCfgConjInfo> loadProdCtlPartCfgConjList2(String _partCfgUid) {
		try {
			List<ProdCtlPartCfgConjRemote> remoteList = getEkpKernelRmi().loadProdCtlPartCfgConjList2(_partCfgUid);
			List<ProdCtlPartCfgConjInfo> list = remoteList.stream().map(MbomFO::parseProdCtlPartCfgConj)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------ProdMod------------------------------------
	@Override
	public ProdModInfo createProdMod(ProdModCreateObj _dto) {
		try {
			return MbomFO.parseProdMod(getEkpKernelRmi().createProdMod(MbomFO.parseProdModCreateObjRemote(_dto)));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteProdMod(String _uid) {
		try {
			return getEkpKernelRmi().deleteProdMod(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public ProdModInfo loadProdMod(String _uid) {
		try {
			ProdModRemote remote = getEkpKernelRmi().loadProdMod(_uid);
			return remote == null ? null : MbomFO.parseProdMod(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public ProdModInfo loadProdModById(String _id) {
		try {
			ProdModRemote remote = getEkpKernelRmi().loadProdModById(_id);
			return remote == null ? null : MbomFO.parseProdMod(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<ProdModInfo> loadProdModList(String _prodUid) {
		try {
			List<ProdModRemote> remoteList = getEkpKernelRmi().loadProdModList(_prodUid);
			List<ProdModInfo> list = remoteList.stream().map(MbomFO::parseProdMod).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// ----------------------------------ProdModItem----------------------------------
	@Override
	public ProdModItemInfo createProdModItem(String prodModUid, String prodCtlUid) {
		try {
			return MbomFO.parseProdModItem(getEkpKernelRmi().createProdModItem(prodModUid, prodCtlUid));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteProdModItem(String _uid) {
		try {
			return getEkpKernelRmi().deleteProdModItem(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public ProdModItemInfo loadProdModItem(String _uid) {
		try {
			ProdModItemRemote remote = getEkpKernelRmi().loadProdModItem(_uid);
			return remote == null ? null : MbomFO.parseProdModItem(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public ProdModItemInfo loadProdModItem(String _prodModUid, String _prodCtlUid) {
		try {
			ProdModItemRemote remote = getEkpKernelRmi().loadProdModItem(_prodModUid, _prodCtlUid);
			return remote == null ? null : MbomFO.parseProdModItem(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public ProdModItemInfo loadProdModItem(String _prodModUid, String _prodCtlUid, String _partCfgUid) {
		try {
			ProdModItemRemote remote = getEkpKernelRmi().loadProdModItem(_prodModUid, _prodCtlUid, _partCfgUid);
			return remote == null ? null : MbomFO.parseProdModItem(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<ProdModItemInfo> loadProdModItemList(String _prodModUid) {
		try {
			List<ProdModItemRemote> remoteList = getEkpKernelRmi().loadProdModItemList(_prodModUid);
			List<ProdModItemInfo> list = remoteList.stream().map(MbomFO::parseProdModItem).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean prodModItemAssignPartCfg(String _uid, String _partCfgUid) {
		try {
			return getEkpKernelRmi().prodModItemAssignPartCfg(_uid, _partCfgUid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean prodModItemUnassignPartCfg(String _uid) {
		try {
			return getEkpKernelRmi().prodModItemUnassignPartCfg(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

}
