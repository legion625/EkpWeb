package ekp.data.service.invt;

import ekp.invt.type.MbsbFlowType;

public class MbsbStmtCreateObj {
	/* Conj的兩個對象：MaterialBinStockBatch和InvtOrderItem */
	private String mbsbUid; //
	private String ioiUid; //

	/* 這個Conj紀錄完整的流向、數量、金額。 */
	private MbsbFlowType mbsbFlowType;
	private double stmtQty; // 記錄異動的數量
	private double stmtValue; // 記錄異動的金額

	public String getMbsbUid() {
		return mbsbUid;
	}

	public void setMbsbUid(String mbsbUid) {
		this.mbsbUid = mbsbUid;
	}

	public String getIoiUid() {
		return ioiUid;
	}

	public void setIoiUid(String ioiUid) {
		this.ioiUid = ioiUid;
	}

	public MbsbFlowType getMbsbFlowType() {
		return mbsbFlowType;
	}

	public void setMbsbFlowType(MbsbFlowType mbsbFlowType) {
		this.mbsbFlowType = mbsbFlowType;
	}

	public double getStmtQty() {
		return stmtQty;
	}

	public void setStmtQty(double stmtQty) {
		this.stmtQty = stmtQty;
	}

	public double getStmtValue() {
		return stmtValue;
	}

	public void setStmtValue(double stmtValue) {
		this.stmtValue = stmtValue;
	}
}
