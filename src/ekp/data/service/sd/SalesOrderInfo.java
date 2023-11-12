package ekp.data.service.sd;

import java.util.List;

import legion.ObjectModelInfo;

public interface SalesOrderInfo extends ObjectModelInfo {

	String getSosn();

	String getTitle();

	String getCustomerUid();
	
	String getCustomerName();

	String getCustomerBan();

	String getSalerId();

	String getSalerName();

	long getSaleDate();

	// -------------------------------------------------------------------------------
	SalesOrderInfo reload();

	List<SalesOrderItemInfo> getSalesOrderItemList();

	default double getSumSoiAmt() {
		return getSalesOrderItemList().stream().mapToDouble(SalesOrderItemInfo::getValue).sum();
	}
}