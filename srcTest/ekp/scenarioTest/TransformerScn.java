package ekp.scenarioTest;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.AbstractEkpInitTest;
import ekp.TestLogMark;
import ekp.data.InvtDataService;
import ekp.data.MbomDataService;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.invt.InvtDelegate;
import ekp.mbom.MbomBuilderDelegate;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartUnit;
import ekp.mf.MfBuilderDelegate;
import ekp.pu.PuBuilderDelegate;
import ekp.sd.SdDelegate;
import legion.DataServiceFactory;
import legion.util.TimeTraveler;

import static org.junit.Assert.*;

public class TransformerScn extends AbstractEkpInitTest {
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);
	
	//
	private static InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);
	
	//
	private InvtDelegate invtDel = InvtDelegate.getInstance();
	private MbomBuilderDelegate mbomDel = MbomBuilderDelegate.getInstance();
	private MfBuilderDelegate mfDel = MfBuilderDelegate.getInstance();
	private PuBuilderDelegate puDel = PuBuilderDelegate.getInstance();
	private SdDelegate sdDel = SdDelegate.getInstance();
	
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

//	@Test
//	public void testTransformerDataMain() {
//
//	}
	
	// -------------------------------------------------------------------------------
	/** 1.testInitMaterialMaster */
	@Test
	@Ignore
	public void testInitMaterialMaster() {
		/* MaterialMaster */
		MaterialMasterInfo mmEl1p = invtDel.buildMm0(tt, "EL-1P", "油入式電力變壓器-單相", "", PartUnit.EAC);
		MaterialMasterInfo mmEl3p = invtDel.buildMm0(tt, "EL-3P", "油入式電力變壓器-三相", "", PartUnit.EAC);
		MaterialMasterInfo mmElF = invtDel.buildMm0(tt, "EL-F", "油入式火力發電用變壓器", "", PartUnit.EAC);
		MaterialMasterInfo mmElW = invtDel.buildMm0(tt, "EL-W", "油入式水力抽蓄用變壓器", "", PartUnit.EAC);
		MaterialMasterInfo mmWdCu08 = invtDel.buildMm0(tt, "WD-CU08", "銅線08繞組", "", PartUnit.EAC);
		MaterialMasterInfo mmWdCu12= invtDel.buildMm0(tt, "WD-CU12", "銅線12繞組", "", PartUnit.EAC);
		MaterialMasterInfo mmWdAl12 = invtDel.buildMm0(tt, "WD-AL12", "鋁線12繞組", "", PartUnit.EAC);
		MaterialMasterInfo mmCrFe = invtDel.buildMm0(tt, "CR-FE", "矽鋼片鐵芯", "", PartUnit.SHE);
		MaterialMasterInfo mmCrMag = invtDel.buildMm0(tt, "CR-MAG", "高導磁性鐵芯", "", PartUnit.SHE);
		MaterialMasterInfo mmSS35CS250 = invtDel.buildMm0(tt, "SS-35CS250", "矽鋼片0.35mm", "中鋼35CS250、電阻率54", PartUnit.MTR);
		MaterialMasterInfo mmSS35CS550 = invtDel.buildMm0(tt, "SS-35CS550", "高導磁性矽鋼片0.35mm", "中鋼35CS550、電阻率30", PartUnit.MTR);
		MaterialMasterInfo mmSS50CS250 = invtDel.buildMm0(tt, "SS-50CS250", "矽鋼片0.50mm", "中鋼35CS250、電阻率62", PartUnit.MTR);
		MaterialMasterInfo mmSS50CS800 = invtDel.buildMm0(tt, "SS-50CS800", "高導磁性矽鋼片0.50mm", "中鋼35CS250、電阻率32", PartUnit.MTR);
		MaterialMasterInfo mmEipEp =  invtDel.buildMm0(tt, "EIP-EP", "環氧樹脂絕緣漆", "虹牌-1030FM、定昌CW1302、傑地478", PartUnit.KGM);
		MaterialMasterInfo mmEipPvdf = invtDel.buildMm0(tt, "EIP-PVDF", "氟碳絕緣漆", "汎鈦機械WFC5288", PartUnit.KGM);
		MaterialMasterInfo mmEifAr = invtDel.buildMm0(tt, "EIF-AR", "Aramid(聚醯胺)絕緣紙", "", PartUnit.SPL);
		MaterialMasterInfo mmEifPet1 = invtDel.buildMm0(tt, "EIF-PET-1", "PET(聚對苯二甲酸乙二酯)絕緣紙", "", PartUnit.SPL);
		MaterialMasterInfo mmEifPi1 = invtDel.buildMm0(tt, "EIF-PI-1", "PI(聚醯亞胺)絕緣紙", "", PartUnit.SPL);
		MaterialMasterInfo mmEifGf1 = invtDel.buildMm0(tt, "EIF-GF-1", "玻璃纖維絕緣紙", "", PartUnit.SPL);
		MaterialMasterInfo mmWdMCu12 = invtDel.buildMm0(tt, "WD-M-CU-12", "銅線1.2mm", "", PartUnit.MTR);
		MaterialMasterInfo mmWdMCu08 = invtDel.buildMm0(tt, "WD-M-CU-08", "銅線0.8mm", "", PartUnit.MTR);
		MaterialMasterInfo mmWdMAl12 = invtDel.buildMm0(tt, "WD-M-AL-12", "鋁線1.2mm", "", PartUnit.MTR);
		MaterialMasterInfo mmWdMAl08 = invtDel.buildMm0(tt, "WD-M-AL-08", "鋁線0.8mm", "", PartUnit.MTR);
		MaterialMasterInfo mmToTotal = invtDel.buildMm0(tt, "TO-TOTAL", "高壓變壓器絕緣油", "", PartUnit.LTR);
		MaterialMasterInfo mmTk = invtDel.buildMm0(tt, "TK", "變壓器油箱", "", PartUnit.EAC);
		MaterialMasterInfo mmTkCpn1 = invtDel.buildMm0(tt, "TK-CPN1", "油箱本體", "", PartUnit.EAC);
		MaterialMasterInfo mmTkCpn2 = invtDel.buildMm0(tt, "TK-CPN2", "油箱蓋", "", PartUnit.EAC);
		MaterialMasterInfo mmTkCpn3 = invtDel.buildMm0(tt, "TK-CPN3", "油箱接頭", "", PartUnit.EAC);
		MaterialMasterInfo mmTkCpn4= invtDel.buildMm0(tt, "TK-CPN4", "油箱支架", "", PartUnit.EAC);
		MaterialMasterInfo mmTkCpn5= invtDel.buildMm0(tt, "TK-CPN5", "O型環", "", PartUnit.EAC);
		MaterialMasterInfo mmTkEclipse= invtDel.buildMm0(tt, "TK-ECLIPSE", "橢圓型儲油槽", "", PartUnit.EAC);
		MaterialMasterInfo mmCtAl =  invtDel.buildMm0(tt, "CT-AL", "壓接端子-鋁", "", PartUnit.EAC);
		MaterialMasterInfo mmCtCu =  invtDel.buildMm0(tt, "CT-CU", "壓接端子-銅", "", PartUnit.EAC);
		MaterialMasterInfo mmProtL =  invtDel.buildMm0(tt, "PROT-L", "過載保護裝置", "", PartUnit.EAC);
		MaterialMasterInfo mmProtSc =  invtDel.buildMm0(tt, "PROT-SC", "短路保護裝置", "", PartUnit.EAC);
		MaterialMasterInfo mmWdConn =  invtDel.buildMm0(tt, "WD-CONN", "相間繞組連接器", "", PartUnit.EAC);
		MaterialMasterInfo mmCsA =  invtDel.buildMm0(tt, "CS-A", "氣冷冷卻系統", "", PartUnit.EAC);
		MaterialMasterInfo mmCsAC1 =  invtDel.buildMm0(tt, "CS-A-C1", "風扇", "", PartUnit.EAC);
		MaterialMasterInfo mmCsAC2 =  invtDel.buildMm0(tt, "CS-A-C2", "風扇罩", "", PartUnit.EAC);
		MaterialMasterInfo mmCsAC3 =  invtDel.buildMm0(tt, "CS-A-C3", "風道", "", PartUnit.EAC);
		MaterialMasterInfo mmCsAC4 =  invtDel.buildMm0(tt, "CS-A-C4", "風扇控制器", "", PartUnit.EAC);
		MaterialMasterInfo mmCsL =  invtDel.buildMm0(tt, "CS-L", "油冷冷卻系統", "", PartUnit.EAC);
		MaterialMasterInfo mmCsLC1 =  invtDel.buildMm0(tt, "CS-L-C1", "油箱", "", PartUnit.EAC);
		MaterialMasterInfo mmCsLC2 =  invtDel.buildMm0(tt, "CS-L-C2", "冷卻油", "", PartUnit.BOT);
		MaterialMasterInfo mmCsLC3 =  invtDel.buildMm0(tt, "CS-L-C3", "油泵", "", PartUnit.EAC);
		MaterialMasterInfo mmCsLC4 =  invtDel.buildMm0(tt, "CS-L-C4", "油冷卻器", "", PartUnit.EAC);
		MaterialMasterInfo mmCsW = invtDel.buildMm0(tt, "CS-W", "水冷冷卻系統", "", PartUnit.EAC);
		MaterialMasterInfo mmCsWC1 = invtDel.buildMm0(tt, "CS-W-C1", "冷卻水管", "", PartUnit.MTR);
		MaterialMasterInfo mmCsWC2 = invtDel.buildMm0(tt, "CS-W-C2", "水泵", "", PartUnit.EAC);
		MaterialMasterInfo mmCsWC3 = invtDel.buildMm0(tt, "CS-W-C3", "水冷器", "", PartUnit.EAC);
		MaterialMasterInfo mmCsWC4 = invtDel.buildMm0(tt, "CS-W-C4", "水箱", "", PartUnit.EAC);
		MaterialMasterInfo mmCsWC5 = invtDel.buildMm0(tt, "CS-W-C5", "水溫感測器", "", PartUnit.EAC);
		MaterialMasterInfo mmCsWC6 = invtDel.buildMm0(tt, "CS-W-C6", "水泵控制器", "", PartUnit.EAC);
		MaterialMasterInfo mmImb = invtDel.buildMm0(tt, "CS-IMB", "絕緣監測系統", "", PartUnit.EAC);
		MaterialMasterInfo mmImbI1 = invtDel.buildMm0(tt, "CS-IMB-I1", "絕緣監測儀", "", PartUnit.EAC);
		MaterialMasterInfo mmImbI1c = invtDel.buildMm0(tt, "CS-IMB-I1C", "絕緣監測儀附件", "", PartUnit.PAC);
		MaterialMasterInfo mmImbI2 = invtDel.buildMm0(tt, "CS-IMB-I2", "絕緣電阻測試儀", "", PartUnit.EAC);
		MaterialMasterInfo mmImbI2c = invtDel.buildMm0(tt, "CS-IMB-I2C", "絕緣電阻測試儀附件", "", PartUnit.PAC);
		MaterialMasterInfo mmImbI3 = invtDel.buildMm0(tt, "CS-IMB-I3", "絕緣油測試儀", "", PartUnit.EAC);
		MaterialMasterInfo mmImbI3c = invtDel.buildMm0(tt, "CS-IMB-I3C", "絕緣油測試儀附件", "", PartUnit.PAC);
		
	}
	
	/** 2.testInitPart */
	@Test
	@Ignore
	public void testInitPart() {
		PartInfo pEl = mbomDel.buildPartType0(tt, "EL", "油入式電力變壓器", PartUnit.EAC);
		PartInfo pElWinding = mbomDel.buildPartType0(tt, "EL-Winding", "繞組", PartUnit.EAC);
		PartInfo pElWindingM = mbomDel.buildPartType0(tt, "EL-Winding-M", "繞組線圈", PartUnit.MTR);
		PartInfo pElWindingEip = mbomDel.buildPartType0(tt, "EL-Winding-EIP", "絕緣漆", PartUnit.KGM);
		PartInfo pElWindingEif = mbomDel.buildPartType0(tt, "EL-Winding-EIF", "絕緣紙", PartUnit.SPL);
		PartInfo pElCore = mbomDel.buildPartType0(tt, "EL-Core", "鐵芯", PartUnit.SHE);
		PartInfo pElCoreSs = mbomDel.buildPartType0(tt, "EL-Core-SS", "矽鋼片", PartUnit.MTR);
		PartInfo pElCoreEip = mbomDel.buildPartType0(tt, "EL-Core-EIP", "絕緣漆", PartUnit.KGM);
		PartInfo pElCoreEif = mbomDel.buildPartType0(tt, "EL-Core-EIF", "絕緣紙", PartUnit.SPL);
		PartInfo pElLeads =  mbomDel.buildPartType0(tt, "EL-Leads", "引線", PartUnit.MTR);
		PartInfo pElTerminal =  mbomDel.buildPartType0(tt, "EL-Terminal", "端子", PartUnit.EAC);
		PartInfo pElTank =  mbomDel.buildPartType0(tt, "EL-Tank", "變壓器油箱", PartUnit.EAC);
		PartInfo pElTankC1 =  mbomDel.buildPartType0(tt, "EL-Tank-C1", "油箱本體", PartUnit.EAC);
		PartInfo pElTankC2 =  mbomDel.buildPartType0(tt, "EL-Tank-C2", "油箱蓋", PartUnit.EAC);
		PartInfo pElTankC3 =  mbomDel.buildPartType0(tt, "EL-Tank-C3", "油箱接頭", PartUnit.EAC);
		PartInfo pElTankC4 =  mbomDel.buildPartType0(tt, "EL-Tank-C4", "油箱支架", PartUnit.EAC);
		PartInfo pElTankC5 =  mbomDel.buildPartType0(tt, "EL-Tank-C5", "O型環", PartUnit.EAC);
		PartInfo pElInsulation = mbomDel.buildPartType0(tt, "EL-Insulation", "絕緣油", PartUnit.LTR);
		PartInfo pElProtL =  mbomDel.buildPartType0(tt, "EL-PROT-L", "過載保護裝置", PartUnit.EAC);
		PartInfo pElProtSc =  mbomDel.buildPartType0(tt, "EL-PROT-SC", "短路保護裝置", PartUnit.EAC);
		PartInfo pElWdConn =  mbomDel.buildPartType0(tt, "EL-WD-CONN", "相間繞組連接器", PartUnit.EAC);
		PartInfo pElCsA =  mbomDel.buildPartType0(tt, "EL-CS-A", "氣冷冷卻系統", PartUnit.EAC);
		PartInfo pElCsAC1 =  mbomDel.buildPartType0(tt, "EL-CS-A-C1", "風扇", PartUnit.EAC);
		PartInfo pElCsAC2 =  mbomDel.buildPartType0(tt, "EL-CS-A-C2", "風扇罩", PartUnit.EAC);
		PartInfo pElCsAC3 =  mbomDel.buildPartType0(tt, "EL-CS-A-C3", "風道", PartUnit.EAC);
		PartInfo pElCsAC4 =  mbomDel.buildPartType0(tt, "EL-CS-A-C4", "風扇控制器", PartUnit.EAC);
		PartInfo pElCsL = mbomDel.buildPartType0(tt, "EL-CS-L", "油冷冷卻系統", PartUnit.EAC);
		PartInfo pElCsLC1 = mbomDel.buildPartType0(tt, "EL-CS-L-C1", "油箱", PartUnit.EAC);
		PartInfo pElCsLC2 = mbomDel.buildPartType0(tt, "EL-CS-L-C2", "冷卻油", PartUnit.BOT);
		PartInfo pElCsLC3 = mbomDel.buildPartType0(tt, "EL-CS-L-C3", "油泵", PartUnit.EAC);
		PartInfo pElCsLC4 = mbomDel.buildPartType0(tt, "EL-CS-L-C4", "油冷卻器", PartUnit.EAC);
		PartInfo pElCsW = mbomDel.buildPartType0(tt, "EL-CS-W", "水冷冷卻系統", PartUnit.EAC);
		PartInfo pElCsWC1 = mbomDel.buildPartType0(tt, "EL-CS-W-C1", "冷卻水管", PartUnit.EAC);
		PartInfo pElCsWC2 = mbomDel.buildPartType0(tt, "EL-CS-W-C2", "水泵", PartUnit.EAC);
		PartInfo pElCsWC3 = mbomDel.buildPartType0(tt, "EL-CS-W-C3", "水冷器", PartUnit.EAC);
		PartInfo pElCsWC4 = mbomDel.buildPartType0(tt, "EL-CS-W-C4", "水箱", PartUnit.EAC);
		PartInfo pElCsWC5 = mbomDel.buildPartType0(tt, "EL-CS-W-C5", "水溫感測器", PartUnit.EAC);
		PartInfo pElCsWC6 = mbomDel.buildPartType0(tt, "EL-CS-W-C6", "水泵控制器", PartUnit.EAC);
		PartInfo pElImb =  mbomDel.buildPartType0(tt, "EL-IMB", "絕緣監測系統", PartUnit.EAC);
		PartInfo pElImbI1 =  mbomDel.buildPartType0(tt, "EL-IMB-I1", "絕緣監測儀", PartUnit.EAC);
		PartInfo pElImbI1c =  mbomDel.buildPartType0(tt, "EL-IMB-I1C", "絕緣監測儀附件", PartUnit.PAC);
		PartInfo pElImbI2 =  mbomDel.buildPartType0(tt, "EL-IMB-I2", "絕緣電阻測試儀", PartUnit.EAC);
		PartInfo pElImbI2c =  mbomDel.buildPartType0(tt, "EL-IMB-I2C", "絕緣電阻測試儀附件", PartUnit.PAC);
		PartInfo pElImbI3 =  mbomDel.buildPartType0(tt, "EL-IMB-I3", "絕緣油測試儀", PartUnit.EAC);
		PartInfo pElImbI3c =  mbomDel.buildPartType0(tt, "EL-IMB-I3C", "絕緣油測試儀附件", PartUnit.PAC);
		
	}

	/** 3.testInitPartAcq */
	@Test
