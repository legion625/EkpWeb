package ekp.data.service.invt;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.InvtDataService;
import ekp.data.MbomDataService;
import ekp.data.PuDataService;
import ekp.data.SdDataService;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.query.PartAcquisitionQueryParam;
import ekp.data.service.pu.PurchItemInfo;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.SalesOrderItemInfo;
import ekp.mbom.type.PartUnit;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.CompareOp;

public class MaterialMasterInfoDto extends ObjectModelInfoDto implements MaterialMasterInfo {

	protected MaterialMasterInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String mano;
	private String name;
	private String specification;

	private PartUnit stdUnit;

	// 所有MaterialBinStockBatch的庫存量和金額(這是redundant屬性，必須確保一致)
	private double sumStockQty;
	private double sumStockValue;

	@Override
	public String getMano() {
		return mano;
	}

	void setMano(String mano) {
		this.mano = mano;
	}

	@Override
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Override
	public String getSpecification() {
		return specification;
	}

	void setSpecification(String specification) {
		this.specification = specification;
	}

	@Override
	public PartUnit getStdUnit() {
		return stdUnit;
	}

	void setStdUnit(PartUnit stdUnit) {
		this.stdUnit = stdUnit;
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
	@Override
	public MaterialMasterInfo reload() {
		return DataServiceFactory.getInstance().getService(InvtDataService.class).loadMaterialMaster(getUid());
	}

	// -------------------------------------------------------------------------------
	private BizObjLoader<List<MaterialInstInfo>> miListLoader = BizObjLoader.of(
			() -> DataServiceFactory.getInstance().getService(InvtDataService.class).loadMaterialInstList(getUid()));

	@Override
	public List<MaterialInstInfo> getMiList(boolean _reload) {
		return miListLoader.getObj(_reload);
	}

	private BizObjLoader<List<MaterialBinStockInfo>> mbsListLoader = BizObjLoader.of(() -> DataServiceFactory
			.getInstance().getService(InvtDataService.class).loadMaterialBinStockList(getUid()));

	@Override
	public List<MaterialBinStockInfo> getMbsList(boolean _reload) {
		return mbsListLoader.getObj(_reload);
	}
	
	private BizObjLoader<List<InvtOrderItemInfo>> ioiListLoader = BizObjLoader.of(() -> DataServiceFactory.getInstance()
			.getService(InvtDataService.class).loadInvtOrderItemListByMm(getUid()));
	
	@Override
	public List<InvtOrderItemInfo> getIoiList(){
		return ioiListLoader.getObj();
	}

	private BizObjLoader<List<SalesOrderItemInfo>> soiListLoader = BizObjLoader.of(() -> DataServiceFactory
			.getInstance().getService(SdDataService.class).loadSalesOrderItemListMyMm(getUid()));

	@Override
	public List<SalesOrderItemInfo> getSoiList() {
		return soiListLoader.getObj();
	}

	private BizObjLoader<List<PurchItemInfo>> piListLoader = BizObjLoader
			.of(() -> DataServiceFactory.getInstance().getService(PuDataService.class).loadPurchItemListByMm(getUid()));

	@Override
	public List<PurchItemInfo> getPiList() {
		return piListLoader.getObj();
	}

	private BizObjLoader<List<PartAcqInfo>> paListLoader = BizObjLoader.of(() -> {
		QueryOperation<PartAcquisitionQueryParam, PartAcqInfo> param = new QueryOperation<>();
		param.appendCondition(QueryOperation.value(PartAcquisitionQueryParam.MM_ASSIGNED, CompareOp.equal, true));
		param.appendCondition(QueryOperation.value(PartAcquisitionQueryParam.MM_UID, CompareOp.equal, getUid()));
		return DataServiceFactory.getInstance().getService(MbomDataService.class).searchPartAcquisition(param)
				.getQueryResult();
	});

	@Override
	public List<PartAcqInfo> getPaList() {
		return paListLoader.getObj();
	}
}
