package ekp.invt;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.TestLogMark;
import ekp.data.InvtDataService;
import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.MaterialBinStockBatchInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.invt.WrhsLocInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.WorkorderMaterialInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.invtOrder.InvtOrderBuilder11;
import ekp.invt.bpu.invtOrder.InvtOrderBuilder12;
import ekp.invt.bpu.invtOrder.InvtOrderBuilder22;
import ekp.invt.bpu.invtOrder.InvtOrderItemBuilder12;
import ekp.invt.bpu.invtOrder.InvtOrderItemBuilder22;
import ekp.invt.bpu.invtOrder.IoBpuApprove;
import ekp.invt.bpu.material.MaterialMasterBuilder0;
import ekp.invt.bpu.wrhsLoc.WrhsBinBuilder1;
import ekp.invt.bpu.wrhsLoc.WrhsLocBuilder0;
import ekp.mbom.type.PartUnit;
import ekp.pu.type.PurchPerfStatus;
import legion.DataServiceFactory;
import legion.biz.BpuFacade;
import legion.util.TimeTraveler;

public class InvtDelegate {
	private Logger log = LoggerFactory.getLogger(InvtDelegate.class);
	//	private Logger log = LoggerFactory.getLogger(TestLogMark.class);
	private static InvtDelegate delegate = new InvtDelegate();

	private InvtDelegate() {
	}

	public static InvtDelegate getInstance() {
		return delegate;
	}