//	@Ignore
	public void testInitPartAcq() {
		/* load MaterialMaster */
		MaterialMasterInfo mmEl1p = invtDataService.loadMaterialMasterByMano("EL-1P");
		MaterialMasterInfo mmEl3p = invtDataService.loadMaterialMasterByMano("EL-3P");
		MaterialMasterInfo mmElF = invtDataService.loadMaterialMasterByMano("EL-F");
		MaterialMasterInfo mmElW = invtDataService.loadMaterialMasterByMano("EL-W");
		MaterialMasterInfo mmWdCu08 = invtDataService.loadMaterialMasterByMano("WD-CU08");
		MaterialMasterInfo mmWdCu12 = invtDataService.loadMaterialMasterByMano("WD-CU12");
		MaterialMasterInfo mmWdAl12 = invtDataService.loadMaterialMasterByMano("WD-AL12");
		MaterialMasterInfo mmCrFe = invtDataService.loadMaterialMasterByMano("CR-FE");
		MaterialMasterInfo mmCrMag = invtDataService.loadMaterialMasterByMano("CR-MAG");
		MaterialMasterInfo mmSS35CS250 = invtDataService.loadMaterialMasterByMano("SS-35CS250");
		MaterialMasterInfo mmSS35CS550 = invtDataService.loadMaterialMasterByMano("SS-35CS550");
		MaterialMasterInfo mmSS50CS250 = invtDataService.loadMaterialMasterByMano("SS-50CS250");
		MaterialMasterInfo mmSS50CS800 = invtDataService.loadMaterialMasterByMano("SS-50CS800");
		MaterialMasterInfo mmEipEp =  invtDataService.loadMaterialMasterByMano("EIP-EP");
		MaterialMasterInfo mmEipPvdf = invtDataService.loadMaterialMasterByMano("EIP-PVDF");
		MaterialMasterInfo mmEifAr = invtDataService.loadMaterialMasterByMano("EIF-AR");
		MaterialMasterInfo mmEifPet1 = invtDataService.loadMaterialMasterByMano("EIF-PET-1");
		MaterialMasterInfo mmEifPi1 = invtDataService.loadMaterialMasterByMano("EIF-PI-1");
		MaterialMasterInfo mmEifGf1 = invtDataService.loadMaterialMasterByMano("EIF-GF-1");
		MaterialMasterInfo mmWdMCu12 = invtDataService.loadMaterialMasterByMano("WD-M-CU-12");
		MaterialMasterInfo mmWdMCu08 = invtDataService.loadMaterialMasterByMano("WD-M-CU-08");
		MaterialMasterInfo mmWdMAl12 = invtDataService.loadMaterialMasterByMano("WD-M-AL-12");
		MaterialMasterInfo mmWdMAl08 = invtDataService.loadMaterialMasterByMano("WD-M-AL-08");
		MaterialMasterInfo mmToTotal = invtDataService.loadMaterialMasterByMano("TO-TOTAL");
		MaterialMasterInfo mmTk = invtDataService.loadMaterialMasterByMano("TK");
		MaterialMasterInfo mmTkCpn1 = invtDataService.loadMaterialMasterByMano("TK-CPN1");
		MaterialMasterInfo mmTkCpn2 = invtDataService.loadMaterialMasterByMano("TK-CPN2");
		MaterialMasterInfo mmTkCpn3 = invtDataService.loadMaterialMasterByMano("TK-CPN3");
		MaterialMasterInfo mmTkCpn4= invtDataService.loadMaterialMasterByMano("TK-CPN4");
		MaterialMasterInfo mmTkCpn5= invtDataService.loadMaterialMasterByMano("TK-CPN5");
		MaterialMasterInfo mmTkEclipse= invtDataService.loadMaterialMasterByMano("TK-ECLIPSE");
		MaterialMasterInfo mmCtAl =  invtDataService.loadMaterialMasterByMano("CT-AL");
		MaterialMasterInfo mmCtCu =  invtDataService.loadMaterialMasterByMano("CT-CU");
		MaterialMasterInfo mmProtL =  invtDataService.loadMaterialMasterByMano("PROT-L");
		MaterialMasterInfo mmProtSc =  invtDataService.loadMaterialMasterByMano("PROT-SC");
		MaterialMasterInfo mmWdConn =  invtDataService.loadMaterialMasterByMano("WD-CONN");
		MaterialMasterInfo mmCsA =  invtDataService.loadMaterialMasterByMano("CS-A");
		MaterialMasterInfo mmCsAC1 =  invtDataService.loadMaterialMasterByMano("CS-A-C1");
		MaterialMasterInfo mmCsAC2 =  invtDataService.loadMaterialMasterByMano("CS-A-C2");
		MaterialMasterInfo mmCsAC3 =  invtDataService.loadMaterialMasterByMano("CS-A-C3");
		MaterialMasterInfo mmCsAC4 =  invtDataService.loadMaterialMasterByMano("CS-A-C4");
		MaterialMasterInfo mmCsL =  invtDataService.loadMaterialMasterByMano("CS-L");
		MaterialMasterInfo mmCsLC1 =  invtDataService.loadMaterialMasterByMano("CS-L-C1");
		MaterialMasterInfo mmCsLC2 =  invtDataService.loadMaterialMasterByMano("CS-L-C2");
		MaterialMasterInfo mmCsLC3 =  invtDataService.loadMaterialMasterByMano("CS-L-C3");
		MaterialMasterInfo mmCsLC4 =  invtDataService.loadMaterialMasterByMano("CS-L-C4");
		MaterialMasterInfo mmCsW = invtDataService.loadMaterialMasterByMano("CS-W");
		MaterialMasterInfo mmCsWC1 = invtDataService.loadMaterialMasterByMano("CS-W-C1");
		MaterialMasterInfo mmCsWC2 = invtDataService.loadMaterialMasterByMano("CS-W-C2");
		MaterialMasterInfo mmCsWC3 = invtDataService.loadMaterialMasterByMano("CS-W-C3");
		MaterialMasterInfo mmCsWC4 = invtDataService.loadMaterialMasterByMano("CS-W-C4");
		MaterialMasterInfo mmCsWC5 = invtDataService.loadMaterialMasterByMano("CS-W-C5");
		MaterialMasterInfo mmCsWC6 = invtDataService.loadMaterialMasterByMano("CS-W-C6");
		MaterialMasterInfo mmImb = invtDataService.loadMaterialMasterByMano("CS-IMB");
		MaterialMasterInfo mmImbI1 = invtDataService.loadMaterialMasterByMano("CS-IMB-I1");
		MaterialMasterInfo mmImbI1c = invtDataService.loadMaterialMasterByMano("CS-IMB-I1C");
		MaterialMasterInfo mmImbI2 = invtDataService.loadMaterialMasterByMano("CS-IMB-I2");
		MaterialMasterInfo mmImbI2c = invtDataService.loadMaterialMasterByMano("CS-IMB-I2C");
		MaterialMasterInfo mmImbI3 = invtDataService.loadMaterialMasterByMano("CS-IMB-I3");
		MaterialMasterInfo mmImbI3c = invtDataService.loadMaterialMasterByMano("CS-IMB-I3C");
		
		/* load part */
		PartInfo pEl = mbomDataService.loadPartByPin("EL");
		PartInfo pElWinding = mbomDataService.loadPartByPin( "EL-Winding");
		PartInfo pElWindingM = mbomDataService.loadPartByPin( "EL-Winding-M");
		PartInfo pElWindingEip = mbomDataService.loadPartByPin( "EL-Winding-EIP");
		PartInfo pElWindingEif = mbomDataService.loadPartByPin( "EL-Winding-EIF");
		PartInfo pElCore = mbomDataService.loadPartByPin( "EL-Core");
		PartInfo pElCoreSs = mbomDataService.loadPartByPin( "EL-Core-SS");
		PartInfo pElCoreEip = mbomDataService.loadPartByPin( "EL-Core-EIP");
		PartInfo pElCoreEif = mbomDataService.loadPartByPin( "EL-Core-EIF");
		PartInfo pElLeads =  mbomDataService.loadPartByPin( "EL-Leads");
		PartInfo pElTerminal =  mbomDataService.loadPartByPin( "EL-Terminal");
		PartInfo pElTank =  mbomDataService.loadPartByPin( "EL-Tank");
		PartInfo pElTankC1 =  mbomDataService.loadPartByPin( "EL-Tank-C1");
		PartInfo pElTankC2 =  mbomDataService.loadPartByPin( "EL-Tank-C2");
		PartInfo pElTankC3 =  mbomDataService.loadPartByPin( "EL-Tank-C3");
		PartInfo pElTankC4 =  mbomDataService.loadPartByPin( "EL-Tank-C4");
		PartInfo pElTankC5 =  mbomDataService.loadPartByPin( "EL-Tank-C5");
		PartInfo pElInsulation = mbomDataService.loadPartByPin( "EL-Insulation");
		PartInfo pElProtL =  mbomDataService.loadPartByPin( "EL-PROT-L");
		PartInfo pElProtSc =  mbomDataService.loadPartByPin( "EL-PROT-SC");
		PartInfo pElWdConn =  mbomDataService.loadPartByPin( "EL-WD-CONN");
		PartInfo pElCsA =  mbomDataService.loadPartByPin( "EL-CS-A");
		PartInfo pElCsAC1 =  mbomDataService.loadPartByPin( "EL-CS-A-C1");
		PartInfo pElCsAC2 =  mbomDataService.loadPartByPin( "EL-CS-A-C2");
		PartInfo pElCsAC3 =  mbomDataService.loadPartByPin( "EL-CS-A-C3");
		PartInfo pElCsAC4 =  mbomDataService.loadPartByPin( "EL-CS-A-C4");
		PartInfo pElCsL = mbomDataService.loadPartByPin( "EL-CS-L");
		PartInfo pElCsLC1 = mbomDataService.loadPartByPin( "EL-CS-L-C1");
		PartInfo pElCsLC2 = mbomDataService.loadPartByPin( "EL-CS-L-C2");
		PartInfo pElCsLC3 = mbomDataService.loadPartByPin( "EL-CS-L-C3");
		PartInfo pElCsLC4 = mbomDataService.loadPartByPin( "EL-CS-L-C4");
		PartInfo pElCsW = mbomDataService.loadPartByPin( "EL-CS-W");
		PartInfo pElCsWC1 = mbomDataService.loadPartByPin( "EL-CS-W-C1");
		PartInfo pElCsWC2 = mbomDataService.loadPartByPin( "EL-CS-W-C2");
		PartInfo pElCsWC3 = mbomDataService.loadPartByPin( "EL-CS-W-C3");
		PartInfo pElCsWC4 = mbomDataService.loadPartByPin( "EL-CS-W-C4");
		PartInfo pElCsWC5 = mbomDataService.loadPartByPin( "EL-CS-W-C5");
		PartInfo pElCsWC6 = mbomDataService.loadPartByPin( "EL-CS-W-C6");
		PartInfo pElImb =  mbomDataService.loadPartByPin( "EL-IMB");
		PartInfo pElImbI1 =  mbomDataService.loadPartByPin( "EL-IMB-I1");
		PartInfo pElImbI1c =  mbomDataService.loadPartByPin( "EL-IMB-I1C");
		PartInfo pElImbI2 =  mbomDataService.loadPartByPin( "EL-IMB-I2");
		PartInfo pElImbI2c =  mbomDataService.loadPartByPin( "EL-IMB-I2C");
		PartInfo pElImbI3 =  mbomDataService.loadPartByPin( "EL-IMB-I3");
		PartInfo pElImbI3c =  mbomDataService.loadPartByPin( "EL-IMB-I3C");
		
		
		/**/
		/* 1.EL油入式電力變壓器 */
		// EL-1P:自製單相繞組
		PartAcqInfo paEl_sp_1p = mbomDel.buildPartAcqType0(pEl, tt, "SP-1P", "自製單相繞組",
				PartAcquisitionType.SELF_PRODUCING);
		assertNotNull(paEl_sp_1p);
		assertTrue(mbomDel.paAssignMm(tt, paEl_sp_1p, mmEl1p)); // 指定料件主檔
		ParsInfo parsEl_sp_1p = mbomDel.buildParsType1(paEl_sp_1p, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsEl_sp_1p);
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_1p, tt, pElWinding, 1)); // 1-1
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_1p, tt, pElCore, 1)); // 1-2
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_1p, tt, pElLeads, 10)); // 1-3
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_1p, tt, pElTerminal, 2)); // 1-4
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_1p, tt, pElTank, 1)); // 1-5
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_1p, tt, pElInsulation, 50)); // 1-6
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_1p, tt, pElProtL, 1)); // 1-7
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_1p, tt, pElProtSc, 1)); // 1-8
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_1p, tt, pElCsA, 1)); // 1-10
		
		// EL-3P:自製三相繞組（三相、火力發電用、水力抽蓄-銅、水力抽蓄-鋁）
		PartAcqInfo paEl_sp_3p = mbomDel.buildPartAcqType0(pEl, tt, "SP-3P", "自製三相繞組",
				PartAcquisitionType.SELF_PRODUCING); // EL-3P:自製三相繞組
		assertNotNull(paEl_sp_3p);
		assertTrue(mbomDel.paAssignMm(tt, paEl_sp_3p, mmEl3p)); // 指定料件主檔
		ParsInfo parsEl_sp_3p = mbomDel.buildParsType1(paEl_sp_3p, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsEl_sp_3p);
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_3p, tt, pElWinding, 3)); // 1-1
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_3p, tt, pElCore, 1)); // 1-2
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_3p, tt, pElLeads, 10)); // 1-3
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_3p, tt, pElTerminal, 2)); // 1-4
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_3p, tt, pElTank, 1)); // 1-5
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_3p, tt, pElInsulation, 50)); // 1-6
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_3p, tt, pElProtL, 1)); // 1-7
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_3p, tt, pElProtSc, 1)); // 1-8
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_3p, tt, pElWdConn, 1)); // 1-9
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_3p, tt, pElCsA, 1)); // 1-10
		
		// EL-F:自製油入式火力發電用變壓器
		PartAcqInfo paEl_sp_f = mbomDel.buildPartAcqType0(pEl, tt, "SP-F", "自製火力發電用",
				PartAcquisitionType.SELF_PRODUCING); // EL-F:自製火力發電用
		assertNotNull(paEl_sp_f);
		assertTrue(mbomDel.paAssignMm(tt, paEl_sp_f, mmElF)); // 指定料件主檔
		ParsInfo parsEl_sp_f = mbomDel.buildParsType1(paEl_sp_f, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsEl_sp_f);
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_f, tt, pElWinding, 3)); // 1-1
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_f, tt, pElCore, 1)); // 1-2
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_f, tt, pElLeads, 10)); // 1-3
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_f, tt, pElTerminal, 2)); // 1-4
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_f, tt, pElTank, 1)); // 1-5
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_f, tt, pElInsulation, 50)); // 1-6
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_f, tt, pElProtL, 1)); // 1-7
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_f, tt, pElProtSc, 1)); // 1-8
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_f, tt, pElWdConn, 1)); // 1-9
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_f, tt, pElCsL, 1)); // 1-11
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_f, tt, pElImb, 1)); // 1-13
		
		// EL-W:自製油入式水力抽蓄用變壓器
		PartAcqInfo paEl_sp_w = mbomDel.buildPartAcqType0(pEl, tt, "SP-W", "自製水力抽蓄用",
				PartAcquisitionType.SELF_PRODUCING); // EL-F:自製火水力抽蓄用
		assertNotNull(paEl_sp_w);
		assertTrue(mbomDel.paAssignMm(tt, paEl_sp_w, mmElW)); // 指定料件主檔
		ParsInfo parsEl_sp_w = mbomDel.buildParsType1(paEl_sp_w, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsEl_sp_w);
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_w, tt, pElWinding, 3)); // 1-1
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_w, tt, pElCore, 1)); // 1-2
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_w, tt, pElLeads, 10)); // 1-3
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_w, tt, pElTerminal, 2)); // 1-4
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_w, tt, pElTank, 1)); // 1-5
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_w, tt, pElInsulation, 50)); // 1-6
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_w, tt, pElProtL, 1)); // 1-7
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_w, tt, pElProtSc, 1)); // 1-8
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_w, tt, pElWdConn, 1)); // 1-9
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_w, tt, pElCsW, 1)); // 1-12
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_w, tt, pElImb, 1)); // 1-13
		
		
		/* 1-1.EL-Winding繞組 */
		// 自製銅線08繞組
		PartAcqInfo paElWinding_sp_cu_08 = mbomDel.buildPartAcqType0(pElWinding, tt, "SP-CU-08", "自製銅線08繞組",
				PartAcquisitionType.SELF_PRODUCING);
		assertNotNull(paElWinding_sp_cu_08);
		assertTrue(mbomDel.paAssignMm(tt, paElWinding_sp_cu_08, mmWdCu08)); // 指定料件主檔
		ParsInfo parsEl_sp_cu_08 = mbomDel.buildParsType1(paElWinding_sp_cu_08, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsEl_sp_cu_08);
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_cu_08, tt, pElWindingM, 8000)); // 1-1-1
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_cu_08, tt, pElWindingEip, 10)); // 1-1-2
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_cu_08, tt, pElWindingEif, 100)); // 1-1-3
		
		// 自製銅線12繞組
		PartAcqInfo paElWinding_sp_cu_12 = mbomDel.buildPartAcqType0(pElWinding, tt, "SP-CU-12", "自製銅線12繞組",
				PartAcquisitionType.SELF_PRODUCING);
		assertNotNull(paElWinding_sp_cu_12);
		assertTrue(mbomDel.paAssignMm(tt, paElWinding_sp_cu_12, mmWdCu12)); // 指定料件主檔
		ParsInfo parsEl_sp_cu_12 = mbomDel.buildParsType1(paElWinding_sp_cu_12, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsEl_sp_cu_12);
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_cu_12, tt, pElWindingM, 10000)); // 1-1-1
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_cu_12, tt, pElWindingEip, 10)); // 1-1-2
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_cu_12, tt, pElWindingEif, 100)); // 1-1-3

		// 自製鋁線12繞組
		PartAcqInfo paElWinding_sp_al_12 = mbomDel.buildPartAcqType0(pElWinding, tt, "SP-AL-12", "自製鋁線12繞組",
				PartAcquisitionType.SELF_PRODUCING);
		assertNotNull(paElWinding_sp_al_12);
		assertTrue(mbomDel.paAssignMm(tt, paElWinding_sp_al_12, mmWdAl12)); // 指定料件主檔
		ParsInfo parsEl_sp_al_12 = mbomDel.buildParsType1(paElWinding_sp_al_12, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsEl_sp_al_12);
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_al_12, tt, pElWindingM, 10000)); // 1-1-1
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_al_12, tt, pElWindingEip, 10)); // 1-1-2
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_al_12, tt, pElWindingEif, 100)); // 1-1-3
		
		/* 1-1-1.EL-Winding-M繞組線圈 */
		PartAcqInfo paElWindingM_pu_cu08 = mbomDel.buildPartAcqType0(pElWindingM, tt, "PU-CU08", "採購0.8mm銅線",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElWindingM_pu_cu08);
		assertTrue(mbomDel.paAssignMm(tt, paElWindingM_pu_cu08, mmWdMCu08)); // 指定料件主檔
		
		PartAcqInfo paElWindingM_pu_cu12 = mbomDel.buildPartAcqType0(pElWindingM, tt, "PU-CU12", "採購1.2mm銅線",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElWindingM_pu_cu12);
		assertTrue(mbomDel.paAssignMm(tt, paElWindingM_pu_cu12, mmWdMCu12)); // 指定料件主檔
		
		PartAcqInfo paElWindingM_pu_al12 = mbomDel.buildPartAcqType0(pElWindingM, tt, "PU-AL12", "採購1.2mm鋁線",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElWindingM_pu_al12);
		assertTrue(mbomDel.paAssignMm(tt, paElWindingM_pu_al12, mmWdMAl12)); // 指定料件主檔
		
		/* 1-1-2.EL-Winding-EIP絕緣漆 */
		PartAcqInfo paElWindingEip_pu_ep = mbomDel.buildPartAcqType0(pElWindingEip, tt, "PU-EP", "採購環氧樹脂絕緣漆",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElWindingEip_pu_ep);
		assertTrue(mbomDel.paAssignMm(tt, paElWindingEip_pu_ep, mmEipEp)); // 指定料件主檔
		
		PartAcqInfo paElWindingEip_pu_pvdf = mbomDel.buildPartAcqType0(pElWindingEip, tt, "PU-PVDF", "採購氟碳絕緣漆",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElWindingEip_pu_pvdf);
		assertTrue(mbomDel.paAssignMm(tt, paElWindingEip_pu_pvdf, mmEipPvdf)); // 指定料件主檔
		
		/* 1-1-3.EL-Winding-EIF絕緣紙 */
		PartAcqInfo paElWindingEif_pu_ar = mbomDel.buildPartAcqType0(pElWindingEif, tt, "PU-AR", "採購Aramid(聚醯胺)絕緣紙",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElWindingEif_pu_ar);
		assertTrue(mbomDel.paAssignMm(tt, paElWindingEif_pu_ar, mmEifAr)); // 指定料件主檔
		
		/* 1-2.EL-Core鐵芯 */
		// 自製矽鋼片鐵芯
		PartAcqInfo paElCore_sp_fe = mbomDel.buildPartAcqType0(pElCore, tt, "SP-FE", "自製矽鋼片鐵芯",
				PartAcquisitionType.SELF_PRODUCING);
		assertNotNull(paElCore_sp_fe);
		assertTrue(mbomDel.paAssignMm(tt, paElCore_sp_fe, mmCrFe)); // 指定料件主檔
		ParsInfo parsEl_sp_fe = mbomDel.buildParsType1(paElCore_sp_fe, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsEl_sp_fe);
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_fe, tt, pElCoreSs, 1000)); // 1-2-1
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_fe, tt, pElCoreEip, 10)); // 1-2-2
		assertNotNull(mbomDel.buildParsPart1(parsEl_sp_fe, tt, pElCoreEif, 100)); // 1-2-3
		
		// 自製高導磁性矽鋼片鐵芯
		PartAcqInfo paElCore_sp_mag = mbomDel.buildPartAcqType0(pElCore, tt, "SP-MAG", "自製高導磁性矽鋼片鐵芯",
				PartAcquisitionType.SELF_PRODUCING);
		assertNotNull(paElCore_sp_mag);
		assertTrue(mbomDel.paAssignMm(tt, paElCore_sp_mag, mmCrMag)); // 指定料件主檔
		ParsInfo parsElCore_sp_mag = mbomDel.buildParsType1(paElCore_sp_mag, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsElCore_sp_mag);
		assertNotNull(mbomDel.buildParsPart1(parsElCore_sp_mag, tt, pElCoreSs, 1000)); // 1-2-1
		assertNotNull(mbomDel.buildParsPart1(parsElCore_sp_mag, tt, pElCoreEip, 10)); // 1-2-2
		assertNotNull(mbomDel.buildParsPart1(parsElCore_sp_mag, tt, pElCoreEif, 100)); // 1-2-3
		
		/* 1-2-1.EL-Core-SS矽鋼片 */
		PartAcqInfo paElCoreSs_pu_35cs250 = mbomDel.buildPartAcqType0(pElCoreSs, tt, "PU-35CS250", "採購矽鋼片0.35mm",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElCoreSs_pu_35cs250);
		assertTrue(mbomDel.paAssignMm(tt, paElCoreSs_pu_35cs250, mmSS35CS250)); // 指定料件主檔
		
		PartAcqInfo paElCoreSs_pu_35cs550 = mbomDel.buildPartAcqType0(pElCoreSs, tt, "PU-35CS550", "採購高導磁性矽鋼片0.35mm",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElCoreSs_pu_35cs550);
		assertTrue(mbomDel.paAssignMm(tt, paElCoreSs_pu_35cs550, mmSS35CS550)); // 指定料件主檔
		
		/* 1-2-2.EL-Core-EIP絕緣漆 */
		PartAcqInfo paElCoreEip_pu_ep = mbomDel.buildPartAcqType0(pElCoreEip, tt, "PU-EP", "採購環氧樹脂絕緣漆",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElCoreEip_pu_ep);
		assertTrue(mbomDel.paAssignMm(tt, paElCoreEip_pu_ep, mmEipEp)); // 指定料件主檔
		
		/* 1-2-3.EL-Core-EIF絕緣紙 */
		PartAcqInfo paElCoreEif_pu_ar = mbomDel.buildPartAcqType0(pElCoreEif, tt, "PU-AR", "採購Aramid(聚醯胺)絕緣紙",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElCoreEif_pu_ar);
		assertTrue(mbomDel.paAssignMm(tt, paElCoreEif_pu_ar, mmEifAr)); // 指定料件主檔
		
		/* 1-3.EL-Leads引線 */
		PartAcqInfo paElLeads_pu_cu08 = mbomDel.buildPartAcqType0(pElLeads, tt, "PU-CU08", "採購銅線0.8mm",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElLeads_pu_cu08);
		assertTrue(mbomDel.paAssignMm(tt, paElLeads_pu_cu08, mmWdMCu08)); // 指定料件主檔
		
		PartAcqInfo paElLeads_pu_al08 = mbomDel.buildPartAcqType0(pElLeads, tt, "PU-AL08", "採購鋁線0.8mm",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElLeads_pu_al08);
		assertTrue(mbomDel.paAssignMm(tt, paElLeads_pu_al08, mmWdMAl08)); // 指定料件主檔
		
		/* 1-4.EL-Terminal端子 */
		PartAcqInfo paElTerminal_pu_cu = mbomDel.buildPartAcqType0(pElTerminal, tt, "PU-CU", "採購壓接端子-銅",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElTerminal_pu_cu);
		assertTrue(mbomDel.paAssignMm(tt, paElTerminal_pu_cu, mmCtCu)); // 指定料件主檔
		
		PartAcqInfo paElTerminal_pu_al = mbomDel.buildPartAcqType0(pElTerminal, tt, "PU-AL", "採購壓接端子-鋁",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElTerminal_pu_al);
		assertTrue(mbomDel.paAssignMm(tt, paElTerminal_pu_al, mmCtAl)); // 指定料件主檔
		
		/* 1.5.EL-Tank變壓器油箱 */
		// 自製
		PartAcqInfo paElTank_sp = mbomDel.buildPartAcqType0(pElTank, tt, "SP", "自製",
				PartAcquisitionType.SELF_PRODUCING);
		assertNotNull(paElTank_sp);
		assertTrue(mbomDel.paAssignMm(tt, paElTank_sp, mmTk)); // 指定料件主檔
		ParsInfo parsElTank_sp = mbomDel.buildParsType1(paElTank_sp, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsElTank_sp);
		assertNotNull(mbomDel.buildParsPart1(parsElTank_sp, tt, pElTankC1, 1)); // 1-5-1
		assertNotNull(mbomDel.buildParsPart1(parsElTank_sp, tt, pElTankC2, 1)); // 1-5-2
		assertNotNull(mbomDel.buildParsPart1(parsElTank_sp, tt, pElTankC3, 2)); // 1-5-3
		assertNotNull(mbomDel.buildParsPart1(parsElTank_sp, tt, pElTankC4, 2)); // 1-5-4
		assertNotNull(mbomDel.buildParsPart1(parsElTank_sp, tt, pElTankC5, 2)); // 1-5-5
		
		//
		PartAcqInfo paElTank_pu_eclipse = mbomDel.buildPartAcqType0(pElTank, tt, "PU_ECLIPSE", "採購橢圓型儲油槽",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElTank_pu_eclipse);
		assertTrue(mbomDel.paAssignMm(tt, paElTank_pu_eclipse, mmTkEclipse)); // 指定料件主檔
		
		/* 1-5-1.EL-Tank-C1油箱本體 */
		PartAcqInfo paElTankC1_pu = mbomDel.buildPartAcqType0(pElTankC1, tt, "PU", "採購",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElTankC1_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElTankC1_pu, mmTkCpn1)); // 指定料件主檔
		
		/* 1-5-2.EL-Tank-C2油箱蓋 */
		PartAcqInfo paElTankC2_pu = mbomDel.buildPartAcqType0(pElTankC2, tt, "PU", "採購",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElTankC2_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElTankC2_pu, mmTkCpn2)); // 指定料件主檔
		
		/* 1-5-3.EL-Tank-C3油箱接頭 */
		PartAcqInfo paElTankC3_pu = mbomDel.buildPartAcqType0(pElTankC3, tt, "PU", "採購",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElTankC3_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElTankC3_pu, mmTkCpn3)); // 指定料件主檔
		
		/* 1-5-4.EL-Tank-C4油箱支架 */
		PartAcqInfo paElTankC4_pu = mbomDel.buildPartAcqType0(pElTankC4, tt, "PU", "採購",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElTankC4_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElTankC4_pu, mmTkCpn4)); // 指定料件主檔
		
		/* 1-5-5.EL-Tank-C5O型環 */
		PartAcqInfo paElTankC5_pu = mbomDel.buildPartAcqType0(pElTankC5, tt, "PU", "採購",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElTankC5_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElTankC5_pu, mmTkCpn5)); // 指定料件主檔
		
		/* 1-6.EL-Insulation絕緣油 */
		PartAcqInfo paElInsulation_pu = mbomDel.buildPartAcqType0(pElInsulation, tt, "PU", "採購",
				PartAcquisitionType.PURCHASING);
		assertNotNull(paElInsulation_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElInsulation_pu, mmToTotal)); // 指定料件主檔
		
		/* 1-7.EL-PROT-L過載保護裝置 */
		PartAcqInfo paElProtL_pu = mbomDel.buildPartAcqType0(pElProtL, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElProtL_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElProtL_pu, mmProtL)); // 指定料件主檔
		
		/* 1-8.EL-PROT-SC短路保護裝置 */
		PartAcqInfo paElProtSc_pu = mbomDel.buildPartAcqType0(pElProtSc, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElProtSc_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElProtSc_pu, mmProtSc)); // 指定料件主檔
		
		/* 1-9.EL-WD-CONN相間繞組連接器 */
		PartAcqInfo paElWdConn_pu = mbomDel.buildPartAcqType0(pElWdConn, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElWdConn_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElWdConn_pu, mmWdConn)); // 指定料件主檔
		
		/* 1-10.EL-CS-A氣冷冷卻系統 */
		PartAcqInfo paElCsA_sp = mbomDel.buildPartAcqType0(pElCsA, tt, "SP", "自製", PartAcquisitionType.SELF_PRODUCING);
		assertNotNull(paElCsA_sp);
		assertTrue(mbomDel.paAssignMm(tt, paElCsA_sp, mmCsA)); // 指定料件主檔
		ParsInfo parsElCsA_sp = mbomDel.buildParsType1(paElCsA_sp, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsElCsA_sp);
		assertNotNull(mbomDel.buildParsPart1(parsElCsA_sp, tt, pElCsAC1, 1)); // 1-10-1
		assertNotNull(mbomDel.buildParsPart1(parsElCsA_sp, tt, pElCsAC2, 1)); // 1-10-2
		assertNotNull(mbomDel.buildParsPart1(parsElCsA_sp, tt, pElCsAC3, 1)); // 1-10-3
		assertNotNull(mbomDel.buildParsPart1(parsElCsA_sp, tt, pElCsAC4, 1)); // 1-10-4
		
		/* 1-10-1.EL-CS-A-C1風扇 */
		PartAcqInfo paElCsAC1_pu = mbomDel.buildPartAcqType0(pElCsAC1, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsAC1_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsAC1_pu, mmCsAC1)); // 指定料件主檔
		
		/* 1-10-2.EL-CS-A-C2風扇罩 */
		PartAcqInfo paElCsAC2_pu = mbomDel.buildPartAcqType0(pElCsAC2, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsAC2_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsAC2_pu, mmCsAC2)); // 指定料件主檔
		
		/* 1-10-3.EL-CS-A-C3風道 */
		PartAcqInfo paElCsAC3_pu = mbomDel.buildPartAcqType0(pElCsAC3, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsAC3_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsAC3_pu, mmCsAC3)); // 指定料件主檔
		
		/* 1-10-4.EL-CS-A-C4風扇控制器 */
		PartAcqInfo paElCsAC4_pu = mbomDel.buildPartAcqType0(pElCsAC4, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsAC4_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsAC4_pu, mmCsAC4)); // 指定料件主檔
		
		/* 1-11.EL-CS-L油冷冷卻系統 */
		PartAcqInfo paElCsL_sp = mbomDel.buildPartAcqType0(pElCsL, tt, "SP", "自製", PartAcquisitionType.SELF_PRODUCING);
		assertNotNull(paElCsL_sp);
		assertTrue(mbomDel.paAssignMm(tt, paElCsL_sp, mmCsL)); // 指定料件主檔
		ParsInfo parsElCsL_sp = mbomDel.buildParsType1(paElCsL_sp, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsElCsL_sp);
		assertNotNull(mbomDel.buildParsPart1(parsElCsL_sp, tt, pElCsLC1, 1)); // 1-11-1
		assertNotNull(mbomDel.buildParsPart1(parsElCsL_sp, tt, pElCsLC2, 1)); // 1-11-2
		assertNotNull(mbomDel.buildParsPart1(parsElCsL_sp, tt, pElCsLC3, 1)); // 1-11-3
		assertNotNull(mbomDel.buildParsPart1(parsElCsL_sp, tt, pElCsLC4, 1)); // 1-11-4
		
		/* 1-11-1.EL-CS-L-C1油箱 */
		PartAcqInfo paElCsLC1_pu = mbomDel.buildPartAcqType0(pElCsLC1, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsLC1_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsLC1_pu, mmCsLC1)); // 指定料件主檔
		
		/* 1-11-2.EL-CS-L-C2冷卻油 */
		PartAcqInfo paElCsLC2_pu = mbomDel.buildPartAcqType0(pElCsLC2, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsLC2_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsLC2_pu, mmCsLC2)); // 指定料件主檔
		
		/* 1-11-3.EL-CS-L-C3油泵 */
		PartAcqInfo paElCsLC3_pu = mbomDel.buildPartAcqType0(pElCsLC3, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsLC3_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsLC3_pu, mmCsLC3)); // 指定料件主檔
		
		/* 1-11-4.EL-CS-L-C4油冷卻器 */
		PartAcqInfo paElCsLC4_pu = mbomDel.buildPartAcqType0(pElCsLC4, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsLC4_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsLC4_pu, mmCsLC4)); // 指定料件主檔
		
		/* 1-12.EL-CS-W水冷冷卻系統 */
		PartAcqInfo paElCsW_sp = mbomDel.buildPartAcqType0(pElCsW, tt, "SP", "自製", PartAcquisitionType.SELF_PRODUCING);
		assertNotNull(paElCsW_sp);
		assertTrue(mbomDel.paAssignMm(tt, paElCsW_sp, mmCsW)); // 指定料件主檔
		ParsInfo parsElCsW_sp = mbomDel.buildParsType1(paElCsW_sp, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsElCsW_sp);
		assertNotNull(mbomDel.buildParsPart1(parsElCsW_sp, tt, pElCsWC1, 2)); // 1-12-1
		assertNotNull(mbomDel.buildParsPart1(parsElCsW_sp, tt, pElCsWC2, 1)); // 1-12-2
		assertNotNull(mbomDel.buildParsPart1(parsElCsW_sp, tt, pElCsWC3, 1)); // 1-12-3
		assertNotNull(mbomDel.buildParsPart1(parsElCsW_sp, tt, pElCsWC4, 1)); // 1-12-4
		assertNotNull(mbomDel.buildParsPart1(parsElCsW_sp, tt, pElCsWC5, 1)); // 1-12-5
		assertNotNull(mbomDel.buildParsPart1(parsElCsW_sp, tt, pElCsWC6, 1)); // 1-12-6
		
		/* 1-12-1.EL-CS-W-C1冷卻水管 */
		PartAcqInfo paElCsWC1_pu = mbomDel.buildPartAcqType0(pElCsWC1, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsWC1_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsWC1_pu, mmCsWC1)); // 指定料件主檔
		
		/* 1-12-2.EL-CS-W-C2水泵 */
		PartAcqInfo paElCsWC2_pu = mbomDel.buildPartAcqType0(pElCsWC2, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsWC2_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsWC2_pu, mmCsWC2)); // 指定料件主檔
		
		/* 1-12-3.EL-CS-W-C3水冷器 */
		PartAcqInfo paElCsWC3_pu = mbomDel.buildPartAcqType0(pElCsWC3, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsWC3_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsWC3_pu, mmCsWC3)); // 指定料件主檔
		
		/* 1-12-4.EL-CS-W-C4水箱 */
		PartAcqInfo paElCsWC4_pu = mbomDel.buildPartAcqType0(pElCsWC4, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsWC4_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsWC4_pu, mmCsWC4)); // 指定料件主檔
		
		/* 1-12-5.EL-CS-W-C5水溫感測器 */
		PartAcqInfo paElCsWC5_pu = mbomDel.buildPartAcqType0(pElCsWC5, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsWC5_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsWC5_pu, mmCsWC5)); // 指定料件主檔
		
		/* 1-12-6.EL-CS-W-C6水泵控制器 */
		PartAcqInfo paElCsWC6_pu = mbomDel.buildPartAcqType0(pElCsWC6, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElCsWC6_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElCsWC6_pu, mmCsWC6)); // 指定料件主檔
	
		/* 1-13.EL-IMB絕緣監測系統 */
		PartAcqInfo paElImb = mbomDel.buildPartAcqType0(pElImb, tt, "SP", "自製", PartAcquisitionType.SELF_PRODUCING);
		assertNotNull(paElImb);
		assertTrue(mbomDel.paAssignMm(tt, paElImb, mmImb)); // 指定料件主檔
		ParsInfo parsElImb = mbomDel.buildParsType1(paElImb, tt,"010","組裝", "把原料組裝成完成品。"); // 建立Pars
		assertNotNull(parsElImb);
		assertNotNull(mbomDel.buildParsPart1(parsElImb, tt, pElImbI1, 1)); // 1-13-1
		assertNotNull(mbomDel.buildParsPart1(parsElImb, tt, pElImbI1c, 1)); // 1-13-2
		assertNotNull(mbomDel.buildParsPart1(parsElImb, tt, pElImbI2, 1)); // 1-13-3
		assertNotNull(mbomDel.buildParsPart1(parsElImb, tt, pElImbI2c, 1)); // 1-13-4
		assertNotNull(mbomDel.buildParsPart1(parsElImb, tt, pElImbI3, 1)); // 1-13-5
		assertNotNull(mbomDel.buildParsPart1(parsElImb, tt, pElImbI3c, 1)); // 1-13-6
		
		
		/* 1-13-1.EL-IMB-I1絕緣監測儀 */
		PartAcqInfo paElImbI1_pu = mbomDel.buildPartAcqType0(pElImbI1, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElImbI1_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElImbI1_pu, mmImbI1)); // 指定料件主檔
		
		/* 1-13-2.EL-IMB-I1C絕緣監測儀附件 */
		PartAcqInfo paElImbI1c_pu = mbomDel.buildPartAcqType0(pElImbI1c, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElImbI1c_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElImbI1c_pu, mmImbI1c)); // 指定料件主檔
		
		/* 1-13-3.EL-IMB-I2絕緣電阻測試儀 */
		PartAcqInfo paElImbI2_pu = mbomDel.buildPartAcqType0(pElImbI2, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElImbI2_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElImbI2_pu, mmImbI2)); // 指定料件主檔
		
		/* 1-13-4.EL-IMB-I2C絕緣電阻測試儀附件 */
		PartAcqInfo paElImbI2c_pu = mbomDel.buildPartAcqType0(pElImbI2c, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElImbI2c_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElImbI2c_pu, mmImbI2c)); // 指定料件主檔
		
		/* 1-13-5.EL-IMB-I3絕緣油測試儀 */
		PartAcqInfo paElImbI3_pu = mbomDel.buildPartAcqType0(pElImbI3, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElImbI3_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElImbI3_pu, mmImbI3)); // 指定料件主檔
		
		/* 1-13-6.EL-IMB-I3C絕緣油測試儀附件 */
		PartAcqInfo paElImbI3c_pu = mbomDel.buildPartAcqType0(pElImbI3c, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		assertNotNull(paElImbI3c_pu);
		assertTrue(mbomDel.paAssignMm(tt, paElImbI3c_pu, mmImbI3c)); // 指定料件主檔
		

		
		// TODO 發佈製程(PartAcq)
		
		
		/* PartCfg */
		/* 建立構型 */
		// EL-1P單相繞組
		PartCfgInfo pcCfgEl1p = mbomDel.buildPartCfg0(pEl.getUid(), pEl.getPin(), tt, "EL-1P", "單相繞組", "");
		assertTrue(mbomDel.runPartCfgEditing(pcCfgEl1p, tt, paEl_sp_1p, paElWinding_sp_cu_08, paElWindingM_pu_cu08,
				paElWindingEip_pu_ep, paElWindingEif_pu_ar)); // 1-1
		assertTrue(mbomDel.runPartCfgEditing(pcCfgEl1p, tt, paElCore_sp_fe, paElCoreSs_pu_35cs250, paElCoreEip_pu_ep,
				paElCoreEif_pu_ar)); // 1-2
		assertTrue(mbomDel.runPartCfgEditing(pcCfgEl1p, tt, paElLeads_pu_cu08, paElTerminal_pu_cu)); // 1-3, 1-4
		assertTrue(mbomDel.runPartCfgEditing(pcCfgEl1p, tt, paElTank_sp, paElTankC1_pu, paElTankC2_pu, paElTankC3_pu, paElTankC4_pu, paElTankC5_pu)); // 1-5
		assertTrue(mbomDel.runPartCfgEditing(pcCfgEl1p, tt, paElInsulation_pu, paElProtL_pu, paElProtSc_pu)); // 1-6~1-8
		assertTrue(mbomDel.runPartCfgEditing(pcCfgEl1p, tt,paElCsA_sp, paElCsAC1_pu, paElCsAC2_pu, paElCsAC3_pu, paElCsAC4_pu )); // 1-10
		
		// EL-3P三相繞組
		PartCfgInfo pcCfgEl3p = mbomDel.buildPartCfg0(pEl.getUid(), pEl.getPin(), tt, "EL-3P", "三相繞組", "");
		assertTrue(mbomDel.runPartCfgEditing(pcCfgEl3p, tt, paEl_sp_3p, paElWinding_sp_cu_08, paElWindingM_pu_cu08,
				paElWindingEip_pu_ep, paElWindingEif_pu_ar)); // 1-1
		assertTrue(mbomDel.runPartCfgEditing(pcCfgEl3p, tt, paElCore_sp_fe, paElCoreSs_pu_35cs250, paElCoreEip_pu_ep,
				paElCoreEif_pu_ar)); // 1-2
		assertTrue(mbomDel.runPartCfgEditing(pcCfgEl3p, tt, paElLeads_pu_cu08, paElTerminal_pu_cu)); // 1-3, 1-4
		assertTrue(mbomDel.runPartCfgEditing(pcCfgEl3p, tt, paElTank_sp, paElTankC1_pu, paElTankC2_pu, paElTankC3_pu, paElTankC4_pu, paElTankC5_pu)); // 1-5
		assertTrue(mbomDel.runPartCfgEditing(pcCfgEl3p, tt, paElInsulation_pu, paElProtL_pu, paElProtSc_pu, paElWdConn_pu)); // 1-6~1-9
		assertTrue(mbomDel.runPartCfgEditing(pcCfgEl3p, tt,paElCsA_sp, paElCsAC1_pu, paElCsAC2_pu, paElCsAC3_pu, paElCsAC4_pu )); // 1-10
		
		// EL-F火力發電用
		PartCfgInfo pcCfgElF = mbomDel.buildPartCfg0(pEl.getUid(), pEl.getPin(), tt, "EL-F", "火力發電用", "");
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElF, tt, paEl_sp_f, paElWinding_sp_cu_08, paElWindingM_pu_cu08,
				paElWindingEip_pu_ep, paElWindingEif_pu_ar)); // 1-1
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElF, tt, paElCore_sp_mag, paElCoreSs_pu_35cs550, paElCoreEip_pu_ep,
				paElCoreEif_pu_ar)); // 1-2
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElF, tt, paElLeads_pu_cu08, paElTerminal_pu_cu)); // 1-3, 1-4
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElF, tt, paElTank_sp, paElTankC1_pu, paElTankC2_pu, paElTankC3_pu, paElTankC4_pu, paElTankC5_pu)); // 1-5
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElF, tt, paElInsulation_pu, paElProtL_pu, paElProtSc_pu, paElWdConn_pu)); // 1-6~1-9
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElF, tt,paElCsL_sp, paElCsLC1_pu, paElCsLC2_pu, paElCsLC3_pu, paElCsLC4_pu )); // 1-11
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElF, tt, paElImb, paElImbI1_pu, paElImbI1c_pu, paElImbI2_pu,
				paElImbI2c_pu, paElImbI3_pu, paElImbI3c_pu)); // 1-13
		
		// EL-W-CU水力抽蓄用-銅
		PartCfgInfo pcCfgElWCu = mbomDel.buildPartCfg0(pEl.getUid(), pEl.getPin(), tt, "EL-W-CU", "水力抽蓄用-銅", "");
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWCu, tt, paEl_sp_w, paElWinding_sp_cu_12, paElWindingM_pu_cu12,
				paElWindingEip_pu_pvdf, paElWindingEif_pu_ar)); // 1-1
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWCu, tt, paElCore_sp_mag, paElCoreSs_pu_35cs550, paElCoreEip_pu_ep,
				paElCoreEif_pu_ar)); // 1-2
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWCu, tt, paElLeads_pu_cu08, paElTerminal_pu_cu)); // 1-3, 1-4
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWCu, tt, paElTank_pu_eclipse)); // 1-5
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWCu, tt, paElInsulation_pu, paElProtL_pu, paElProtSc_pu,
				paElWdConn_pu)); // 1-6~1-9
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWCu, tt, paElCsW_sp, paElCsWC1_pu, paElCsWC2_pu, paElCsWC3_pu,
				paElCsWC4_pu, paElCsWC5_pu, paElCsWC6_pu)); // 1-12
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWCu, tt, paElImb, paElImbI1_pu, paElImbI1c_pu, paElImbI2_pu,
				paElImbI2c_pu, paElImbI3_pu, paElImbI3c_pu)); // 1-13
		
		// EL-W-AL水力抽蓄用-鋁
		PartCfgInfo pcCfgElWAl = mbomDel.buildPartCfg0(pEl.getUid(), pEl.getPin(), tt, "EL-W-AL", "水力抽蓄用-鋁", "");
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWAl, tt, paEl_sp_w, paElWinding_sp_al_12, paElWindingM_pu_al12,
				paElWindingEip_pu_pvdf, paElWindingEif_pu_ar)); // 1-1
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWAl, tt, paElCore_sp_mag, paElCoreSs_pu_35cs550, paElCoreEip_pu_ep,
				paElCoreEif_pu_ar)); // 1-2
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWAl, tt, paElLeads_pu_al08, paElTerminal_pu_al)); // 1-3, 1-4
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWAl, tt, paElTank_pu_eclipse)); // 1-5
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWAl, tt, paElInsulation_pu, paElProtL_pu, paElProtSc_pu,
				paElWdConn_pu)); // 1-6~1-9
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWAl, tt, paElCsW_sp, paElCsWC1_pu, paElCsWC2_pu, paElCsWC3_pu,
				paElCsWC4_pu, paElCsWC5_pu, paElCsWC6_pu)); // 1-12
		assertTrue(mbomDel.runPartCfgEditing(pcCfgElWAl, tt, paElImb, paElImbI1_pu, paElImbI1c_pu, paElImbI2_pu,
				paElImbI2c_pu, paElImbI3_pu, paElImbI3c_pu)); // 1-13
		
		// TODO
	}
	
}
