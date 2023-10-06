package ekp.sd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.data.SdDataService;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.SalesOrderItemCreateObj;
import ekp.data.service.sd.SalesOrderItemInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class SalesOrderItemBuilder extends Bpu<SalesOrderItemInfo> {
	
	private static SdDataService sdDataService = DataServiceFactory.getInstance().getService(SdDataService.class);

	/* base */
	private String soUid;

	private String mmUid;
	private String mmMano;
	private String mmName;
	private String mmSpec;
	private double qty;
	private double value;

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected SalesOrderItemBuilder appendSoUid(String soUid) {
		this.soUid = soUid;
		return this;
	}

	protected SalesOrderItemBuilder appendMmUid(String mmUid) {
		this.mmUid = mmUid;
		return this;
	}

	protected SalesOrderItemBuilder appendMmMano(String mmMano) {
		this.mmMano = mmMano;
		return this;
	}

	protected SalesOrderItemBuilder appendMmName(String mmName) {
		this.mmName = mmName;
		return this;
	}

	protected SalesOrderItemBuilder appendMmSpec(String mmSpec) {
		this.mmSpec = mmSpec;
		return this;
	}

	protected SalesOrderItemBuilder appendQty(double qty) {
		this.qty = qty;
		return this;
	}

	protected SalesOrderItemBuilder appendValue(double value) {
		this.value = value;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getSoUid() {
		return soUid;
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

	public String getMmSpec() {
		return mmSpec;
	}

	public double getQty() {
		return qty;
	}

	public double getValue() {
		return value;
	}

	// -------------------------------------------------------------------------------
	private SalesOrderItemCreateObj packSalesOrderItemCreateObj() {
		SalesOrderItemCreateObj dto = new SalesOrderItemCreateObj();
		dto.setMmUid(getMmUid());
		dto.setMmMano(getMmMano());
		dto.setMmName(getMmName());
		dto.setMmSpec(getMmSpec());
		dto.setQty(getQty());
		dto.setValue(getValue());
		return dto;
	}

	// -------------------------------------------------------------------------------
	protected final boolean verifyThis(StringBuilder _msg, boolean _full) {
		boolean v = true;

		if (_full) {
			if (DataFO.isEmptyString(getSoUid())) {
				_msg.append("SoUid should NOT be empty.").append(System.lineSeparator());
				v = false;
			}
		}

		//
		if (getQty() <= 0 && getValue() <= 0) {
			_msg.append("Qty/Value error.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	// -------------------------------------------------------------------------------
	protected final SalesOrderItemInfo buildSalesOrderItem(TimeTraveler _tt, String _soUid) {
		TimeTraveler tt = new TimeTraveler();
		log.debug("_soUid: {}", _soUid);
		SalesOrderItemInfo soi = sdDataService.createSalesOrderItem(_soUid, packSalesOrderItemCreateObj());
		if (soi == null) {
			tt.travel();
			log.error("sdDataService.createSalesOrderItem return null.");
			return null;
		}
		tt.addSite("revert createSalesOrderItem", () -> sdDataService.deleteSalesOrderItem(soi.getUid()));
		log.info("sdDataService.createSalesOrderItem");

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return soi;
	}

}
