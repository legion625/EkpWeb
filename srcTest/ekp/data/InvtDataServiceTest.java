package ekp.data;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.AbstractEkpInitTest;
import ekp.TestLogMark;
import legion.DataServiceFactory;

public class InvtDataServiceTest extends AbstractEkpInitTest {
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);
	private static InvtDataService dataService;

	@BeforeClass
	public static void beforeClass() {
		dataService = DataServiceFactory.getInstance().getService(InvtDataService.class);
		log.debug("dataService: {}", dataService);
	}

	@Test
	public void testCallBack() {
		log.debug("testEkpKernelServiceRemoteCallBack: {}", dataService.testEkpKernelServiceRemoteCallBack());
	}
}
