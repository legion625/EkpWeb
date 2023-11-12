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
import ekp.data.SdDataService;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.sd.BizPartnerInfo;
import legion.DataServiceFactory;
import legion.util.TimeTraveler;

public class TestPu extends AbstractEkpInitTest{
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);
	
	private static InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);
	private static SdDataService sdDataService = DataServiceFactory.getInstance().getService(SdDataService.class);
	
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
		PartAcqInfo paElWindingM_pu_cu08 = mbomDataService.loadPartAcquisition("EL-Winding-M", "PU-CU08");
		String title = "採購" + mmWdMCu08.getName();
		BizPartnerInfo supplier = sdDataService.loadBizPartnerByBpsn("BP2302"); // 中鋼
		WrhsBinInfo wb =  invtDataService.loadWrhsBin("2023!183!7!1878"); // WB-A1
		PurchInfo p = puDel.buildPurchAll(tt, title, supplier, wb, mmWdMCu08, paElWindingM_pu_cu08, 100000, 5000000);
		// TODO
	}
	
	
}
