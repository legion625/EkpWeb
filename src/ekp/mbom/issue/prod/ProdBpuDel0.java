package ekp.mbom.issue.prod;

import java.util.List;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;
import ekp.data.service.mbom.ProdModInfo;
import ekp.invt.bpu.material.MaterialMasterBpuDel0;
import legion.util.TimeTraveler;

public class ProdBpuDel0 extends ProdBpu {
	/* base */
	private ProdInfo prod;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected ProdBpuDel0 appendBase() {
		/* base */
		prod= (ProdInfo) args[0];
		appendProd(prod);

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
		if(getProd()==null) {
			v = false;
			_msg.append("Product null.").append(System.lineSeparator());
		}
		
		//
		List<ProdCtlInfo> prodCtlList = getProd().getProdCtlList();
		if(!prodCtlList.isEmpty()) {
			v = false;
			_msg.append("prodCtlList should be empty.").append(System.lineSeparator());
		}

		//
		List<ProdModInfo> prodModList = getProd().getProdModList();
		if(!prodModList.isEmpty()) {
			v = false;
			_msg.append("prodModList should be empty.").append(System.lineSeparator());
		}
		
		return v;
	}
	
	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		if(!mbomDataService.deleteProd(getProd().getUid())) {
			log.error("mbomDataService.deleteProd return false. [{}][{}][{}]", getProd().getUid(), getProd().getId(), getProd().getName());
			return false;
		}
		log.info("mbomDataService.deleteProd. [{}][{}][{}]", getProd().getUid(), getProd().getId(),
				getProd().getName());
		return true;
	}
	
	

}
