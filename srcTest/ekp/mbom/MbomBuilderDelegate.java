package ekp.mbom;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.TestLogMark;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mbom.PprocInfo;
import ekp.data.MbomDataService;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;
import ekp.mbom.issue.MbomBpuType;
import ekp.mbom.issue.parsPart.ParsPartBuilder0;
import ekp.mbom.issue.parsPart.ParsPartBuilder1;
import ekp.mbom.issue.parsProc.ParsProcBuilder0;
import ekp.mbom.issue.part.PartBuilder0;
import ekp.mbom.issue.partAcq.PaBpuPublish;
import ekp.mbom.issue.partAcq.PartAcqBuilder0;
import ekp.mbom.issue.partAcq.PaBpuAsignMm;
import ekp.mbom.issue.partAcqRoutingStep.ParsBuilder1;
import ekp.mbom.issue.partCfg.PartCfgBuilder0;
import ekp.mbom.issue.partCfg.PartCfgBpuEditing;
import ekp.mbom.issue.partCfg.PartCfgBpuPublish;
import ekp.mbom.issue.prod.ProdBuilder0;
import ekp.mbom.issue.prod.ProdBpuEditCtl;
import ekp.mbom.issue.prodCtl.ProdCtlBpuPartCfgConj;
import ekp.mbom.issue.prodCtl.ProdCtlBuilder0;
import ekp.mbom.type.PartAcqStatus;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartCfgStatus;
import ekp.mbom.type.PartUnit;
import legion.DataServiceFactory;
import legion.biz.BpuFacade;
import legion.util.LogUtil;
import legion.util.TimeTraveler;
import legion.web.zk.ZkUtil;

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
	public PartInfo buildPartType0(TimeTraveler _tt, String _pin, String _name, PartUnit _unit) {
		PartBuilder0 pb = bpuFacade.getBuilder(MbomBpuType.PART_0);
		pb.appendPin(_pin).appendName(_name).appendUnit(_unit);

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
		assertEquals(_pin, p.getPin());
		assertEquals(_name, p.getName());

		return p;
	}

	public PartInfo buildPartType0(TimeTraveler _tt) {
		return buildPartType0(_tt, "TEST_PIN", "TEST_NAME", PartUnit.EAC);
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------PartAcq------------------------------------
	public PartAcqInfo buildPartAcqType0(PartInfo _p, TimeTraveler _tt, String _id, String _name,
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
		PartAcqInfo pa = pab.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), pa);

		// check
		assertEquals(_p.getUid(), pa.getPartUid());
		assertEquals(_p.getPin(), pa.getPartPin());
		assertEquals(_id, pa.getId());
		assertEquals(_name, pa.getName());
		assertEquals(_type, pa.getType());

		return pa;
	}
	
	public PartAcqInfo buildPartAcqType01(PartInfo _p, TimeTraveler _tt) {
		return buildPartAcqType0(_p, _tt, "TEST_ACQ_ID_1", "TEST_ACQ_NAME_1", PartAcquisitionType.PURCHASING);
	}

	public PartAcqInfo buildPartAcqType02(PartInfo _p, TimeTraveler _tt) {
		return buildPartAcqType0(_p, _tt, "TEST_ACQ_ID_2", "TEST_ACQ_NAME_2", PartAcquisitionType.OUTSOURCING);
	}

	public PartAcqInfo buildPartAcqType03(PartInfo _p, TimeTraveler _tt) {
		return buildPartAcqType0(_p, _tt, "TEST_ACQ_ID_3", "TEST_ACQ_NAME_3", PartAcquisitionType.SELF_PRODUCING);
	}
	
	public boolean paAssignMm(TimeTraveler _tt, PartAcqInfo _pa, MaterialMasterInfo _mm) {
		PaBpuAsignMm bpu = bpuFacade.getBuilder(MbomBpuType.PART_ACQ_$ASSIGN_MM, _pa);
		bpu.appendMm(_mm);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(bpu.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(bpu.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		Boolean result = bpu.build(msgBuild, _tt);
		assertTrue(result);

		// check
		// TODO

		return result;
	}
	
	public boolean paPublish(PartAcqInfo _pa, TimeTraveler _tt) {
		PaBpuPublish b = bpuFacade.getBuilder(MbomBpuType.PART_ACQ_$PUBLISH, _pa);
		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(b.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(b.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		boolean result = b.build(msgBuild, _tt);
		assertTrue(result);

		// check
		PartAcqInfo pa = _pa.reload();
		assertEquals(PartAcqStatus.PUBLISHED, pa.getStatus());
		assertTrue(pa.getPublishTime() > 0);

		return result;
	}
	
	public void paRevertPublish(PartAcqInfo _pa, boolean _writeDb) {
		// check
		assertEquals(PartAcqStatus.PUBLISHED, _pa.getStatus());
		assertTrue(_pa.getPublishTime() > 0);

		if (_writeDb) {
			MbomDataService dataService = DataServiceFactory.getInstance().getService(MbomDataService.class);
			log.info("dataService.partAcqRevertPublish(_pa.getUid()): {}",
					dataService.partAcqRevertPublish(_pa.getUid()));
		}

		return;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------PartAcqRoutingStep-------------------------------
	public ParsInfo buildParsType1(PartAcqInfo _partAcq, TimeTraveler _tt, String _seq, String _name, String _desp) {
		ParsBuilder1 parsb = bpuFacade.getBuilder(MbomBpuType.PARS_1, _partAcq);
		parsb.appendSeq(_seq).appendName(_name).appendDesp(_desp);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(parsb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(parsb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		ParsInfo pars = parsb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), pars);

		// check
		assertEquals(_partAcq.getUid(), pars.getPartAcqUid());
		assertEquals(_seq, pars.getSeq());
		assertEquals(_name, pars.getName());
		assertEquals(_desp, pars.getDesp());

		return pars;
	}
	
	public ParsInfo buildPartAcqRoutingStepType0(PartAcqInfo _partAcq, TimeTraveler _tt) {
		return buildParsType1(_partAcq, _tt, "TEST_PARS_ID", "TEST_PARS_NAME", "TEST_PARS_DESP");
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------ParsProc------------------------------------
	public PprocInfo buildParsProc0(String _parsUid, TimeTraveler _tt) {
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
		PprocInfo pproc = pprocb.build(msgBuild, _tt);
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
	public PpartInfo buildParsPart0(String _parsUid, TimeTraveler _tt) {
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
		PpartInfo ppart = ppartb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), ppart);

		// check
		assertEquals(_parsUid, ppart.getParsUid());

		return ppart;
	}

	public PpartInfo buildParsPart1(ParsInfo _pars, TimeTraveler _tt, PartInfo _part,
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
		PpartInfo ppart = ppartb.build(msgBuild, _tt);
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
	public PartCfgInfo buildPartCfg0(String _rootPartUid, String _rootPartPin, TimeTraveler _tt, String _id,
			String _name, String _desp) {
		PartCfgBuilder0 pcb = bpuFacade.getBuilder(MbomBpuType.PART_CFG_0);
		pcb.appendRootPartUid(_rootPartUid).appendRootPartPin(_rootPartPin);
		pcb.appendId(_id).appendName(_name).appendDesp(_desp);

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
		assertEquals(_id, pc.getId());
		assertEquals(_name, pc.getName());
		assertEquals(_desp, pc.getDesp());

		assertEquals(PartCfgStatus.EDITING, pc.getStatus());

		return pc;
	}

	public PartCfgInfo buildPartCfg0(String _rootPartUid, String _rootPartPin, TimeTraveler _tt) {
		return buildPartCfg0(_rootPartUid, _rootPartPin, _tt, "TEST_PC_ID", "TEST_PC_NAME", "TEST_PC_DESP");
	}
	
	public boolean runPartCfgEditing(PartCfgInfo _pc, TimeTraveler _tt, PartAcqInfo... _partAcqs) {
		PartCfgBpuEditing bpu = bpuFacade.getBuilder(MbomBpuType.PART_CFG_$EDITING, _pc);
		for (PartAcqInfo _partAcq : _partAcqs)
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
	
	public boolean runPartCfgPublish(TimeTraveler _tt, PartCfgInfo _pc) {
		PartCfgBpuPublish bpu = bpuFacade.getBuilder(MbomBpuType.PART_CFG_$PUBLISH, _pc);

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
	// -------------------------------------Prod--------------------------------------
	public ProdInfo buildProd0(TimeTraveler _tt, String _id, String _name) {
		ProdBuilder0 pb = bpuFacade.getBuilder(MbomBpuType.PROD_0);
		pb.appendId(_id).appendName(_name);

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
		assertEquals(_id, p.getId());
		assertEquals(_name, p.getName());

		return p;
	}
	
	public ProdInfo buildProd0(TimeTraveler _tt) {
		return buildProd0(_tt, "TEST_ID", "TEST_NAME");
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
	public ProdCtlInfo buildProdCtl0(TimeTraveler _tt, String _id, int _lv, String _name, boolean _req) {
		ProdCtlBuilder0 pcb = bpuFacade.getBuilder(MbomBpuType.PROD_CTL_0);
		pcb.appendId(_id).appendLv(_lv).appendName(_name).appendReq(_req);

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
		assertEquals(_id, pc.getId());
		assertEquals(_lv, pc.getLv());
		assertEquals(_name, pc.getName());
		assertEquals(_req, pc.isReq());

		return pc;
		
	} 
	
	public ProdCtlInfo buildProdCtl01(TimeTraveler _tt) {
		return buildProdCtl0(_tt, "TEST_LV1_ID", 1, "TEST_LV1_NAME", true);
//		ProdCtlBuilder0 pcb = bpuFacade.getBuilder(MbomBpuType.PROD_CTL_0);
//		pcb.appendId("TEST_LV1_ID").appendLv(1).appendName("TEST_LV1_NAME").appendReq(true);
//
//		// validate
//		StringBuilder msgValidate = new StringBuilder();
//		assertTrue(pcb.validate(msgValidate), msgValidate.toString());
//
//		// verify
//		StringBuilder msgVerify = new StringBuilder();
//		assertTrue(pcb.verify(msgVerify), msgVerify.toString());
//
//		// build
//		StringBuilder msgBuild = new StringBuilder();
//		ProdCtlInfo pc = pcb.build(msgBuild, _tt);
//		assertNotNull(msgBuild.toString(), pc);
//
//		// check
//		assertEquals("TEST_LV1_ID", pc.getId());
//		assertEquals(1, pc.getLv());
//		assertEquals("TEST_LV1_NAME", pc.getName());
//		assertEquals(true, pc.isReq());
//
//		return pc;
	}
	
	public ProdCtlInfo buildProdCtl02(TimeTraveler _tt) {
		return buildProdCtl0(_tt, "TEST_LV2_ID", 2, "TEST_LV2_NAME", true);
//		ProdCtlBuilder0 pcb = bpuFacade.getBuilder(MbomBpuType.PROD_CTL_0);
//		pcb.appendId("TEST_LV2_ID").appendLv(2).appendName("TEST_LV2_NAME").appendReq(true);
//
//		// validate
//		StringBuilder msgValidate = new StringBuilder();
//		assertTrue(pcb.validate(msgValidate), msgValidate.toString());
//
//		// verify
//		StringBuilder msgVerify = new StringBuilder();
//		assertTrue(pcb.verify(msgVerify), msgVerify.toString());
//
//		// build
//		StringBuilder msgBuild = new StringBuilder();
//		ProdCtlInfo pc = pcb.build(msgBuild, _tt);
//		assertNotNull(msgBuild.toString(), pc);
//
//		// check
//		assertEquals("TEST_LV2_ID", pc.getId());
//		assertEquals(2, pc.getLv());
//		assertEquals("TEST_LV2_NAME", pc.getName());
//		assertEquals(true, pc.isReq());
//
//		return pc;
	}
	
	public ProdCtlInfo buildProdCtl03(TimeTraveler _tt) {
		return buildProdCtl0(_tt, "TEST_LV3_ID", 3, "TEST_LV3_NAME", true);
//		
//		ProdCtlBuilder0 pcb = bpuFacade.getBuilder(MbomBpuType.PROD_CTL_0);
//		pcb.appendId("TEST_LV3_ID").appendLv(3).appendName("TEST_LV3_NAME").appendReq(true);
//
//		// validate
//		StringBuilder msgValidate = new StringBuilder();
//		assertTrue(pcb.validate(msgValidate), msgValidate.toString());
//
//		// verify
//		StringBuilder msgVerify = new StringBuilder();
//		assertTrue(pcb.verify(msgVerify), msgVerify.toString());
//
//		// build
//		StringBuilder msgBuild = new StringBuilder();
//		ProdCtlInfo pc = pcb.build(msgBuild, _tt);
//		assertNotNull(msgBuild.toString(), pc);
//
//		// check
//		assertEquals("TEST_LV3_ID", pc.getId());
//		assertEquals(3, pc.getLv());
//		assertEquals("TEST_LV3_NAME", pc.getName());
//		assertEquals(true, pc.isReq());
//
//		return pc;
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
