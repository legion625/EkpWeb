package ekp.data.service.sd;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import ekp.data.SdDataService;
import ekp.data.service.pu.PuFO;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.query.PurchQueryParam;
import ekp.data.service.sd.query.SalesOrderItemQueryParam;
import ekp.data.service.sd.query.SalesOrderQueryParam;
import ekp.serviceFacade.rmi.pu.PurchRemote;
import ekp.serviceFacade.rmi.sd.SalesOrderCreateObjRemote;
import ekp.serviceFacade.rmi.sd.SalesOrderItemCreateObjRemote;
import ekp.serviceFacade.rmi.sd.SalesOrderItemRemote;
import ekp.serviceFacade.rmi.sd.SalesOrderRemote;
import legion.util.LogUtil;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public class SdDataServiceImp implements SdDataService {
	private Logger log = LoggerFactory.getLogger(SdDataServiceImp.class);

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
	// ----------------------------------SalesOrder-----------------------------------
	@Override
	public SalesOrderInfo createSalesOrder(SalesOrderCreateObj _dto) {
		try {
			SalesOrderCreateObjRemote dto = SdFO.parseSalesOrderCreateObjRemote(_dto);
			return SdFO.parseSalesOrder(getEkpKernelRmi().createSalesOrder(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteSalesOrder(String _uid) {
		try {
			return getEkpKernelRmi().deleteSalesOrder(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public SalesOrderInfo loadSalesOrder(String _uid) {
		try {
			SalesOrderRemote remote = getEkpKernelRmi().loadSalesOrder(_uid);
			return remote == null ? null : SdFO.parseSalesOrder(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public SalesOrderInfo loadSalesOrderBySosn(String _sosn) {
		try {
			SalesOrderRemote remote = getEkpKernelRmi().loadSalesOrderBySosn(_sosn);
			return remote == null ? null : SdFO.parseSalesOrder(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public QueryOperation<SalesOrderQueryParam, SalesOrderInfo> searchSalesOrder(
			QueryOperation<SalesOrderQueryParam, SalesOrderInfo> _param,
			Map<SalesOrderQueryParam, QueryValue[]> _existsDetailMap) {
		try {
			QueryOperation<SalesOrderQueryParam, SalesOrderRemote> paramRemote = (QueryOperation<SalesOrderQueryParam, SalesOrderRemote>) _param
					.copy();
			paramRemote = getEkpKernelRmi().searchSalesOrder(paramRemote, _existsDetailMap);
			List<SalesOrderInfo> list = paramRemote.getQueryResult().stream().map(SdFO::parseSalesOrder)
					.collect(Collectors.toList());
			_param.setQueryResult(list);
			_param.setTotal(paramRemote.getTotal());
			return _param;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	// -------------------------------------------------------------------------------
	// --------------------------------SalesOrderItem---------------------------------
	@Override
	public SalesOrderItemInfo createSalesOrderItem(String _soUid, SalesOrderItemCreateObj _dto) {
		try {
			SalesOrderItemCreateObjRemote dto = SdFO.parseSalesOrderItemCreateObjRemote(_dto);
			return SdFO.parseSalesOrderItem(getEkpKernelRmi().createSalesOrderItem(_soUid, dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteSalesOrderItem(String _uid) {
		try {
			return getEkpKernelRmi().deleteSalesOrderItem(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public SalesOrderItemInfo loadSalesOrderItem(String _uid) {
		try {
			return SdFO.parseSalesOrderItem(getEkpKernelRmi().loadSalesOrderItem(_uid));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<SalesOrderItemInfo> loadSalesOrderItemList(String _soUid) {
		try {
			List<SalesOrderItemRemote> remoteList = getEkpKernelRmi().loadSalesOrderItemList(_soUid);
			List<SalesOrderItemInfo> list = remoteList.stream().map(SdFO::parseSalesOrderItem)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public List<SalesOrderItemInfo> loadSalesOrderItemListMyMm(String _mmUid){
		try {
			List<SalesOrderItemRemote> remoteList = getEkpKernelRmi().loadSalesOrderItemListMyMm(_mmUid);
			List<SalesOrderItemInfo> list = remoteList.stream().map(SdFO::parseSalesOrderItem)
					.collect(Collectors.toList());
			return list;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public QueryOperation<SalesOrderItemQueryParam, SalesOrderItemInfo> searchSalesOrderItem(
			QueryOperation<SalesOrderItemQueryParam, SalesOrderItemInfo> _param){
		try {
			QueryOperation<SalesOrderItemQueryParam, SalesOrderItemRemote> paramRemote = (QueryOperation<SalesOrderItemQueryParam, SalesOrderItemRemote>) _param
					.copy();
			paramRemote = getEkpKernelRmi().searchSalesOrderItem(paramRemote);
			List<SalesOrderItemInfo> list = paramRemote.getQueryResult().stream().map(SdFO::parseSalesOrderItem)
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
	public boolean soiFinishDeliver(String _uid, long _finishDeliveredDate) {
		try {
			return getEkpKernelRmi().soiFinishDeliver(_uid, _finishDeliveredDate);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean soiRevertFinishDeliver(String _uid) {
		try {
			return getEkpKernelRmi().soiRevertFinishDeliver(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

}
