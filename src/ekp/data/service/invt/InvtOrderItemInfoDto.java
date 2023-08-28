package ekp.data.service.invt;

import ekp.data.BizObjLoader;
import ekp.data.InvtDataService;
import ekp.invt.type.InvtOrderType;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class InvtOrderItemInfoDto extends ObjectModelInfoDto implements InvtOrderItemInfo {

	protected InvtOrderItemInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String ioUid; // invt order uid
	private String mmUid;

	private InvtOrderType ioType;
	private double orderQty; // 記錄異動的數量
	private double orderValue; // 記錄異動的金額

	private boolean miAssigned;
	private String miUid;
	private boolean wrhsBinAssigned;
	private String wrhsBinUid;

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
	public boolean isMiAssigned() {
		return miAssigned;
	}

	void setMiAssigned(boolean miAssigned) {
		this.miAssigned = miAssigned;
	}

	@Override
	public String getMiUid() {
		return miUid;
	}

	void setMiUid(String miUid) {
		this.miUid = miUid;
	}

	@Override
	public boolean isWrhsBinAssigned() {
		return wrhsBinAssigned;
	}

	void setWrhsBinAssigned(boolean wrhsBinAssigned) {
		this.wrhsBinAssigned = wrhsBinAssigned;
	}

	@Override
	public String getWrhsBinUid() {
		return wrhsBinUid;
	}

	void setWrhsBinUid(String wrhsBinUid) {
		this.wrhsBinUid = wrhsBinUid;
	}

	// -------------------------------------------------------------------------------
	@Override
	public InvtOrderItemInfo reload() {
		return DataServiceFactory.getInstance().getService(InvtDataService.class).loadInvtOrderItem(getUid());
	}

	// -------------------------------------------------------------------------------
	private BizObjLoader<MaterialInstInfo> miLoader = BizObjLoader
			.of(() -> DataServiceFactory.getInstance().getService(InvtDataService.class).loadMaterialInst(getMiUid()));

	@Override
	public MaterialInstInfo getMi() {
		return miLoader.getObj();
	}

}
