package ekp.web.control.zk.wo;

import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.query.WorkorderQueryParam;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mf.type.WorkorderStatus;
import ekp.web.control.zk.common.NormalSearchLine;
import ekp.web.control.zk.common.SearchConfig;

public class WoSearchConfig extends SearchConfig<WorkorderQueryParam, WorkorderInfo>{

	@Override
	protected NormalSearchLine<WorkorderQueryParam, WorkorderInfo>[] initNormalSearchLines() {
		NormalSearchLine nspWoNo = NormalSearchLine.ofTxbLine("Workorder No.", WorkorderQueryParam.WO_NO);
		NormalSearchLine nspStatusIdx = NormalSearchLine.ofCbbLine("Status", WorkorderStatus.values(), WorkorderQueryParam.STATUS_IDX);
		NormalSearchLine nspPartPin = NormalSearchLine.ofTxbLine("Part PIN", WorkorderQueryParam.PART_PIN);
//		NormalSearchLine nspPartAcqId = NormalSearchLine.ofCbbLine("獲得方式",PartAcquisitionType.values(),  WorkorderQueryParam.PART_ACQ_ID);
		NormalSearchLine nspPartAcqMmMano = NormalSearchLine.ofTxbLine("Material No.", WorkorderQueryParam.PART_ACQ_MM_MANO);
		NormalSearchLine nspPartCfgId = NormalSearchLine.ofTxbLine("Part Cfg. ID", WorkorderQueryParam.PART_CFG_ID);
		return new NormalSearchLine[] {nspWoNo,nspStatusIdx, nspPartPin, nspPartAcqMmMano,nspPartCfgId  };
	}

}
