package ekp.invt.bpu.material;

import ekp.data.InvtDataService;
import ekp.data.service.invt.MaterialBinStockBatchCreateObj;
import ekp.data.service.invt.MaterialBinStockBatchInfo;
import ekp.data.service.invt.MaterialBinStockInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public class MbsbBuilder extends Bpu<MaterialBinStockBatchInfo> {
	private static InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);

	/* base */
	private String mbsUid;
	private String miUid;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected Bpu<MaterialBinStockBatchInfo> appendBase() {
		// TODO Auto-generated method stub
		return null;
	}

	public MbsbBuilder appendMbsUid(String mbsUid) {
		this.mbsUid = mbsUid;
		return this;
	}

	public MbsbBuilder appendMiUid(String miUid) {
		this.miUid = miUid;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getMbsUid() {
		return mbsUid;
	}

	public String getMiUid() {
		return miUid;
	}

	// -------------------------------------------------------------------------------
	private MaterialBinStockBatchCreateObj packMaterialBinStockBatchCreateObj() {
		MaterialBinStockBatchCreateObj dto = new MaterialBinStockBatchCreateObj();
		dto.setMbsUid(getMbsUid());
		dto.setMiUid(getMiUid());
		return dto;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;

		if (DataFO.isEmptyString(getMbsUid())) {
			_msg.append("mbsUid should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		if (DataFO.isEmptyString(getMiUid())) {
			_msg.append("miUid should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected MaterialBinStockBatchInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		MaterialBinStockBatchInfo mbsb = invtDataService
				.createMaterialBinStockBatch(packMaterialBinStockBatchCreateObj());
		if (mbsb == null) {
			tt.travel();
			log.error("invtDataService.createMaterialBinStockBatch return null.");
			return null;
		}
		tt.addSite("revert createMaterialBinStockBatch",
				() -> invtDataService.deleteMaterialBinStockBatch(mbsb.getUid()));
		log.info("invtDataService.createMaterialBinStockBatch [{}][{}][{}]", mbsb.getUid(), mbsb.getMbsUid(),
				mbsb.getMiUid());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return mbsb;
	}

}
