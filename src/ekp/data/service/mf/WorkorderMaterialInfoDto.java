package ekp.data.service.mf;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.InvtDataService;
import ekp.data.MfDataService;
import ekp.data.service.invt.MaterialMasterInfo;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class WorkorderMaterialInfoDto extends ObjectModelInfoDto implements WorkorderMaterialInfo {
	protected WorkorderMaterialInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String woUid;
	private String woNo;

	// 料件基本檔
	private String mmUid;
	private String mmMano;
	private String mmName;
	private double qty0; // 待領用量
	private double qty1; // 已領用量

	@Override
	public String getWoUid() {
		return woUid;
	}

	void setWoUid(String woUid) {
		this.woUid = woUid;
	}

	@Override
	public String getWoNo() {
		return woNo;
	}

	void setWoNo(String woNo) {
		this.woNo = woNo;
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
	public double getQty0() {
		return qty0;
	}

	void setQty0(double qty0) {
		this.qty0 = qty0;
	}

	@Override
	public double getQty1() {
		return qty1;
	}

	void setQty1(double qty1) {
		this.qty1 = qty1;
	}

	// -------------------------------------------------------------------------------
	@Override
	public WorkorderMaterialInfo reload() {
		return DataServiceFactory.getInstance().getService(MfDataService.class).loadWorkorderMaterial(getUid());
	}
	
	// -------------------------------------------------------------------------------
	private BizObjLoader<MaterialMasterInfo> mmLoader = BizObjLoader.of(
			() -> DataServiceFactory.getInstance().getService(InvtDataService.class).loadMaterialMaster(getMmUid()));

	@Override
	public MaterialMasterInfo getMm() {
		return mmLoader.getObj();
	}

}
