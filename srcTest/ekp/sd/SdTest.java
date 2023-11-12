package ekp.sd;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.AbstractEkpInitTest;
import ekp.TestLogMark;
import ekp.data.SdDataService;
import ekp.data.service.sd.BizPartnerInfo;
import legion.DataServiceFactory;
import legion.util.TimeTraveler;

public class SdTest extends AbstractEkpInitTest{
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);
	
	private static SdDataService sdDataService = DataServiceFactory.getInstance().getService(SdDataService.class);
	
	private SdDelegate seDel = SdDelegate.getInstance();
	
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
	public void testBuildBizPartner() {
		assertNotNull(seDel.buildBizPartner(tt, "中國鋼鐵股份有限公司", "30414175"));
		assertNotNull(seDel.buildBizPartner(tt, "元冊科技股份有限公司", "23850518")); // 專業生產電源與磁性元件，主要產品包括變壓器鐵芯、繞組線、磁性材料等。
		assertNotNull(seDel.buildBizPartner(tt, "寶登實業有限公司", "84403689")); // 專業生產電子零件，主要產品包括電感、電阻、晶體管等。
		assertNotNull(seDel.buildBizPartner(tt, "台灣電氣硝子股份有限公司", "70848676")); // 全球知名的絕緣材料供應商，主要產品包括玻璃纖維、環氧樹脂等。
		assertNotNull(seDel.buildBizPartner(tt, "台灣杜邦股份有限公司", "11907311")); // 全球知名的化工材料供應商，主要產品包括聚酯、聚丙烯等。
		
		
	}
	

}
