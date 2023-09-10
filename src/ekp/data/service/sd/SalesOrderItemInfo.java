package ekp.data.service.sd;

import legion.ObjectModelInfo;

public interface SalesOrderItemInfo extends ObjectModelInfo{

	String getSoUid();

	String getMmUid();

	String getMmMano();

	String getMmName();

	String getMmSpec();

	double getQty();

	double getValue();

	boolean isAllDelivered();

	long getFinishDeliveredDate();

}