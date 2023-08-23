package ekp.data.service.pu;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.PuDataService;
import ekp.pu.type.PurchPerfStatus;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class PurchInfoDto extends ObjectModelInfoDto implements PurchInfo {
	protected PurchInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------attribute-----------------------------------
	private String puNo; // 購案案號
	private String title; // 名稱
	private String supplierName;
	private String supplierBan; // 供應商統編（臺灣）
	private PurchPerfStatus perfStatus; // 履約狀態
	private long perfTime; // 履約時間

	// -------------------------------------------------------------------------------
	// ---------------------------------getter&setter---------------------------------
	@Override
	public String getPuNo() {
		return puNo;
	}

	void setPuNo(String puNo) {
		this.puNo = puNo;
	}

	@Override
	public String getTitle() {
		return title;
	}

	void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getSupplierName() {
		return supplierName;
	}

	void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	@Override
	public String getSupplierBan() {
		return supplierBan;
	}

	void setSupplierBan(String supplierBan) {
		this.supplierBan = supplierBan;
	}

	@Override
	public PurchPerfStatus getPerfStatus() {
		return perfStatus;
	}

	void setPerfStatus(PurchPerfStatus perfStatus) {
		this.perfStatus = perfStatus;
	}

	@Override
	public long getPerfTime() {
		return perfTime;
	}

	void setPerfTime(long perfTime) {
		this.perfTime = perfTime;
	}

	// -------------------------------------------------------------------------------
	@Override
	public PurchInfo reload() {
		return DataServiceFactory.getInstance().getService(PuDataService.class).loadPurch(getUid());
	}

	// -------------------------------------------------------------------------------
	private BizObjLoader<List<PurchItemInfo>> purchItemListLoader = BizObjLoader
			.of(() -> DataServiceFactory.getInstance().getService(PuDataService.class).loadPurchItemList(getUid()));

	@Override
	public List<PurchItemInfo> getPurchItemList() {
		return purchItemListLoader.getObj();
	}

}
