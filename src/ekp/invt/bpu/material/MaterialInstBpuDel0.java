package ekp.invt.bpu.material;

import ekp.data.service.invt.MaterialInstInfo;
import ekp.invt.bpu.wrhsLoc.WrhsBinBpuDel0;
import legion.util.TimeTraveler;

public class MaterialInstBpuDel0 extends MaterialInstBpu{
	/* base */
	private MaterialInstInfo mi;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected MaterialInstBpuDel0 appendBase() {
		/* base */
		mi = (MaterialInstInfo) args[0];
		appendMi(mi);

		/* data */
		// none

		return this;
	}
	
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		
		// TODO 檢查是否有MaterialBinStockBatch指向它
		return true;
	}
	
	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		if(!invtDataService.deleteMaterialInst(getMi().getUid())) {
			log.error("invtDataService.deleteMaterialInst return false. [{}][{}]", getMi().getUid(), getMi().getMisn());
			return false;
		}
		log.info("invtDataService.deleteMaterialInst. [{}][{}]", getMi().getUid(), getMi().getMisn());
		return true;
	}
}
