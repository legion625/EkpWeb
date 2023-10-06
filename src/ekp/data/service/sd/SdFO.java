package ekp.data.service.sd;

import ekp.serviceFacade.rmi.sd.SalesOrderCreateObjRemote;
import ekp.serviceFacade.rmi.sd.SalesOrderItemCreateObjRemote;
import ekp.serviceFacade.rmi.sd.SalesOrderItemRemote;
import ekp.serviceFacade.rmi.sd.SalesOrderRemote;

public class SdFO {
	// -------------------------------------------------------------------------------
	// ----------------------------------SalesOrder-----------------------------------
	public static SalesOrderInfo parseSalesOrder(SalesOrderRemote _remote) {
		SalesOrderInfoDto dto = new SalesOrderInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setSosn(_remote.getSosn());
		dto.setTitle(_remote.getTitle());
		dto.setCustomerName(_remote.getCustomerName());
		dto.setCustomerBan(_remote.getCustomerBan());
		dto.setSalerId(_remote.getSalerId());
		dto.setSalerName(_remote.getSalerName());
		dto.setSaleDate(_remote.getSaleDate());
		return dto;
	}

	public static SalesOrderCreateObjRemote parseSalesOrderCreateObjRemote(SalesOrderCreateObj _dto) {
		SalesOrderCreateObjRemote remote = new SalesOrderCreateObjRemote();
		remote.setTitle(_dto.getTitle());
		remote.setCustomerName(_dto.getCustomerName());
		remote.setCustomerBan(_dto.getCustomerBan());
		remote.setSalerId(_dto.getSalerId());
		remote.setSalerName(_dto.getSalerName());
		remote.setSaleDate(_dto.getSaleDate());
		return remote;
	}

	// -------------------------------------------------------------------------------
	// --------------------------------SalesOrderItem---------------------------------
	public static SalesOrderItemInfo parseSalesOrderItem(SalesOrderItemRemote _remote) {
		SalesOrderItemInfoDto dto = new SalesOrderItemInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setSoUid(_remote.getSoUid());
		dto.setMmUid(_remote.getMmUid());
		dto.setMmMano(_remote.getMmMano());
		dto.setMmName(_remote.getMmName());
		dto.setMmSpec(_remote.getMmSpec());
		dto.setQty(_remote.getQty());
		dto.setValue(_remote.getValue());
		dto.setAllDelivered(_remote.isAllDelivered());
		dto.setFinishDeliveredDate(_remote.getFinishDeliveredDate());
		return dto;
	}

	public static SalesOrderItemCreateObjRemote parseSalesOrderItemCreateObjRemote(SalesOrderItemCreateObj _dto) {
		SalesOrderItemCreateObjRemote remote = new SalesOrderItemCreateObjRemote();
		remote.setMmUid(_dto.getMmUid());
		remote.setMmMano(_dto.getMmMano());
		remote.setMmName(_dto.getMmName());
		remote.setMmSpec(_dto.getMmSpec());
		remote.setQty(_dto.getQty());
		remote.setValue(_dto.getValue());
		return remote;
	}

}
