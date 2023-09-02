package ekp.invt.bpu.material;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.data.service.invt.MaterialBinStockBatchInfo;
import ekp.data.service.invt.MaterialBinStockInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.invt.MaterialBinStockFacade;
import ekp.invt.type.MbsbFlowType;
import legion.biz.Bpu;

public class MbsbStmtBuilderByPurchItem extends MbsbStmtBuilder{
	
	/* base */
	private PurchItemInfo pi;
	
	/**/
	private MaterialInstInfo mi;
	private WrhsBinInfo wb;
	// mi+wb->mbsb，在InvtOrderItemBuilder11.buildProcess才能給定。
	// mbsbStmtBuilder的verify把mbsbUid和ioiUid放在_full裡才檢查。
	
	
	
	// -------------------------------------------------------------------------------
	@Override
	protected MbsbStmtBuilderByPurchItem appendBase() {
		/* base */
		pi =(PurchItemInfo) args[0];
		
		
		// mbsbUid和ioiUid在執行面才能產生
//		if (mbsb == null) {
//			log.error("getMbsb return null.");
//			return null;
//		}
		
//		appendMbsbUid(mbsb.getUid());
//		appendIoiUid(ioi.getUid());
		
		/* data */
		appendMbsbFlowType(MbsbFlowType.IN);
		// 假設訂購數量和金額全數入帳
		appendStmtQty(pi.getQty()).appendStmtValue(pi.getValue());
		
		return this;
	}

	
	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
//	public MbsbStmtBuilderByPurchItem appendMbsb() {
//
//	}
	public MbsbStmtBuilderByPurchItem appendIoiUid(String ioiUid) {
		return (MbsbStmtBuilderByPurchItem) super.appendIoiUid(ioiUid);
	}
	
	
	public MbsbStmtBuilderByPurchItem appendMi(MaterialInstInfo mi) {
		this.mi = mi;
		return this;
	}

	public MbsbStmtBuilderByPurchItem appendWb(WrhsBinInfo wb) {
		this.wb = wb;
		return this;
	}
	
	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	@Override
	public String getMbsbUid() {
		String mbsbUid =getMbsb() == null ? null : getMbsb().getUid();
		log.debug("getMbsbUid: {}", mbsbUid);
		return mbsbUid;
	}
	
	public PurchItemInfo getPi() {
		return pi;
	}

	public MaterialInstInfo getMi() {
		return mi;
	}

	public WrhsBinInfo getWb() {
		return wb;
	}
	
	private MaterialBinStockBatchInfo getMbsb() {
		if (getMi() == null || getWb() == null)
			return null;

		MaterialBinStockFacade mbsFacade = MaterialBinStockFacade.get();
		MaterialBinStockInfo mbs = mbsFacade.getMbs(getPi().getMmUid(), getWb().getUid());
		if (mbs == null) {
			log.error("getMbs return null.");
			return null;
		}
		return mbsFacade.getMbsb(mbs, getMi().getUid());
	}

	
	// -------------------------------------------------------------------------------
	

}

