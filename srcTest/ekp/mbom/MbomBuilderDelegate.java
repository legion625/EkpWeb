package ekp.mbom;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.TestLogMark;
import ekp.data.service.mbom.ParsPartInfo;
import ekp.data.service.mbom.ParsProcInfo;
import ekp.data.service.mbom.PartAcqRoutingStepInfo;
import ekp.data.service.mbom.PartAcquisitionInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.mbom.issue.MbomBuilderType;
import ekp.mbom.issue.parsPart.ParsPartBuilder0;
import ekp.mbom.issue.parsProc.ParsProcBuilder0;
import ekp.mbom.issue.part.PartBuilder0;
import ekp.mbom.issue.partAcq.PartAcqBuilder0;
import ekp.mbom.issue.partAcqRoutingStep.PartAcqRoutingStepBuilder0;
import ekp.mbom.issue.partCfg.PartCfgBuilder0;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartCfgStatus;
import legion.biz.IssueFacade;
import legion.util.TimeTraveler;

public class MbomBuilderDelegate {
	private Logger log = LoggerFactory.getLogger(TestLogMark.class);

	private static MbomBuilderDelegate delegate = new MbomBuilderDelegate();

	private MbomBuilderDelegate() {
	}

	public static MbomBuilderDelegate getInstance() {
		return delegate;
	}

	// -------------------------------------------------------------------------------
	private final IssueFacade issueFacade = IssueFacade.getInstance();

