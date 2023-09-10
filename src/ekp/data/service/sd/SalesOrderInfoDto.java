package ekp.data.service.sd;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.SdDataService;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class SalesOrderInfoDto extends ObjectModelInfoDto implements SalesOrderInfo {
	protected SalesOrderInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String sosn;
	private String title;
	private String customerName;
	private String customerBan;

	private String salerId;
	private String salerName;
	private long saleDate;

	@Override
	public String getSosn() {
		return sosn;
	}

	void setSosn(String sosn) {
		this.sosn = sosn;
	}

	@Override
	public String getTitle() {
		return title;
	}

	void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getCustomerName() {
		return customerName;
	}

	void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public String getCustomerBan() {
		return customerBan;
	}

	void setCustomerBan(String customerBan) {
		this.customerBan = customerBan;
	}

	@Override
	public String getSalerId() {
		return salerId;
	}

	void setSalerId(String salerId) {
		this.salerId = salerId;
	}

	@Override
	public String getSalerName() {
		return salerName;
	}

	void setSalerName(String salerName) {
		this.salerName = salerName;
	}

	@Override
	public long getSaleDate() {
		return saleDate;
	}

	void setSaleDate(long saleDate) {
		this.saleDate = saleDate;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public SalesOrderInfo reload() {
		return DataServiceFactory.getInstance().getService(SdDataService.class).loadSalesOrder(getUid());
	}

	private BizObjLoader<List<SalesOrderItemInfo>> salesOrderItemListLoader = BizObjLoader.of(
			() -> DataServiceFactory.getInstance().getService(SdDataService.class).loadSalesOrderItemList(getUid()));

	@Override
	public List<SalesOrderItemInfo> getSalesOrderItemList() {
		return salesOrderItemListLoader.getObj();
	}
}
