package ekp;

import org.junit.Test;

import legion.SystemInfoDefault;

public class EkpWebTest extends AbstractEkpInitTest{

	@Test
	public void test0() {
		log.debug("test0");
		log.debug("SystemInfoDefault.getInstance().getVersion(): {}", SystemInfoDefault.getInstance().getVersion());
	}
}
