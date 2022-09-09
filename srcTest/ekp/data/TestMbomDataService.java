package ekp.data;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.AbstractEkpInitTest;
import ekp.TestLogMark;
import legion.DataServiceFactory;

public class TestMbomDataService extends AbstractEkpInitTest {
//	private Logger log = LoggerFactory.getLogger(TestLogMark.class);
	private static MbomDataService dataService;

	@BeforeClass
	public static void beforeClass() {
		dataService = DataServiceFactory.getInstance().getService(MbomDataService.class);
		log.debug("dataService: {}", dataService);
	}

	@Test
	public void test() {
		System.out.println("test");
		log.debug("testEkpKernelServiceRemoteCallBack: {}", dataService.testEkpKernelServiceRemoteCallBack());
	}

}
