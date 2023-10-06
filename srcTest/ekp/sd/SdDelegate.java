package ekp.sd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.TestLogMark;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.sd.SalesOrderInfo;
import legion.biz.BpuFacade;
import legion.util.TimeTraveler;

public class SdDelegate {
	private Logger log = LoggerFactory.getLogger(TestLogMark.class);

	private static SdDelegate delegate = new SdDelegate();

	private SdDelegate() {
	}

	public static SdDelegate getInstance() {
		return delegate;
	}

	// -------------------------------------------------------------------------------
	private final BpuFacade bpuFacade = BpuFacade.getInstance();

	// -------------------------------------------------------------------------------
	// ----------------------------------SalesOrder-----------------------------------
	public SalesOrderInfo buildSalesOrder11(TimeTraveler _tt, String _title, String _customerName, String _customerBan
			, MaterialMasterInfo _mm, double _qty, double _value) {
		SalesOrderBuilder1 sb = bpuFacade.getBuilder(SdBpuType.SO_1);
		sb.appendTitle(_title).appendCustomerName(_customerName).appendCustomerBan(_customerBan);
		
		SalesOrderItemBuilder1 soib1 = sb.addSoiBuilder();
		soib1.appendMm(_mm).appendQty(_qty).appendValue(_value);
		
		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(sb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(sb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		SalesOrderInfo so = sb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), so);
		
		// check
		assertEquals(_title, so.getTitle());
		assertEquals(_customerName, so.getCustomerName());
		assertEquals(_customerBan, so.getCustomerBan());
		assertEquals(1, so.getSalesOrderItemList().size());
		
		// TODO
		
		return so;
	}
	
}
