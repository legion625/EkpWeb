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
import ekp.data.service.mbom.query.PartQueryParam;
import ekp.mbom.MbomBuilderDelegate;
import ekp.serviceFacade.rmi.mbom.PartCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.PartRemote;
import legion.DataServiceFactory;
import legion.util.TimeTraveler;
import legion.util.query.QueryOperation;

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
//		PartInfo part2 = dataService.loadPartByPin("A2");
		
		PartInfo part1 = dataService.loadPartByPin("A1");
		PartInfo part2 = dataService.loadPartByPin("A1");
		log.debug("hashcode: {}\t{}", part1.hashCode(), part2.hashCode());
		log.debug("uid: {}\t{}", part1.getUid(), part2.getUid());
		log.debug("equals: {}", part1.equals(part2));
//		assertTrue(part1.equals(part2));

	}
	
	@Test
	public void testSearchPart() {
		QueryOperation<PartQueryParam, PartInfo> param = new QueryOperation<>();
		param.setLimit(5);
		param = dataService.searchPart(param);
		log.debug("param.getTotal(): {}", param.getTotal());
		log.debug("limit: {}\t{}", param.getLimit()[0], param.getLimit()[1]);
		log.debug("param.getQueryResult().size(): {}", param.getQueryResult().size());
	}
	

	
}
