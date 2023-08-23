package ekp.invt.bpu.material;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialBinStockBatchInfo;
import ekp.data.service.invt.MaterialBinStockInfo;
import ekp.invt.MaterialBinStockFacade;
import ekp.invt.type.MbsbFlowType;

public class MbsbStmtBuilderByIoi extends MbsbStmtBuilder{
	/* base */
	private InvtOrderItemInfo ioi;
	
	/* data */
	private MaterialBinStockBatchInfo mbsb;
	// TODO
	
 	
	
	// -------------------------------------------------------------------------------
	@Override
	protected MbsbStmtBuilderByIoi appendBase() {
		/* base */
		ioi = (InvtOrderItemInfo) args[0];

		
		MaterialBinStockFacade mbsFacade = MaterialBinStockFacade.get();
		MaterialBinStockInfo mbs = mbsFacade.getMbs(ioi.getMmUid(), ioi.getWrhsBinUid());
		if (mbs == null) {
			log.error("getMbs return null.");
			return null;
		}
		mbsb = mbsFacade.getMbsb(mbs, ioi.getMiUid());
		if (mbsb == null) {
			log.error("getMbsb return null.");
			return null;
		}

		appendMbsbUid(mbsb.getUid());
		appendIoiUid(ioi.getUid());
		appendMbsbFlowType(MbsbFlowType.IN);
		// 假設訂購數量和金額全數入帳
		appendStmtQty(ioi.getOrderQty()).appendStmtValue(ioi.getOrderValue());
		
		
		/* data */
		// none
		
		return this;
	}
	
}
