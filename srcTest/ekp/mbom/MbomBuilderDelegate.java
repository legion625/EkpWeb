package ekp.mbom;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.TestLogMark;
import ekp.data.service.mbom.PartInfo;
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
		PartBuilder0 pb = issueFacade.getBuilder(PartBuilderType.TYPE0);
		pb.appendPin("TEST_PIN").appendName("TEST_NAME");
		
		// validate
		StringBuilder msgValidate = new StringBuilder();
//		if (!pb.validate(msgValidate)) {
//			log.warn("validate return false: {}", msgValidate.toString());
//			return null;
//		}
		assertTrue(pb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
//		if (!pb.verify(msgVerify)) {
//			log.warn("verify return false: {}", msgVerify.toString());
//			return null;
//		}
		assertTrue(pb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		log.debug("_tt.getSiteCount(): {}", _tt.getSiteCount());
		PartInfo p = pb.build(msgBuild, _tt);
		log.debug("_tt.getSiteCount(): {}", _tt.getSiteCount());
		assertNotNull(msgBuild.toString(), p);
//		if (p == null) {
//			log.error("build return null: {}", msgBuild.toString());
//			return null;
//		}
//		

		//
		assertEquals("TEST_PIN", p.getPin());
		assertEquals("TEST_NAME", p.getName());

		return p;
	}
}
