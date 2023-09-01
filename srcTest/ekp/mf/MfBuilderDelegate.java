package ekp.mf;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.TestLogMark;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.mf.bpu.MfBpuType;
import ekp.mf.bpu.WoBuilder1;
import legion.biz.BpuFacade;
import legion.util.TimeTraveler;

public class MfBuilderDelegate {
	private Logger log = LoggerFactory.getLogger(TestLogMark.class);
	
	private static MfBuilderDelegate delegate = new MfBuilderDelegate();
	private MfBuilderDelegate() {}
	public static MfBuilderDelegate getInstance() {
		return delegate;
	}
	
	// -------------------------------------------------------------------------------
	private final BpuFacade bpuFacade = BpuFacade.getInstance();
	
	// -------------------------------------------------------------------------------
	// -----------------------------------Workorder-----------------------------------
	public WorkorderInfo buildWo(TimeTraveler _tt, PartInfo _p, PartAcqInfo _pa) {
		WoBuilder1 wob = bpuFacade.getBuilder(MfBpuType.WO_1, _p);
		wob.appendPa(_pa);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(wob.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
//		boolean verify =wob.verify(msgVerify); 
//		if(!verify)
//			log.error("{}", msgVerify.toString());
//		assertTrue(verify);
		assertTrue(wob.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		WorkorderInfo wo = wob.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), wo);

		// check
		// TODO

		return wo;
	}
	
}
