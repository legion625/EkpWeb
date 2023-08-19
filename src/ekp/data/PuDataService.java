package ekp.data;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import ekp.data.service.pu.PurchCreateObj;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.PurchItemCreateObj;
import ekp.data.service.pu.PurchItemInfo;
import ekp.data.service.pu.query.PurchQueryParam;
import legion.IntegrationService;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public interface PuDataService extends IntegrationService, EkpKernelRmi {

	// -------------------------------------------------------------------------------
	// -------------------------------------Purch-------------------------------------
	public PurchInfo createPurch(PurchCreateObj _dto);

	public boolean deletePurch(String _uid);

	public PurchInfo loadPurch(String _uid);

	public PurchInfo loadPurchByPuNo(String _puNo);

	public QueryOperation<PurchQueryParam, PurchInfo> searchPurch(QueryOperation<PurchQueryParam, PurchInfo> _param,
			Map<PurchQueryParam, QueryValue[]> _existsDetailMap);

	public boolean purchToPerf(String _uid);

	public boolean purchRevertToPerf(String _uid);

	public boolean purchPerf(String _uid, long _perfTime);

	public boolean purchRevertPerf(String _uid);

	// -------------------------------------------------------------------------------
	// -----------------------------------PurchItem-----------------------------------
	public PurchItemInfo createPurchItem(PurchItemCreateObj _dto);

	public boolean deletePurchItem(String _uid);

	public PurchItemInfo loadPurchItem(String _uid);

	public List<PurchItemInfo> loadPurchItemList(String _purchUid);

	public List<PurchItemInfo> loadPurchItemListByMm(String _mmUid);
}
