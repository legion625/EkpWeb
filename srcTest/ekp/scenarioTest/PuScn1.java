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
import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgConjInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.invt.InvtDelegate;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.material.MaterialMasterBuilder0;
import ekp.mbom.MbomBuilderDelegate;
import ekp.mbom.type.PartAcquisitionType;
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
	private static MbomDataService mbomDataService;
	private static PuDataService puDataService;

	//
	private InvtDelegate invtDel = InvtDelegate.getInstance();
	private MbomBuilderDelegate mbomDel = MbomBuilderDelegate.getInstance();
	private PuBuilderDelegate puDel = PuBuilderDelegate.getInstance();

	//
	private TimeTraveler tt;

	@BeforeClass
	public static void beforeClass() {
		invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);
		mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);
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
		log.debug("================================================================");
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
		MaterialMasterInfo mmA = mmList.get(0), mmB = mmList.get(1), mmC = mmList.get(2);
		
		
		/* 1.建立MBOM */
		log.debug("================================================================");
		/* 1a.建立PART */
		PartInfo partA = mbomDel.buildPartType0(tt, "A", "PART_A", PartUnit.EAC);
		log.info("1a-1.建立partA。 [{}][{}][{}]", partA.getPin(), partA.getName(), partA.getUnitName());
		PartAcqInfo paA = mbomDel.buildPartAcqType0(partA, tt, "PART_ACQ_A", "PART_A自製", PartAcquisitionType.SELF_PRODUCING);
		log.info("1a-2.建立paA。 [{}][{}][{}][{}]", paA.getId(), paA.getName(), paA.getTypeName(), paA.getStatusName());
		ParsInfo parsA = mbomDel. buildParsType1(paA, tt,"010","組裝A", "把原料組裝成完成品。");
		log.info("1a-3.建立parsA。 [{}][{}][{}]", parsA.getSeq(), parsA.getName(), parsA.getDesp());
		assertTrue(mbomDel.partAssignMm(tt, partA, mmList.get(0)));
		partA = partA.reload();
		log.info("1a-4.partA完成指定料件基本檔。 [{}][{}][{}]", partA.getPin(), partA.isMmAssigned(), partA.getMmMano());
		/* 1b.建立part B */
		PartInfo partB = mbomDel.buildPartType0(tt, "B", "PART_B", PartUnit.SPL);
		log.info("1b-1.建立partB。 [{}][{}][{}]", partB.getPin(), partB.getName(), partB.getUnitName());
		PartAcqInfo paB = mbomDel.buildPartAcqType0(partB, tt, "PART_ACQ_B", "PART_B採購", PartAcquisitionType.PURCHASING);
		log.info("1b-2.建立paB。 [{}][{}][{}][{}]", paB.getId(), paB.getName(), paB.getTypeName(), paB.getStatusName());
		/* 1c.建立partC */
		PartInfo partC = mbomDel.buildPartType0(tt, "C", "PART_C", PartUnit.SHE);
		log.info("1c-1.建立partC。 [{}][{}][{}]", partC.getPin(), partC.getName(), partC.getUnitName());
		PartAcqInfo paC = mbomDel.buildPartAcqType0(partC, tt, "PART_ACQ_C", "PART_C採購", PartAcquisitionType.PURCHASING);
		log.info("1c-2.建立paC。 [{}][{}][{}][{}]", paC.getId(), paC.getName(), paC.getTypeName(), paC.getStatusName());
		
		
		/* 1x.建立關連 */
		PpartInfo ppartAb =  mbomDel.buildParsPart1(parsA, tt, partB, 2);
		log.info("1x-1.關連AB [{}][{}][{}]", ppartAb.getPars().getPa().getPartPin(), ppartAb.getPartPin(), ppartAb.getPartReqQty());
		PpartInfo ppartAc =  mbomDel.buildParsPart1(parsA, tt, partC, 3);
		log.info("1x-2.關連AC [{}][{}][{}]", ppartAc.getPars().getPa().getPartPin(), ppartAc.getPartPin(), ppartAc.getPartReqQty());
		// 發布製程
		boolean b1x3a = mbomDel.paPublish(paA, tt);
		paA = paA.reload();
		log.info("1x-3a [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3a), paA.getId(), paA.getName(), paA.getTypeName(), paA.getStatusName());
		boolean b1x3b = mbomDel.paPublish(paB, tt);
		paB = paB.reload();
		log.info("1x-3b [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3b), paB.getId(), paB.getName(), paB.getTypeName(), paB.getStatusName());
		boolean b1x3c = mbomDel.paPublish(paC, tt);
		paC = paC.reload();
		log.info("1x-3c [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3c), paC.getId(), paC.getName(), paC.getTypeName(), paC.getStatusName());
		
		/* 1y.建立構型 */
		PartCfgInfo pcA1 = mbomDel.buildPartCfg0(partA.getUid(), partA.getPin(), tt, "PCA1_ID", "PCA1_NAME", "PCA1_DESP");
		log.info("1y-1. 建立構型A1 [{}][{}][{}][{}][{}]",pcA1.getRootPartPin(),  pcA1.getId(), pcA1.getName(), pcA1.getStatusName(), pcA1.getDesp());
		log.info("1y-2. 構型指定PartAcq", DataUtil.getStr(mbomDel.runPartCfgEditing(pcA1, tt, paA, paB, paC)));
		for(PartCfgConjInfo pcc: pcA1.getPccList(true))
			log.info("  [{}][{}][{}][{}][{}][{}]", pcc.getPartCfg().getId(), pcc.getPartCfg().getRootPartPin(), pcc.getPartAcq().getPartPin(),  pcc.getPartAcq().getId(), pcc.getPartAcq().getName(), pcc.getPartAcq().getStatusName());
		// 發布構型
		boolean b1y3 = mbomDel.runPartCfgPublish(tt, pcA1);
		pcA1 = pcA1.reload();
		log.info("1y-3. 發布構型A1 [{}][{}][{}][{}][{}][{}]",DataUtil.getStr(b1y3), pcA1.getRootPartPin(),  pcA1.getId(), pcA1.getName(), pcA1.getStatusName(), pcA1.getDesp());

		/* 2a.產生購案 */
		log.debug("================================================================");
		// 先取第1筆MM
		String[][] bizPartners = MockData.bizPartner;
		Random random = new Random();
		int i = random.nextInt(bizPartners.length);
		PurchInfo p0 = puDel.buildPurch12(tt, "採購BC", bizPartners[i][0], bizPartners[i][1], mmB, 20, 2000, "採購MM000B共20個",
				mmC, 100, 3000, "採購MM000C共100個");
		assertNotNull("p0 should NOT be null.", p0);
		log.info("2a.完成建立購案。 [{}][{}]", p0.getPuNo(), p0.getTitle());
		
		/* 2b.購案履約（依Purch產生InvtOrder） */
		InvtOrderInfo io = invtDel.buildIo1(tt, p0, "USER1", "Min-Hua", wb);
		assertNotNull("io should NOT be null.", io);
		log.info("2b.完成產生InvtOrder。 [{}][{}][{}]", io.getIosn(), io.getStatus(), io.getIoiList().size());
		
		
		// TODO
		/* 2c.InvtOrder登帳（產生MaterialBinStock帳值） */
		assertTrue(invtDel.ioApv(tt, io));
		io = io.reload();
		log.info("2c.完成InvtOrder登帳。 [{}][{}][{}]", io.getIosn(), io.getStatus(), io.getIoiList().size());
		
		// showMbsRelatedInfo
		showMbsRelatedInfo(mmList);
		
		/* 3a. */
		log.debug("================================================================");
		// TODO 產生工令
		
		// TODO 工令領料單
		
		
		
//		invtDataService.loadMaterialBinStockList(_mmUid)
		// TODO
		
		
		
		
//		PurchInfo p = puDel.buildPurch(tt);
	}
	
	private void showMbsRelatedInfo(List<MaterialMasterInfo> mmList) {
		for (MaterialMasterInfo mm : mmList) {
			mm = mm.reload();
			log.debug("{}\t{}\t{}\t{}\t{}", mm.getMano(), mm.getName(), mm.getStdUnit(), mm.getSumStockQty(),
					mm.getSumStockValue());
			List<MaterialBinStockInfo> mbsList = mm.getMbsList();
			for (MaterialBinStockInfo mbs : mbsList) {
				log.debug("  {}\t{}\t{}\t{}\t{}", mbs.getMano(), mbs.getWrhsLocName(), mbs.getWrhsBinName(),
						mbs.getSumStockQty(), mbs.getSumStockValue());
				for (MaterialBinStockBatchInfo mbsb : mbs.getMbsbList()) {
					log.debug("    {}\t{}\t{}\t{}\t{}\t{}", mbsb.getMi().getMisn(), mbsb.getMi().getMiac(),
							mbsb.getMi().getMiacSrcNo(), mbsb.getStockQty(), mbsb.getStockValue());
					for (MbsbStmtInfo stmt : mbsb.getStmtList()) {
						log.debug("      {}\t{}\t{}\t{}", stmt.getMbsbFlowType(), stmt.getStmtQty(),
								stmt.getStmtValue(), stmt.getPostingStatus());
					}
				}
			}
		}
	}
	

}
