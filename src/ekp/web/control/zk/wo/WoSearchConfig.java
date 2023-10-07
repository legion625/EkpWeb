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
		NormalSearchLine nspWoNo = NormalSearchLine.ofTxbLine("工令編號", WorkorderQueryParam.WO_NO);
		NormalSearchLine nspStatusIdx = NormalSearchLine.ofCbbLine("狀態", WorkorderStatus.values(), WorkorderQueryParam.STATUS_IDX);
		NormalSearchLine nspPartPin = NormalSearchLine.ofTxbLine("零件編號", WorkorderQueryParam.PART_PIN);
//		NormalSearchLine nspPartAcqId = NormalSearchLine.ofCbbLine("獲得方式",PartAcquisitionType.values(),  WorkorderQueryParam.PART_ACQ_ID);
		NormalSearchLine nspPartAcqMmMano = NormalSearchLine.ofTxbLine("料號", WorkorderQueryParam.PART_ACQ_MM_MANO);
		NormalSearchLine nspPartCfgId = NormalSearchLine.ofTxbLine("構型ID", WorkorderQueryParam.PART_CFG_ID);
		return new NormalSearchLine[] {nspWoNo,nspStatusIdx, nspPartPin, nspPartAcqMmMano,nspPartCfgId  };
	}

}
