package ekp.data;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import ekp.data.service.sd.BizPartnerCreateObj;
import ekp.data.service.sd.BizPartnerInfo;
import ekp.data.service.sd.SalesOrderCreateObj;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.SalesOrderItemCreateObj;
import ekp.data.service.sd.SalesOrderItemInfo;
import ekp.data.service.sd.query.SalesOrderItemQueryParam;
import ekp.data.service.sd.query.SalesOrderQueryParam;
import ekp.serviceFacade.rmi.sd.BizPartnerCreateObjRemote;
import ekp.serviceFacade.rmi.sd.BizPartnerRemote;
import ekp.serviceFacade.rmi.sd.SalesOrderCreateObjRemote;
import ekp.serviceFacade.rmi.sd.SalesOrderItemCreateObjRemote;
import ekp.serviceFacade.rmi.sd.SalesOrderItemRemote;
import ekp.serviceFacade.rmi.sd.SalesOrderRemote;
import legion.IntegrationService;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public interface SdDataService extends IntegrationService, EkpKernelRmi {
	// -------------------------------------------------------------------------------
	// ----------------------------------BizPartner-----------------------------------
	public BizPartnerInfo createBizPartner(BizPartnerCreateObj _dto) ;
	public boolean deleteBizPartner(String _uid) ;
	public BizPartnerInfo loadBizPartner(String _uid) ;
	public BizPartnerInfo loadBizPartnerByBpsn(String _bpsn) ;
	public List<BizPartnerInfo> loadBizPartnerList() ;
	
	// -------------------------------------------------------------------------------
	// ----------------------------------SalesOrder-----------------------------------
	public SalesOrderInfo createSalesOrder(SalesOrderCreateObj _dto);

	public boolean deleteSalesOrder(String _uid);

	public SalesOrderInfo loadSalesOrder(String _uid);

	public SalesOrderInfo loadSalesOrderBySosn(String _sosn);

	public QueryOperation<SalesOrderQueryParam, SalesOrderInfo> searchSalesOrder(
			QueryOperation<SalesOrderQueryParam, SalesOrderInfo> _param,
			Map<SalesOrderQueryParam, QueryValue[]> _existsDetailMap);

	// -------------------------------------------------------------------------------
	// --------------------------------SalesOrderItem---------------------------------
	public SalesOrderItemInfo createSalesOrderItem(String _soUid, SalesOrderItemCreateObj _dto);

	public boolean deleteSalesOrderItem(String _uid);

	public SalesOrderItemInfo loadSalesOrderItem(String _uid);

	public List<SalesOrderItemInfo> loadSalesOrderItemList(String _soUid);

	public List<SalesOrderItemInfo> loadSalesOrderItemListMyMm(String _mmUid);

	public QueryOperation<SalesOrderItemQueryParam, SalesOrderItemInfo> searchSalesOrderItem(
			QueryOperation<SalesOrderItemQueryParam, SalesOrderItemInfo> _param);

	public boolean soiFinishDeliver(String _uid, long _finishDeliveredDate);

	public boolean soiRevertFinishDeliver(String _uid);
}
