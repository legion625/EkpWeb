package ekp.web.control.zk.mbom;

import ekp.data.service.invt.query.InvtOrderQueryParam;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.invt.type.InvtOrderStatus;
import ekp.mbom.type.PartCfgStatus;
import ekp.web.control.zk.common.NormalSearchLine;
import ekp.web.control.zk.common.SearchConfig;

public class PartCfgSearchConfig extends SearchConfig<PartCfgQueryParam, PartCfgInfo> {

	@Override
	protected NormalSearchLine<PartCfgQueryParam, PartCfgInfo>[] initNormalSearchLines() {
		NormalSearchLine nspRootPartPin = NormalSearchLine.ofTxbLine("根節點PIN", PartCfgQueryParam.ROOT_PART_PIN);
		NormalSearchLine nspStatusIdx = NormalSearchLine.ofCbbLine("狀態", PartCfgStatus.values(),
				PartCfgQueryParam.STATUS_IDX);
		NormalSearchLine nspId = NormalSearchLine.ofTxbLine("ID", PartCfgQueryParam.ID);
		NormalSearchLine nspName = NormalSearchLine.ofTxbLine("名稱", PartCfgQueryParam.NAME);
		NormalSearchLine nspDesp = NormalSearchLine.ofTxbLine("描述", PartCfgQueryParam.DESP);
		return new NormalSearchLine[] {nspRootPartPin, nspStatusIdx, nspId, nspName,nspDesp };
	}

}
