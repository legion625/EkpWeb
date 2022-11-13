package ekp.mbom;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.TestLogMark;
import ekp.data.service.mbom.ParsPartInfo;
import ekp.data.service.mbom.ParsProcInfo;
import ekp.data.service.mbom.PartAcqRoutingStepInfo;
import ekp.data.service.mbom.PartAcquisitionInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;
import ekp.mbom.issue.MbomBpuType;
import ekp.mbom.issue.parsPart.ParsPartBuilder0;
import ekp.mbom.issue.parsPart.ParsPartBuilder1;
import ekp.mbom.issue.parsProc.ParsProcBuilder0;
import ekp.mbom.issue.part.PartBuilder0;
import ekp.mbom.issue.partAcq.PartAcqBuilder0;
import ekp.mbom.issue.partAcqRoutingStep.PartAcqRoutingStepBuilder0;
import ekp.mbom.issue.partCfg.PartCfgBuilder0;
import ekp.mbom.issue.partCfg.PartCfgBpuEditing;
import ekp.mbom.issue.prod.ProdBuilder0;
import ekp.mbom.issue.prod.ProdBpuEditCtl;
import ekp.mbom.issue.prodCtl.ProdCtlBpuPartCfgConj;
import ekp.mbom.issue.prodCtl.ProdCtlBuilder0;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartCfgStatus;
import legion.biz.BpuFacade;
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
	private final BpuFacade bpuFacade = BpuFacade.getInstance();

	// -------------------------------------------------------------------------------
	// -------------------------------------Part--------------------------------------
	public PartInfo buildPartType0(TimeTraveler _tt, String _pin, String _name) {
		PartBuilder0 pb = bpuFacade.getBuilder(MbomBpuType.PART_0);
//		pb.appendPin("TEST_PIN").appendName("TEST_NAME");
		pb.appendPin(_pin).appendName(_name);

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
//		assertEquals("TEST_PIN", p.getPin());
//		assertEquals("TEST_NAME", p.getName());
		assertEquals(_pin, p.getPin());
		assertEquals(_name, p.getName());
		
		return p;
	}

	public PartInfo buildPartType0(TimeTraveler _tt) {
		return buildPartType0(_tt, "TEST_PIN", "TEST_NAME");
	}
	
	// -------------------------------------------------------------------------------
	// ------------------------------------PartAcq------------------------------------
	public PartAcquisitionInfo buildPartAcqType0(PartInfo _p, TimeTraveler _tt, String _id, String _name,
			PartAcquisitionType _type) {
		PartAcqBuilder0 pab = bpuFacade.getBuilder(MbomBpuType.PART_ACQ_0);
		pab.appendPartUid(_p.getUid()).appendPartPin(_p.getPin());
		pab.appendId(_id).appendName(_name).appendType(_type);

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
		assertEquals(_id, pa.getId());
		assertEquals(_name, pa.getName());
		assertEquals(_type, pa.getType());

		return pa;
	}
	
	public PartAcquisitionInfo buildPartAcqType01(PartInfo _p, TimeTraveler _tt) {
		return buildPartAcqType0(_p, _tt, "TEST_ACQ_ID_1", "TEST_ACQ_NAME_1", PartAcquisitionType.PURCHASING);
		
//		PartAcqBuilder0 pab = issueFacade.getBuilder(MbomBuilderType.PART_ACQ_0);
//		pab.appendPartUid(_p.getUid()).appendPartPin(_p.getPin());
//		pab.appendId("TEST_ACQ_ID_1").appendName("TEST_ACQ_NAME_1").appendType(PartAcquisitionType.PURCHASING);
////		pab.appendId(_id).appendName(_name).appendType(_type);
//
//		// validate
//		StringBuilder msgValidate = new StringBuilder();
//		assertTrue(pab.validate(msgValidate), msgValidate.toString());
//
//		// verify
//		StringBuilder msgVerify = new StringBuilder();
//		assertTrue(pab.verify(msgVerify), msgVerify.toString());
//
//		// build
//		StringBuilder msgBuild = new StringBuilder();
//		PartAcquisitionInfo pa = pab.build(msgBuild, _tt);
//		assertNotNull(msgBuild.toString(), pa);
//
//		// check
//		assertEquals(_p.getUid(), pa.getPartUid());
//		assertEquals(_p.getPin(), pa.getPartPin());
//		assertEquals("TEST_ACQ_ID_1", pa.getId());
//		assertEquals("TEST_ACQ_NAME_1", pa.getName());
//		assertEquals(PartAcquisitionType.PURCHASING, pa.getType());
////		assertEquals(_id, pa.getId());
////		assertEquals(_name, pa.getName());
////		assertEquals(_type, pa.getType());
//
//		return pa;
	}

	public PartAcquisitionInfo buildPartAcqType02(PartInfo _p, TimeTraveler _tt) {
		return buildPartAcqType0(_p, _tt, "TEST_ACQ_ID_2", "TEST_ACQ_NAME_2", PartAcquisitionType.OUTSOURCING);
//		PartAcqBuilder0 pab = issueFacade.getBuilder(MbomBuilderType.PART_ACQ_0);
//		pab.appendPartUid(_p.getUid()).appendPartPin(_p.getPin());
//		pab.appendId("TEST_ACQ_ID_2").appendName("TEST_ACQ_NAME_2").appendType(PartAcquisitionType.SELF_PRODUCING);
//
//		// validate
//		StringBuilder msgValidate = new StringBuilder();
//		assertTrue(pab.validate(msgValidate), msgValidate.toString());
//
//		// verify
//		StringBuilder msgVerify = new StringBuilder();
//		assertTrue(pab.verify(msgVerify), msgVerify.toString());
//
//		// build
//		StringBuilder msgBuild = new StringBuilder();
//		PartAcquisitionInfo pa = pab.build(msgBuild, _tt);
//		assertNotNull(msgBuild.toString(), pa);
//
//		// check
//		assertEquals(_p.getUid(), pa.getPartUid());
//		assertEquals(_p.getPin(), pa.getPartPin());
//		assertEquals("TEST_ACQ_ID_2", pa.getId());
//		assertEquals("TEST_ACQ_NAME_2", pa.getName());
//		assertEquals(PartAcquisitionType.SELF_PRODUCING, pa.getType());
//
//		return pa;
	}

	public PartAcquisitionInfo buildPartAcqType03(PartInfo _p, TimeTraveler _tt) {
		return buildPartAcqType0(_p, _tt, "TEST_ACQ_ID_3", "TEST_ACQ_NAME_3", PartAcquisitionType.SELF_PRODUCING);
//		PartAcqBuilder0 pab = issueFacade.getBuilder(MbomBuilderType.PART_ACQ_0);
//		pab.appendPartUid(_p.getUid()).appendPartPin(_p.getPin());
//		pab.appendId("TEST_ACQ_ID_3").appendName("TEST_ACQ_NAME_3").appendType(PartAcquisitionType.SELF_PRODUCING);
//
//		// validate
//		StringBuilder msgValidate = new StringBuilder();
//		assertTrue(pab.validate(msgValidate), msgValidate.toString());
//
//		// verify
//		StringBuilder msgVerify = new StringBuilder();
//		assertTrue(pab.verify(msgVerify), msgVerify.toString());
//
//		// build
//		StringBuilder msgBuild = new StringBuilder();
//		PartAcquisitionInfo pa = pab.build(msgBuild, _tt);
//		assertNotNull(msgBuild.toString(), pa);
//
//		// check
//		assertEquals(_p.getUid(), pa.getPartUid());
//		assertEquals(_p.getPin(), pa.getPartPin());
//		assertEquals("TEST_ACQ_ID_3", pa.getId());
//		assertEquals("TEST_ACQ_NAME_3", pa.getName());
//		assertEquals(PartAcquisitionType.SELF_PRODUCING, pa.getType());
//
//		return pa;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------PartAcqRoutingStep-------------------------------
	public PartAcqRoutingStepInfo buildPartAcqRoutingStepType0(String _partAcqUid, TimeTraveler _tt, String _id, String _name, String _desp) {
		PartAcqRoutingStepBuilder0 parsb = bpuFacade.getBuilder(MbomBpuType.PART_ACQ_ROUTING_STEP_0);
		parsb.appendPartAcqUid(_partAcqUid);
//		parsb.appendId("TEST_PARS_ID").appendName("TEST_PARS_NAME").appendDesp("TEST_PARS_DESP");
		parsb.appendId(_id).appendName(_name).appendDesp(_desp);

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
//		assertEquals("TEST_PARS_ID", pars.getId());
//		assertEquals("TEST_PARS_NAME", pars.getName());
//		assertEquals("TEST_PARS_DESP", pars.getDesp());
		assertEquals(_id, pars.getId());
		assertEquals(_name, pars.getName());
		assertEquals(_desp, pars.getDesp());

		return pars;
	}
	
	public PartAcqRoutingStepInfo buildPartAcqRoutingStepType0(String _partAcqUid, TimeTraveler _tt) {
		return buildPartAcqRoutingStepType0(_partAcqUid, _tt, "TEST_PARS_ID", "TEST_PARS_NAME", "TEST_PARS_DESP");
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------ParsProc------------------------------------
	public ParsProcInfo buildParsProc0(String _parsUid, TimeTraveler _tt) {
		ParsProcBuilder0 pprocb = bpuFacade.getBuilder(MbomBpuType.PARS_PROC_0);
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
		ParsPartBuilder0 ppartb = bpuFacade.getBuilder(MbomBpuType.PARS_PART_0);
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

	public ParsPartInfo buildParsPart1(PartAcqRoutingStepInfo _pars, TimeTraveler _tt, PartInfo _part,
			double _partReqQty) {
		ParsPartBuilder1 ppartb = bpuFacade.getBuilder(MbomBpuType.PARS_PART_1, _pars);
		ppartb.appendPart(_part).appendPartReqQty(_partReqQty);

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
		assertEquals(_pars.getUid(), ppart.getParsUid());
		assertEquals(true, ppart.isAssignPart());
		assertEquals(_part.getUid(), ppart.getPartUid());
		assertEquals(_part.getPin(), ppart.getPartPin());
		assertEquals(_partReqQty, ppart.getPartReqQty());

		return ppart;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------PartCfg------------------------------------
	public PartCfgInfo buildPartCfg0(String _rootPartUid, String _rootPartPin, TimeTraveler _tt) {
		PartCfgBuilder0 pcb = bpuFacade.getBuilder(MbomBpuType.PART_CFG_0);
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
	
	public boolean runPartCfgEditing(PartCfgInfo _pc, TimeTraveler _tt, PartAcquisitionInfo... _partAcqs) {
		PartCfgBpuEditing bpu = bpuFacade.getBuilder(MbomBpuType.PART_CFG_$EDITING, _pc);
		for (PartAcquisitionInfo _partAcq : _partAcqs)
			bpu.appendPartAcq(_partAcq);

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
	
	// -------------------------------------------------------------------------------
	// -------------------------------------Part--------------------------------------
	public ProdInfo buildProd0(TimeTraveler _tt) {
		ProdBuilder0 pb = bpuFacade.getBuilder(MbomBpuType.PROD_0);
		pb.appendId("TEST_ID").appendName("TEST_NAME");

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(pb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(pb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		ProdInfo p = pb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), p);

		// check
		assertEquals("TEST_ID", p.getId());
		assertEquals("TEST_NAME", p.getName());

		return p;
	}
	
	public boolean runProdEditCtl(ProdInfo _p, TimeTraveler _tt, Map<ProdCtlInfo, ProdCtlInfo> _prodCtlParentMap) {
		ProdBpuEditCtl bpu = bpuFacade.getBuilder(MbomBpuType.PROD_$EDIT_CTL, _p);
		for (Entry<ProdCtlInfo, ProdCtlInfo> entry : _prodCtlParentMap.entrySet()) {
			ProdCtlInfo pcChild = entry.getKey();
			ProdCtlInfo pcParent = entry.getValue();
			if (pcParent == null) {
				bpu.appendProdCtl(pcChild);
			} else {
				bpu.appendProdCtl(pcChild, pcParent);
			}
		}

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

	// -------------------------------------------------------------------------------
	// ------------------------------------PartCtl------------------------------------
	public ProdCtlInfo buildProdCtl01(TimeTraveler _tt) {
		ProdCtlBuilder0 pcb = bpuFacade.getBuilder(MbomBpuType.PROD_CTL_0);
		pcb.appendId("TEST_LV1_ID").appendLv(1).appendName("TEST_LV1_NAME").appendReq(true);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(pcb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(pcb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		ProdCtlInfo pc = pcb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), pc);

		// check
		assertEquals("TEST_LV1_ID", pc.getId());
		assertEquals(1, pc.getLv());
		assertEquals("TEST_LV1_NAME", pc.getName());
		assertEquals(true, pc.isReq());

		return pc;
	}
	
	public ProdCtlInfo buildProdCtl02(TimeTraveler _tt) {
		ProdCtlBuilder0 pcb = bpuFacade.getBuilder(MbomBpuType.PROD_CTL_0);
		pcb.appendId("TEST_LV2_ID").appendLv(2).appendName("TEST_LV2_NAME").appendReq(true);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(pcb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(pcb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		ProdCtlInfo pc = pcb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), pc);

		// check
		assertEquals("TEST_LV2_ID", pc.getId());
		assertEquals(2, pc.getLv());
		assertEquals("TEST_LV2_NAME", pc.getName());
		assertEquals(true, pc.isReq());

		return pc;
	}
	
	public ProdCtlInfo buildProdCtl03(TimeTraveler _tt) {
		ProdCtlBuilder0 pcb = bpuFacade.getBuilder(MbomBpuType.PROD_CTL_0);
		pcb.appendId("TEST_LV3_ID").appendLv(3).appendName("TEST_LV3_NAME").appendReq(true);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(pcb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(pcb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		ProdCtlInfo pc = pcb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), pc);

		// check
		assertEquals("TEST_LV3_ID", pc.getId());
		assertEquals(3, pc.getLv());
		assertEquals("TEST_LV3_NAME", pc.getName());
		assertEquals(true, pc.isReq());

		return pc;
	}

	public boolean runProdCtlPartCfgConj(ProdCtlInfo _prodCtl, TimeTraveler _tt, PartCfgInfo... _partCfgs) {
		ProdCtlBpuPartCfgConj bpu = bpuFacade.getBuilder(MbomBpuType.PROD_CTL_$PART_CFG_CONJ, _prodCtl);
		for (PartCfgInfo _partCfg : _partCfgs)
			bpu.appendPartCfg(_partCfg);

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
