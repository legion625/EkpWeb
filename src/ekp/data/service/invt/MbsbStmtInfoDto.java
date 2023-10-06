package ekp.data.service.invt;

import ekp.data.BizObjLoader;
import ekp.data.InvtDataService;
import ekp.invt.type.MbsbFlowType;
import ekp.invt.type.PostingStatus;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class MbsbStmtInfoDto extends ObjectModelInfoDto implements MbsbStmtInfo{

	protected MbsbStmtInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}
	/* Conj的兩個對象：MaterialBinStockBatch和InvtOrderItem */
	private String mbsbUid; //
	private String ioiUid; //

	/* 這個Conj紀錄完整的流向、數量、金額。 */
	private MbsbFlowType mbsbFlowType;
	private double stmtQty; // 記錄異動的數量
	private double stmtValue; // 記錄異動的金額

	private PostingStatus postingStatus; // 登帳狀態
	private long postingTime; // 登帳時間

	@Override
	public String getMbsbUid() {
		return mbsbUid;
	}

	void setMbsbUid(String mbsbUid) {
		this.mbsbUid = mbsbUid;
	}

	@Override
	public String getIoiUid() {
		return ioiUid;
	}

	void setIoiUid(String ioiUid) {
		this.ioiUid = ioiUid;
	}

	@Override
	public MbsbFlowType getMbsbFlowType() {
		return mbsbFlowType;
	}

	void setMbsbFlowType(MbsbFlowType mbsbFlowType) {
		this.mbsbFlowType = mbsbFlowType;
	}

	@Override
	public double getStmtQty() {
		return stmtQty;
	}

	void setStmtQty(double stmtQty) {
		this.stmtQty = stmtQty;
	}

	@Override
	public double getStmtValue() {
		return stmtValue;
	}

	void setStmtValue(double stmtValue) {
		this.stmtValue = stmtValue;
	}

	@Override
	public PostingStatus getPostingStatus() {
		return postingStatus;
	}

	void setPostingStatus(PostingStatus postingStatus) {
		this.postingStatus = postingStatus;
	}

	@Override
	public long getPostingTime() {
		return postingTime;
	}

	void setPostingTime(long postingTime) {
		this.postingTime = postingTime;
	}

	// -------------------------------------------------------------------------------
	private BizObjLoader<MaterialBinStockBatchInfo> mbsbLoader = BizObjLoader.of(() -> DataServiceFactory.getInstance()
			.getService(InvtDataService.class).loadMaterialBinStockBatch(getMbsbUid()));

	@Override
	public MaterialBinStockBatchInfo getMbsb() {
		return mbsbLoader.getObj();
	}
	
	private BizObjLoader<InvtOrderItemInfo> ioiLoader = BizObjLoader.of(()->DataServiceFactory.getInstance().getService(InvtDataService.class).loadInvtOrderItem(getIoiUid()));
	
	@Override
	public InvtOrderItemInfo getIoi() {
		return ioiLoader.getObj();
	}
}