	// -------------------------------------------------------------------------------
	// -------------------------------------Part--------------------------------------
	public PartInfo buildPartType0(TimeTraveler _tt) {
		PartBuilder0 pb = issueFacade.getBuilder(MbomBuilderType.PART_0);
		pb.appendPin("TEST_PIN").appendName("TEST_NAME");

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(pb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(pb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		PartInfo p = pb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), p);

		// check
		assertEquals("TEST_PIN", p.getPin());
		assertEquals("TEST_NAME", p.getName());

		return p;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------PartAcq------------------------------------
	public PartAcquisitionInfo buildPartAcqType01(PartInfo _p, TimeTraveler _tt) {
		PartAcqBuilder0 pab = issueFacade.getBuilder(MbomBuilderType.PART_ACQ_0);
		pab.appendPartUid(_p.getUid()).appendPartPin(_p.getPin());
		pab.appendId("TEST_ACQ_ID_1").appendName("TEST_ACQ_NAME_1").appendType(PartAcquisitionType.PURCHASING);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(pab.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(pab.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		PartAcquisitionInfo pa = pab.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), pa);

		// check
		assertEquals(_p.getUid(), pa.getPartUid());
		assertEquals(_p.getPin(), pa.getPartPin());
		assertEquals("TEST_ACQ_ID_1", pa.getId());
		assertEquals("TEST_ACQ_NAME_1", pa.getName());
		assertEquals(PartAcquisitionType.PURCHASING, pa.getType());

		return pa;
	}

	public PartAcquisitionInfo buildPartAcqType02(PartInfo _p, TimeTraveler _tt) {
		PartAcqBuilder0 pab = issueFacade.getBuilder(MbomBuilderType.PART_ACQ_0);
		pab.appendPartUid(_p.getUid()).appendPartPin(_p.getPin());
		pab.appendId("TEST_ACQ_ID_2").appendName("TEST_ACQ_NAME_2").appendType(PartAcquisitionType.SELF_PRODUCING);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(pab.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(pab.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		PartAcquisitionInfo pa = pab.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), pa);

		// check
		assertEquals(_p.getUid(), pa.getPartUid());
		assertEquals(_p.getPin(), pa.getPartPin());
		assertEquals("TEST_ACQ_ID_2", pa.getId());
		assertEquals("TEST_ACQ_NAME_2", pa.getName());
		assertEquals(PartAcquisitionType.SELF_PRODUCING, pa.getType());

		return pa;
	}

	public PartAcquisitionInfo buildPartAcqType03(PartInfo _p, TimeTraveler _tt) {
		PartAcqBuilder0 pab = issueFacade.getBuilder(MbomBuilderType.PART_ACQ_0);
		pab.appendPartUid(_p.getUid()).appendPartPin(_p.getPin());
		pab.appendId("TEST_ACQ_ID_3").appendName("TEST_ACQ_NAME_3").appendType(PartAcquisitionType.SELF_PRODUCING);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(pab.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(pab.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		PartAcquisitionInfo pa = pab.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), pa);

		// check
		assertEquals(_p.getUid(), pa.getPartUid());
		assertEquals(_p.getPin(), pa.getPartPin());
		assertEquals("TEST_ACQ_ID_3", pa.getId());
		assertEquals("TEST_ACQ_NAME_3", pa.getName());
		assertEquals(PartAcquisitionType.SELF_PRODUCING, pa.getType());

		return pa;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------PartAcqRoutingStep-------------------------------
	public PartAcqRoutingStepInfo buildPartAcqRoutingStepType0(String _partAcqUid, TimeTraveler _tt) {
		PartAcqRoutingStepBuilder0 parsb = issueFacade.getBuilder(MbomBuilderType.PART_ACQ_ROUTING_STEP_0);
		parsb.appendPartAcqUid(_partAcqUid);
		parsb.appendId("TEST_PARS_ID").appendName("TEST_PARS_NAME").appendDesp("TEST_PARS_DESP");

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(parsb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(parsb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		PartAcqRoutingStepInfo pars = parsb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), pars);

		// check
		assertEquals(_partAcqUid, pars.getPartAcqUid());
		assertEquals("TEST_PARS_ID", pars.getId());
		assertEquals("TEST_PARS_NAME", pars.getName());
		assertEquals("TEST_PARS_DESP", pars.getDesp());

		return pars;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------ParsProc------------------------------------
	public ParsProcInfo buildParsProc0(String _parsUid, TimeTraveler _tt) {
		ParsProcBuilder0 pprocb = issueFacade.getBuilder(MbomBuilderType.PARS_PROC_0);
		pprocb.appendParsUid(_parsUid);
		pprocb.appendSeq("TEST_PARS_PROC_SEQ").appendName("TEST_PARS_PROC_NAME").appendDesp("TEST_PARS_PROC_DESP");

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(pprocb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(pprocb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		ParsProcInfo pproc = pprocb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), pproc);

		// check
		assertEquals(_parsUid, pproc.getParsUid());
		assertEquals("TEST_PARS_PROC_SEQ", pproc.getSeq());
		assertEquals("TEST_PARS_PROC_NAME", pproc.getName());
		assertEquals("TEST_PARS_PROC_DESP", pproc.getDesp());

		return pproc;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------ParsPart------------------------------------
	public ParsPartInfo buildParsPart0(String _parsUid, TimeTraveler _tt) {
		ParsPartBuilder0 ppartb = issueFacade.getBuilder(MbomBuilderType.PARS_PART_0);
		ppartb.appendParsUid(_parsUid);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(ppartb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(ppartb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		ParsPartInfo ppart = ppartb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), ppart);

		// check
		assertEquals(_parsUid, ppart.getParsUid());

		return ppart;
	}
	
	// -------------------------------------------------------------------------------
	// ------------------------------------PartCfg------------------------------------
	public PartCfgInfo buildPartCfg0(String _rootPartUid, String _rootPartPin, TimeTraveler _tt) {
		PartCfgBuilder0 pcb = issueFacade.getBuilder(MbomBuilderType.PART_CFG_0);
		pcb.appendRootPartUid(_rootPartUid).appendRootPartPin(_rootPartPin);
		pcb.appendId("TEST_PC_ID").appendName("TEST_PC_NAME").appendDesp("TEST_PC_DESP");

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(pcb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(pcb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		PartCfgInfo pc = pcb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), pc);

		// check
		assertEquals(_rootPartUid, pc.getRootPartUid());
		assertEquals(_rootPartPin, pc.getRootPartPin());
		assertEquals("TEST_PC_ID", pc.getId());
		assertEquals("TEST_PC_NAME", pc.getName());
		assertEquals("TEST_PC_DESP", pc.getDesp());
		
		assertEquals(PartCfgStatus.EDITING, pc.getStatus());
		
		return pc;
	}

}