	// -------------------------------------------------------------------------------
	private final BpuFacade bpuFacade = BpuFacade.getInstance();
	private InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);

	// -------------------------------------------------------------------------------
	// ------------------------------------WrhsLoc------------------------------------
	public WrhsLocInfo buildWrhsLoc0(TimeTraveler _tt, String _id, String _name) {
		WrhsLocBuilder0 wlb = bpuFacade.getBuilder(InvtBpuType.WL_0);
		wlb.appendId(_id).appendName(_name);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(wlb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(wlb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		WrhsLocInfo wl = wlb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), wl);

		// check
		// TODO
		return wl;
	}
	
	// -------------------------------------------------------------------------------
	// ------------------------------------WrhsBin------------------------------------
	public WrhsBinInfo buildWrhsBin(TimeTraveler _tt, WrhsLocInfo _wl, String _id, String _name) {
		WrhsBinBuilder1 wbb = bpuFacade.getBuilder(InvtBpuType.WB_1, _wl);
		wbb.appendId(_id).appendName(_name);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(wbb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(wbb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		WrhsBinInfo wb = wbb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), wb);

		// check
		assertEquals(_id, wb.getId());
		assertEquals(_name, wb.getName());

		return wb;
	}
	

	// -------------------------------------------------------------------------------
	// --------------------------------MaterialMaster---------------------------------
	public MaterialMasterInfo buildMm0(TimeTraveler _tt, String _mano, String _name, String _spec, PartUnit _stdUnit) {
		MaterialMasterBuilder0 mmb = bpuFacade.getBuilder(InvtBpuType.MM_0);
		mmb.appendMano(_mano).appendName(_name).appendSpecification(_spec).appendStdUnit(_stdUnit);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(mmb.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(mmb.verify(msgVerify), msgVerify.toString());

		// build
		StringBuilder msgBuild = new StringBuilder();
		MaterialMasterInfo mm = mmb.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), mm);

		// check
		// TODO

		return mm;
	}
	
	// -------------------------------------------------------------------------------
	// -----------------------------------InvtOrder-----------------------------------
	public InvtOrderInfo buildIo11(TimeTraveler _tt, PurchInfo _p, String _applierId, String _applierName,
			WrhsBinInfo _wb) {
		InvtOrderBuilder11 iob = bpuFacade.getBuilder(InvtBpuType.IO_11, _p);
		iob.appendApplierId(_applierId).appendApplierName(_applierName);
		iob.appendWb(_wb);
		
		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(iob.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(iob.verify(msgVerify), msgVerify.toString());
		
		// build
		StringBuilder msgBuild = new StringBuilder();
		InvtOrderInfo io = iob.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), io);
		
		// check
		assertEquals(_applierId,io.getApplierId());
		assertEquals(_applierName,io.getApplierName());
		PurchInfo p = _p.reload();
		assertEquals(p.getPurchItemList().size(), io.getIoiList().size());
		assertEquals(PurchPerfStatus.PERFED, p.getPerfStatus());

		return io;
	}
	
	public InvtOrderInfo buildIo12(TimeTraveler _tt, WorkorderInfo _wo, String _applierId, String _applierName,
			WrhsBinInfo _wb) {
		InvtOrderBuilder12 iob = bpuFacade.getBuilder(InvtBpuType.IO_12, _wo);
		iob.appendApplierId(_applierId).appendApplierName(_applierName);
		iob.appendWb(_wb);
		InvtOrderItemBuilder12 ioib =iob.getIoiBuilder();
		double orderValue =ioib.getSumSrcMiValue()*3;
		ioib.appendOrderValue(orderValue);
		
		
		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(iob.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(iob.verify(msgVerify), msgVerify.toString());
		
		// build
		StringBuilder msgBuild = new StringBuilder();
		InvtOrderInfo io = iob.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), io);
		
		// check
		assertEquals(_applierId,io.getApplierId());
		assertEquals(_applierName,io.getApplierName());
		WorkorderInfo wo = _wo.reload();
		assertEquals(1, io.getIoiList().size());
		MaterialInstInfo woPartMi = wo.getPartMi();
		assertNotNull(woPartMi);

		return io;
	}
	
	
	public boolean ioApv(TimeTraveler _tt,InvtOrderInfo _io ) {
		IoBpuApprove bpu = bpuFacade.getBuilder(InvtBpuType.IO_$APPROVE, _io);

		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(bpu.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(bpu.verify(msgVerify), msgVerify.toString());
		
		// build
		StringBuilder msgBuild = new StringBuilder();
		Boolean result = bpu.build(msgBuild, _tt);
		assertTrue(result);
		
		// check
		// TODO
		
		return result;
	}
	
	public InvtOrderInfo buildIo22(TimeTraveler _tt,WorkorderInfo _wo, String _applierId, String _applierName
		) {
		InvtOrderBuilder22 iob = bpuFacade.getBuilder(InvtBpuType.IO_22, _wo);
		iob.appendApplierId(_applierId).appendApplierName(_applierName);
		
		List<InvtOrderItemBuilder22> ioibList = iob.getInvtOrderItemBuilderList();
		for(InvtOrderItemBuilder22 ioib: ioibList) {
			WorkorderMaterialInfo wom =ioib.getWom(); 
			MaterialMasterInfo mm = wom.getMm();
			List<MaterialBinStockBatchInfo> mbsbList =  mm.getMbsbList();
			log.debug("{}\t{}", mm.getMano(), mbsbList.size() );
			double a = wom.getQty0();
			double alcQty = 0;
			for (MaterialBinStockBatchInfo mbsb : mbsbList) {
				alcQty = Math.min(a, mbsb.getStockQty()); // 先找出「待分配數量」和「庫存數量」的較小值，作為「分配量」
				double stmtValue = alcQty * mbsb.getStockValue() / mbsb.getStockQty();
				ioib.addMbsbStmtBuilder(mbsb.getUid(), alcQty, stmtValue);
				a -= alcQty; // 更新「待分配數量」。（mbsb的數量只是參考，要在kernel到時該帳被post時才會生效。）
				if (a <= 0) // 若A已分配滿足，結束for迴圈。
					break;
			}
			assertTrue(a == 0); // a必須要分配完。
			log.debug("{}\t{}", ioib.getWom().getMmMano(), ioib.getMbsbStmtBuilderList().size());
		}
		
		
		// validate
		StringBuilder msgValidate = new StringBuilder();
		assertTrue(iob.validate(msgValidate), msgValidate.toString());

		// verify
		StringBuilder msgVerify = new StringBuilder();
		assertTrue(iob.verify(msgVerify), msgVerify.toString());
		
		// build
		StringBuilder msgBuild = new StringBuilder();
		InvtOrderInfo io = iob.build(msgBuild, _tt);
		assertNotNull(msgBuild.toString(), io);
		
		// check
		assertEquals(_applierId,io.getApplierId());
		assertEquals(_applierName,io.getApplierName());
		WorkorderInfo wo = _wo.reload();
		assertEquals(wo.getWomList().size(), io.getIoiList().size());
		for (WorkorderMaterialInfo wom : wo.getWomList()) {
			assertEquals(0d, wom.getQty0());
			assertTrue(wom.getQty1() > 0);
		}

		return io;
	}

}
