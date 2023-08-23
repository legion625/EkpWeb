package ekp.invt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.InvtDataService;
import ekp.data.service.invt.MaterialBinStockBatchInfo;
import ekp.data.service.invt.MaterialBinStockCreateObj;
import ekp.data.service.invt.MaterialBinStockInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.invt.bpu.material.MbsBuilder;
import ekp.invt.bpu.material.MbsbBuilder;
import legion.DataServiceFactory;
import legion.util.TimeTraveler;

public class MaterialBinStockFacade {
	private Logger log = LoggerFactory.getLogger(MaterialBinStockFacade.class);

	private final static MaterialBinStockFacade INSTANCE = new MaterialBinStockFacade();

	private MaterialBinStockFacade() {
	}

	public final static MaterialBinStockFacade get() {
		return INSTANCE;
	}

//	private InvtDataService invtDataService;

	// -------------------------------------------------------------------------------
	public MaterialBinStockInfo getMbs(String _mmUid, String _wrhsBinUid) {
		InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);

		MaterialBinStockInfo mbs = invtDataService.loadMaterialBinStock(_mmUid, _wrhsBinUid);
		if (mbs != null)
			return mbs;

		MaterialMasterInfo mm = invtDataService.loadMaterialMaster(_mmUid);
		if (mm == null) {
			log.error("mm error. [{}]", _mmUid);
			return null;
		}

		/**/
		TimeTraveler tt = new TimeTraveler();		
		MbsBuilder b = new MbsBuilder();
		b.appendMmUid(_mmUid).appendMano(mm.getMano()).appendwrhsBinUid(_wrhsBinUid);

		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			log.error("MbsBuilder verify return false. {}", msg.toString());
			return null;
		}
		mbs = b.build(new StringBuilder(), null);
		if (mbs == null) {
			log.error("MbsBuilder.build return null. {}", msg.toString());
			return null;
		}
		return mbs;
	}
	
	public MaterialBinStockBatchInfo getMbsb(String _mbsUid, String _miUid) {
		InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);
		MaterialBinStockBatchInfo mbsb = invtDataService.loadMaterialBinStockBatch(_mbsUid, _miUid);
		if (mbsb != null)
			return mbsb;

		MaterialBinStockInfo mbs = invtDataService.loadMaterialBinStock(_mbsUid);
		if (mbs == null) {
			log.error("mbs error. [{}]", _mbsUid);
			return null;
		}

		MaterialInstInfo mi = invtDataService.loadMaterialInst(_miUid);
		if (mi == null) {
			log.error("mi error. [{}]", _miUid);
			return null;
		}

		/**/
		TimeTraveler tt = new TimeTraveler();
		MbsbBuilder b = new MbsbBuilder();
		b.appendMbsUid(_mbsUid).appendMiUid(_miUid);

		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			log.error("MbsbBuilder verify return false. {}", msg.toString());
			return null;
		}
		mbsb = b.build(new StringBuilder(), null);
		if (mbsb == null) {
			log.error("MbsbBuilder.build return null. {}", msg.toString());
			return null;
		}
		return mbsb;
	}
	
	public MaterialBinStockBatchInfo getMbsb(MaterialBinStockInfo _mbs, String _miUid) {
		InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);
		MaterialBinStockBatchInfo mbsb = invtDataService.loadMaterialBinStockBatch(_mbs.getUid(), _miUid);
		if (mbsb != null)
			return mbsb;

		MaterialInstInfo mi = invtDataService.loadMaterialInst(_miUid);
		if (mi == null) {
			log.error("mi error. [{}]", _miUid);
			return null;
		}

		/**/
		TimeTraveler tt = new TimeTraveler();
		MbsbBuilder b = new MbsbBuilder();
		b.appendMbsUid(_mbs.getUid()).appendMiUid(_miUid);

		StringBuilder msg = new StringBuilder();
		if (!b.verify(msg)) {
			log.error("MbsbBuilder verify return false. {}", msg.toString());
			return null;
		}
		mbsb = b.build(new StringBuilder(), null);
		if (mbsb == null) {
			log.error("MbsbBuilder.build return null. {}", msg.toString());
			return null;
		}
		return mbsb;
	}
	

}
