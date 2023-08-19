package ekp.scenarioTest;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.AbstractEkpInitTest;
import ekp.TestLogMark;
import ekp.data.MbomDataService;
import ekp.data.PuDataService;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.invt.InvtDelegate;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.material.MaterialMasterBuilder0;
import ekp.mbom.type.PartUnit;
import ekp.mock.MockData;
import ekp.pu.PuBuilderDelegate;
import ekp.util.DataUtil;
import legion.DataServiceFactory;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public class PuScn1 extends AbstractEkpInitTest {
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);

	//
	private static PuDataService puDataService;

	//
	private InvtDelegate invtDel = InvtDelegate.getInstance();
	private PuBuilderDelegate puDel = PuBuilderDelegate.getInstance();

	//
	private TimeTraveler tt;

	@BeforeClass
	public static void beforeClass() {
		puDataService = DataServiceFactory.getInstance().getService(PuDataService.class);
//		log.debug("puDataService: {}", puDataService);
	}

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
	public void testPuScn1() {
		log.debug("testPuScn1");

		/* 建立MaterialMaster */
		String[][] materialNames = MockData.materialNames;
		List<MaterialMasterInfo> mmList = new ArrayList<>();
		for (String[] i : materialNames) {
			MaterialMasterInfo mm = invtDel.buildMm0(tt, i[0], i[1].strip(), "", PartUnit.get(i[2]));
			assertNotNull("mm should NOT be null.", mm);
			mmList.add(mm);
		}
		log.info("1.完成建立料件基本檔。");

		/* 產生購案 */
		// 先取第1筆MM
		String[][] bizPartners = MockData.bizPartner;
		Random random = new Random();
		int i = random.nextInt(bizPartners.length);
		MaterialMasterInfo mm0 = mmList.get(0);
		
		
		PurchInfo p0 = puDel.buildPurch11(tt, "採購" + mm0.getName(), bizPartners[i][0], bizPartners[i][1]
				, mm0, 50,500000, "採購"+mm0.getName()+"共50"+mm0.getStdUnitChtName());
		assertNotNull("p0 should NOT be null.", p0);
		log.debug("{}\t{}",p0.getPuNo(), p0.getPerfStatus());
		
		/*  */
		
		
		
//		PurchInfo p = puDel.buildPurch(tt);
	}
	

}
