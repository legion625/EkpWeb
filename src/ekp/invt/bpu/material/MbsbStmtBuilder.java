package ekp.invt.bpu.material;

import ekp.data.InvtDataService;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MbsbStmtCreateObj;
import ekp.data.service.invt.MbsbStmtInfo;
import ekp.invt.type.MbsbFlowType;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class MbsbStmtBuilder extends Bpu<MbsbStmtInfo> {
	private static InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);

	/* base */
	/* Conj的兩個對象：MaterialBinStockBatch和InvtOrderItem */
	private String mbsbUid; //
	private String ioiUid; //

	/* 這個Conj紀錄完整的流向、數量、金額。 */
	private MbsbFlowType mbsbFlowType;
	private double stmtQty; // 記錄異動的數量
	private double stmtValue; // 記錄異動的金額

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected MbsbStmtBuilder appendMbsbUid(String mbsbUid) {
		this.mbsbUid = mbsbUid;
		return this;
	}

	protected MbsbStmtBuilder appendIoiUid(String ioiUid) {
		this.ioiUid = ioiUid;
		return this;
	}

	protected MbsbStmtBuilder appendMbsbFlowType(MbsbFlowType mbsbFlowType) {
		this.mbsbFlowType = mbsbFlowType;
		return this;
	}

	protected MbsbStmtBuilder appendStmtQty(double stmtQty) {
		this.stmtQty = stmtQty;
		return this;
	}

	protected MbsbStmtBuilder appendStmtValue(double stmtValue) {
		this.stmtValue = stmtValue;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getMbsbUid() {
		return mbsbUid;
	}

	public String getIoiUid() {
		return ioiUid;
	}

	public MbsbFlowType getMbsbFlowType() {
		return mbsbFlowType;
	}

	public double getStmtQty() {
		return stmtQty;
	}

	public double getStmtValue() {
		return stmtValue;
	}

	// -------------------------------------------------------------------------------
	protected MbsbStmtCreateObj packMbsbStmtCreateObj() {
		MbsbStmtCreateObj dto = new MbsbStmtCreateObj();
		dto.setMbsbUid(getMbsbUid());
		dto.setIoiUid(getIoiUid());
		dto.setMbsbFlowType(getMbsbFlowType());
		dto.setStmtQty(getStmtQty());
		dto.setStmtValue(getStmtValue());
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

		if (DataFO.isEmptyString(getMbsbUid())) {
			_msg.append("mbsbUid should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		if (DataFO.isEmptyString(getIoiUid())) {
			_msg.append("ioiUid should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		if (getMbsbFlowType() == null || MbsbFlowType.UNDEFINED == getMbsbFlowType()) {
			_msg.append("mbsbFlowType error.").append(System.lineSeparator());
			v = false;
		}

		if (getStmtQty() == 0 && getStmtValue() == 0) {
			_msg.append("Qty/Value error.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}
	
	@Override
	protected MbsbStmtInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		MbsbStmtInfo stmt = invtDataService.createMbsbStmt(packMbsbStmtCreateObj());
		if (stmt == null) {
			tt.travel();
			log.error("invtDataService.createMbsbStmt return null.");
			return null;
		}
		tt.addSite("revert createMbsbStmt", () -> invtDataService.deleteMbsbStmt(stmt.getUid()));
		log.info("invtDataService.createMbsbStmt [{}][{}][{}][{}][{}][{}]", stmt.getUid(), stmt.getMbsbUid(),
				stmt.getIoiUid(), stmt.getMbsbFlowType(), stmt.getStmtQty(), stmt.getStmtValue());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return stmt;
	}

}
