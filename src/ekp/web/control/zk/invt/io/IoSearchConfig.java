package ekp.web.control.zk.invt.io;

import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.query.InvtOrderQueryParam;
import ekp.data.service.sd.query.SalesOrderQueryParam;
import ekp.invt.type.InvtOrderStatus;
import ekp.web.control.zk.common.NormalSearchLine;
import ekp.web.control.zk.common.SearchConfig;

public class IoSearchConfig extends SearchConfig<InvtOrderQueryParam, InvtOrderInfo> {

	@Override
	protected NormalSearchLine<InvtOrderQueryParam, InvtOrderInfo>[] initNormalSearchLines() {
		NormalSearchLine nspIosn = NormalSearchLine.ofTxbLine("序號", InvtOrderQueryParam.IOSN);
		NormalSearchLine nspStatusIdx = NormalSearchLine.ofCbbLine("狀態", InvtOrderStatus.values(),
				InvtOrderQueryParam.STATUS_IDX);
		NormalSearchLine nspApplierName = NormalSearchLine.ofTxbLine("申請人姓名", InvtOrderQueryParam.APPLIER_NAME);
		NormalSearchLine nspRemark = NormalSearchLine.ofTxbLine("備註", InvtOrderQueryParam.REMARK);
		return new NormalSearchLine[] {nspIosn,nspStatusIdx,nspApplierName, nspRemark  };
	}

}
