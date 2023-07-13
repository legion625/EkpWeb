package ekp.data.service.invt;

import ekp.mbom.type.PartUnit;
import legion.ObjectModelInfoDto;

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

}
