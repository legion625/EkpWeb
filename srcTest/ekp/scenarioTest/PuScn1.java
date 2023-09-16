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
import ekp.data.service.mbom.ProdInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.SalesOrderItemInfo;
import ekp.invt.InvtDelegate;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.material.MaterialMasterBuilder0;
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
		MaterialMasterInfo mmA1 = mmList.get(0), mmB1 = mmList.get(1), mmC1 = mmList.get(2);
		
		
		/* 1.建立MBOM */
		log.info("================================================================");
		/* 1a.建立partA1 */
		PartInfo partA1 = mbomDel.buildPartType0(tt, "A1", "PART_A1", PartUnit.EAC);
		log.info("1a-1.建立partA1。 [{}][{}][{}]", partA1.getPin(), partA1.getName(), partA1.getUnitName());
		PartAcqInfo paA1 = mbomDel.buildPartAcqType0(partA1, tt, "PART_ACQ_A1", "PART_A1自製", PartAcquisitionType.SELF_PRODUCING);
		log.info("1a-2.建立paA1。 [{}][{}][{}][{}]", paA1.getId(), paA1.getName(), paA1.getTypeName(), paA1.getStatusName());
		ParsInfo parsA1 = mbomDel. buildParsType1(paA1, tt,"010","組裝A1", "把原料組裝成完成品。");
		log.info("1a-3.建立parsA1。 [{}][{}][{}]", parsA1.getSeq(), parsA1.getName(), parsA1.getDesp());
		assertTrue(mbomDel.paAssignMm(tt, paA1, mmA1));
		paA1 = paA1.reload();
		log.info("1a-4.paA1完成指定料件基本檔。 [{}][{}][{}]", paA1.getPartPin(), paA1.isMmAssigned(), paA1.getMmMano());
		
		/* 1b.建立partB1 */
		PartInfo partB1 = mbomDel.buildPartType0(tt, "B1", "PART_B1", PartUnit.SPL);
		log.info("1b-1.建立partB1。 [{}][{}][{}]", partB1.getPin(), partB1.getName(), partB1.getUnitName());
		PartAcqInfo paB1 = mbomDel.buildPartAcqType0(partB1, tt, "PART_ACQ_B1", "PART_B1採購", PartAcquisitionType.PURCHASING);
		log.info("1b-2.建立paB1。 [{}][{}][{}][{}]", paB1.getId(), paB1.getName(), paB1.getTypeName(), paB1.getStatusName());
		//
		assertTrue(mbomDel.paAssignMm(tt, paB1, mmB1));
		paB1 = paB1.reload();
		log.info("1b-4.paB1完成指定料件基本檔。 [{}][{}][{}]", paB1.getPartPin(), paB1.isMmAssigned(), paB1.getMmMano());
		
		/* 1c.建立partC1 */
		PartInfo partC1 = mbomDel.buildPartType0(tt, "C1", "PART_C1", PartUnit.SHE);
		log.info("1c-1.建立partC1。 [{}][{}][{}]", partC1.getPin(), partC1.getName(), partC1.getUnitName());
		PartAcqInfo paC1 = mbomDel.buildPartAcqType0(partC1, tt, "PART_ACQ_C1", "PART_C1採購", PartAcquisitionType.PURCHASING);
		log.info("1c-2.建立paC1。 [{}][{}][{}][{}]", paC1.getId(), paC1.getName(), paC1.getTypeName(), paC1.getStatusName());
		//
		assertTrue(mbomDel.paAssignMm(tt, paC1, mmC1));
		paC1 = paC1.reload();
		log.info("1c-4.paC1完成指定料件基本檔。 [{}][{}][{}]", paC1.getPartPin(), paC1.isMmAssigned(), paC1.getMmMano());
		
		
		/* 1x.建立關連 */
		PpartInfo ppartA1B1 =  mbomDel.buildParsPart1(parsA1, tt, partB1, 2);
		log.info("1x-1.關連A1-B1 [{}][{}][{}]", ppartA1B1.getPars().getPa().getPartPin(), ppartA1B1.getPartPin(), ppartA1B1.getPartReqQty());
		PpartInfo ppartA1C1 =  mbomDel.buildParsPart1(parsA1, tt, partC1, 3);
		log.info("1x-2.關連A1C1 [{}][{}][{}]", ppartA1C1.getPars().getPa().getPartPin(), ppartA1C1.getPartPin(), ppartA1C1.getPartReqQty());
		// 發布製程
		boolean b1x3A1 = mbomDel.paPublish(paA1, tt);
		paA1 = paA1.reload();
		log.info("1x-3-A1 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3A1), paA1.getId(), paA1.getName(), paA1.getTypeName(), paA1.getStatusName());
		boolean b1x3B1 = mbomDel.paPublish(paB1, tt);
		paB1 = paB1.reload();
		log.info("1x-3-B1 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3B1), paB1.getId(), paB1.getName(), paB1.getTypeName(), paB1.getStatusName());
		boolean b1x3C1 = mbomDel.paPublish(paC1, tt);
		paC1 = paC1.reload();
		log.info("1x-3-C1 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3C1), paC1.getId(), paC1.getName(), paC1.getTypeName(), paC1.getStatusName());
		
		/* 1y.建立構型 */
		PartCfgInfo pcA1 = mbomDel.buildPartCfg0(partA1.getUid(), partA1.getPin(), tt, "PCA1_ID", "PCA1_NAME", "PCA1_DESP");
		log.info("1y-1. 建立構型A1 [{}][{}][{}][{}][{}]",pcA1.getRootPartPin(),  pcA1.getId(), pcA1.getName(), pcA1.getStatusName(), pcA1.getDesp());
		log.info("1y-2. 構型指定PartAcq", DataUtil.getStr(mbomDel.runPartCfgEditing(pcA1, tt, paA1, paB1, paC1)));
		for(PartCfgConjInfo pcc: pcA1.getPccList(true))
			log.info("  [{}][{}][{}][{}][{}][{}]", pcc.getPartCfg().getId(), pcc.getPartCfg().getRootPartPin(), pcc.getPartAcq().getPartPin(),  pcc.getPartAcq().getId(), pcc.getPartAcq().getName(), pcc.getPartAcq().getStatusName());
		// 發布構型
		boolean b1y3A1 = mbomDel.runPartCfgPublish(tt, pcA1);
		pcA1 = pcA1.reload();
		log.info("1y-3-A1. 發布構型A1 [{}][{}][{}][{}][{}][{}]",DataUtil.getStr(b1y3A1), pcA1.getRootPartPin(),  pcA1.getId(), pcA1.getName(), pcA1.getStatusName(), pcA1.getDesp());

		/* 2a.產生購案 */
		log.info("================================================================");
		// 先取第1筆MM
		String[][] bizPartners = MockData.bizPartner;
		Random random = new Random();
		int i = random.nextInt(bizPartners.length);
		PurchInfo p0 = puDel.buildPurch12(tt, "採購B1C1", bizPartners[i][0], bizPartners[i][1], mmB1, 20, 2000,
				"採購MM000B共20個", mmC1, 100, 3000, "採購MM000C共100個");
		assertNotNull("p0 should NOT be null.", p0);
		log.info("2a.完成建立購案。 [{}][{}]", p0.getPuNo(), p0.getTitle());
		for (PurchItemInfo pi : p0.getPurchItemList()) {
			log.info("  [{}][{}][{}][{}][{}]", pi.getMmUid(), pi.getMmMano(), pi.getMmStdUnit(), pi.getQty(),
					pi.getValue());
		}
		
		/* 2b.購案履約（依Purch產生InvtOrder、InvtOrderItem、MbsbStmt） */
		InvtOrderInfo io2b = invtDel.buildIo11(tt, p0, "USER1", "Min-Hua", wbA101);
		assertNotNull("io2b should NOT be null.", io2b);
		log.info("2b.完成產生InvtOrder。 [{}][{}][{}][{}]", io2b.getIosn(), io2b.getStatus(), io2b.getIoiList().size(),io2b.getMbsbStmtList().size());
		
		/* 2c.InvtOrder登帳 */
		assertTrue(invtDel.ioApv(tt, io2b));
		io2b = io2b.reload();
		log.info("2c.完成InvtOrder登帳。 [{}][{}][{}][{}]", io2b.getIosn(), io2b.getStatus(), io2b.getIoiList().size(),io2b.getMbsbStmtList().size());
		showIoRelatedInfo(io2b);
		
		// showMbsRelatedInfo
		showMbsRelatedInfo(mmList);
		
		/* 3a. */
		log.debug("================================================================");
//		WorkorderInfo wo = mfDel.buildWo(tt, partA1, paA1, 10);
		WorkorderInfo wo = mfDel.buildWo(tt, paA1, pcA1, 10);
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
		mmA1 = mmA1.reload();
		log.debug("mmA.getAvgValue(): {}", mmA1.getAvgValue());
		i = random.nextInt(bizPartners.length);
		SalesOrderInfo soA1 = sdDel.buildSalesOrder11(tt, "銷售訂單1", bizPartners[i][0], bizPartners[i][1], mmA1, 1,
				mmA1.getAvgValue() * 1.5);
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
		
		// TODO
//		
		
		/* 9a. */
		log.debug("================================================================");
		ProdInfo prodA = mbomDel.buildProd0(tt, "ProdA", "Product A");
		assertNotNull("prodA should NOT be null.", prodA);
		log.info("9a.完成建立產品A。 [{}][{}]", prodA.getId(), prodA.getName());
		
		ProdCtlInfo prodCtl1A = mbomDel.buildProdCtl0(tt, "ProdCtlA", 1, "A", true);
		
		
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

	

}
