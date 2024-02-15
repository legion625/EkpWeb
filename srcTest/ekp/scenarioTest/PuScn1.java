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
import legion.util.NumberFormatUtil;
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
//		tt.travel();
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
		WrhsBinInfo wbA103 = invtDel.buildWrhsBin(tt, wl, "WB-A103", "儲位A103");
		assertNotNull("wbA103 should NOT be null.", wbA103);
		log.info("0a.完成建立庫房和儲位。 [{}][{}][{}][{}]", wl.getId(), wl.getName(), wbA101.getId(), wbA101.getName(), wbA102.getId(), wbA102.getName(),wbA103.getId(), wbA103.getName());
		
		
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
		MaterialMasterInfo mmA = mmList.get(0), mmB = mmList.get(1), mmC1 = mmList.get(2),mmC3 = mmList.get(3), mmD = mmList.get(4),
				mmE = mmList.get(5), mmF = mmList.get(6), mmG = mmList.get(7),
				mmAfa = mmList.get(8), mmBeta = mmList.get(9), mmGamma = mmList.get(10);
		
		
		/* XXX 1.建立MBOM */
		log.info("================================================================");
		/* 1a-1.建立partA */
		PartInfo partA = mbomDel.buildPartType0(tt, "A", "PART_A", PartUnit.EAC);
		log.info("1a-1.建立partA。 [{}][{}][{}]", partA.getPin(), partA.getName(), partA.getUnitName());
		PartInfo partAfa = mbomDel.buildPartType0(tt, "Afa", "PART_Afa", PartUnit.EAC);
		log.info("1a-1.建立partAfa。 [{}][{}][{}]", partAfa.getPin(), partAfa.getName(), partAfa.getUnitName());
		/* 1a-2.建立pa */
		log.info("1a-2.建立pa");
		PartAcqInfo paA1 = mbomDel.buildPartAcqType0(partA, tt, "PART_ACQ_A1", "A1自製", PartAcquisitionType.SP);
		PartAcqInfo paA2 = mbomDel.buildPartAcqType0(partA, tt, "PART_ACQ_A2", "A2委外", PartAcquisitionType.OS);
		PartAcqInfo paA3 = mbomDel.buildPartAcqType0(partA, tt, "PART_ACQ_A3", "A3採購", PartAcquisitionType.PU);
		PartAcqInfo paAfa3 = mbomDel.buildPartAcqType0(partAfa, tt, "PART_ACQ_Afa3", "Afa3自製", PartAcquisitionType.SP);
		log.info("paA1: [{}][{}][{}][{}]", paA1.getId(), paA1.getName(), paA1.getTypeName(), paA1.getStatusName());
		log.info("paA2: [{}][{}][{}][{}]", paA2.getId(), paA2.getName(), paA2.getTypeName(), paA2.getStatusName());
		log.info("paA3: [{}][{}][{}][{}]", paA3.getId(), paA3.getName(), paA3.getTypeName(), paA3.getStatusName());
		log.info("paAfa3: [{}][{}][{}][{}]", paAfa3.getId(), paAfa3.getName(), paAfa3.getTypeName(), paAfa3.getStatusName());
		
		/* 1a-3.建立pars */
		log.info("1a-3.建立pars");
		ParsInfo parsA1 = mbomDel.buildParsType1(paA1, tt,"010","組裝A", "把原料組裝成完成品。");
		log.info("parsA1: [{}][{}][{}]", parsA1.getSeq(), parsA1.getName(), parsA1.getDesp());
		ParsInfo parsA2 = mbomDel.buildParsType1(paA2, tt, "010", "供料委外組裝A", "提供料B");
		log.info("parsA2: [{}][{}][{}]", parsA2.getSeq(), parsA2.getName(), parsA2.getDesp());
		ParsInfo parsAfa3 = mbomDel.buildParsType1(paAfa3, tt, "010", "組裝Afa", "把原料組裝成完成品。");
		log.info("parsAfa3: [{}][{}][{}]", parsAfa3.getSeq(), parsAfa3.getName(), parsAfa3.getDesp());
	
		
		/* 1a-4.指定料件基本檔 */
		log.info("1a-4");
		assertTrue(mbomDel.paAssignMm(tt, paA1, mmA));
		assertTrue(mbomDel.paAssignMm(tt, paA2, mmA));
		assertTrue(mbomDel.paAssignMm(tt, paA3, mmA));
		assertTrue(mbomDel.paAssignMm(tt, paAfa3, mmAfa));
		paA1 = paA1.reload();
		paA2 = paA2.reload();
		paA3 = paA3.reload();
		paAfa3 = paAfa3.reload();
		log.info("paA1完成指定料件基本檔。 [{}][{}][{}]", paA1.getPartPin(), paA1.isMmAssigned(), paA1.getMmMano());
		log.info("paA2完成指定料件基本檔。 [{}][{}][{}]", paA2.getPartPin(), paA2.isMmAssigned(), paA2.getMmMano());
		log.info("paA3完成指定料件基本檔。 [{}][{}][{}]", paA3.getPartPin(), paA3.isMmAssigned(), paA3.getMmMano());
		log.info("paAfa3完成指定料件基本檔。 [{}][{}][{}]", paAfa3.getPartPin(), paAfa3.isMmAssigned(), paAfa3.getMmMano());
		
		/* 1a-5.更新參考成本 */
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paA1, 900d));
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paAfa3, 2000d));
		paA1 = paA1.reload();
		paAfa3 = paAfa3.reload();
		log.info("1a-5.paA1完成更新參考成本。 [{}][{}][{}]", paA1.getName(), paA1.getPartPin(), paA1.getRefUnitCost());
		log.info("1a-5.paAfa3完成更新參考成本。 [{}][{}][{}]", paAfa3.getName(), paAfa3.getPartPin(), paAfa3.getRefUnitCost());
		
		
		/* 1b.建立partB */
		PartInfo partB = mbomDel.buildPartType0(tt, "B", "PART_B", PartUnit.SPL);
		log.info("1b-1.建立partB。 [{}][{}][{}]", partB.getPin(), partB.getName(), partB.getUnitName());
		PartInfo partBeta = mbomDel.buildPartType0(tt, "Beta", "PART_Beta", PartUnit.SPL);
		log.info("1b-1.建立partBeta。 [{}][{}][{}]", partBeta.getPin(), partBeta.getName(), partBeta.getUnitName());
		
		/* 1b-2.建立pa */
		log.info("1b-2.建立pa");
		PartAcqInfo paB1 = mbomDel.buildPartAcqType0(partB, tt, "PART_ACQ_B1", "B1採購", PartAcquisitionType.PU);
		log.info("paB1: [{}][{}][{}][{}]", paB1.getId(), paB1.getName(), paB1.getTypeName(), paB1.getStatusName());
		PartAcqInfo paB3 = mbomDel.buildPartAcqType0(partB, tt, "PART_ACQ_B3", "B3自製", PartAcquisitionType.SP);
		log.info("paB3: [{}][{}][{}][{}]", paB3.getId(), paB3.getName(), paB3.getTypeName(), paB3.getStatusName());
		PartAcqInfo paBeta1 = mbomDel.buildPartAcqType0(partBeta, tt, "PART_ACQ_Beta1", "Beta1採購", PartAcquisitionType.PU);
		log.info("paBeta1: [{}][{}][{}][{}]", paBeta1.getId(), paBeta1.getName(), paBeta1.getTypeName(), paBeta1.getStatusName());
		
		/* 1b-3.建立pars */
		log.info("1b-3.建立pars");
		ParsInfo parsB3 = mbomDel.buildParsType1(paB3, tt,"010","自製B", "製造料件B。");
		log.info("parsB3: [{}][{}][{}]", parsB3.getSeq(), parsB3.getName(), parsB3.getDesp());
		// 指定料件基本檔
		assertTrue(mbomDel.paAssignMm(tt, paB1, mmB));
		assertTrue(mbomDel.paAssignMm(tt, paB3, mmB));
		assertTrue(mbomDel.paAssignMm(tt, paBeta1, mmBeta));
		paB1 = paB1.reload();
		paB3 = paB3.reload();
		paBeta1 = paBeta1.reload();
		log.info("1b-4.paB1完成指定料件基本檔。 [{}][{}][{}]", paB1.getPartPin(), paB1.isMmAssigned(), paB1.getMmMano());
		log.info("1b-4.paB3完成指定料件基本檔。 [{}][{}][{}]", paB3.getPartPin(), paB3.isMmAssigned(), paB3.getMmMano());
		log.info("1b-4.paBeta1完成指定料件基本檔。 [{}][{}][{}]", paBeta1.getPartPin(), paBeta1.isMmAssigned(), paBeta1.getMmMano());
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paB1, 100d));
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paB3, 60d));
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paBeta1, 300d));
		paB1 = paB1.reload();
		paB3 = paB3.reload();
		paBeta1 = paBeta1.reload();
		log.info("1b-5.paB1完成更新參考成本。 [{}][{}][{}]", paB1.getName(), paB1.getPartPin(), paB1.getRefUnitCost());
		log.info("1b-5.paB3完成更新參考成本。 [{}][{}][{}]", paB3.getName(), paB3.getPartPin(), paB3.getRefUnitCost());
		log.info("1b-5.paBeta1完成更新參考成本。 [{}][{}][{}]", paBeta1.getName(), paBeta1.getPartPin(), paBeta1.getRefUnitCost());
		
		/* 1c.建立partC */
		PartInfo partC = mbomDel.buildPartType0(tt, "C", "PART_C", PartUnit.SHE);
		log.info("1c-1.建立partC。 [{}][{}][{}]", partC.getPin(), partC.getName(), partC.getUnitName());
		PartInfo partGamma = mbomDel.buildPartType0(tt, "Gamma", "PART_Gamma", PartUnit.SHE);
		log.info("1c-1.建立partGamma。 [{}][{}][{}]", partGamma.getPin(), partGamma.getName(), partGamma.getUnitName());
		
		PartAcqInfo paC1 = mbomDel.buildPartAcqType0(partC, tt, "PART_ACQ_C1", "C1採購", PartAcquisitionType.PU);
		log.info("1c-2.建立paC1。 [{}][{}][{}][{}]", paC1.getId(), paC1.getName(), paC1.getTypeName(), paC1.getStatusName());
		PartAcqInfo paC3 = mbomDel.buildPartAcqType0(partC, tt, "PART_ACQ_C3", "C3自製", PartAcquisitionType.SP);
		log.info("1c-2.建立paC3。 [{}][{}][{}][{}]", paC3.getId(), paC3.getName(), paC3.getTypeName(), paC3.getStatusName());
		PartAcqInfo paGamma1 = mbomDel.buildPartAcqType0(partGamma, tt, "PART_ACQ_Gamma1", "Gamma1採購", PartAcquisitionType.PU);
		log.info("1c-2.建立paGamma1。 [{}][{}][{}][{}]", paGamma1.getId(), paGamma1.getName(), paGamma1.getTypeName(), paGamma1.getStatusName());
		
		/* 1c-3.建立pars */
		log.info("1c-3.建立pars");
		ParsInfo parsC3 = mbomDel.buildParsType1(paC3, tt,"010","自製C", "製造料件C。");
		log.info("parsC3: [{}][{}][{}]", parsC3.getSeq(), parsC3.getName(), parsC3.getDesp());
		// 指定料件基本檔
		assertTrue(mbomDel.paAssignMm(tt, paC1, mmC1));
		assertTrue(mbomDel.paAssignMm(tt, paC3, mmC3));
		assertTrue(mbomDel.paAssignMm(tt, paGamma1, mmGamma));
		paC1 = paC1.reload();
		paC3 = paC3.reload();
		paGamma1 = paGamma1.reload();
		log.info("1c-4.paC1完成指定料件基本檔。 [{}][{}][{}]", paC1.getPartPin(), paC1.isMmAssigned(), paC1.getMmMano());
		log.info("1c-4.paC3完成指定料件基本檔。 [{}][{}][{}]", paC3.getPartPin(), paC3.isMmAssigned(), paC3.getMmMano());
		log.info("1c-4.paGamma1完成指定料件基本檔。 [{}][{}][{}]", paGamma1.getPartPin(), paGamma1.isMmAssigned(), paGamma1.getMmMano());
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paC1, 35d));
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paC3, 25d));
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paGamma1, 80d));
		paC1 = paC1.reload();
		paC3 = paC3.reload();
		paGamma1 = paGamma1.reload();
		log.info("1c-5.paC1完成更新參考成本。 [{}][{}][{}]", paC1.getName(), paC1.getPartPin(), paC1.getRefUnitCost());
		log.info("1c-5.paC3完成更新參考成本。 [{}][{}][{}]", paC3.getName(), paC3.getPartPin(), paC3.getRefUnitCost());
		log.info("1c-5.paGamma1完成更新參考成本。 [{}][{}][{}]", paGamma1.getName(), paGamma1.getPartPin(), paGamma1.getRefUnitCost());
		
		/* 1d.建立partD */
		PartInfo partD = mbomDel.buildPartType0(tt, "D", "PART_D", PartUnit.MMT);
		log.info("1d-1.建立partD。 [{}][{}][{}]", partD.getPin(), partD.getName(), partD.getUnitName());
		PartAcqInfo paD = mbomDel.buildPartAcqType0(partD, tt, "PART_ACQ_D", "PART_D採購", PartAcquisitionType.PU);
		log.info("1d-2.建立paD。 [{}][{}][{}][{}]", paD.getId(), paD.getName(), paD.getTypeName(), paD.getStatusName());
		assertTrue(mbomDel.paAssignMm(tt, paD, mmD));
		paD = paD.reload();
		log.info("1d-4.paD完成指定料件基本檔。 [{}][{}][{}]", paD.getPartPin(), paD.isMmAssigned(), paD.getMmMano());
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paD, 0.25d));
		paD = paD.reload();
		log.info("1b-5.paD完成更新參考成本。 [{}][{}][{}]", paD.getName(), paD.getPartPin(), paD.getRefUnitCost());
		
		/* 1e.建立partE */
		PartInfo partE = mbomDel.buildPartType0(tt, "E", "PART_E", PartUnit.EAC);
		log.info("1e-1.建立partE。 [{}][{}][{}]", partE.getPin(), partE.getName(), partE.getUnitName());
		PartAcqInfo paE = mbomDel.buildPartAcqType0(partE, tt, "PART_ACQ_E", "PART_E採購", PartAcquisitionType.PU);
		log.info("1e-2.建立paE。 [{}][{}][{}][{}]", paE.getId(), paE.getName(), paE.getTypeName(), paE.getStatusName());
		assertTrue(mbomDel.paAssignMm(tt, paE, mmE));
		paE = paE.reload();
		log.info("1e-4.paE完成指定料件基本檔。 [{}][{}][{}]", paE.getPartPin(), paE.isMmAssigned(), paE.getMmMano());
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paE, 0.25d));
		paE = paE.reload();
		log.info("1e-5.paE完成更新參考成本。 [{}][{}][{}]", paE.getName(), paE.getPartPin(), paE.getRefUnitCost());
		
		/* 1f.建立partF */
		PartInfo partF = mbomDel.buildPartType0(tt, "F", "PART_F", PartUnit.CMK);
		log.info("1f-1.建立partF。 [{}][{}][{}]", partF.getPin(), partF.getName(), partF.getUnitName());
		PartAcqInfo paF = mbomDel.buildPartAcqType0(partF, tt, "PART_ACQ_F", "PART_F採購", PartAcquisitionType.PU);
		log.info("1f-2.建立paF。 [{}][{}][{}][{}]", paF.getId(), paF.getName(), paF.getTypeName(), paF.getStatusName());
		assertTrue(mbomDel.paAssignMm(tt, paF, mmF));
		paF = paF.reload();
		log.info("1f-4.paF完成指定料件基本檔。 [{}][{}][{}]", paF.getPartPin(), paF.isMmAssigned(), paF.getMmMano());
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paF, 0.01d));
		paF = paF.reload();
		log.info("1f-5.paF完成更新參考成本。 [{}][{}][{}]", paF.getName(), paF.getPartPin(), paF.getRefUnitCost());
		
		/* 1g.建partG */
		PartInfo partG = mbomDel.buildPartType0(tt, "G", "PART_G", PartUnit.EAC);
		log.info("1g-1.建立partG。 [{}][{}][{}]", partG.getPin(), partG.getName(), partG.getUnitName());
		PartAcqInfo paG = mbomDel.buildPartAcqType0(partG, tt, "PART_ACQ_G", "PART_G採購", PartAcquisitionType.PU);
		log.info("1g-2.建立paG。 [{}][{}][{}][{}]", paG.getId(), paG.getName(), paG.getTypeName(), paG.getStatusName());
		assertTrue(mbomDel.paAssignMm(tt, paG, mmG));
		paG = paG.reload();
		log.info("1g-4.paG完成指定料件基本檔。 [{}][{}][{}]", paG.getPartPin(), paG.isMmAssigned(), paG.getMmMano());
		assertTrue(mbomDel.runPaUpdateRefUnitCost(tt, paG, 15d));
		paG = paG.reload();
		log.info("1g-5.paG完成更新參考成本。 [{}][{}][{}]", paG.getName(), paG.getPartPin(), paG.getRefUnitCost());
		
		/* 1x.建立關連 */
		// parsA1
		PpartInfo ppartA1B =  mbomDel.buildParsPart1(parsA1, tt, partB, 2);
		log.info("1x-A1-B.關連A1-B [{}][{}][{}]", ppartA1B.getPars().getPa().getPartPin(), ppartA1B.getPartPin(), ppartA1B.getPartReqQty());
		PpartInfo ppartA1C =  mbomDel.buildParsPart1(parsA1, tt, partC, 3);
		log.info("1x-A1-C.關連A1-C [{}][{}][{}]", ppartA1C.getPars().getPa().getPartPin(), ppartA1C.getPartPin(), ppartA1C.getPartReqQty());
		// parsA2
		PpartInfo ppartA2B =  mbomDel.buildParsPart1(parsA2, tt, partB, 2);
		log.info("1x-A2-B.關連A2-B [{}][{}][{}]", ppartA2B.getPars().getPa().getPartPin(), ppartA2B.getPartPin(), ppartA2B.getPartReqQty());
		// parsAfa3
		PpartInfo ppartAfa3Beta = mbomDel.buildParsPart1(parsAfa3, tt, partBeta, 4);
		log.info("1x-Afa3-Beta.關連Afa3-Beta [{}][{}][{}]", ppartAfa3Beta.getPars().getPa().getPartPin(), ppartAfa3Beta.getPartPin(), ppartAfa3Beta.getPartReqQty());
		PpartInfo ppartAfa3Gamma= mbomDel.buildParsPart1(parsAfa3, tt, partGamma, 3);
		log.info("1x-Afa3-Gamma.關連Afa3-Gamma [{}][{}][{}]", ppartAfa3Gamma.getPars().getPa().getPartPin(), ppartAfa3Gamma.getPartPin(), ppartAfa3Gamma.getPartReqQty());
		
		// parsB3
		PpartInfo ppartB3D = mbomDel.buildParsPart1(parsB3, tt, partD, 20);
		log.info("1x-B3-D.關連B3-D [{}][{}][{}]", ppartB3D.getPars().getPa().getPartPin(), ppartB3D.getPartPin(), ppartB3D.getPartReqQty());
		PpartInfo ppartB3E = mbomDel.buildParsPart1(parsB3, tt, partE, 1);
		log.info("1x-B3-E.關連B3-E [{}][{}][{}]", ppartB3E.getPars().getPa().getPartPin(), ppartB3E.getPartPin(), ppartB3E.getPartReqQty());
		// parsC3
		PpartInfo ppartC3F = mbomDel.buildParsPart1(parsC3, tt, partF, 100);
		log.info("1x-C3-F.關連C3-F [{}][{}][{}]", ppartC3F.getPars().getPa().getPartPin(), ppartC3F.getPartPin(), ppartC3F.getPartReqQty());
		PpartInfo ppartC3G = mbomDel.buildParsPart1(parsC3, tt, partG, 1);
		log.info("1x-C3-G.關連C3-G [{}][{}][{}]", ppartC3G.getPars().getPa().getPartPin(), ppartC3G.getPartPin(), ppartC3G.getPartReqQty());
		
		// 發布製程
		boolean b1x3A1 = mbomDel.paPublish(paA1, tt);
		paA1 = paA1.reload();
		log.info("1x-3-A1 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3A1), paA1.getId(), paA1.getName(), paA1.getTypeName(), paA1.getStatusName());
		boolean b1x3A3 = mbomDel.paPublish(paA3, tt);
		paA3 = paA3.reload();
		log.info("1x-3-A3 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3A3), paA3.getId(), paA3.getName(), paA3.getTypeName(), paA3.getStatusName());
		boolean b1x3Afa3 = mbomDel.paPublish(paAfa3, tt);
		paAfa3 = paAfa3.reload();
		log.info("1x-3-Afa3 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3Afa3), paAfa3.getId(), paAfa3.getName(), paAfa3.getTypeName(), paAfa3.getStatusName());
		boolean b1x3B1 = mbomDel.paPublish(paB1, tt);
		paB1 = paB1.reload();
		log.info("1x-3-B1 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3B1), paB1.getId(), paB1.getName(), paB1.getTypeName(), paB1.getStatusName());
		boolean b1x3B3 = mbomDel.paPublish(paB3, tt);
		paB3 = paB3.reload();
		log.info("1x-3-B3 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3B3), paB3.getId(), paB3.getName(), paB3.getTypeName(), paB3.getStatusName());
		boolean b1x3Beta1 = mbomDel.paPublish(paBeta1, tt);
		paBeta1 = paBeta1.reload();
		log.info("1x-3-Beta1 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3Beta1), paBeta1.getId(), paBeta1.getName(), paBeta1.getTypeName(), paBeta1.getStatusName());
		boolean b1x3C1 = mbomDel.paPublish(paC1, tt);
		paC1 = paC1.reload();
		log.info("1x-3-C1 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3C1), paC1.getId(), paC1.getName(), paC1.getTypeName(), paC1.getStatusName());
		boolean b1x3C3 = mbomDel.paPublish(paC3, tt);
		paC3 = paC3.reload();
		log.info("1x-3-C3 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3C3), paC3.getId(), paC3.getName(), paC3.getTypeName(), paC3.getStatusName());
		boolean b1x3Gamma1 = mbomDel.paPublish(paGamma1, tt);
		paGamma1 = paGamma1.reload();
		log.info("1x-3-Gamma1 [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3Gamma1), paGamma1.getId(), paGamma1.getName(), paGamma1.getTypeName(), paGamma1.getStatusName());
		boolean b1x3D = mbomDel.paPublish(paD, tt);
		paD = paD.reload();
		log.info("1x-3-D [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3D), paD.getId(), paD.getName(), paD.getTypeName(), paD.getStatusName());
		boolean b1x3E = mbomDel.paPublish(paE, tt);
		paE = paE.reload();
		log.info("1x-3-E [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3E), paE.getId(), paE.getName(), paE.getTypeName(), paE.getStatusName());
		boolean b1x3F = mbomDel.paPublish(paF, tt);
		paF = paF.reload();
		log.info("1x-3-F [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3F), paF.getId(), paF.getName(), paF.getTypeName(), paF.getStatusName());
		boolean b1x3G = mbomDel.paPublish(paG, tt);
		paG = paG.reload();
		log.info("1x-3-G [{}][{}][{}][{}][{}]", DataUtil.getStr(b1x3G), paG.getId(), paG.getName(), paG.getTypeName(), paG.getStatusName());
		
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
		
		/* 1y-4.建立構型 */
		PartCfgInfo pcCfg4 = mbomDel.buildPartCfg0(partA.getUid(), partA.getPin(), tt, "PART_CFG_4", "PART_CFG_4_NAME", "PART_CFG_4_DESP");
		log.info("1y-4-1. 建立構型Cfg4 [{}][{}][{}][{}][{}]",pcCfg4.getRootPartPin(),  pcCfg4.getId(), pcCfg4.getName(), pcCfg4.getStatusName(), pcCfg4.getDesp());
		log.info("1y-4-2. 構型指定PartAcq", DataUtil.getStr(mbomDel.runPartCfgEditing(pcCfg4, tt, paA1, paB3, paC3, paD, paE, paF, paG)));
		for(PartCfgConjInfo pcc: pcCfg4.getPccList(true))
			log.info("  [{}][{}][{}][{}][{}][{}]", pcc.getPartCfg().getId(), pcc.getPartCfg().getRootPartPin(), pcc.getPartAcq().getPartPin(),  pcc.getPartAcq().getId(), pcc.getPartAcq().getName(), pcc.getPartAcq().getStatusName());
		// 發布構型
		boolean b1y3Cfg4 = mbomDel.runPartCfgPublish(tt, pcCfg4);
		pcCfg4 = pcCfg4.reload();
		log.info("1y-4-3. 發布構型Cfg4 [{}][{}][{}][{}][{}][{}]",DataUtil.getStr(b1y3Cfg4), pcCfg4.getRootPartPin(),  pcCfg4.getId(), pcCfg4.getName(), pcCfg4.getStatusName(), pcCfg4.getDesp());
		
		/* 1y-Afa.建立構型 */
		PartCfgInfo pcCfgAfa = mbomDel.buildPartCfg0(partAfa.getUid(), partAfa.getPin(), tt, "PART_CFG_Afa", "PART_CFG_Afa_NAME", "PART_CFG_Afa_DESP");
		log.info("1y-Afa-1. 建立構型CfgAfa [{}][{}][{}][{}][{}]",pcCfgAfa.getRootPartPin(),  pcCfgAfa.getId(), pcCfgAfa.getName(), pcCfgAfa.getStatusName(), pcCfgAfa.getDesp());
		log.info("1y-Afa-2. 構型指定PartAcq", DataUtil.getStr(mbomDel.runPartCfgEditing(pcCfgAfa, tt, paAfa3, paBeta1, paGamma1)));
		for(PartCfgConjInfo pcc: pcCfgAfa.getPccList(true))
			log.info("  [{}][{}][{}][{}][{}][{}]", pcc.getPartCfg().getId(), pcc.getPartCfg().getRootPartPin(), pcc.getPartAcq().getPartPin(),  pcc.getPartAcq().getId(), pcc.getPartAcq().getName(), pcc.getPartAcq().getStatusName());
		// 發布構型
		boolean b1y3CfgAfa = mbomDel.runPartCfgPublish(tt, pcCfgAfa);
		pcCfgAfa = pcCfgAfa.reload();
		log.info("1y-Afa-3. 發布構型CfgAfa [{}][{}][{}][{}][{}][{}]",DataUtil.getStr(b1y3CfgAfa), pcCfgAfa.getRootPartPin(),  pcCfgAfa.getId(), pcCfgAfa.getName(), pcCfgAfa.getStatusName(), pcCfgAfa.getDesp());
		

		/* XXX 2.產生購案 */
		log.info("================================================================");
		/* 2a */
		String[][] bizPartners = MockData.bizPartner;
		Random random = new Random();
		int i = random.nextInt(bizPartners.length);
		/* 2a-1.建立購案 */
		PurchInfo p1 = puDel.buildPurch12(tt, "採購B1C1", bizPartners[i][0], bizPartners[i][1], mmB, paB1, 100, 10000,
				"採購MM000B共100個", mmC1, paC1, 100, 3000, "採購MM000C共100個");
		assertNotNull("p1 should NOT be null.", p1);
		log.info("2a-1.完成建立購案。 [{}][{}]", p1.getPuNo(), p1.getTitle());
		for (PurchItemInfo pi : p1.getPurchItemList()) 
			log.info("  [{}][{}][{}][{}][{}][{}][{}]", pi.getMmUid(), pi.getMmMano(), pi.getMmStdUnit(),
					pi.getRefPa().getId(), pi.getRefPaType().getName(), pi.getQty(), pi.getValue());

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
		for (PurchItemInfo pi : p2.getPurchItemList()) 
			log.info("  [{}][{}][{}][{}][{}][{}][{}]", pi.getMmUid(), pi.getMmMano(), pi.getMmStdUnit(),
					pi.getRefPa().getId(), pi.getRefPaType().getName(), pi.getQty(), pi.getValue());
		
		/* 2b-2-1. 供料IO */
		InvtOrderInfo io2b1 = invtDel.buildIo21(tt, "USER1", "Min-Hua", p2.getPurchItemList().get(0), paA2, pcCfg2, 10);
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
		
		/* 2c */
		i = random.nextInt(bizPartners.length);
		/* 2c-1.建立購案 */
		PurchInfo p3 = puDel.buildPurch11(tt, "採購A3", bizPartners[i][0], bizPartners[i][1], mmA, paA3, 10, 10000,
				"採購A3共10個");
		assertNotNull("p3 should NOT be null.", p3);
		log.info("2c-1.完成建立購案。 [{}][{}]", p3.getPuNo(), p3.getTitle());
		for (PurchItemInfo pi : p3.getPurchItemList())
			log.info("  [{}][{}][{}][{}][{}][{}][{}]", pi.getMmUid(), pi.getMmMano(), pi.getMmStdUnit(),
					pi.getRefPa().getId(), pi.getRefPaType().getName(), pi.getQty(), pi.getValue());
		/* 2c-2.購案履約（依Purch產生InvtOrder、InvtOrderItem、MbsbStmt） */
		InvtOrderInfo io2c = invtDel.buildIo11(tt, p3, "USER1", "Min-Hua", wbA103);
		assertNotNull("io2c should NOT be null.", io2c);
		log.info("2c-2.完成產生InvtOrder。 [{}][{}][{}][{}]", io2c.getIosn(), io2c.getStatus(), io2c.getIoiList().size(),
				io2c.getMbsbStmtList().size());

		/* 2c-3.InvtOrder登帳 */
		assertTrue(invtDel.ioApv(tt, io2c));
		io2c = io2c.reload();
		log.info("2c-3.完成InvtOrder登帳。 [{}][{}][{}][{}]", io2c.getIosn(), io2c.getStatus(), io2c.getIoiList().size(),
				io2c.getMbsbStmtList().size());
		showIoRelatedInfo(io2c);
		
		
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
		assertTrue(mbomDel.runProdCtlPartCfgConj(prodCtlA, tt, Map.entry(paA1, pcCfg1),Map.entry(paA1, pcCfg4),Map.entry(paA2, pcCfg2), Map.entry(paA3, pcCfg3), Map.entry(paAfa3, pcCfgAfa)));
		assertTrue(mbomDel.runProdCtlPartCfgConj(prodCtlB, tt, Map.entry(paB1, pcCfg1),Map.entry(paB3, pcCfg4),Map.entry(paBeta1, pcCfgAfa)));
		assertTrue(mbomDel.runProdCtlPartCfgConj(prodCtlC, tt, Map.entry(paC1, pcCfg1),Map.entry(paC3, pcCfg4),Map.entry(paGamma1, pcCfgAfa)));
		
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
		showProdModInfo(prodMod1);
		
		
		log.debug("================================================================");
		log.debug("================================================================");
		/* 9c-2-1.建立模型2 */
		ProdModInfo prodMod2 = mbomDel.buildProdMod1(tt, prodA,"M2","Model2","The second model.");
		assertNotNull(prodMod2);
		log.info("9c-2-1.完成建立模型1");
		/* 9c-2-2.模型2指定構型及獲取方式 */
		ProdModItemInfo prodModItem2A = prodMod2.getProdModItem(prodCtlA.getUid());
		assertTrue(mbomDel.runProdModItemAssignPartAcqCfg(tt, prodModItem2A, pcCfg2.getUid(), paA2.getUid()));
		log.info("9c-2-2.完成模型2指定構型");
		/* output */
		log.debug("----------------------------------------------------------------");
		showProdModInfo(prodMod2);
		
		
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
		showProdModInfo(prodMod3);
		
		log.debug("================================================================");
		log.debug("================================================================");
		/* 9c-4-1.建立模型4 */
		ProdModInfo prodMod4 = mbomDel.buildProdMod1(tt, prodA, "M4", "Model4", "The fourth model.");
		assertNotNull(prodMod4);
		log.info("9c-4-1.完成建立模型4");
		/* 9c-4-2.模型4指定構型及獲取方式 */
		ProdModItemInfo prodModItem4A = prodMod4.getProdModItem(prodCtlA.getUid());
		assertTrue(mbomDel.runProdModItemAssignPartAcqCfg(tt, prodModItem4A, pcCfg4.getUid(), paA1.getUid()));
		log.info("9c-4-2.完成模型4指定構型");
		
		/* output */
		log.debug("----------------------------------------------------------------");
		showProdModInfo(prodMod4);
		
		
		log.debug("================================================================");
		log.debug("================================================================");
		/* 9c-5-1.建立模型5 */
		ProdModInfo prodMod5 = mbomDel.buildProdMod1(tt, prodA, "M5", "Model5", "The fifth model.");
		assertNotNull(prodMod5);
		log.info("9c-5-1.完成建立模型5");
		/* 9c-5-2.模型5指定構型及獲取方式 */
		ProdModItemInfo prodModItem5A = prodMod5.getProdModItem(prodCtlA.getUid());
		assertTrue(mbomDel.runProdModItemAssignPartAcqCfg(tt, prodModItem5A, pcCfgAfa.getUid(), paAfa3.getUid()));
		log.info("9c-5-2.完成模型5指定構型");
		/* output */
		log.debug("----------------------------------------------------------------");
		showProdModInfo(prodMod4);
		
		
		
		log.debug("================================================================");
		log.debug("================================================================");
		/* 9c-P-1.建立模型P */
		ProdModInfo prodModP = mbomDel.buildProdMod1(tt, prodA, "MP", "ModelP", "The P model.");
		assertNotNull(prodModP);
		log.info("9c-P-1.完成建立模型P");
		/* 9c-P-2.模型P指定構型及獲取方式 */
		ProdModItemInfo prodModItemPA = prodModP.getProdModItem(prodCtlA.getUid());
		assertTrue(mbomDel.runProdModItemAssignPartAcqCfg(tt, prodModItemPA, pcCfg1.getUid(), paA1.getUid()));
		ProdModItemInfo prodModItemPC = prodModP.getProdModItem(prodCtlC.getUid());
		assertTrue(mbomDel.runProdModItemAssignPartAcqCfg(tt, prodModItemPC, pcCfg4.getUid(), paC3.getUid()));
		log.info("9c-P-2.完成模型P指定構型");
		/* output */
		log.debug("----------------------------------------------------------------");
		showProdModInfo(prodModP);
		
		
		log.debug("================================================================");
		log.debug("================================================================");
		/* 9c-Q-1.建立模型Q */
		ProdModInfo prodModQ = mbomDel.buildProdMod1(tt, prodA, "MQ", "ModelQ", "The Q model.");
		assertNotNull(prodModQ);
		log.info("9c-Q-1.完成建立模型Q");
		/* 9c-Q-2.模型Q指定構型及獲取方式 */
		ProdModItemInfo prodModItemQA = prodModQ.getProdModItem(prodCtlA.getUid());
		assertTrue(mbomDel.runProdModItemAssignPartAcqCfg(tt, prodModItemQA, pcCfg4.getUid(), paA1.getUid()));
		ProdModItemInfo prodModItemQB = prodModQ.getProdModItem(prodCtlB.getUid());
		assertTrue(mbomDel.runProdModItemAssignPartAcqCfg(tt, prodModItemQB, pcCfgAfa.getUid(), paBeta1.getUid()));
		log.info("9c-Q-2.完成模型Q指定構型");
		/* output */
		log.debug("----------------------------------------------------------------");
		prodModQ = prodModQ.reload();
		showProdModInfo(prodModQ);
	}
	
	// -------------------------------------------------------------------------------
	private void showMbsRelatedInfo(List<MaterialMasterInfo> mmList) {
		for (MaterialMasterInfo mm : mmList) {
			mm = mm.reload();
			log.debug("{}\t{}\t{}\t{}\t{}", mm.getMano(), mm.getName(), mm.getStdUnit(),
					NumberFormatUtil.getDecimalString(mm.getSumStockQty(), 2),
					NumberFormatUtil.getDecimalString(mm.getSumStockValue(), 2));
			List<MaterialBinStockInfo> mbsList = mm.getMbsList();
			for (MaterialBinStockInfo mbs : mbsList) {
				log.debug("  {}\t{}\t{}\t{}\t{}", mbs.getMano(), mbs.getWrhsLocName(), mbs.getWrhsBinName(),
						NumberFormatUtil.getDecimalString(mbs.getSumStockQty(), 2),
						NumberFormatUtil.getDecimalString(mbs.getSumStockValue(), 2));
				for (MaterialBinStockBatchInfo mbsb : mbs.getMbsbList()) {
					log.debug("    {}\t{}\t{}\t{}\t{}", mbsb.getMi().getMisn(), mbsb.getMi().getMiac(),
							mbsb.getMi().getMiacSrcNo(), NumberFormatUtil.getDecimalString(mbsb.getStockQty(), 2),
							NumberFormatUtil.getDecimalString(mbsb.getStockValue(), 2));
					for (MbsbStmtInfo stmt : mbsb.getStmtList()) {
						log.debug("      {}\t{}\t{}\t{}\t{}", stmt.getIoiIoType().getName(), stmt.getMbsbFlowType(),
								NumberFormatUtil.getDecimalString(stmt.getStmtQty(), 2),
								NumberFormatUtil.getDecimalString(stmt.getStmtValue(), 2), stmt.getPostingStatus());
					}
				}
			}
		}
	}
	
	private void showIoRelatedInfo(InvtOrderInfo io) {
		io = io.reload();
		log.debug("{}\t{}\t{}\t{}", io.getIosn(), io.getApplierName(), DateFormatUtil.transToTime(io.getApplyTime()),
				DateFormatUtil.transToTime(io.getApvTime()));
		for (InvtOrderItemInfo ioi : io.getIoiList()) {
			log.debug("  {}\t{}\t{}\t{}\t{}", ioi.getMmUid(), ioi.getIoTypeName(),
					NumberFormatUtil.getDecimalString(ioi.getOrderQty(), 2),
					NumberFormatUtil.getDecimalString(ioi.getOrderValue(), 2),
					DataUtil.getStr(ioi.isMbsbStmtCreated()));
			for (MbsbStmtInfo stmt : ioi.getMbsbStmtList()) {
				MaterialInstInfo mi = stmt.getMbsb().getMi();
				log.debug("      {}\t{}\t{}\t{}\t{}\t{}", stmt.getMbsbFlowType(),
						NumberFormatUtil.getDecimalString(stmt.getStmtQty(), 2),
						NumberFormatUtil.getDecimalString(stmt.getStmtValue(), 2), stmt.getPostingStatus(),
						mi.getMmUid(), mi.getUid());
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
		log.info("{}\t{}\t{}", prodMod.getProd().getId(), prodMod.getId(), prodMod.getName());
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

		/**
		 * 待修，如ProdModPaTreeDto模式
		 */
		@Deprecated // 
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
			NumberFormatUtil.getDecimalString(_pmp.getPartAcq().getRefUnitCost(), 2)	, 
			NumberFormatUtil.getDecimalString(_pmp.getPartAcq().getMm().getAvgStockValue(), 2)
			,
			
			NumberFormatUtil.getDecimalString(_pmp.getPartAcq().getMm().getIoiAvgOrderValue(InvtOrderType.I1), 2)	,
			NumberFormatUtil.getDecimalString(_pmp.getPartAcq().getMm().getIoiAvgOrderValue(InvtOrderType.I2), 2),
			NumberFormatUtil.getDecimalString(_pmp.getPartAcq().getMm().getIoiAvgOrderValue(InvtOrderType.O2), 2),
			NumberFormatUtil.getDecimalString(_pmp.getPartAcq().getMm().getIoiAvgOrderValue(InvtOrderType.O9), 2)

		);
		int seq = 0;
		for (ProdModPaNew childPmp : _pmp.getChildrenList())
			showProdModPaNew(childPmp, prefix, ++seq);
	}
}

