package ekp.data.service.invt;

import ekp.data.BizObjLoader;
import ekp.data.InvtDataService;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class MaterialBinStockInfoDto extends ObjectModelInfoDto implements MaterialBinStockInfo {

	protected MaterialBinStockInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String mmUid; // material master的uid
	private String mano; // (redundant attribute)

	private String wrhsBinUid; // biz key (對應的wrhsBin)

	// 所有MaterialBinStockBatch的庫存量和金額(這是redundant屬性，必須確保一致)
	private double sumStockQty;
	private double sumStockValue;

	@Override
	public String getMmUid() {
		return mmUid;
	}

	void setMmUid(String mmUid) {
		this.mmUid = mmUid;
	}

	@Override
	public String getMano() {
		return mano;
	}

	void setMano(String mano) {
		this.mano = mano;
	}

	@Override
	public String getWrhsBinUid() {
		return wrhsBinUid;
	}

	void setWrhsBinUid(String wrhsBinUid) {
		this.wrhsBinUid = wrhsBinUid;
	}

	@Override
	public double getSumStockQty() {
		return sumStockQty;
	}

	void setSumStockQty(double sumStockQty) {
		this.sumStockQty = sumStockQty;
	}

	@Override
	public double getSumStockValue() {
		return sumStockValue;
	}

	void setSumStockValue(double sumStockValue) {
		this.sumStockValue = sumStockValue;
	}

	// -------------------------------------------------------------------------------
	private BizObjLoader<MaterialMasterInfo> mmLoader = BizObjLoader.of(
			() -> DataServiceFactory.getInstance().getService(InvtDataService.class).loadMaterialMaster(getMmUid()));

	@Override
	public MaterialMasterInfo getMm() {
		return mmLoader.getObj();
	}

	private BizObjLoader<WrhsBinInfo> wrhsBinLoader = BizObjLoader
			.of(() -> DataServiceFactory.getInstance().getService(InvtDataService.class).loadWrhsBin(getWrhsBinUid()));

	@Override
	public WrhsBinInfo getWrhsBin() {
		return wrhsBinLoader.getObj();
	}
}
