package ekp.web.control.zk.mbom;

import ekp.data.service.mbom.PpartSkewer;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.data.service.mbom.query.PpartSkewerQueryParam;
import ekp.web.control.zk.common.NormalSearchLine;
import ekp.web.control.zk.common.SearchConfig;

public class PpartSkewerSearchConfig extends SearchConfig<PpartSkewerQueryParam, PpartSkewer> {

	@Override
	protected NormalSearchLine<PpartSkewerQueryParam, PpartSkewer>[] initNormalSearchLines() {
		NormalSearchLine nspParentPartPin = NormalSearchLine.ofTxbLine("父階PIN", PpartSkewerQueryParam.P_PIN);
		NormalSearchLine nspParentPartName= NormalSearchLine.ofTxbLine("父階料件名稱", PpartSkewerQueryParam.P_NAME);
		NormalSearchLine nspPaId =  NormalSearchLine.ofTxbLine("父階獲取方式ID", PpartSkewerQueryParam.PA_ID);
		NormalSearchLine nspPaName=  NormalSearchLine.ofTxbLine("父階獲取方式名稱", PpartSkewerQueryParam.PA_NAME);
		NormalSearchLine nspPartPin =  NormalSearchLine.ofTxbLine("子階PIN", PpartSkewerQueryParam.PART_PIN);
		NormalSearchLine nspPartName=  NormalSearchLine.ofTxbLine("子階料件名稱", PpartSkewerQueryParam.PART_NAME);
		NormalSearchLine nspPartCfgRootPartPin = NormalSearchLine.ofTxbLine("構型根節點PIN", PpartSkewerQueryParam.PC_ROOT_PART_PIN);
		return new NormalSearchLine[] {
				nspParentPartPin, nspParentPartName, nspPaId,nspPaName, nspPartPin
				, nspPartName, nspPartCfgRootPartPin
		};
	}

}