package ekp.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.AbstractEkpInitTest;
import ekp.TestLogMark;
import ekp.data.service.mbom.PartCreateObj;
import ekp.data.service.mbom.PartInfo;
import ekp.mbom.MbomBuilderDelegate;
import ekp.serviceFacade.rmi.mbom.PartCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.PartRemote;
import legion.DataServiceFactory;
import legion.util.TimeTraveler;

public class MbomDataServiceTest extends AbstractEkpInitTest {
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);
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

	@Test
	public void testCreatePart() {
		PartCreateObj partCo = new PartCreateObj();
		partCo.setPin("A2");
		partCo.setName("聰明機器貓");

		PartInfo part = dataService.createPart(partCo);
		assertNotNull(part);
		log.debug("{}\t{}\t{}", part.getUid(), part.getPin(), part.getName());
	}

	@Test
	public void testLoadPart() {
		// TODO 待RMi改寫後再測測看還會不會跳出error的log。
//		PartInfo part1 = dataService.loadPart("2022!7!8!1");
		PartInfo part2 = dataService.loadPartByPin("A2");
//		log.debug("{}\t{}", part1.getUid(), part2.getUid());
//		assertTrue(part1.equals(part2));

	}
	

	
}
