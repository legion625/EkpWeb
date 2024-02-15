package ekp.web.control.zk.mbom;

import ekp.data.service.mbom.PpartSkewer;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.data.service.mbom.query.PpartSkewerQueryParam;
import ekp.web.control.zk.common.NormalSearchLine;
import ekp.web.control.zk.common.SearchConfig;

public class PpartSkewerSearchConfig extends SearchConfig<PpartSkewerQueryParam, PpartSkewer> {

	@Override
	protected NormalSearchLine<PpartSkewerQueryParam, PpartSkewer>[] initNormalSearchLines() {
		NormalSearchLine nspParentPartPin = NormalSearchLine.ofTxbLine("Parent Part PIN", PpartSkewerQueryParam.P_PIN);
		NormalSearchLine nspParentPartName= NormalSearchLine.ofTxbLine("Parent Part Name", PpartSkewerQueryParam.P_NAME);
		NormalSearchLine nspPaId =  NormalSearchLine.ofTxbLine("Parent Part Acq. ID", PpartSkewerQueryParam.PA_ID);
		NormalSearchLine nspPaName=  NormalSearchLine.ofTxbLine("Parent Part Acq. Name", PpartSkewerQueryParam.PA_NAME);
		NormalSearchLine nspPartPin =  NormalSearchLine.ofTxbLine("Child Part PIN", PpartSkewerQueryParam.PART_PIN);
		NormalSearchLine nspPartName=  NormalSearchLine.ofTxbLine("Child Part Name", PpartSkewerQueryParam.PART_NAME);
		NormalSearchLine nspPartCfgRootPartPin = NormalSearchLine.ofTxbLine("Part Conf. Root Part PIN", PpartSkewerQueryParam.PC_ROOT_PART_PIN);
		return new NormalSearchLine[] {
				nspParentPartPin, nspParentPartName, nspPaId,nspPaName, nspPartPin
				, nspPartName, nspPartCfgRootPartPin
		};
	}

}
