package ekp.pu;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.AbstractEkpInitTest;
import ekp.TestLogMark;
import ekp.data.InvtDataService;
import ekp.data.MbomDataService;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mbom.PartAcqInfo;
import legion.DataServiceFactory;
import legion.util.TimeTraveler;

public class TestPu extends AbstractEkpInitTest{
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);
	
	private static InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);
	
	private PuBuilderDelegate puDel = PuBuilderDelegate.getInstance();
	
	//
	private TimeTraveler tt;
	
	@Before
	public void before() {
		tt = new TimeTraveler();
	}
	
	@After
	public void after() {
		tt.travel();
	}
	
	// -------------------------------------------------------------------------------
	@Test
	public void testBuildPurchAll() {
		MaterialMasterInfo mmWdMCu08 = invtDataService.loadMaterialMasterByMano("WD-M-CU-08");
		PartAcqInfo paElWindingM_pu_cu08 = mbomDataService.loadPartAcquisition(_partPin, _id);
		
//		puDel.buildPurchAll(tt, _title, _supplierName, _supplierBan, _wb, _mm, _pa, _qty, _value);
		// TODO
	}
	
	
}
