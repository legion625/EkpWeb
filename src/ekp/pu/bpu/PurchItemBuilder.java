package ekp.pu.bpu;

import ekp.data.PuDataService;
import ekp.data.service.pu.PurchItemCreateObj;
import ekp.data.service.pu.PurchItemInfo;
import ekp.mbom.type.PartUnit;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class PurchItemBuilder extends Bpu<PurchItemInfo> {
	private static PuDataService puDataService = DataServiceFactory.getInstance().getService(PuDataService.class);

	/* base */
	/* purchUid+mmUid作為biz key */
	private String purchUid;
	private String mmUid;

	/* 快照了物料基本檔當下的各欄位 */
	private String mmMano; // 物料基本檔料號
	private String mmName; // 品名
	private String mmSpecification;
	private PartUnit mmStdUnit;
	// 依物料基本檔輸入採購的數量和總價
	private double qty;
	private double value;
	//
	private String remark; // 備註（補充說明）

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected PurchItemBuilder appendPurchUid(String purchUid) {
		this.purchUid = purchUid;
		return this;
	}

	protected PurchItemBuilder appendMmUid(String mmUid) {
		this.mmUid = mmUid;
		return this;
	}

	protected PurchItemBuilder appendMmMano(String mmMano) {
		this.mmMano = mmMano;
		return this;
	}

	protected PurchItemBuilder appendMmName(String mmName) {
		this.mmName = mmName;
		return this;
	}

	protected PurchItemBuilder appendMmSpecification(String mmSpecification) {
		this.mmSpecification = mmSpecification;
		return this;
	}

	protected PurchItemBuilder appendMmStdUnit(PartUnit mmStdUnit) {
		this.mmStdUnit = mmStdUnit;
		return this;
	}

	protected PurchItemBuilder appendQty(double qty) {
		this.qty = qty;
		return this;
	}

	protected PurchItemBuilder appendValue(double value) {
		this.value = value;
		return this;
	}

	protected PurchItemBuilder appendRemark(String remark) {
		this.remark = remark;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getPurchUid() {
		return purchUid;
	}

	public String getMmUid() {
		return mmUid;
	}

	public String getMmMano() {
		return mmMano;
	}

	public String getMmName() {
		return mmName;
	}

	public String getMmSpecification() {
		return mmSpecification;
	}

	public PartUnit getMmStdUnit() {
		return mmStdUnit;
	}

	public double getQty() {
		return qty;
	}

	public double getValue() {
		return value;
	}

	public String getRemark() {
		return remark;
	}

	// -------------------------------------------------------------------------------
	private PurchItemCreateObj packPurchItemCreateObj() {
		PurchItemCreateObj dto = new PurchItemCreateObj();
		dto.setPurchUid(getPurchUid());
		dto.setMmUid(getMmUid());
		dto.setMmMano(getMmMano());
		dto.setMmName(getMmName());
		dto.setMmSpecification(getMmSpecification());
		dto.setMmStdUnit(getMmStdUnit());
		dto.setQty(getQty());
		dto.setValue(getValue());
		dto.setRemark(getRemark());
		return dto;
	}
	
	// -------------------------------------------------------------------------------
	protected final boolean verifyThis(StringBuilder _msg, boolean _full) {
		boolean v = true;
		
		if(_full) {
			if(DataFO.isEmptyString(getPurchUid())) {
				_msg.append("PurchUid should NOT be empty.").append(System.lineSeparator());
				v = false;
			}
		}
		
		// TODO
		
		return v;
	}
	
	
	// -------------------------------------------------------------------------------
	protected final PurchItemInfo buildPurchItem(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		PurchItemInfo pi = puDataService.createPurchItem(packPurchItemCreateObj());
		if (pi == null) {
			tt.travel();
			log.error("puDataService.createPurchItem return null.");
			return null;
		}
		tt.addSite("revert createPurchItem", () -> puDataService.deletePurchItem(pi.getUid()));
		log.info("puDataService.createPurchItem");

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return pi;
	}
}
