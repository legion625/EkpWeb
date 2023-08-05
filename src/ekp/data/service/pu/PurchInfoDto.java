package ekp.data.service.pu;

import java.util.List;

import ekp.data.BizObjLoader;
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
	
	// -------------------------------------------------------------------------------
	private BizObjLoader<List<PurchItemInfo>> purchItemListLoader = BizObjLoader.of(DataServiceFactory.getInstance().getService(PurchDataService.class).loadPurchItemList(getUid()));
	
	@Override
	public List<PurchItemInfo> getPurchItemList(){
		return purchItemListLoader.getObj();
	}

}
