package ekp.pu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.TestLogMark;
import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.data.service.sd.BizPartnerInfo;
import ekp.pu.bpu.PuBpuType;
import ekp.pu.bpu.PurchBuilder1;
import ekp.pu.bpu.PurchBuilderAll;
import ekp.pu.bpu.PurchItemBuilder1;
import legion.biz.BpuFacade;
import legion.util.TimeTraveler;

public class PuBuilderDelegate {
	private Logger log = LoggerFactory.getLogger(TestLogMark.class);

	private static PuBuilderDelegate delegate = new PuBuilderDelegate();

	private PuBuilderDelegate() {
	}

	public static PuBuilderDelegate getInstance() {
		return delegate;
	}

	// -------------------------------------------------------------------------------
	private final BpuFacade bpuFacade = BpuFacade.getInstance();
	
	
	// -------------------------------------------------------------------------------
	// -------------------------------------Purch-------------------------------------
	/** 只採購1項 */
	public PurchInfo buildPurch11(TimeTraveler _tt, String _title, String _supplierName, String _supplierBan,
			MaterialMasterInfo _mm, PartAcqInfo _pa, double _qty, double _value, String _remark) {
		PurchBuilder1 pb = bpuFacade.getBuilder(PuBpuType.P_0);
		pb.appendTitle(_title).appendSupplierName(_supplierName).appendSupplierBan(_supplierBan);

		PurchItemBuilder1 pib1 = pb.addPiBuilder();
		pib1.appendMm(_mm).appendQty(_qty).appendValue(_value).appendRemark(_remark);
		pib1.appendPa(_pa);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(msgValidate.toString(), pb.validate(msgValidate));

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(msgVerify.toString(), pb.verify(msgVerify));

		// build
		StringBuilder msgBuild = new StringBuilder();
		PurchInfo p = pb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), p);

		// check
		assertEquals(_title, p.getTitle());
		assertEquals(_supplierName, p.getSupplierName());
		assertEquals(_supplierBan, p.getSupplierBan());
		assertEquals(1, p.getPurchItemList().size());

		// TODO

		return p;
	}
	
	/** 只採購2項 */
	public PurchInfo buildPurch12(TimeTraveler _tt, String _title, String _supplierName, String _supplierBan
			, MaterialMasterInfo _mm1,PartAcqInfo _pa1, double _qty1, double _value1, String _remark1
			, MaterialMasterInfo _mm2,PartAcqInfo _pa2, double _qty2, double _value2, String _remark2
			) {
		PurchBuilder1 pb = bpuFacade.getBuilder(PuBpuType.P_0);
		pb.appendTitle(_title).appendSupplierName(_supplierName).appendSupplierBan(_supplierBan);

		PurchItemBuilder1 pib1 = pb.addPiBuilder();
		pib1.appendMm(_mm1).appendQty(_qty1).appendValue(_value1).appendRemark(_remark1);
		pib1.appendPa(_pa1);
		PurchItemBuilder1 pib2 = pb.addPiBuilder();
		pib2.appendMm(_mm2).appendQty(_qty2).appendValue(_value2).appendRemark(_remark2);
		pib2.appendPa(_pa2);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(msgValidate.toString(), pb.validate(msgValidate));

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(msgVerify.toString(), pb.verify(msgVerify));

		// build
		StringBuilder msgBuild = new StringBuilder();
		PurchInfo p = pb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), p);

		// check
		assertEquals(_title, p.getTitle());
		assertEquals(_supplierName, p.getSupplierName());
		assertEquals(_supplierBan, p.getSupplierBan());
		assertEquals(2, p.getPurchItemList().size());

		// TODO

		return p;
	}
	
	public PurchInfo buildPurchAll(TimeTraveler _tt,String _title, BizPartnerInfo _supplier, WrhsBinInfo _wb, MaterialMasterInfo _mm, PartAcqInfo _pa
			, double _qty, double _value) {
		PurchBuilderAll pb = bpuFacade.getBuilder(PuBpuType.P_ALL);
		pb.appendTitle(_title).appendSupplier(_supplier);
//		.appendSupplierName(_supplierName).appendSupplierBan(_supplierBan);
		pb.appendWb(_wb);
		PurchItemBuilder1 pib = pb.addPiBuilder();
		pib.appendMm(_mm).appendPa(_pa).appendQty(_qty).appendValue(_value);
		
		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(msgValidate.toString(), pb.validate(msgValidate));

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(msgVerify.toString(), pb.verify(msgVerify));

		// build
		StringBuilder msgBuild = new StringBuilder();
		PurchInfo p = pb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), p);
		
		// check
		assertEquals(_title, p.getTitle());
		assertEquals(_supplier.getUid(), p.getSupplierUid());
		assertEquals(_supplier.getName(), p.getSupplierName());
		assertEquals(_supplier.getBan(), p.getSupplierBan());
		assertEquals(1, p.getPurchItemList().size());
		
		PurchItemInfo pi = p.getPurchItemList().get(0);
		assertNotNull(pi);
		List<InvtOrderItemInfo> ioiList = pi.getIoiListIoType11();
		assertNotNull(ioiList);
		assertEquals(1, ioiList.size());
		
		InvtOrderItemInfo ioi = ioiList.get(0);
		assertEquals(ioi.getOrderQty(), pi.getQty(), 0.01);
		assertEquals(ioi.getOrderValue(), pi.getValue(), 0.01);
		
		List<MaterialInstInfo> miList =  pi.getMiList();
		assertNotNull(miList);
		assertEquals(1, miList.size());
		MaterialInstInfo mi = miList.get(0);
		assertEquals(mi.getQty(), pi.getQty(), 0.01);
		assertEquals(mi.getValue(), pi.getValue(), 0.01);

		return p;
	}
	 
	
}
