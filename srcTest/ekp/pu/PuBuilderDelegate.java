package ekp.pu;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.TestLogMark;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.pu.bpu.PuBpuType;
import ekp.pu.bpu.PurchBuilder1;
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
			MaterialMasterInfo _mm, double _qty, double _value, String _remark) {
		PurchBuilder1 pb = bpuFacade.getBuilder(PuBpuType.P_0);
		pb.appendTitle(_title).appendSupplierName(_supplierName).appendSupplierBan(_supplierBan);

		PurchItemBuilder1 pib1 = pb.addPiBuilder();
		pib1.appendMm(_mm).appendQty(_qty).appendValue(_value).appendRemark(_remark);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(pb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(pb.verify(msgVerify), msgVerify.toString());

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
			, MaterialMasterInfo _mm1, double _qty1, double _value1, String _remark1
			, MaterialMasterInfo _mm2, double _qty2, double _value2, String _remark2
			) {
		PurchBuilder1 pb = bpuFacade.getBuilder(PuBpuType.P_0);
		pb.appendTitle(_title).appendSupplierName(_supplierName).appendSupplierBan(_supplierBan);

		PurchItemBuilder1 pib1 = pb.addPiBuilder();
		pib1.appendMm(_mm1).appendQty(_qty1).appendValue(_value1).appendRemark(_remark1);
		PurchItemBuilder1 pib2 = pb.addPiBuilder();
		pib2.appendMm(_mm2).appendQty(_qty2).appendValue(_value2).appendRemark(_remark2);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(pb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(pb.verify(msgVerify), msgVerify.toString());

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
	 
	
}
