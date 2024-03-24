package ekp.data.service.invt;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.InvtDataService;
import ekp.invt.type.InvtOrderType;
import ekp.invt.type.IoiTargetType;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class InvtOrderItemInfoDto extends ObjectModelInfoDto implements InvtOrderItemInfo {

	protected InvtOrderItemInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String ioUid; // invt order uid
	private String mmUid;

	private InvtOrderType ioType;
	private IoiTargetType targetType;
	private String targetUid;
	private String targetBizKey;
	private double orderQty; // 記錄異動的數量
	private double orderValue; // 記錄異動的金額

	private boolean mbsbStmtCreated;

	@Override
	public String getIoUid() {
		return ioUid;
	}

	void setIoUid(String ioUid) {
		this.ioUid = ioUid;
	}

	@Override
	public String getMmUid() {
		return mmUid;
	}

	void setMmUid(String mmUid) {
		this.mmUid = mmUid;
	}

	@Override
	public InvtOrderType getIoType() {
		return ioType;
	}

	void setIoType(InvtOrderType ioType) {
		this.ioType = ioType;
	}

	@Override
	public IoiTargetType getTargetType() {
		return targetType;
	}

	void setTargetType(IoiTargetType targetType) {
		this.targetType = targetType;
	}

	@Override
	public String getTargetUid() {
		return targetUid;
	}

	void setTargetUid(String targetUid) {
		this.targetUid = targetUid;
	}

	@Override
	public String getTargetBizKey() {
		return targetBizKey;
	}

	void setTargetBizKey(String targetBizKey) {
		this.targetBizKey = targetBizKey;
	}

	@Override
	public double getOrderQty() {
		return orderQty;
	}

	void setOrderQty(double orderQty) {
		this.orderQty = orderQty;
	}

	@Override
	public double getOrderValue() {
		return orderValue;
	}

	void setOrderValue(double orderValue) {
		this.orderValue = orderValue;
	}

	@Override
	public boolean isMbsbStmtCreated() {
		return mbsbStmtCreated;
	}

	void setMbsbStmtCreated(boolean mbsbStmtCreated) {
		this.mbsbStmtCreated = mbsbStmtCreated;
	}

	// -------------------------------------------------------------------------------
	@Override
	public InvtOrderItemInfo reload() {
		return DataServiceFactory.getInstance().getService(InvtDataService.class).loadInvtOrderItem(getUid());
	}

	// -------------------------------------------------------------------------------
	private BizObjLoader<InvtOrderInfo> ioLoader = BizObjLoader.IO.get();
	
	
	@Override
	public InvtOrderInfo getIo() {
		return ioLoader.getObj(getIoUid());
	}

	private BizObjLoader<MaterialMasterInfo> mmLoader = BizObjLoader.MM.get();

	@Override
	public MaterialMasterInfo getMm() {
		return mmLoader.getObj(getMmUid());
	}
	
	private BizObjLoader<List<MbsbStmtInfo>> mbsbStmtListLoader = BizObjLoader.of(
			() -> DataServiceFactory.getInstance().getService(InvtDataService.class).loadMbsbStmtListByIoi(getUid()));

	@Override
	public List<MbsbStmtInfo> getMbsbStmtList() {
		return mbsbStmtListLoader.getObj();
	}

	// -------------------------------------------------------------------------------
//	private BizObjLoader<MaterialInstInfo> miLoader = BizObjLoader
//			.of(() -> DataServiceFactory.getInstance().getService(InvtDataService.class).loadMaterialInst(getMiUid()));
//
//	@Override
//	public MaterialInstInfo getMi() {
//		return miLoader.getObj();
//	}

}
