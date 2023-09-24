package ekp.scenarioTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialBinStockBatchInfo;
import ekp.data.service.invt.MaterialBinStockInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MaterialInstSrcConjInfo;
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
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdCtlPartCfgConjInfo;
import ekp.data.service.mbom.ProdInfo;
import ekp.data.service.mbom.ProdModInfo;
import ekp.data.service.mbom.ProdModItemInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.SalesOrderItemInfo;
import ekp.invt.InvtDelegate;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.material.MaterialMasterBuilder0;
import ekp.invt.type.InvtOrderType;
import ekp.mbom.MbomBuilderDelegate;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartUnit;
import ekp.mf.MfBuilderDelegate;
import ekp.mock.MockData;
import ekp.pu.PuBuilderDelegate;
import ekp.sd.SdDelegate;
import ekp.util.DataUtil;
import legion.DataServiceFactory;
import legion.util.DataFO;
import legion.util.DateFormatUtil;
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
	private MfBuilderDelegate mfDel = MfBuilderDelegate.getInstance();
	private PuBuilderDelegate puDel = PuBuilderDelegate.getInstance();
	private SdDelegate sdDel = SdDelegate.getInstance();

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
		
		WrhsBinInfo wbA101 = invtDel.buildWrhsBin(tt, wl, "WB-A101", "儲位A101");
		assertNotNull("wbA101 should NOT be null.", wbA101);
		WrhsBinInfo wbA102 = invtDel.buildWrhsBin(tt, wl, "WB-A102", "儲位A102");
		assertNotNull("wbA102 should NOT be null.", wbA102);
		log.info("0a.完成建立庫房和儲位。 [{}][{}][{}][{}]", wl.getId(), wl.getName(), wbA101.getId(), wbA101.getName(), wbA102.getId(), wbA102.getName());
		
		
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
		MaterialMasterInfo mmA = mmList.get(0), mmB1 = mmList.get(1), mmC1 = mmList.get(2);
		
		
		/* XXX 1.建立MBOM */
		log.info("================================================================");
		/* 1a-1.建立partA */
		PartInfo partA = mbomDel.buildPartType0(tt, "A", "PART_A", PartUnit.EAC);
		log.info("1a-1.建立partA。 [{}][{}][{}]", partA.getPin(), partA.getName(), partA.getUnitName());
		/* 1a-2.建立pa */
		log.info("1a-2.建立pa");
		PartAcqInfo paA1 = mbomDel.buildPartAcqType0(partA, tt, "PART_ACQ_A1", "PART_A1自製", PartAcquisitionType.SELF_PRODUCING);
		PartAcqInfo paA2 = mbomDel.buildPartAcqType0(partA, tt, "PART_ACQ_A2", "PART_A2委外", PartAcquisitionType.OUTSOURCING);
		PartAcqInfo paA3 = mbomDel.buildPartAcqType0(partA, tt, "PART_ACQ_A3", "PART_A3採購", PartAcquisitionType.PURCHASING);
		log.info("paA1: [{}][{}][{}][{}]", paA1.getId(), paA1.getName(), paA1.getTypeName(), paA1.getStatusName());
		log.info("paA2: [{}][{}][{}][{}]", paA2.getId(), paA2.getName(), paA2.getTypeName(), paA2.getStatusName());
		log.info("paA3: [{}][{}][{}][{}]", paA3.getId(), paA3.getName(), paA3.getTypeName(), paA3.getStatusName());
		/* 1a-3.建立pars */
		log.info("1a-3.建立pars");
		ParsInfo parsA1 = mbomDel.buildParsType1(paA1, tt,"010","組裝A", "把原料組裝成完成品。");
		log.info("parsA1: [{}][{}][{}]", parsA1.getSeq(), parsA1.getName(), parsA1.getDesp());
		ParsInfo parsA2 = mbomDel.buildParsType1(paA2, tt, "010", "供料委外組裝A", "提供料B");
		log.info("parsA2: [{}][{}][{}]", parsA2.getSeq(), parsA2.getName(), parsA2.getDesp());
		
		/* 1a-4.指定料件基本檔 */
		log.info("1a-4");
		paA1 = paA1.reload();
		assertTrue(mbomDel.paAssignMm(tt, paA1, mmA));
		assertTrue(mbomDel.paAssignMm(tt, paA2, mmA));
		assertTrue(mbomDel.paAssignMm(tt, paA3, mmA));
		paA1 = paA1.reload();
		paA2 = paA2.reload();
		paA3 = paA3.reload();
		log.info("paA1完成指定料件基本檔。 [{}][{}][{}]", paA1.getPartPin(), paA1.isMmAssigned(), paA1.getMmMano());
		log.info("paA2完成指定料件基本檔。 [{}][{}][{}]", paA2.getPartPin(), paA2.isMmAssigned(), paA2.getMmMano());
		log.info("paA3完成指定料件基本檔。 [{}][{}][{}]", paA3.getPartPin(), paA3.isMmAssigned(), paA3.getMmMano());
		
		/* 1a-5.更新參考成本 */
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paA1, 900d));
		paA1 = paA1.reload();
		log.info("1a-5.paA1完成更新參考成本。 [{}][{}][{}]", paA1.getName(), paA1.getPartPin(), paA1.getRefUnitCost());
		
		
		/* 1b.建立partB */
		PartInfo partB = mbomDel.buildPartType0(tt, "B", "PART_B", PartUnit.SPL);
		log.info("1b-1.建立partB。 [{}][{}][{}]", partB.getPin(), partB.getName(), partB.getUnitName());
		PartAcqInfo paB1 = mbomDel.buildPartAcqType0(partB, tt, "PART_ACQ_B1", "PART_B1採購", PartAcquisitionType.PURCHASING);
		log.info("1b-2.建立paB1。 [{}][{}][{}][{}]", paB1.getId(), paB1.getName(), paB1.getTypeName(), paB1.getStatusName());
		//
		assertTrue(mbomDel.paAssignMm(tt, paB1, mmB1));
		paB1 = paB1.reload();
		log.info("1b-4.paB1完成指定料件基本檔。 [{}][{}][{}]", paB1.getPartPin(), paB1.isMmAssigned(), paB1.getMmMano());
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paB1, 100d));
		paB1 = paB1.reload();
		log.info("1b-5.paB1完成更新參考成本。 [{}][{}][{}]", paB1.getName(), paB1.getPartPin(), paB1.getRefUnitCost());
		
		/* 1c.建立partC */
		PartInfo partC = mbomDel.buildPartType0(tt, "C", "PART_C", PartUnit.SHE);
		log.info("1c-1.建立partC。 [{}][{}][{}]", partC.getPin(), partC.getName(), partC.getUnitName());
		PartAcqInfo paC1 = mbomDel.buildPartAcqType0(partC, tt, "PART_ACQ_C1", "PART_C1採購", PartAcquisitionType.PURCHASING);
		log.info("1c-2.建立paC1。 [{}][{}][{}][{}]", paC1.getId(), paC1.getName(), paC1.getTypeName(), paC1.getStatusName());
		//
		assertTrue(mbomDel.paAssignMm(tt, paC1, mmC1));
		paC1 = paC1.reload();
		log.info("1c-4.paC1完成指定料件基本檔。 [{}][{}][{}]", paC1.getPartPin(), paC1.isMmAssigned(), paC1.getMmMano());
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paC1, 35d));
		paC1 = paC1.reload();
		log.info("1c-5.paC1完成更新參考成本。 [{}][{}][{}]", paC1.getName(), paC1.getPartPin(), paC1.getRefUnitCost());
		
		/* 1x.建立關連 */
		// parsA1
		PpartInfo ppartA1B =  mbomDel.buildParsPart1(parsA1, tt, partB, 2);
		log.info("1x-A1-1.關連A1-B [{}][{}][{}]", ppartA1B.getPars().getPa().getPartPin(), ppartA1B.getPartPin(), ppartA1B.getPartReqQty());
		PpartInfo ppartA1C =  mbomDel.buildParsPart1(parsA1, tt, partC, 3);
		log.info("1x-A1-2.關連A1-C [{}][{}][{}]", ppartA1C.getPars().getPa().getPartPin(), ppartA1C.getPartPin(), ppartA1C.getPartReqQty());
		// parsA2
		PpartInfo ppartA2B =  mbomDel.buildParsPart1(parsA2, tt, partB, 2);
		log.info("1x-A2-1.關連A2-B [{}][{}][{}]", ppartA2B.getPars().getPa().getPartPin(), ppartA2B.getPartPin(), ppartA2B.getPartReqQty());
		
		// 發布製程
		boolean b1x3A1 = mbomDel.paPublish(paA1, tt);
		paA1 = paA1.reload();
		log.info("1x-3-A1 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3A1), paA1.getId(), paA1.getName(), paA1.getTypeName(), paA1.getStatusName());
		boolean b1x3A3 = mbomDel.paPublish(paA3, tt);
		paA3 = paA3.reload();
		log.info("1x-3-A3 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3A3), paA3.getId(), paA3.getName(), paA3.getTypeName(), paA3.getStatusName());
		boolean b1x3B1 = mbomDel.paPublish(paB1, tt);
		paB1 = paB1.reload();
		log.info("1x-3-B1 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3B1), paB1.getId(), paB1.getName(), paB1.getTypeName(), paB1.getStatusName());
		boolean b1x3C1 = mbomDel.paPublish(paC1, tt);
		paC1 = paC1.reload();
		log.info("1x-3-C1 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3C1), paC1.getId(), paC1.getName(), paC1.getTypeName(), paC1.getStatusName());
		
		/* 1y-1.建立構型 */
		PartCfgInfo pcCfg1 = mbomDel.buildPartCfg0(partA.getUid(), partA.getPin(), tt, "PART_CFG_1", "PART_CFG_1_NAME", "PART_CFG_1_DESP");
		log.info("1y-1-1. 建立構型Cfg1 [{}][{}][{}][{}][{}]",pcCfg1.getRootPartPin(),  pcCfg1.getId(), pcCfg1.getName(), pcCfg1.getStatusName(), pcCfg1.getDesp());
		log.info("1y-1-2. 構型指定PartAcq", DataUtil.getStr(mbomDel.runPartCfgEditing(pcCfg1, tt, paA1, paB1, paC1)));
		for(PartCfgConjInfo pcc: pcCfg1.getPccList(true))
			log.info("  [{}][{}][{}][{}][{}][{}]", pcc.getPartCfg().getId(), pcc.getPartCfg().getRootPartPin(), pcc.getPartAcq().getPartPin(),  pcc.getPartAcq().getId(), pcc.getPartAcq().getName(), pcc.getPartAcq().getStatusName());
		// 發布構型
		boolean b1y3Cfg1 = mbomDel.runPartCfgPublish(tt, pcCfg1);
		pcCfg1 = pcCfg1.reload();
		log.info("1y-1-3. 發布構型Cfg1 [{}][{}][{}][{}][{}][{}]",DataUtil.getStr(b1y3Cfg1), pcCfg1.getRootPartPin(),  pcCfg1.getId(), pcCfg1.getName(), pcCfg1.getStatusName(), pcCfg1.getDesp());
		
		/* 1y-2.建立構型 */
		PartCfgInfo pcCfg2 = mbomDel.buildPartCfg0(partA.getUid(), partA.getPin(), tt, "PART_CFG_2", "PART_CFG_2_NAME", "PART_CFG_2_DESP");
		log.info("1y-2-1. 建立構型Cfg2 [{}][{}][{}][{}][{}]",pcCfg2.getRootPartPin(),  pcCfg2.getId(), pcCfg2.getName(), pcCfg2.getStatusName(), pcCfg2.getDesp());
		log.info("1y-2-2. 構型指定PartAcq", DataUtil.getStr(mbomDel.runPartCfgEditing(pcCfg2, tt, paA2, paB1)));
		for(PartCfgConjInfo pcc: pcCfg2.getPccList(true))
			log.info("  [{}][{}][{}][{}][{}][{}]", pcc.getPartCfg().getId(), pcc.getPartCfg().getRootPartPin(), pcc.getPartAcq().getPartPin(),  pcc.getPartAcq().getId(), pcc.getPartAcq().getName(), pcc.getPartAcq().getStatusName());
		
		/* 1y-3.建立構型 */
		PartCfgInfo pcCfg3  = mbomDel.buildPartCfg0(partA.getUid(), partA.getPin(), tt, "PART_CFG_3", "PART_CFG_3_NAME", "PART_CFG_3_DESP");
		log.info("1y-3-1. 建立構型Cfg3 [{}][{}][{}][{}][{}]",pcCfg3.getRootPartPin(),  pcCfg3.getId(), pcCfg3.getName(), pcCfg3.getStatusName(), pcCfg3.getDesp());
		log.info("1y-3-2. 構型指定PartAcq", DataUtil.getStr(mbomDel.runPartCfgEditing(pcCfg3, tt, paA3)));
		for(PartCfgConjInfo pcc: pcCfg3.getPccList(true))
			log.info("  [{}][{}][{}][{}][{}][{}]", pcc.getPartCfg().getId(), pcc.getPartCfg().getRootPartPin(), pcc.getPartAcq().getPartPin(),  pcc.getPartAcq().getId(), pcc.getPartAcq().getName(), pcc.getPartAcq().getStatusName());
		

		/* XXX 2.產生購案 */
		log.info("================================================================");
		/* 2a */
		String[][] bizPartners = MockData.bizPartner;
		Random random = new Random();
		int i = random.nextInt(bizPartners.length);
		/* 2a-1.建立購案 */
		PurchInfo p1 = puDel.buildPurch12(tt, "採購B1C1", bizPartners[i][0], bizPartners[i][1], mmB1, paB1, 100, 10000,
				"採購MM000B共100個", mmC1, paC1, 100, 3000, "採購MM000C共100個");
		assertNotNull("p1 should NOT be null.", p1);
		log.info("2a-1.完成建立購案。 [{}][{}]", p1.getPuNo(), p1.getTitle());
		for (PurchItemInfo pi : p1.getPurchItemList()) {
			log.info("  [{}][{}][{}][{}][{}][{}][{}]", pi.getMmUid(), pi.getMmMano(), pi.getMmStdUnit(),
					pi.getRefPa().getId(), pi.getRefPaType().getName(), pi.getQty(), pi.getValue());
		}

		/* 2a-2.購案履約（依Purch產生InvtOrder、InvtOrderItem、MbsbStmt） */
		InvtOrderInfo io2a = invtDel.buildIo11(tt, p1, "USER1", "Min-Hua", wbA101);
		assertNotNull("io2a should NOT be null.", io2a);
		log.info("2a-2.完成產生InvtOrder。 [{}][{}][{}][{}]", io2a.getIosn(), io2a.getStatus(), io2a.getIoiList().size(),
				io2a.getMbsbStmtList().size());

		/* 2a-3.InvtOrder登帳 */
		assertTrue(invtDel.ioApv(tt, io2a));
		io2a = io2a.reload();
		log.info("2a-3.完成InvtOrder登帳。 [{}][{}][{}][{}]", io2a.getIosn(), io2a.getStatus(), io2a.getIoiList().size(),
				io2a.getMbsbStmtList().size());
		showIoRelatedInfo(io2a);
		
		
		/* 2b */
		i = random.nextInt(bizPartners.length);
		/* 2b-1. */
		PurchInfo p2 = puDel.buildPurch11(tt, "供料委外A2", bizPartners[i][0], bizPartners[i][1], mmA, paA2, 10, 3500, "供料委外A2共10個");
		assertNotNull("p2 should NOT be null.", p2);
		log.info("2b-1.完成建立購案。 [{}][{}]", p2.getPuNo(), p2.getTitle());
		for (PurchItemInfo pi : p2.getPurchItemList()) {
			log.info("  [{}][{}][{}][{}][{}][{}][{}]", pi.getMmUid(), pi.getMmMano(), pi.getMmStdUnit(),
					pi.getRefPa().getId(), pi.getRefPaType().getName(), pi.getQty(), pi.getValue());
		}
		
		/* 2b-2-1. 供料IO */
		InvtOrderInfo io2b1 = invtDel.buildIo21(tt, "USER1", "Min-Hua", p2, paA2, pcCfg2, 10);
		assertNotNull("io2b should NOT be null.", io2b1);
		log.info("2b-2-1.完成產生io2b1。 [{}][{}][{}][{}]", io2b1.getIosn(), io2b1.getStatus(), io2b1.getIoiList().size(),io2b1.getMbsbStmtList().size());
		
		/* 2b-2-2. 購案履約-入庫IO(（依Purch產生InvtOrder、InvtOrderItem、MbsbStmt）) */
		InvtOrderInfo io2b2 = invtDel.buildIo11(tt, p2, "USER1", "Min-Hua", wbA102);
		assertNotNull("io2b2 should NOT be null.", io2b2);
		log.info("2b-2-2.完成產生InvtOrder。 [{}][{}][{}][{}]", io2b2.getIosn(), io2b2.getStatus(),
				io2b2.getIoiList().size(), io2b2.getMbsbStmtList().size());
		
		/* 2b-3-1. 供料登帳 */
		assertTrue(invtDel.ioApv(tt, io2b1));
		io2b1 = io2b1.reload();
		log.info("2b-3-1.完成供料IO登帳。 [{}][{}][{}][{}]", io2b1.getIosn(), io2b1.getStatus(), io2b1.getIoiList().size(),
				io2b1.getMbsbStmtList().size());
		showIoRelatedInfo(io2b1);
		
		/* 2b-3-2. 入庫登帳*/
		assertTrue(invtDel.ioApv(tt, io2b2));
		io2b2 = io2b2.reload();
		log.info("2b-3-2.完成供料委外入庫IO登帳。 [{}][{}][{}][{}]", io2b2.getIosn(), io2b2.getStatus(), io2b2.getIoiList().size(),
				io2b2.getMbsbStmtList().size());
		showIoRelatedInfo(io2b2);
		
		
		
		
		
		
		
		// showMbsRelatedInfo
		showMbsRelatedInfo(mmList);
		
		/* 3a. */
		log.debug("================================================================");
		WorkorderInfo wo = mfDel.buildWo(tt, paA1, pcCfg1, 10);
		assertNotNull("wo should NOT be null.", wo);
		log.info("3a.產生工令。 [{}][{}][{}][{}][{}][{}][{}]", wo.getWoNo(), wo.getPartPin(), wo.getPartAcqId(), wo.getPartAcqMmMano(),wo.getPartCfgId(), wo.getRqQty(),  wo.getStatusName());
		
		log.debug("wo.getWomList().size(): {}", wo.getWomList().size());
		
		/* 3b.工令領料（依Wo產生InvtOrder、InvtOrderItem、MbsbStmt） */
		InvtOrderInfo io3b = invtDel.buildIo22(tt, wo, "USER1", "Min-Hua");
		assertNotNull("io3b should NOT be null.", io3b);
		log.info("3b.完成產生io3b。 [{}][{}][{}][{}]", io3b.getIosn(), io3b.getStatus(), io3b.getIoiList().size(),io3b.getMbsbStmtList().size());
		
		/* 3c.InvtOrder登帳 */
		assertTrue(invtDel.ioApv(tt, io3b));
		io3b = io3b.reload();
		log.info("3c.完成io3b登帳。 [{}][{}][{}][{}]", io3b.getIosn(), io3b.getStatus(), io3b.getIoiList().size(),io3b.getMbsbStmtList().size());
		showIoRelatedInfo(io3b);
		
		// showMbsRelatedInfo
		showMbsRelatedInfo(mmList);
		
		/* 3d.工令開工 */
		assertTrue(mfDel.runWoStart(tt, wo));
		wo = wo.reload();
		log.info("3d.工令開工。 [{}][{}][{}]", wo.getWoNo(), wo.getStatusName(), DateFormatUtil.transToTime(wo.getStartWorkTime()));
		
		/* 3e.工令完工 */
		assertTrue(mfDel.runWoFinishWork(tt, wo));
		wo = wo.reload();
		log.info("3e.工令完工。 [{}][{}][{}]", wo.getWoNo(), wo.getStatusName(), DateFormatUtil.transToTime(wo.getFinishWorkTime()));
		
		/* 3f.工令入庫（依Wo產生InvtOrder、InvtOrderItem、MbsbStmt、MaterialInstConj） */
		InvtOrderInfo io3f =  invtDel.buildIo12(tt, wo, "USER1", "Min-Hua", wbA102);
		assertNotNull("io3f should NOT be null.", io3f);
		wo = wo.reload();
		MaterialInstInfo woPartMi = wo.getPartMi();
		log.info("3f.工令入庫 [{}][{}][{}][{}][{}]", wo.getWoNo(), wo.getStatusName(), woPartMi.getMisn(),
				woPartMi.getMiacName(), woPartMi.getMiacSrcNo());
		for (MaterialInstSrcConjInfo srcMisc : woPartMi.getSrcMaterialInstSrcConjList()) {
			log.info("  [{}][{}][{}][{}]", srcMisc.getMi().getMm().getMano(), srcMisc.getSrcMi().getMm().getMano(),
					srcMisc.getSrcMiQty(), srcMisc.getSrcMiValue());
		}
		
		/* 3g.io3f登帳 */
		assertTrue(invtDel.ioApv(tt, io3f));
		io3f = io3f.reload();
		log.info("3g.完成io3f登帳。 [{}][{}][{}][{}]", io3f.getIosn(), io3f.getStatus(), io3f.getIoiList().size(),
				io3f.getMbsbStmtList().size());
		showIoRelatedInfo(io3f);
		
		// showMbsRelatedInfo
		showMbsRelatedInfo(mmList);
		
		/* 4a. */
		log.debug("================================================================");
		mmA = mmA.reload();
		log.debug("mmA.getAvgValue(): {}", mmA.getAvgStockValue());
		i = random.nextInt(bizPartners.length);
		SalesOrderInfo soA1 = sdDel.buildSalesOrder11(tt, "銷售訂單1", bizPartners[i][0], bizPartners[i][1], mmA, 1,
				mmA.getAvgStockValue() * 1.5);
		assertNotNull("so should NOT be null.", soA1);
		log.info("4a.完成建立銷售訂單。 [{}][{}]", soA1.getSosn(), soA1.getTitle() );
		for(SalesOrderItemInfo soi: soA1.getSalesOrderItemList()) {
			log.info("  [{}][{}][{}][{}][{}][{}]", soi.getMmUid(), soi.getMmMano(),soi.getMmName(), soi.getMmSpec(), soi.getQty(), soi.getValue());
		}
		
		/* 4b.銷售單成品出庫（依Wo產生InvtOrder、InvtOrderItem、MbsbStmt） */
		InvtOrderInfo io4b = invtDel.buildIo29(tt, soA1, "USER1", "Min-Hua");
		assertNotNull("io4b should NOT be null.", io4b);
		log.info("4b.完成產生io4b。 [{}][{}][{}][{}]", io4b.getIosn(), io4b.getStatus(), io4b.getIoiList().size(),io4b.getMbsbStmtList().size());
		soA1 = soA1.reload();
		for(SalesOrderItemInfo soi: soA1.getSalesOrderItemList()) {
			log.info("  [{}][{}][{}][{}][{}][{}]",  soi.getMmMano(),soi.getMmName(), soi.getQty(), soi.getValue(), soi.getSumIoiOrderQty(), soi.getSumIoiOrderValue());
		}
		
		/* 4c.InvtOrder登帳 */
		assertTrue(invtDel.ioApv(tt, io4b));
		io4b = io4b.reload();
		log.info("4c.完成io4b登帳。 [{}][{}][{}][{}]", io4b.getIosn(), io4b.getStatus(), io4b.getIoiList().size(),io4b.getMbsbStmtList().size());
		showIoRelatedInfo(io4b);
		
		// showMbsRelatedInfo
		showMbsRelatedInfo(mmList);
		
		/* 9a.建立產品A */
		log.debug("================================================================");
		ProdInfo prodA = mbomDel.buildProd0(tt, "ProdA", "Product A");
		assertNotNull("prodA should NOT be null.", prodA);
		log.info("9a.完成建立產品A。 [{}][{}]", prodA.getId(), prodA.getName());
		/* 9b.建立產品A分類 */
		ProdCtlInfo prodCtlA = mbomDel.buildProdCtl0(tt, 1, "產品A", true);
		ProdCtlInfo prodCtlB = mbomDel.buildProdCtl0(tt, 2, "模組B", false);
		ProdCtlInfo prodCtlC = mbomDel.buildProdCtl0(tt, 2, "模組C", false);
		
		Map<ProdCtlInfo, ProdCtlInfo> prodCtlParentMapA = new HashMap<>();
		prodCtlParentMapA.put(prodCtlB, prodCtlA);
		prodCtlParentMapA.put(prodCtlC, prodCtlA);
		assertTrue(mbomDel.runProdEditCtl(prodA, tt, prodCtlParentMapA));
		//
		prodCtlA = prodCtlA.reload();
		prodCtlB = prodCtlB.reload();
		prodCtlC = prodCtlC.reload();
		log.info("9b-A.完成建立產品分類A。 [{}][{}][{}][{}]", prodCtlA.getLv(), prodCtlA.getName(),DataUtil.getStr(prodCtlA.isReq()),  prodCtlA.getProd().getId());
		log.info("9b-B.完成建立產品分類B。 [{}][{}][{}][{}]", prodCtlB.getLv(),prodCtlB.getName(),DataUtil.getStr(prodCtlB.isReq()),  prodCtlB.getProd().getId());
		log.info("9b-C.完成建立產品分類C。 [{}][{}][{}][{}]", prodCtlC.getLv(),prodCtlC.getName(),DataUtil.getStr(prodCtlC.isReq()),  prodCtlC.getProd().getId());
		
		// 設定每個產品分類可對應的構型
		assertTrue(mbomDel.runProdCtlPartCfgConj(prodCtlA, tt, Map.entry(paA1, pcCfg1), Map.entry(paA3, pcCfg3)));
		assertTrue(mbomDel.runProdCtlPartCfgConj(prodCtlB, tt, Map.entry(paB1, pcCfg1)));
		assertTrue(mbomDel.runProdCtlPartCfgConj(prodCtlC, tt, Map.entry(paC1, pcCfg1)));
		
		showProdInfo(prodA);
		
		// ---------------------------------------------------------------------------
		log.debug("================================================================");
		log.debug("================================================================");
		/* 9c-1-1.建立模型1 */
		ProdModInfo prodMod1 = mbomDel.buildProdMod1(tt, prodA, "M1", "Model1", "The first model.");
		assertNotNull(prodMod1);
		log.info("9c-1-1.完成建立模型1");
		/* 9c-1-2.模型1指定構型及獲取方式 */
		ProdModItemInfo prodModItem1A = prodMod1.getProdModItem(prodCtlA.getUid());
		assertTrue(mbomDel.runProdModItemAssignPartAcqCfg(tt, prodModItem1A, pcCfg1.getUid(), paA1.getUid()));
		log.info("9c-1-2.完成模型1指定構型");
		
		/* output */
		log.debug("----------------------------------------------------------------");
		prodMod1 = prodMod1.reload();
		showProdModInfo(prodMod1);
//		List<ProdModPaNew> rootPmpList = parseRootProdModPaNewList(prodMod1);
//		int count = 0;
//		for (ProdModPaNew pmp : rootPmpList)
//			showProdModPaNew(pmp, "", ++count);
		
		
		log.debug("================================================================");
		log.debug("================================================================");
		/* 9c-3-1.建立模型3 */
		ProdModInfo prodMod3 = mbomDel.buildProdMod1(tt, prodA, "M3", "Model3", "The third model.");
		assertNotNull(prodMod3);
		log.info("9c-3-1.完成建立模型3");
		/* 9c-3-2.模型3指定構型及獲取方式 */
		ProdModItemInfo prodModItem3A = prodMod3.getProdModItem(prodCtlA.getUid());
		assertTrue(mbomDel.runProdModItemAssignPartAcqCfg(tt, prodModItem3A, pcCfg3.getUid(), paA3.getUid()));
		log.info("9c-3-2.完成模型3指定構型");
		
		/* output */
		log.debug("----------------------------------------------------------------");
		prodMod3 = prodMod3.reload();
		showProdModInfo(prodMod3);
		
	}
	
	// -------------------------------------------------------------------------------
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
					log.debug("    {}\t{}\t{}\t{}\t{}", mbsb.getMi().getMisn(), mbsb.getMi().getMiac(),
							mbsb.getMi().getMiacSrcNo(), mbsb.getStockQty(), mbsb.getStockValue());
					for (MbsbStmtInfo stmt : mbsb.getStmtList()) {
						log.debug("      {}\t{}\t{}\t{}\t{}", stmt.getIoiIoType().getName(), stmt.getMbsbFlowType(), stmt.getStmtQty(),
								stmt.getStmtValue(), stmt.getPostingStatus());
					}
				}
			}
		}
	}
	
	private void showIoRelatedInfo(InvtOrderInfo io) {
		io = io.reload();
		log.debug("{}\t{}\t{}\t{}", io.getIosn(), io.getApplierName(),
				DateFormatUtil.transToTime(io.getApplyTime()), DateFormatUtil.transToTime(io.getApvTime()));
		for(InvtOrderItemInfo ioi: io.getIoiList()) {
			log.debug("  {}\t{}\t{}\t{}\t{}",ioi.getMmUid(), ioi.getIoTypeName(), ioi.getOrderQty(), ioi.getOrderValue(), DataUtil.getStr(ioi.isMbsbStmtCreated()));
			for(MbsbStmtInfo stmt: ioi.getMbsbStmtList()) {
				MaterialInstInfo mi = stmt.getMbsb().getMi();
				log.debug("      {}\t{}\t{}\t{}\t{}\t{}", stmt.getMbsbFlowType(), stmt.getStmtQty(),
						stmt.getStmtValue(), stmt.getPostingStatus(), mi.getMmUid(), mi.getUid());
			}
		}
		
	}
	
	private void showProdInfo(ProdInfo prod) {
		prod = prod.reload();
		log.debug("{}\t{}", prod.getId(), prod.getName());
		for(ProdCtlInfo prodCtl: prod.getProdCtlListLv1()) {
			showProdCtlInfo(prodCtl, 1);
		}
	}
	
	private void showProdCtlInfo(ProdCtlInfo prodCtl, int lv) {
		StringBuilder sbLvSpace = new StringBuilder();
		for (int i = 0; i < lv; i++)
			sbLvSpace.append(" ");
		log.info("{}{}\t{}\t{}", sbLvSpace.toString(), prodCtl.getLv(), prodCtl.getName(), 
				DataUtil.getStr(prodCtl.isReq()));
		for (ProdCtlPartCfgConjInfo pcpcc : prodCtl.getPcpccList()) {
			log.info("  {}構型- [{}][{}]", sbLvSpace.toString(), pcpcc.getPartCfg().getId(), pcpcc.getPartAcq().getId());
		}

		for (ProdCtlInfo childProdCtl : prodCtl.getChildrenList()) {
			showProdCtlInfo(childProdCtl, lv + 1);
		}
	}

	private void showProdModInfo(ProdModInfo prodMod) {
		prodMod = prodMod.reload();
//		log.debug("prodMod.hashCode(): {}",prodMod.hashCode());
		log.info("{}\t{}\t{}", prodMod.getProd().getId(), prodMod.getId(), prodMod.getName());
//		log.debug("prodMod.getProdModItemList().size(): {}", prodMod.getProdModItemList().size());
		for (ProdModItemInfo prodModItem : prodMod.getProdModItemList()) {
			log.info("  {}\t{}\t{}\t{}\t{}", prodModItem.getProdCtl().getLv(),
					DataUtil.getStr(prodModItem.getProdCtl().isReq()),
					DataUtil.getStr(prodModItem.isPartAcqCfgAssigned()),
					prodModItem.isPartAcqCfgAssigned() ? prodModItem.getPartCfg().getId() : "",
					prodModItem.isPartAcqCfgAssigned() ? prodModItem.getPartAcq().getId() : "");
		}
		
		//
		List<ProdModPaNew> rootPmpList = parseRootProdModPaNewList(prodMod);
		int count = 0;
		for (ProdModPaNew pmp : rootPmpList)
			showProdModPaNew(pmp, "", ++count);
	}
	
	// -------------------------------------------------------------------------------
	// 以Pa為主體
	private class ProdModPaNew{
		private PartAcqInfo partAcq; 
		private PartCfgInfo partCfg;
		
		private ProdModItemInfo pmi; // 若沒指定到，就是NULL。
		
		private PpartInfo parentPpart; // 若有parentPpart，才能知道「配賦量」。根節點的parentPpart是NULL。
		
		private List<ProdModPaNew> childrenList;

		private ProdModPaNew(PartAcqInfo partAcq, PartCfgInfo partCfg, ProdModItemInfo pmi, PpartInfo parentPpart,
				List<ProdModPaNew> childrenList) {
			this.partAcq = partAcq;
			this.partCfg = partCfg;
			this.pmi = pmi;
			this.parentPpart = parentPpart;
			this.childrenList = childrenList;
		}

		public PartAcqInfo getPartAcq() {
			return partAcq;
		}

		public PartCfgInfo getPartCfg() {
			return partCfg;
		}

		public ProdModItemInfo getPmi() {
			return pmi;
		}

		public PpartInfo getParentPpart() {
			return parentPpart;
		}

		public List<ProdModPaNew> getChildrenList() {
			return childrenList;
		}
		
		// ---------------------------------------------------------------------------
		public double getQty() {
			return getParentPpart() == null ? 1 : getParentPpart().getPartReqQty();
		}
		
	}
	
	public List<ProdModPaNew> parseRootProdModPaNewList(ProdModInfo _prodMod){
		List<ProdModItemInfo> lv1PmiList = _prodMod.getProdModItemListLv1();
		
		List<ProdModPaNew> pmpList = new ArrayList<>();
		for (ProdModItemInfo pmi : lv1PmiList) {
			assertTrue(pmi.isPartAcqCfgAssigned()); // lv1一定要有指定
			PartAcqInfo partAcq = pmi.getPartAcq(); // lv1一定要有指定
			PartCfgInfo partCfg = pmi.getPartCfg(); // lv1一定要有指定
			
			ProdModPaNew pmp = parseProdModPaNew(_prodMod, partAcq, partCfg, pmi, null);
			pmpList.add(pmp);
		}
		return pmpList;
	}
	
	private ProdModPaNew parseProdModPaNew(ProdModInfo _prodMod, PartAcqInfo _partAcq, PartCfgInfo _partCfg,
			ProdModItemInfo _prodModItem, PpartInfo _parentPpart) {
		List<ProdModPaNew> childrenList = new ArrayList<>();
		for (PpartInfo ppart : _partAcq.getPpartList()) {
			ProdModItemInfo childPmi = _prodMod.getProdModItemByPartUid(ppart.getPartUid());
			PartCfgInfo childPartCfg = childPmi == null ? _partCfg : childPmi.getPartCfg();
			assertNotNull(childPartCfg);
			PartAcqInfo childPartAcq = childPartCfg.getPartAcqByPart(ppart.getPartUid());
			assertNotNull(childPartAcq);

			ProdModPaNew childPmp = parseProdModPaNew(_prodMod, childPartAcq, childPartCfg, childPmi, ppart);
			childrenList.add(childPmp);
		}

		ProdModPaNew pmp = new ProdModPaNew(_partAcq, _partCfg, _prodModItem, _parentPpart, childrenList);
		return pmp;
	}
	
	// -------------------------------------------------------------------------------
	private void showProdModPaNew(ProdModPaNew _pmp, String _prefix, int _seq) {
		String prefix = _prefix + (!DataFO.isEmptyString(_prefix) ? "-" : "") + _seq;

		log.debug("{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}", prefix, _pmp.getPartAcq().getPartPin(),

				_pmp.getQty(), _pmp.getPartCfg().getName(), _pmp.getPartAcq().getId(), _pmp.getPartAcq().getName(),
				_pmp.getPartAcq().getRefUnitCost(), _pmp.getPartAcq().getMm().getAvgStockValue(),
				_pmp.getPartAcq().getMm().getIoiAvgOrderValue(InvtOrderType.I1),
				_pmp.getPartAcq().getMm().getIoiAvgOrderValue(InvtOrderType.I2),
				_pmp.getPartAcq().getMm().getIoiAvgOrderValue(InvtOrderType.O2),
				_pmp.getPartAcq().getMm().getIoiAvgOrderValue(InvtOrderType.O9)

		);
		int seq = 0;
		for (ProdModPaNew childPmp : _pmp.getChildrenList())
			showProdModPaNew(childPmp, prefix, ++seq);
	}
}

