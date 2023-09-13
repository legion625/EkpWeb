package ekp.data.service.sd;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.InvtDataService;
import ekp.data.SdDataService;
import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.query.InvtOrderItemQueryParam;
import ekp.invt.type.IoiTargetType;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.CompareOp;

public class SalesOrderItemInfoDto extends ObjectModelInfoDto implements SalesOrderItemInfo {
	protected SalesOrderItemInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String soUid;
	private String mmUid;
	private String mmMano;
	private String mmName;
	private String mmSpec;
	private double qty;
	private double value;

	private boolean allDelivered; // 是否已交貨
	private long finishDeliveredDate;

	@Override
	public String getSoUid() {
		return soUid;
	}

	void setSoUid(String soUid) {
		this.soUid = soUid;
	}

	@Override
	public String getMmUid() {
		return mmUid;
	}

	void setMmUid(String mmUid) {
		this.mmUid = mmUid;
	}

	@Override
	public String getMmMano() {
		return mmMano;
	}

	void setMmMano(String mmMano) {
		this.mmMano = mmMano;
	}

	@Override
	public String getMmName() {
		return mmName;
	}

	void setMmName(String mmName) {
		this.mmName = mmName;
	}

	@Override
	public String getMmSpec() {
		return mmSpec;
	}

	void setMmSpec(String mmSpec) {
		this.mmSpec = mmSpec;
	}

	@Override
	public double getQty() {
		return qty;
	}

	void setQty(double qty) {
		this.qty = qty;
	}

	@Override
	public double getValue() {
		return value;
	}

	void setValue(double value) {
		this.value = value;
	}

	@Override
	public boolean isAllDelivered() {
		return allDelivered;
	}

	void setAllDelivered(boolean allDelivered) {
		this.allDelivered = allDelivered;
	}

	@Override
	public long getFinishDeliveredDate() {
		return finishDeliveredDate;
	}

	void setFinishDeliveredDate(long finishDeliveredDate) {
		this.finishDeliveredDate = finishDeliveredDate;
	}
	
	
	@Override
	public SalesOrderItemInfo reload() {
		return DataServiceFactory.getInstance().getService(SdDataService.class).loadSalesOrderItem(getUid());
	}
	
	// -------------------------------------------------------------------------------
	private BizObjLoader<SalesOrderInfo> soLoader = BizObjLoader.of(()->DataServiceFactory.getInstance().getService(SdDataService.class).loadSalesOrder(getSoUid()));

	@Override
	public SalesOrderInfo getSo() {
		return soLoader.getObj();
	}
	
	private BizObjLoader<MaterialMasterInfo> mmLoader = BizObjLoader.of(
			() -> DataServiceFactory.getInstance().getService(InvtDataService.class).loadMaterialMaster(getMmUid())); 
	@Override
	public MaterialMasterInfo getMm() {
		return mmLoader.getObj();
	}
	
	private BizObjLoader<List<InvtOrderItemInfo>> ioiListLoader = BizObjLoader.of(() -> {
		QueryOperation<InvtOrderItemQueryParam, InvtOrderItemInfo> param = new QueryOperation<>();

		param.appendCondition(QueryOperation.value(InvtOrderItemQueryParam.TARGET_TYPE_IDX, CompareOp.equal,
				IoiTargetType.SOI.getIdx()));
		param.appendCondition(QueryOperation.value(InvtOrderItemQueryParam.TARGET_UID, CompareOp.equal, getUid()));
		param = DataServiceFactory.getInstance().getService(InvtDataService.class).searchInvtOrderItem(param, null);
		return param.getQueryResult();

	});

	@Override
	public List<InvtOrderItemInfo> getIoiList() {
		return ioiListLoader.getObj();
	}

}
