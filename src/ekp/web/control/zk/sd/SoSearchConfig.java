package ekp.web.control.zk.sd;

import ekp.data.service.mf.query.WorkorderQueryParam;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.query.SalesOrderQueryParam;
import ekp.web.control.zk.common.NormalSearchLine;
import ekp.web.control.zk.common.SearchConfig;

public class SoSearchConfig extends SearchConfig<SalesOrderQueryParam, SalesOrderInfo>{

	@Override
	protected NormalSearchLine<SalesOrderQueryParam, SalesOrderInfo>[] initNormalSearchLines() {
		NormalSearchLine nspSosn = NormalSearchLine.ofTxbLine("序號", SalesOrderQueryParam.SOSN);
		NormalSearchLine nspTitle = NormalSearchLine.ofTxbLine("名稱", SalesOrderQueryParam.TITLE);
		NormalSearchLine nspCustomerName = NormalSearchLine.ofTxbLine("客戶名稱", SalesOrderQueryParam.CUSTOMER_NAME);
		NormalSearchLine nspCustomerBan = NormalSearchLine.ofTxbLine("客戶統編", SalesOrderQueryParam.CUSTOMER_BAN);
		NormalSearchLine nspSalerName = NormalSearchLine.ofTxbLine("銷售人姓名", SalesOrderQueryParam.SALER_NAME);
		return new NormalSearchLine[] {nspSosn,nspTitle, nspCustomerName, nspCustomerBan, nspSalerName };
	}
	

}
