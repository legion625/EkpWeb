package ekp.data.service.invt;

import ekp.data.BizObjLoader;
import ekp.data.InvtDataService;
import ekp.invt.type.InvtOrderType;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class InvtOrderItemInfoDto extends ObjectModelInfoDto implements InvtOrderItemInfo{

	protected InvtOrderItemInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}
	
	private String ioUid; // invt order uid
	private String mmUid;
	private String miUid;
	private String wrhsBinUid;

	private InvtOrderType ioType;
	private double orderQty; // 記錄異動的數量
	private double orderValue; // 記錄異動的金額
	@Override
	public String getIoUid() {
		return ioUid;
	}
	void setIoUid(String ioUid) {
		this.ioUid = ioUid;
	}
	
	public String getMmUid() {
		return mmUid;
	}
	void setMmUid(String mmUid) {
		this.mmUid = mmUid;
	}
	public String getMiUid() {
		return miUid;
	}
	void setMiUid(String miUid) {
		this.miUid = miUid;
	}
	
	public String getWrhsBinUid() {
		return wrhsBinUid;
	}
	void setWrhsBinUid(String wrhsBinUid) {
		this.wrhsBinUid = wrhsBinUid;
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

	// -------------------------------------------------------------------------------
	private BizObjLoader<MaterialInstInfo> miLoader = BizObjLoader
			.of(() -> DataServiceFactory.getInstance().getService(InvtDataService.class).loadMaterialInst(getMiUid()));

	@Override
	public MaterialInstInfo getMi() {
		return miLoader.getObj();
	}

}
