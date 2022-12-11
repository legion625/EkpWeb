package ekp.mbom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

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
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mbom.PprocInfo;
import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartCreateObj;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartUnit;
import ekp.serviceFacade.rmi.mbom.PartCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.PartRemote;
import legion.DataServiceFactory;
import legion.util.TimeTraveler;

import static ekp.mbom.type.PartAcquisitionType.*;

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
//	@Ignore
	public void testMbomScenario() {
		log.debug("test 1");
		/* Part */
		PartInfo p = mbomDel.buildPartType0(tt);

		PartAcqInfo pa1 = mbomDel.buildPartAcqType01(p, tt);
		ParsInfo pars1 = mbomDel.buildPartAcqRoutingStepType0(pa1, tt);
		PprocInfo pproc1 = mbomDel.buildParsProc0(pars1.getUid(), tt);
		PpartInfo ppart1 = mbomDel.buildParsPart0(pars1.getUid(), tt);
		PartAcqInfo pa2 = mbomDel.buildPartAcqType02(p, tt);
		PartAcqInfo pa3 = mbomDel.buildPartAcqType03(p, tt);

		/* PartCfg */
		PartCfgInfo partCfg11 = mbomDel.buildPartCfg0(p.getUid(), p.getPin(), tt, "TEST_PC_11", "TEST_PC_11_NAME", "TEST_PC_11_DESP");
		mbomDel.runPartCfgEditing(partCfg11, tt, pa1);

		PartCfgInfo partCfg12 = mbomDel.buildPartCfg0(p.getUid(), p.getPin(), tt, "TEST_PC_12", "TEST_PC_12_NAME", "TEST_PC_12_DESP");
		mbomDel.runPartCfgEditing(partCfg12, tt, pa2);

		PartCfgInfo partCfg13 = mbomDel.buildPartCfg0(p.getUid(), p.getPin(), tt, "TEST_PC_13", "TEST_PC_13_NAME", "TEST_PC_13_DESP");
		mbomDel.runPartCfgEditing(partCfg13, tt, pa3);

		/* Prod */
		ProdInfo prod = mbomDel.buildProd0(tt);
		ProdCtlInfo prodCtl1 = mbomDel.buildProdCtl01(tt);
		ProdCtlInfo prodCtl2 = mbomDel.buildProdCtl02(tt);
		ProdCtlInfo prodCtl3 = mbomDel.buildProdCtl03(tt);
		
		Map<ProdCtlInfo, ProdCtlInfo> prodCtlParentMap = new HashMap<>();
		prodCtlParentMap.put(prodCtl2, prodCtl1);
		prodCtlParentMap.put(prodCtl3, prodCtl2);
		mbomDel.runProdEditCtl(prod, tt, prodCtlParentMap);
		
		mbomDel.runProdCtlPartCfgConj(prodCtl1, tt, partCfg11, partCfg12, partCfg13);
	}
	
	@Test
	public void testMbomScenarioMcd() {
		log.debug("testMbomScenarioMcd");
		/* Part */
		// A.漢堡
		PartInfo part_A11 = mbomDel.buildPartType0(tt, "MCD-A1-1", "大麥克", PartUnit.EA);
//		PartInfo part_A12 = mbomDel.buildPartType0(tt, "A1_2", "無敵大麥克");
//		PartInfo part_L1 = mbomDel.buildPartType0(tt, "B1", "麵包");
//		PartInfo part_L21 = mbomDel.buildPartType0(tt, "B21", "芝麻麵包（單層）");
		// F.副餐
		PartInfo part_F1S = mbomDel.buildPartType0(tt, "MCD-F1S", "薯條（小）", PartUnit.EA);
		PartInfo part_F1M = mbomDel.buildPartType0(tt, "MCD-F1M", "薯條（中）", PartUnit.EA);
		PartInfo part_F1L = mbomDel.buildPartType0(tt, "MCD-F1L", "薯條（大）", PartUnit.EA);
		PartInfo part_F10 = mbomDel.buildPartType0(tt, "MCD-F10", "冷凍薯條", PartUnit.GRAM);
		
		// L.漢堡-麵包
		PartInfo part_L22 = mbomDel.buildPartType0(tt, "MCD-L22", "芝麻麵包（雙層）", PartUnit.EA);
		// M.漢堡-肉
		PartInfo part_M11 = mbomDel.buildPartType0(tt, "MCD-M11", "牛肉", PartUnit.EA);
		PartInfo part_M11_1 = mbomDel.buildPartType0(tt, "MCD-M11-1", "冷凍牛肉（臺灣）", PartUnit.EA);
		PartInfo part_M11_2 = mbomDel.buildPartType0(tt, "MCD-M11-2", "冷凍牛肉（澳洲）", PartUnit.EA);
		PartInfo part_M11_3 = mbomDel.buildPartType0(tt, "MCD-M11-3", "冷凍牛肉（美國）", PartUnit.EA);
		// 
//		PartInfo part_M21 = mbomDel.buildPartType0(tt, "M21", "雞排");
//		PartInfo part_M22 = mbomDel.buildPartType0(tt, "M22", "炸雞腿");
//		PartInfo part_M23 = mbomDel.buildPartType0(tt, "M23", "煎雞腿");
//		PartInfo part_N1 = mbomDel.buildPartType0(tt, "N1", "生菜");
//		PartInfo part_N2 = mbomDel.buildPartType0(tt, "N2", "起司");
//		PartInfo part_N3 = mbomDel.buildPartType0(tt, "N3", "酸黃瓜");
//		PartInfo part_N4 = mbomDel.buildPartType0(tt, "N4", "蕃茄");
		
		/* PartAcq */
		// A.漢堡
		PartAcqInfo partAcq_A11_SP = mbomDel.buildPartAcqType0(part_A11, tt, "SP", "自製", SELF_PRODUCING);
		
		// F.副餐
		PartAcqInfo partAcq_F1S_SP = mbomDel.buildPartAcqType0(part_F1S, tt, "SP", "自製", SELF_PRODUCING); // 薯條（小）
		PartAcqInfo partAcq_F1M_SP = mbomDel.buildPartAcqType0(part_F1M, tt, "SP", "自製", SELF_PRODUCING); // 薯條（中）
		PartAcqInfo partAcq_F1L_SP = mbomDel.buildPartAcqType0(part_F1L, tt, "SP", "自製", SELF_PRODUCING); // 薯條（大）
		
		// L.漢堡-麵包
		PartAcqInfo partAcq_L22_PU = mbomDel.buildPartAcqType0(part_L22, tt, "PU", "採購", PURCHASING); 
		
		// M.漢堡-肉
		PartAcqInfo partAcq_M11_SP1 = mbomDel.buildPartAcqType0(part_M11, tt, "SP1", "自製-臺灣", SELF_PRODUCING);
		PartAcqInfo partAcq_M11_SP2 = mbomDel.buildPartAcqType0(part_M11, tt, "SP2", "自製-澳洲", SELF_PRODUCING);
		PartAcqInfo partAcq_M11_SP3 = mbomDel.buildPartAcqType0(part_M11, tt, "SP3", "自製-美國", SELF_PRODUCING);
		
		/* PartAcqRoutingStep */
		// A.漢堡
		ParsInfo pars_A11_SP_ASM = mbomDel.buildParsType1(partAcq_A11_SP, tt, "ASM", "組裝", "");
		
		
		// 漢堡-肉
		ParsInfo pars_M11_SP1_SAUTE = mbomDel.buildParsType1(partAcq_M11_SP1, tt, "SAUTE", "煎", "");
		ParsInfo pars_M11_SP2_SAUTE = mbomDel.buildParsType1(partAcq_M11_SP2, tt, "SAUTE", "煎", "");
		ParsInfo pars_M11_SP3_SAUTE = mbomDel.buildParsType1(partAcq_M11_SP3, tt, "SAUTE", "煎", "");

		/* ParsPart */
		PpartInfo ppart_A11_SP_ASM_L = mbomDel.buildParsPart1(pars_A11_SP_ASM, tt, part_L22, 1); // 大麥克-麵包
		PpartInfo ppart_A11_SP_ASM_M = mbomDel.buildParsPart1(pars_A11_SP_ASM, tt, part_M11, 2); // 大麥克-牛肉
		PpartInfo ppart_M11_SP1_SAUTE_ = mbomDel.buildParsPart1(pars_M11_SP1_SAUTE, tt, part_M11_1, 1); // 牛肉-冷凍牛肉（臺灣）
		PpartInfo ppart_M11_SP2_SAUTE_ = mbomDel.buildParsPart1(pars_M11_SP2_SAUTE, tt, part_M11_2, 1); // 牛肉-冷凍牛肉（澳洲）
		PpartInfo ppart_M11_SP3_SAUTE_ = mbomDel.buildParsPart1(pars_M11_SP3_SAUTE, tt, part_M11_3, 1); // 牛肉-冷凍牛肉（美國）
		
		/* PartCfg */
		PartCfgInfo partCfgA11_MTW = mbomDel.buildPartCfg0(part_A11.getUid(), part_A11.getPin(), tt, "MTW","臺灣牛","使用臺灣本土牛肉"); // 大麥克-構型1
		mbomDel.runPartCfgEditing(partCfgA11_MTW, tt, partAcq_A11_SP, partAcq_L22_PU, partAcq_M11_SP1);
		
		PartCfgInfo partCfgA11_MAU = mbomDel.buildPartCfg0(part_A11.getUid(), part_A11.getPin(), tt, "MAU","澳洲牛","使用澳洲進口牛肉"); // 大麥克-構型2
		mbomDel.runPartCfgEditing(partCfgA11_MAU, tt, partAcq_A11_SP, partAcq_L22_PU, partAcq_M11_SP2);
		
		PartCfgInfo partCfgA11_MUS = mbomDel.buildPartCfg0(part_A11.getUid(), part_A11.getPin(), tt, "MUS","美國牛","使用美國進口牛肉"); // 大麥克-構型3
		mbomDel.runPartCfgEditing(partCfgA11_MUS, tt, partAcq_A11_SP, partAcq_L22_PU, partAcq_M11_SP3);
		
		/* Prod */
		ProdInfo prod_B = mbomDel.buildProd0(tt, "MCD-B","MCD超值全餐");
		ProdCtlInfo prodCtl_B = mbomDel.buildProdCtl0(tt,"MCD-B" , 1, "MCD超值全餐",true);
		ProdCtlInfo prodCtl_B1 = mbomDel.buildProdCtl0(tt,"MCD-B1" , 2, "MCD超值全餐-主餐",true);
		ProdCtlInfo prodCtl_B2 = mbomDel.buildProdCtl0(tt,"MCD-B2" , 2, "MCD超值全餐-副餐",true);
		ProdCtlInfo prodCtl_B3 = mbomDel.buildProdCtl0(tt,"MCD-B3" , 2, "MCD超值全餐-飲料",true);
		
		Map<ProdCtlInfo, ProdCtlInfo> prodCtlParentMap = new HashMap<>();
		prodCtlParentMap.put(prodCtl_B1, prodCtl_B);
		prodCtlParentMap.put(prodCtl_B2, prodCtl_B);
		prodCtlParentMap.put(prodCtl_B3, prodCtl_B);
		mbomDel.runProdEditCtl(prod_B, tt, prodCtlParentMap);
		
		mbomDel.runProdCtlPartCfgConj(prodCtl_B1, tt, partCfgA11_MTW, partCfgA11_MAU, partCfgA11_MUS);
		
		/* ProdMod */
		

	}
	
	@Test
	@Ignore
	public void testPaRevertPublish() {
		String paUid = "2022!44!7!21";
		PartAcqInfo pa =  dataService.loadPartAcquisition(paUid);
		mbomDel.paRevertPublish(pa, false);
		
		
//		paRevertPublish
	} 

}
