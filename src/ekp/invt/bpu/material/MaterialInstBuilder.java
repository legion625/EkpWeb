package ekp.invt.bpu.material;

import ekp.data.InvtDataService;
import ekp.data.service.invt.MaterialInstCreateObj;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.invt.type.MaterialInstAcqChannel;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class MaterialInstBuilder extends Bpu<MaterialInstInfo> {
	private static InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);

	/* base */
	private String mmUid;

	private MaterialInstAcqChannel miac;
	private double qty; // 數量
	private double value; // 帳值
	private long effDate; // 生效日期
	private long expDate; // 失效日期

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected MaterialInstBuilder appendMmUid(String mmUid) {
		this.mmUid = mmUid;
		return this;
	}

	protected MaterialInstBuilder appendMiac(MaterialInstAcqChannel miac) {
		this.miac = miac;
		return this;
	}

	protected MaterialInstBuilder appendQty(double qty) {
		this.qty = qty;
		return this;
	}

	protected MaterialInstBuilder appendValue(double value) {
		this.value = value;
		return this;
	}

	protected MaterialInstBuilder appendEffDate(long effDate) {
		this.effDate = effDate;
		return this;
	}

	protected MaterialInstBuilder appendExpDate(long expDate) {
		this.expDate = expDate;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getMmUid() {
		return mmUid;
	}

	public MaterialInstAcqChannel getMiac() {
		return miac;
	}

	public double getQty() {
		return qty;
	}

	public double getValue() {
		return value;
	}

	public long getEffDate() {
		return effDate;
	}

	public long getExpDate() {
		return expDate;
	}

	// -------------------------------------------------------------------------------
	private MaterialInstCreateObj packMaterialInstCreateObj() {
		MaterialInstCreateObj dto = new MaterialInstCreateObj();
		dto.setMiac(getMiac());
		dto.setQty(getQty());
		dto.setValue(getValue());
		dto.setEffDate(getEffDate());
		dto.setExpDate(getExpDate());
		;
		return dto;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = true;

		if (DataFO.isEmptyString(getMmUid())) {
			_msg.append("mmUid should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		if (getMiac() == null || MaterialInstAcqChannel.UNDEFINED == getMiac()) {
			_msg.append("MaterialInstAcqChannel error.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	@Override
	protected MaterialInstInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		MaterialInstInfo mi = invtDataService.createMaterialInst(packMaterialInstCreateObj());
		if (mi == null) {
			tt.travel();
			log.error("invtDataService.createMaterialInst return null.");
			return null;
		}
		tt.addSite("revert createMaterialInst", () -> invtDataService.deleteMaterialInst(mi.getUid()));
		log.info("invtDataService.createMaterialInst [{}][{}][{}][{}][{}][{}]", mi.getUid(),mi.getMisn(), mi.getMmUid(), mi.getMiac(),
				mi.getQty(), mi.getValue());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return mi;
	}
}
