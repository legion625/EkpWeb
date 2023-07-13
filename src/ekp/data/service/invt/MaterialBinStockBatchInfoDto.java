package ekp.data.service.invt;

import legion.ObjectModelInfoDto;

public class MaterialBinStockBatchInfoDto extends ObjectModelInfoDto implements MaterialBinStockBatchInfo {

	protected MaterialBinStockBatchInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	/* bizKey */
	private String mbsUid; // 主要ref的MaterialBinStock
	private String miUid; // material inst uid

	/**/
	private double stockQty; // 當前的存量餘額
	private double stockValue; // 當前的帳值餘額

	@Override
	public String getMbsUid() {
		return mbsUid;
	}

	void setMbsUid(String mbsUid) {
		this.mbsUid = mbsUid;
	}

	@Override
	public String getMiUid() {
		return miUid;
	}

	void setMiUid(String miUid) {
		this.miUid = miUid;
	}

	@Override
	public double getStockQty() {
		return stockQty;
	}

	void setStockQty(double stockQty) {
		this.stockQty = stockQty;
	}

	@Override
	public double getStockValue() {
		return stockValue;
	}

	void setStockValue(double stockValue) {
		this.stockValue = stockValue;
	}

}
