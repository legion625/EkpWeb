package ekp.web.control.zk.mbom;

import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.query.PartQueryParam;
import ekp.web.control.zk.common.NormalSearchLine;
import ekp.web.control.zk.common.SearchConfig;

public class PartSearchConfig extends SearchConfig<PartQueryParam, PartInfo>{

	@Override
	protected NormalSearchLine<PartQueryParam, PartInfo>[] initNormalSearchLines() {
		NormalSearchLine nspPin = NormalSearchLine.ofTxbLine("PIN", PartQueryParam.PIN);
		NormalSearchLine nspName = NormalSearchLine.ofTxbLine("Name", PartQueryParam.NAME);
		NormalSearchLine nspUnitId = NormalSearchLine.ofTxbLine("Unit", PartQueryParam.UNIT_ID);
		return new NormalSearchLine[] { nspPin, nspName, nspUnitId };
	}

}
