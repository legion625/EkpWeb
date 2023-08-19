package ekp.invt;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.TestLogMark;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.material.MaterialMasterBuilder0;
import ekp.mbom.type.PartUnit;
import legion.biz.BpuFacade;
import legion.util.TimeTraveler;

public class InvtDelegate {
	private Logger log = LoggerFactory.getLogger(TestLogMark.class);
	private static InvtDelegate delegate = new InvtDelegate();

	private InvtDelegate() {
	}

	public static InvtDelegate getInstance() {
		return delegate;
	}

	// -------------------------------------------------------------------------------
	private final BpuFacade bpuFacade = BpuFacade.getInstance();

	// -------------------------------------------------------------------------------
	// ---------------------------------MaterialMaster---------------------------------
	public MaterialMasterInfo buildMm0(TimeTraveler _tt, String _mano, String _name, String _spec, PartUnit _stdUnit) {
		MaterialMasterBuilder0 mmb = bpuFacade.getBuilder(InvtBpuType.MM_0);
		mmb.appendMano(_mano).appendName(_name).appendSpecification(_spec).appendStdUnit(_stdUnit);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(mmb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(mmb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		MaterialMasterInfo mm = mmb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), mm);

		// check
		// TODO

		return mm;
	}

}
