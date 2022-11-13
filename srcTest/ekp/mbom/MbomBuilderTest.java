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
import ekp.data.service.mbom.ParsPartInfo;
import ekp.data.service.mbom.ParsProcInfo;
import ekp.data.service.mbom.PartAcqRoutingStepInfo;
import ekp.data.service.mbom.PartAcquisitionInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartCreateObj;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;
import ekp.mbom.type.PartAcquisitionType;
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
	@Ignore
	public void testMbomScenario() {
		log.debug("test 1");
		/* Part */
		PartInfo p = mbomDel.buildPartType0(tt);
		
		PartAcquisitionInfo pa1 = mbomDel.buildPartAcqType01(p, tt);
		PartAcqRoutingStepInfo pars1 = mbomDel.buildPartAcqRoutingStepType0(pa1.getUid(), tt);
		ParsProcInfo pproc1 = mbomDel.buildParsProc0(pars1.getUid(), tt);
		ParsPartInfo ppart1 = mbomDel.buildParsPart0(pars1.getUid(), tt);
		PartAcquisitionInfo pa2 = mbomDel.buildPartAcqType02(p, tt);
		PartAcquisitionInfo pa3 = mbomDel.buildPartAcqType03(p, tt);
		
		/* PartCfg */
		PartCfgInfo partCfg11 = mbomDel.buildPartCfg0(p.getUid(), p.getPin(), tt);
		mbomDel.runPartCfgEditing(partCfg11, tt, pa1);
		
		PartCfgInfo partCfg12 = mbomDel.buildPartCfg0(p.getUid(), p.getPin(), tt);
		mbomDel.runPartCfgEditing(partCfg12, tt, pa2);
		
		PartCfgInfo partCfg13 = mbomDel.buildPartCfg0(p.getUid(), p.getPin(), tt);
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
		PartInfo part_A11 = mbomDel.buildPartType0(tt, "A1_1", "大麥克");
//		PartInfo part_A12 = mbomDel.buildPartType0(tt, "A1_2", "無敵大麥克");
//		PartInfo part_L1 = mbomDel.buildPartType0(tt, "B1", "麵包");
//		PartInfo part_L21 = mbomDel.buildPartType0(tt, "B21", "芝麻麵包（單層）");
		PartInfo part_L22 = mbomDel.buildPartType0(tt, "B22", "芝麻麵包（雙層）");
		PartInfo part_M11 = mbomDel.buildPartType0(tt, "M11", "牛肉");
		PartInfo part_M11_1 = mbomDel.buildPartType0(tt, "M11_1", "冷凍牛肉（臺灣）");
		PartInfo part_M11_2 = mbomDel.buildPartType0(tt, "M11_2", "冷凍牛肉（澳洲）");
		PartInfo part_M11_3 = mbomDel.buildPartType0(tt, "M11_3", "冷凍牛肉（美國）");
		
//		PartInfo part_M21 = mbomDel.buildPartType0(tt, "M21", "雞排");
//		PartInfo part_M22 = mbomDel.buildPartType0(tt, "M22", "炸雞腿");
//		PartInfo part_M23 = mbomDel.buildPartType0(tt, "M23", "煎雞腿");
//		PartInfo part_N1 = mbomDel.buildPartType0(tt, "N1", "生菜");
//		PartInfo part_N2 = mbomDel.buildPartType0(tt, "N2", "起司");
//		PartInfo part_N3 = mbomDel.buildPartType0(tt, "N3", "酸黃瓜");
//		PartInfo part_N4 = mbomDel.buildPartType0(tt, "N4", "蕃茄");
		
		/* PartAcq */
		PartAcquisitionInfo partAcq_A11_SP = mbomDel.buildPartAcqType0(part_A11, tt, "SP", "自製", SELF_PRODUCING);
		PartAcquisitionInfo partAcq_M11_SP1 = mbomDel.buildPartAcqType0(part_M11, tt, "SP1", "自製-臺灣", SELF_PRODUCING);
		PartAcquisitionInfo partAcq_M11_SP2 = mbomDel.buildPartAcqType0(part_M11, tt, "SP2", "自製-澳洲", SELF_PRODUCING);
		PartAcquisitionInfo partAcq_M11_SP3 = mbomDel.buildPartAcqType0(part_M11, tt, "SP3", "自製-美國", SELF_PRODUCING);
		
		/* PartAcqRoutingStep */
		PartAcqRoutingStepInfo pars_A11_SP_ASM = mbomDel.buildPartAcqRoutingStepType0(partAcq_A11_SP.getUid(), tt,
				"ASM", "組裝", "");
		PartAcqRoutingStepInfo pars_M11_SP1_SAUTE = mbomDel.buildPartAcqRoutingStepType0(partAcq_M11_SP1.getUid(), tt,
				"SAUTE", "煎", "");
		PartAcqRoutingStepInfo pars_M11_SP2_SAUTE = mbomDel.buildPartAcqRoutingStepType0(partAcq_M11_SP2.getUid(), tt,
				"SAUTE", "煎", "");
		PartAcqRoutingStepInfo pars_M11_SP3_SAUTE = mbomDel.buildPartAcqRoutingStepType0(partAcq_M11_SP3.getUid(), tt,
				"SAUTE", "煎", "");
		
		/* ParsPart */
//		ParsPartInfo ppart_A1_1_SP_ASM_1 = mbomDel.buildParsPart0(pars_A1_1_SP_ASM.getUid(), tt);
		ParsPartInfo ppart_A11_SP_ASM_L = mbomDel.buildParsPart1(pars_A11_SP_ASM, tt, part_L22, 1); // 大麥克-麵包
		ParsPartInfo ppart_A11_SP_ASM_M = mbomDel.buildParsPart1(pars_A11_SP_ASM, tt, part_M11, 2); // 大麥克-牛肉
		ParsPartInfo ppart_M11_SP1_SAUTE_ = mbomDel.buildParsPart1(pars_M11_SP1_SAUTE, tt, part_M11_1, 1); // 牛肉-冷凍牛肉（臺灣）
		ParsPartInfo ppart_M11_SP2_SAUTE_ = mbomDel.buildParsPart1(pars_M11_SP2_SAUTE, tt, part_M11_2, 1); // 牛肉-冷凍牛肉（澳洲）
		ParsPartInfo ppart_M11_SP3_SAUTE_ = mbomDel.buildParsPart1(pars_M11_SP3_SAUTE, tt, part_M11_3, 1); // 牛肉-冷凍牛肉（美國）
		
		
		
		
		
		

	}

}
