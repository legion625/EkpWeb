package ekp.mbom.issue.prodMod;

import java.util.List;

import ekp.data.service.mbom.ProdModInfo;
import ekp.data.service.mbom.ProdModItemInfo;
import legion.util.TimeTraveler;

public class ProdModBpuDel0 extends ProdModBpu {
	/* base */
	private ProdModInfo prodMod;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected ProdModBpuDel0 appendBase() {
		/* base */
		prodMod = (ProdModInfo) args[0];
		appendProdMod(prodMod);

		/* data */
		// none

		return this;
	}

	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;
		if (getProdMod() == null) {
			v = false;
			_msg.append("Product model null.").append(System.lineSeparator());
		}

		//
		List<ProdModItemInfo> pmiList = getProdMod().getProdModItemList();
		if (!pmiList.isEmpty()) {
			v = false;
			_msg.append("prodModItemList should be empty.").append(System.lineSeparator());
		}

		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		if (!mbomDataService.deleteProdMod(getProdMod().getUid())) {
			log.error("mbomDataService.deleteProdMod return false. [{}][{}][{}]", getProdMod().getUid(),
					getProdMod().getId(), getProdMod().getName());
			return false;
		}
		log.info("mbomDataService.deleteProdMod. [{}][{}][{}]", getProdMod().getUid(), getProdMod().getId(),
				getProdMod().getName());
		return true;
	}

}
