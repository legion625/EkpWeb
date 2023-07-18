package ekp.invt.bpu.material;

import java.util.List;

import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.invt.bpu.wrhsLoc.WrhsBinBpuDel0;
import legion.util.TimeTraveler;

public class MaterialMasterBpuDel0 extends MaterialMasterBpu {
	/* base */
	private MaterialMasterInfo mm;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected MaterialMasterBpuDel0 appendBase() {
		/* base */
		mm = (MaterialMasterInfo) args[0];
		appendMm(mm);

		/* data */
		// none

		return this;
	}

	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = true;
		if(getMm()==null) {
			v = false;
			_msg.append("MaterialMaster null.").append(System.lineSeparator());
		}

		List<MaterialInstInfo> miList = getMm().getMiList();
		if (!miList.isEmpty()) {
			v = false;
			_msg.append("miList should be empty.").append(System.lineSeparator());
		}

		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		if (!invtDataService.deleteMaterialMaster(getMm().getUid())) {
			log.error("invtDataService.deleteMaterialMaster return false. [{}][{}][{}]", getMm().getUid(),
					getMm().getMano(), getMm().getName());
			return false;
		}
		log.info("invtDataService.deleteMaterialMaster. [{}][{}][{}]", getMm().getUid(), getMm().getMano(),
				getMm().getName());
		return true;
	}

}
