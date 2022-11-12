package ekp.mbom;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.TestLogMark;
import ekp.data.service.mbom.PartAcquisitionInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.mbom.issue.part.PartBuilder0;
import ekp.mbom.issue.part.PartBuilderType;
import ekp.mbom.issue.partAcq.PartAcqBuilder0;
import ekp.mbom.issue.partAcq.PartAcqBuilderType;
import ekp.mbom.type.PartAcquisitionType;
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

	public PartInfo buildPartType0(TimeTraveler _tt) {
		PartBuilder0 pb = issueFacade.getBuilder(PartBuilderType.T0);
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
	
	public PartAcquisitionInfo buildPartType01(PartInfo _p, TimeTraveler _tt) {
		PartAcqBuilder0 pab = issueFacade.getBuilder(PartAcqBuilderType.T0);
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
	
	public PartAcquisitionInfo buildPartType02(PartInfo _p, TimeTraveler _tt) {
		PartAcqBuilder0 pab = issueFacade.getBuilder(PartAcqBuilderType.T0);
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
	
	public PartAcquisitionInfo buildPartType03(PartInfo _p, TimeTraveler _tt) {
		PartAcqBuilder0 pab = issueFacade.getBuilder(PartAcqBuilderType.T0);
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
}
