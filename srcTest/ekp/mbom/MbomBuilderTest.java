package ekp.mbom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.AbstractEkpInitTest;
import ekp.TestLogMark;
import ekp.data.MbomDataService;
import ekp.data.service.mbom.PartAcquisitionInfo;
import ekp.data.service.mbom.PartCreateObj;
import ekp.data.service.mbom.PartInfo;
import ekp.serviceFacade.rmi.mbom.PartCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.PartRemote;
import legion.DataServiceFactory;
import legion.util.TimeTraveler;

public class MbomBuilderTest extends AbstractEkpInitTest {
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);
	private static MbomDataService dataService;

	private MbomBuilderDelegate mbomDel = MbomBuilderDelegate.getInstance();
	
	private TimeTraveler tt;
	
	@BeforeClass
	public static void beforeClass() {
		dataService = DataServiceFactory.getInstance().getService(MbomDataService.class);
		log.debug("dataService: {}", dataService);
	}

	@Before
	public void before() {
		tt = new TimeTraveler();
	}
	@After
	public void after() {
		tt.travel();
	}
	
	@Test
	@Ignore
	public void test() {
		System.out.println("test");
		log.debug("testEkpKernelServiceRemoteCallBack: {}", dataService.testEkpKernelServiceRemoteCallBack());
	}
	
	@Test
	public void testPartBuilder0() {
//		TimeTraveler tt = new TimeTraveler();
		log.debug("test 1");
		//
		PartInfo p = mbomDel.buildPartType0(tt);
		PartAcquisitionInfo pa1 = mbomDel.buildPartType01(p, tt);
		PartAcquisitionInfo pa2 = mbomDel.buildPartType02(p, tt);
		PartAcquisitionInfo pa3 = mbomDel.buildPartType03(p, tt);
		
//		tt.travel();
	}
	
	

}
