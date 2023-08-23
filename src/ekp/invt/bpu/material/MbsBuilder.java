package ekp.invt.bpu.material;

import ekp.data.InvtDataService;
import ekp.data.service.invt.MaterialBinStockCreateObj;
import ekp.data.service.invt.MaterialBinStockInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public class MbsBuilder extends Bpu<MaterialBinStockInfo> {
	private static InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);

	/* base */
	private String mmUid;
	private String mano;
	private String wrhsBinUid;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected Bpu<MaterialBinStockInfo> appendBase() {
		// TODO Auto-generated method stub
		return null;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public MbsBuilder appendMmUid(String mmUid) {
		this.mmUid = mmUid;
		return this;
	}

	public MbsBuilder appendMano(String mano) {
		this.mano = mano;
		return this;
	}

	public MbsBuilder appendwrhsBinUid(String wrhsBinUid) {
		this.wrhsBinUid = wrhsBinUid;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getMmUid() {
		return mmUid;
	}

	public String getMano() {
		return mano;
	}

	public String getwrhsBinUid() {
		return wrhsBinUid;
	}

	// -------------------------------------------------------------------------------
	private MaterialBinStockCreateObj packMaterialBinStockCreateObj() {
		MaterialBinStockCreateObj dto = new MaterialBinStockCreateObj();
		dto.setMmUid(getMmUid());
		dto.setMano(getMano());
		dto.setWrhsBinUid(getwrhsBinUid());
		return dto;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = true;

		if (DataFO.isEmptyString(getMmUid())) {
			_msg.append("mmUid should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		if (DataFO.isEmptyString(getMano())) {
			_msg.append("mano should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		if (DataFO.isEmptyString(getwrhsBinUid())) {
			_msg.append("WrhsBin should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	protected MaterialBinStockInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		MaterialBinStockInfo mbs = invtDataService.createMaterialBinStock(packMaterialBinStockCreateObj());
		if (mbs == null) {
			tt.travel();
			log.error("invtDataService.createMaterialBinStock return null.");
			return null;
		}
		tt.addSite("revert createMaterialBinStock", () -> invtDataService.deleteMaterialBinStock(mbs.getUid()));
		log.info("invtDataService.createMaterialBinStock [{}][{}][{}][{}]", mbs.getUid(), mbs.getMmUid(), mbs.getMano(),
				mbs.getWrhsBinUid());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return mbs;
	}

}
