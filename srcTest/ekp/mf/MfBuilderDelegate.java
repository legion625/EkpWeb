package ekp.mf;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.TestLogMark;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.WorkorderMaterialInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.mf.bpu.MfBpuType;
import ekp.mf.bpu.WoBpuFinishWork;
import ekp.mf.bpu.WoBpuStart;
import ekp.mf.bpu.WoBuilder1;
import legion.biz.BpuFacade;
import legion.util.TimeTraveler;

public class MfBuilderDelegate {
	private Logger log = LoggerFactory.getLogger(TestLogMark.class);

	private static MfBuilderDelegate delegate = new MfBuilderDelegate();

	private MfBuilderDelegate() {
	}

	public static MfBuilderDelegate getInstance() {
		return delegate;
	}

	// -------------------------------------------------------------------------------
	private final BpuFacade bpuFacade = BpuFacade.getInstance();

	// -------------------------------------------------------------------------------
	// -----------------------------------Workorder-----------------------------------
	public WorkorderInfo buildWo(TimeTraveler _tt,  PartAcqInfo _pa, PartCfgInfo _pc, double _rqQty) {
//		WoBuilder1 wob = bpuFacade.getBuilder(MfBpuType.WO_1, _p);
		WoBuilder1 wob = bpuFacade.getBuilder(MfBpuType.WO_1,_pa , _pc);
//		wob.appendPa(_pa, _rqQty);
		wob.appendRqQty(_rqQty);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(wob.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(wob.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		WorkorderInfo wo = wob.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), wo);

		// check
		Map<String, PpartInfo> ppartMap = _pa.getPpartList().stream()
//				.collect(Collectors.toMap(pp -> pp.getPart().getMmMano(), pp -> pp));
				.collect(Collectors.toMap(pp -> pp.getPart().getPa(_pc).getMmMano(), pp -> pp));
		for (WorkorderMaterialInfo wom : wo.getWomList()) {
			PpartInfo ppart = ppartMap.get(wom.getMmMano());
			assertNotNull(ppart);
			assertEquals(_rqQty * ppart.getPartReqQty(), wom.getQty0());
		}

		return wo;
	}

	public boolean runWoStart(TimeTraveler _tt, WorkorderInfo _wo) {
		WoBpuStart bpu = bpuFacade.getBuilder(MfBpuType.WO_$START, _wo);
		bpu.appendStartWorkTime(System.currentTimeMillis());

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(bpu.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(bpu.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		boolean b = bpu.build(msgBuild, _tt);
		assertTrue(b, msgBuild.toString());

		// check
		// none

		return b;
	}

	public boolean runWoFinishWork(TimeTraveler _tt, WorkorderInfo _wo) {
		WoBpuFinishWork bpu = bpuFacade.getBuilder(MfBpuType.WO_$FINISH_WORK, _wo);
		bpu.appendFinishWorkTime(System.currentTimeMillis());

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(bpu.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(bpu.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		boolean b = bpu.build(msgBuild, _tt);
		assertTrue(b, msgBuild.toString());

		// check
		// none

		return b;
	}

}
