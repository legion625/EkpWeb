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
import ekp.data.MbomDataService;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mbom.PartAcqInfo;
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

public class TransformerScn extends AbstractEkpInitTest {
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);
	
	//
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
		MaterialMasterInfo mmEl = invtDel.buildMm0(tt, "EL", "油入式電力變壓器", "", PartUnit.EAC);
		MaterialMasterInfo mmWd = invtDel.buildMm0(tt, "WD", "繞組", "", PartUnit.EAC);
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
		MaterialMasterInfo mmWdCu12 = invtDel.buildMm0(tt, "WD-CU-12", "銅線1.2mm", "", PartUnit.MTR);
		MaterialMasterInfo mmWdCu08 = invtDel.buildMm0(tt, "WD-CU-08", "銅線0.8mm", "", PartUnit.MTR);
		MaterialMasterInfo mmWdAl12 = invtDel.buildMm0(tt, "WD-AL-12", "鋁線1.2mm", "", PartUnit.MTR);
		MaterialMasterInfo mmWdAl08 = invtDel.buildMm0(tt, "WD-AL-08", "鋁線0.8mm", "", PartUnit.MTR);
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
		PartInfo pElCore = mbomDel.buildPartType0(tt, "EL-Core", "鐵芯", PartUnit.EAC);
		PartInfo pElCoreSs = mbomDel.buildPartType0(tt, "EL-Core-SS", "矽鋼片", PartUnit.EAC);
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
	public void testInitPartAcq() {
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
//		String paEl1PId = "EL-1P";
//		String paEl1PName = "單相";
		
		/* 1.EL油入式電力變壓器 */
		PartAcqInfo paEl_sp_1p = mbomDel.buildPartAcqType0(pEl, tt, "SP-1P", "自製單相繞組",
				PartAcquisitionType.SELF_PRODUCING); // EL-1P:自製單相繞組
		PartAcqInfo paEl_sp_3p = mbomDel.buildPartAcqType0(pEl, tt, "SP-3P", "自製三相繞組",
				PartAcquisitionType.SELF_PRODUCING); // EL-3P:自製三相繞組
		
		/* 1-1.EL-Winding繞組 */
		PartAcqInfo paElWinding_sp_80 = mbomDel.buildPartAcqType0(pElWinding, tt, "SP-80", "自製8000匝繞組",
				PartAcquisitionType.SELF_PRODUCING);
		PartAcqInfo paElWinding_sp_100 = mbomDel.buildPartAcqType0(pElWinding, tt, "SP-100", "自製10000匝繞組",
				PartAcquisitionType.SELF_PRODUCING);
		
		/* 1-1-1.EL-Winding-M繞組線圈 */
		PartAcqInfo paElWindingM_pu_cu08 = mbomDel.buildPartAcqType0(pElWindingM, tt, "PU-CU08", "採購0.8mm銅線",
				PartAcquisitionType.PURCHASING);
		PartAcqInfo paElWindingM_pu_cu12 = mbomDel.buildPartAcqType0(pElWindingM, tt, "PU-CU12", "採購1.2mm銅線",
				PartAcquisitionType.PURCHASING);
		PartAcqInfo paElWindingM_pu_al12 = mbomDel.buildPartAcqType0(pElWindingM, tt, "PU-AL12", "採購1.2mm鋁線",
				PartAcquisitionType.PURCHASING);
		
		/* 1-1-2.EL-Winding-EIP絕緣漆 */
		PartAcqInfo paElWindingEip_pu_ep = mbomDel.buildPartAcqType0(pElWindingEip, tt, "PU-EP", "採購環氧樹脂絕緣漆",
				PartAcquisitionType.PURCHASING);
		PartAcqInfo paElWindingEip_pu_pvdf = mbomDel.buildPartAcqType0(pElWindingEip, tt, "PU-PVDF", "採購氟碳絕緣漆",
				PartAcquisitionType.PURCHASING);
		
		/* 1-1-3.EL-Winding-EIF絕緣紙 */
		PartAcqInfo paElWindingEif_pu_ar = mbomDel.buildPartAcqType0(pElWindingEif, tt, "PU-AR", "採購Aramid(聚醯胺)絕緣紙",
				PartAcquisitionType.PURCHASING);
		
		/* 1-2.EL-Core鐵芯 */
		PartAcqInfo paElCore_sp_fe = mbomDel.buildPartAcqType0(pElCore, tt, "SP-FE", "自製矽鋼片鐵芯",
				PartAcquisitionType.SELF_PRODUCING);
		PartAcqInfo paElCore_sp_mag = mbomDel.buildPartAcqType0(pElCore, tt, "SP-MAG", "自製高導磁性矽鋼片鐵芯",
				PartAcquisitionType.SELF_PRODUCING);
		
		/* 1-2-1.EL-Core-SS矽鋼片 */
		PartAcqInfo paElCoreSs_pu_35cs250 = mbomDel.buildPartAcqType0(pElCoreSs, tt, "PU-35CS250", "採購矽鋼片0.35mm",
				PartAcquisitionType.PURCHASING);
		PartAcqInfo paElCoreSs_pu_35cs550 = mbomDel.buildPartAcqType0(pElCoreSs, tt, "PU-35CS550", "採購高導磁性矽鋼片0.35mm",
				PartAcquisitionType.PURCHASING);
		
		/* 1-2-2.EL-Core-EIP絕緣漆 */
		PartAcqInfo paElCoreEip_pu_ep = mbomDel.buildPartAcqType0(pElCoreEip, tt, "PU-EP", "採購環氧樹脂絕緣漆",
				PartAcquisitionType.PURCHASING);
		
		/* 1-2-3.EL-Core-EIF絕緣紙 */
		PartAcqInfo paElCoreEif_pu_ar = mbomDel.buildPartAcqType0(pElCoreEif, tt, "PU-AR", "採購Aramid(聚醯胺)絕緣紙",
				PartAcquisitionType.PURCHASING);
		
		/* 1-3.EL-Leads引線 */
		PartAcqInfo paElLeads_pu_cu08 = mbomDel.buildPartAcqType0(pElLeads, tt, "PU-CU08", "採購銅線0.8mm",
				PartAcquisitionType.PURCHASING);
		PartAcqInfo paElLeads_pu_al08 = mbomDel.buildPartAcqType0(pElLeads, tt, "PU-AL08", "採購鋁線0.8mm",
				PartAcquisitionType.PURCHASING);
		
		/* 1-4.EL-Terminal端子 */
		PartAcqInfo paElTerminal_pu_cu = mbomDel.buildPartAcqType0(pElTerminal, tt, "PU-CU", "採購壓接端子-銅",
				PartAcquisitionType.PURCHASING);
		PartAcqInfo paElTerminal_pu_al = mbomDel.buildPartAcqType0(pElTerminal, tt, "PU-AL", "採購壓接端子-鋁",
				PartAcquisitionType.PURCHASING);
		
		/* 1.5.EL-Tank變壓器油箱 */
		PartAcqInfo paElTank_sp = mbomDel.buildPartAcqType0(pElTank, tt, "SP", "自製",
				PartAcquisitionType.SELF_PRODUCING);
		PartAcqInfo paElTank_pu_eclipse = mbomDel.buildPartAcqType0(pElTank, tt, "PU_ECLIPSE", "採購橢圓型儲油槽",
				PartAcquisitionType.PURCHASING);
		/* 1-5-1.EL-Tank-C1油箱本體 */
		PartAcqInfo paElTankC1_pu = mbomDel.buildPartAcqType0(pElTankC1, tt, "PU", "採購",
				PartAcquisitionType.PURCHASING);
		/* 1-5-2.EL-Tank-C2油箱蓋 */
		PartAcqInfo paElTankC2_pu = mbomDel.buildPartAcqType0(pElTankC2, tt, "PU", "採購",
				PartAcquisitionType.PURCHASING);
		/* 1-5-3.EL-Tank-C3油箱接頭 */
		PartAcqInfo paElTankC3_pu = mbomDel.buildPartAcqType0(pElTankC3, tt, "PU", "採購",
				PartAcquisitionType.PURCHASING);
		/* 1-5-4.EL-Tank-C4油箱支架 */
		PartAcqInfo paElTankC4_pu = mbomDel.buildPartAcqType0(pElTankC4, tt, "PU", "採購",
				PartAcquisitionType.PURCHASING);
		/* 1-5-5.EL-Tank-C5O型環 */
		PartAcqInfo paElTankC5_pu = mbomDel.buildPartAcqType0(pElTankC5, tt, "PU", "採購",
				PartAcquisitionType.PURCHASING);
		
		/* 1-6.EL-Insulation絕緣油 */
		PartAcqInfo paElInsulation_pu = mbomDel.buildPartAcqType0(pElInsulation, tt, "PU", "採購",
				PartAcquisitionType.PURCHASING);
		/* 1-7.EL-PROT-L過載保護裝置 */
		PartAcqInfo paElProtL_pu = mbomDel.buildPartAcqType0(pElProtL, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-8.EL-PROT-SC短路保護裝置 */
		PartAcqInfo paElProtSc_pu = mbomDel.buildPartAcqType0(pElProtSc, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-9.EL-WD-CONN相間繞組連接器 */
		PartAcqInfo paElWdConn_pu = mbomDel.buildPartAcqType0(pElWdConn, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		
		/* 1-10.EL-CS-A氣冷冷卻系統 */
		PartAcqInfo paElCsA_sp = mbomDel.buildPartAcqType0(pElCsA, tt, "SP", "自製", PartAcquisitionType.SELF_PRODUCING);
		/* 1-10-1.EL-CS-A-C1風扇 */
		PartAcqInfo paElCsAC1_pu = mbomDel.buildPartAcqType0(pElCsAC1, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-10-2.EL-CS-A-C2風扇罩 */
		PartAcqInfo paElCsAC2_pu = mbomDel.buildPartAcqType0(pElCsAC2, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-10-3.EL-CS-A-C3風道 */
		PartAcqInfo paElCsAC3_pu = mbomDel.buildPartAcqType0(pElCsAC3, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-10-4.EL-CS-A-C4風扇控制器 */
		PartAcqInfo paElCsAC4_pu = mbomDel.buildPartAcqType0(pElCsAC4, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		
		/* 1-11.EL-CS-L油冷冷卻系統 */
		PartAcqInfo paElCsL_sp = mbomDel.buildPartAcqType0(pElCsL, tt, "SP", "自製", PartAcquisitionType.SELF_PRODUCING);
		/* 1-11-1.EL-CS-L-C1油箱 */
		PartAcqInfo paElCsLC1_pu = mbomDel.buildPartAcqType0(pElCsLC1, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-11-2.EL-CS-L-C2冷卻油 */
		PartAcqInfo paElCsLC2_pu = mbomDel.buildPartAcqType0(pElCsLC2, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-11-3.EL-CS-L-C3油泵 */
		PartAcqInfo paElCsLC3_pu = mbomDel.buildPartAcqType0(pElCsLC3, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-11-4.EL-CS-L-C4油冷卻器 */
		PartAcqInfo paElCsLC4_pu = mbomDel.buildPartAcqType0(pElCsLC4, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		
		/* 1-12.EL-CS-W水冷冷卻系統 */
		PartAcqInfo paElCsW_sp = mbomDel.buildPartAcqType0(pElCsW, tt, "SP", "自製", PartAcquisitionType.SELF_PRODUCING);
		/* 1-12-1.EL-CS-W-C1冷卻水管 */
		PartAcqInfo paElCsWC1_pu = mbomDel.buildPartAcqType0(pElCsWC1, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-12-2.EL-CS-W-C2水泵 */
		PartAcqInfo paElCsWC2_pu = mbomDel.buildPartAcqType0(pElCsWC2, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-12-3.EL-CS-W-C3水冷器 */
		PartAcqInfo paElCsWC3_pu = mbomDel.buildPartAcqType0(pElCsWC3, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-12-4.EL-CS-W-C4水箱 */
		PartAcqInfo paElCsWC4_pu = mbomDel.buildPartAcqType0(pElCsWC4, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-12-5.EL-CS-W-C5水溫感測器 */
		PartAcqInfo paElCsWC5_pu = mbomDel.buildPartAcqType0(pElCsWC5, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-12-6.EL-CS-W-C6水泵控制器 */
		PartAcqInfo paElCsWC6_pu = mbomDel.buildPartAcqType0(pElCsWC6, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
	
		/* 1-13.EL-IMB絕緣監測系統 */
		PartAcqInfo paElImb = mbomDel.buildPartAcqType0(pElImb, tt, "SP", "自製", PartAcquisitionType.SELF_PRODUCING);
		/* 1-13-1.EL-IMB-I1絕緣監測儀 */
		PartAcqInfo paElImbI1_pu = mbomDel.buildPartAcqType0(pElImbI1, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-13-2.EL-IMB-I1C絕緣監測儀附件 */
		PartAcqInfo paElImbI1c_pu = mbomDel.buildPartAcqType0(pElImbI1c, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-13-3.EL-IMB-I2絕緣電阻測試儀 */
		PartAcqInfo paElImbI2_pu = mbomDel.buildPartAcqType0(pElImbI2, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-13-4.EL-IMB-I2C絕緣電阻測試儀附件 */
		PartAcqInfo paElImbI2c_pu = mbomDel.buildPartAcqType0(pElImbI2c, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-13-5.EL-IMB-I3絕緣油測試儀 */
		PartAcqInfo paElImbI3_pu = mbomDel.buildPartAcqType0(pElImbI3, tt, "PU", "採購", PartAcquisitionType.PURCHASING);
		/* 1-13-6.EL-IMB-I3C絕緣油測試儀附件 */
		PartAcqInfo paElImbI3c_pu = mbomDel.buildPartAcqType0(pElImbI3c, tt, "PU", "採購", PartAcquisitionType.PURCHASING);

	}
}
