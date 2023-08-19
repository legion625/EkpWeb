package ekp.data.service.pu;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import ekp.data.InvtDataService;
import ekp.data.PuDataService;
import ekp.data.service.invt.InvtFO;
import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.query.InvtOrderItemQueryParam;
import ekp.data.service.pu.query.PurchQueryParam;
import ekp.serviceFacade.rmi.invt.InvtOrderItemRemote;
import ekp.serviceFacade.rmi.invt.WrhsLocCreateObjRemote;
import ekp.serviceFacade.rmi.pu.PurchCreateObjRemote;
import ekp.serviceFacade.rmi.pu.PurchItemCreateObjRemote;
import ekp.serviceFacade.rmi.pu.PurchItemRemote;
import ekp.serviceFacade.rmi.pu.PurchRemote;
import legion.util.LogUtil;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public class PuDataServiceImp implements PuDataService{
	private Logger log = LoggerFactory.getLogger(PuDataServiceImp.class);
	
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
	@Override
	public String getSrcEkpKernelRmi() {
		return srcEkpKernelRmi;
	}
	
	// -------------------------------------------------------------------------------
	// -------------------------------------Purch-------------------------------------
	@Override
	public PurchInfo createPurch(PurchCreateObj _dto) {
		try {
			PurchCreateObjRemote dto = PuFO.parsePurchCreateObjRemote(_dto);
			return PuFO.parsePurch(getEkpKernelRmi().createPurch(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}
	@Override
	public boolean deletePurch(String _uid) {
		try {
			return getEkpKernelRmi().deletePurch(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public PurchInfo loadPurch(String _uid) {
		try {
			PurchRemote remote = getEkpKernelRmi().loadPurch(_uid);
			return remote == null ? null : PuFO.parsePurch(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}
	@Override
	public PurchInfo loadPurchByPuNo(String _puNo) {
		try {
			PurchRemote remote = getEkpKernelRmi().loadPurchByPuNo(_puNo);
			return remote == null ? null : PuFO.parsePurch(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}
	@Override
	public QueryOperation<PurchQueryParam, PurchInfo> searchPurch(QueryOperation<PurchQueryParam, PurchInfo> _param,
			Map<PurchQueryParam, QueryValue[]> _existsDetailMap){
		try {
			QueryOperation<PurchQueryParam, PurchRemote> paramRemote = (QueryOperation<PurchQueryParam, PurchRemote>) _param
					.copy();
			paramRemote = getEkpKernelRmi().searchPurch(paramRemote, _existsDetailMap);
			List<PurchInfo> list = paramRemote.getQueryResult().stream().map(PuFO::parsePurch)
					.collect(Collectors.toList());
			_param.setQueryResult(list);
			_param.setTotal(paramRemote.getTotal());
			return _param;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}
	@Override
	public boolean purchToPerf(String _uid) {
		try {
			return getEkpKernelRmi().purchToPerf(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}
	@Override
	public boolean purchRevertToPerf(String _uid) {
		try {
			return getEkpKernelRmi().purchRevertToPerf(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}
	@Override
	public boolean purchPerf(String _uid, long _perfTime) {
		try {
			return getEkpKernelRmi().purchPerf(_uid, _perfTime);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}
	@Override
	public boolean purchRevertPerf(String _uid) {
		try {
			return getEkpKernelRmi().purchRevertPerf(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------PurchItem-----------------------------------
	@Override
	public PurchItemInfo createPurchItem(PurchItemCreateObj _dto) {
		try {
			PurchItemCreateObjRemote dto = PuFO.parsePurchItemCreateObjRemote(_dto);
			return PuFO.parsePurchItem(getEkpKernelRmi().createPurchItem(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}
	@Override
	public boolean deletePurchItem(String _uid) {
		try {
			return getEkpKernelRmi().deletePurchItem(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}
	@Override
	public PurchItemInfo loadPurchItem(String _uid) {
		try {
			PurchItemRemote remote = getEkpKernelRmi().loadPurchItem(_uid);
			return remote == null ? null : PuFO.parsePurchItem(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<PurchItemInfo> loadPurchItemList(String _purchUid) {
		try {
			List<PurchItemRemote> remoteList = getEkpKernelRmi().loadPurchItemList(_purchUid);
			List<PurchItemInfo> list = remoteList.stream().map(PuFO::parsePurchItem).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}
	@Override
	public List<PurchItemInfo> loadPurchItemListByMm(String _mmUid){
		try {
			List<PurchItemRemote> remoteList = getEkpKernelRmi().loadPurchItemListByMm(_mmUid);
			List<PurchItemInfo> list = remoteList.stream().map(PuFO::parsePurchItem).collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}
}
