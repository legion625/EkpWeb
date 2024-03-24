package ekp.data.service.sd;

import ekp.data.SdDataService;
import legion.DataServiceFactory;
import legion.ObjectModelInfo;

public interface BizPartnerInfo extends ObjectModelInfo {

	String getBpsn();

	String getName();

	String getBan();

	boolean isSupplier();

	boolean isCustomer();

}