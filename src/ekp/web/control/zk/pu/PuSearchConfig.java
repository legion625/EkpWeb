package ekp.web.control.zk.pu;

import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.query.PurchQueryParam;
import ekp.pu.type.PurchPerfStatus;
import ekp.web.control.zk.common.NormalSearchLine;
import ekp.web.control.zk.common.SearchConfig;

public class PuSearchConfig extends SearchConfig<PurchQueryParam, PurchInfo> {

	// -------------------------------------------------------------------------------
	@Override
	protected NormalSearchLine<PurchQueryParam, PurchInfo>[] initNormalSearchLines() {

		NormalSearchLine<PurchQueryParam, PurchInfo> nslPuNo = NormalSearchLine.ofTxbLine("購案案號",
				PurchQueryParam.PU_NO);
		NormalSearchLine<PurchQueryParam, PurchInfo> nslTitle = NormalSearchLine.ofTxbLine("購案名稱",
				PurchQueryParam.TITLE);
		NormalSearchLine<PurchQueryParam, PurchInfo> nslSupplierBan = NormalSearchLine.ofTxbLine("供應商統編",
				PurchQueryParam.SUPPLIER_BAN);
		NormalSearchLine<PurchQueryParam, PurchInfo> nslSupplierName = NormalSearchLine.ofTxbLine("供應商名稱",
				PurchQueryParam.SUPPLIER_NAME);
		
		NormalSearchLine<PurchQueryParam, PurchInfo> nslPerfStatusIdx = 
				NormalSearchLine.ofCbbLine("履約狀態", PurchPerfStatus.values(),PurchQueryParam.PERF_STATUS_IDX);

		return new NormalSearchLine[] { nslPuNo, nslTitle, nslSupplierBan, nslSupplierName
				, nslPerfStatusIdx};
	}

}