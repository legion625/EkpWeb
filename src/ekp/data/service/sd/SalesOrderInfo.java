package ekp.data.service.sd;

import legion.ObjectModelInfo;

public interface SalesOrderInfo extends ObjectModelInfo{

	String getSosn();

	String getTitle();

	String getCustomerName();

	String getCustomerBan();

	String getSalerId();

	String getSalerName();

	long getSaleDate();

}