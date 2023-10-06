package ekp.data.service.invt;

import ekp.data.BizObjLoader;
import ekp.data.InvtDataService;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class MaterialInstSrcConjInfoDto extends ObjectModelInfoDto implements MaterialInstSrcConjInfo {

	protected MaterialInstSrcConjInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String miUid;
	private String srcMiUid;
	private double srcMiQty;
	private double srcMiValue;

	@Override
	public String getMiUid() {
		return miUid;
	}

	void setMiUid(String miUid) {
		this.miUid = miUid;
	}

	@Override
	public String getSrcMiUid() {
		return srcMiUid;
	}

	void setSrcMiUid(String srcMiUid) {
		this.srcMiUid = srcMiUid;
	}

	@Override
	public double getSrcMiQty() {
		return srcMiQty;
	}

	void setSrcMiQty(double srcMiQty) {
		this.srcMiQty = srcMiQty;
	}

	@Override
	public double getSrcMiValue() {
		return srcMiValue;
	}

	void setSrcMiValue(double srcMiValue) {
		this.srcMiValue = srcMiValue;
	}
	
	// -------------------------------------------------------------------------------
	private BizObjLoader<MaterialInstInfo> miLoader = BizObjLoader
			.of(DataServiceFactory.getInstance().getService(InvtDataService.class)::loadMaterialInst);

	@Override
	public MaterialInstInfo getMi() {
		return miLoader.getObj(getMiUid());
	}

	@Override
	public MaterialInstInfo getSrcMi() {
		return miLoader.getObj(getSrcMiUid());
	}

}
