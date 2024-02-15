package ekp.web.control.zk.sd;

import ekp.data.service.mf.query.WorkorderQueryParam;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.query.SalesOrderQueryParam;
import ekp.web.control.zk.common.NormalSearchLine;
import ekp.web.control.zk.common.SearchConfig;

public class SoSearchConfig extends SearchConfig<SalesOrderQueryParam, SalesOrderInfo>{

	@Override
	protected NormalSearchLine<SalesOrderQueryParam, SalesOrderInfo>[] initNormalSearchLines() {
		NormalSearchLine nspSosn = NormalSearchLine.ofTxbLine("Sales Order No.", SalesOrderQueryParam.SOSN);
		NormalSearchLine nspTitle = NormalSearchLine.ofTxbLine("Title", SalesOrderQueryParam.TITLE);
		NormalSearchLine nspCustomerName = NormalSearchLine.ofTxbLine("Customer Name", SalesOrderQueryParam.CUSTOMER_NAME);
		NormalSearchLine nspCustomerBan = NormalSearchLine.ofTxbLine("Customer BAN", SalesOrderQueryParam.CUSTOMER_BAN);
		NormalSearchLine nspSalerName = NormalSearchLine.ofTxbLine("Sales Name", SalesOrderQueryParam.SALER_NAME);
		return new NormalSearchLine[] {nspSosn,nspTitle, nspCustomerName, nspCustomerBan, nspSalerName };
	}
	

}
