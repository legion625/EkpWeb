package ekp.scenarioTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import ekp.data.InvtDataService;
import ekp.data.MbomDataService;
import ekp.data.PuDataService;
import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.MaterialBinStockBatchInfo;
import ekp.data.service.invt.MaterialBinStockInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.MbsbStmtInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.invt.WrhsLocInfo;
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
	private static InvtDataService invtDataService;
	private static PuDataService puDataService;

	//
	private InvtDelegate invtDel = InvtDelegate.getInstance();
	private PuBuilderDelegate puDel = PuBuilderDelegate.getInstance();

	//
	private TimeTraveler tt;

	@BeforeClass
	public static void beforeClass() {
		invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);
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

		/* 0a.建立WrhsLoc和WrhsBin */
		WrhsLocInfo wl = invtDel.buildWrhsLoc0(tt, "WL-A", "庫儲A");
		assertNotNull("wl should NOT be null.", wl);
		
		WrhsBinInfo wb = invtDel.buildWrhsBin(tt, wl, "WB-A101", "儲位A101");
		assertNotNull("wb should NOT be null.", wb);
		
		log.info("0a.完成建立庫房和儲位。 [{}][{}][{}][{}]", wl.getId(), wl.getName(), wb.getId(), wb.getName());
		
		
		/* 0b.建立MaterialMaster */
		String[][] materialNames = MockData.materialNames;
		List<MaterialMasterInfo> mmList = new ArrayList<>();
		for (String[] i : materialNames) {
			MaterialMasterInfo mm = invtDel.buildMm0(tt, i[0], i[1].strip(), "", PartUnit.get(i[2]));
			assertNotNull("mm should NOT be null.", mm);
			mmList.add(mm);
		}
		log.info("0b.完成建立料件基本檔。 [{}]", mmList.size());
		for (MaterialMasterInfo mm : mmList) {
			log.debug("{}\t{}\t{}\t{}\t{}", mm.getMano(), mm.getName(), mm.getStdUnit(), mm.getSumStockQty(),
					mm.getSumStockValue());
		}

		/* 1a.產生購案 */
		// 先取第1筆MM
		String[][] bizPartners = MockData.bizPartner;
		Random random = new Random();
		int i = random.nextInt(bizPartners.length);
		MaterialMasterInfo mm0 = mmList.get(0);
		
		PurchInfo p0 = puDel.buildPurch11(tt, "採購" + mm0.getName(), bizPartners[i][0], bizPartners[i][1], mm0, 50,
				500000, "採購" + mm0.getName() + "共50" + mm0.getStdUnitChtName());
		assertNotNull("p0 should NOT be null.", p0);
		log.info("1a.完成建立購案。 [{}][{}]", p0.getPuNo(), p0.getTitle());
		
		/* 1b.購案履約（依Purch產生InvtOrder） */
		InvtOrderInfo io = invtDel.buildIo1(tt, p0, "USER1", "Min-Hua", wb);
		assertNotNull("io should NOT be null.", io);
		log.info("1b.完成產生InvtOrder。 [{}][{}][{}]", io.getIosn(), io.getStatus(), io.getIoiList().size());
		
		/* 1c.InvtOrder登帳（產生MaterialBinStock帳值） */
		assertTrue(invtDel.ioApv(tt, io));
		io = io.reload();
		log.info("1c.完成InvtOrder登帳。 [{}][{}][{}]", io.getIosn(), io.getStatus(), io.getIoiList().size());
		for (MaterialMasterInfo mm : mmList) {
			mm = mm.reload();
			log.debug("{}\t{}\t{}\t{}\t{}", mm.getMano(), mm.getName(), mm.getStdUnit(), mm.getSumStockQty(),
					mm.getSumStockValue());
			List<MaterialBinStockInfo> mbsList = mm.getMbsList();
			for (MaterialBinStockInfo mbs : mbsList) {
				log.debug("  {}\t{}\t{}\t{}\t{}", mbs.getMano(), mbs.getWrhsLocName(), mbs.getWrhsBinName(),
						mbs.getSumStockQty(), mbs.getSumStockValue());
				for (MaterialBinStockBatchInfo mbsb : mbs.getMbsbList()) {
					log.debug("    {}\t{}\t{}\t{}\t{}\t{}", mbsb.getMi().getMisn(), mbsb.getMi().getMiac(),mbsb.getMi().getMiacSrcNo(),
							mbsb.getStockQty(), mbsb.getStockValue());
					for(MbsbStmtInfo stmt: mbsb.getStmtList()) {
						log.debug("      {}\t{}\t{}\t{}", stmt.getMbsbFlowType(), stmt.getStmtQty(), stmt.getStmtValue(), stmt.getPostingStatus());
					}
				}

			}
		}
		
		
		/**/
		
//		invtDataService.loadMaterialBinStockList(_mmUid)
		// TODO
		
		
		
		
//		PurchInfo p = puDel.buildPurch(tt);
	}
	

}
