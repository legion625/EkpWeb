package ekp;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import legion.SystemInfoDefault;

public class EkpWebTest extends AbstractEkpInitTest{
	private static Logger log = LoggerFactory.getLogger(EkpWebTest.class);
	@Test
	public void test0() {
		log.debug("test0");
		log.debug("SystemInfoDefault.getInstance().getVersion(): {}", SystemInfoDefault.getInstance().getVersion());
	}
}
