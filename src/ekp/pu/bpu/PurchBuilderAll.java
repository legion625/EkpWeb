package ekp.pu.bpu;

import java.util.ArrayList;
import java.util.List;

import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.pu.PurchInfo;
import ekp.invt.bpu.InvtBpuType;
import ekp.invt.bpu.invtOrder.InvtOrderBuilder11;
import ekp.invt.bpu.invtOrder.IoBpuApprove;
import legion.biz.BpuFacade;
import legion.util.TimeTraveler;

public class PurchBuilderAll extends PurchBuilder {
	/* base */
	// none

	/* data */
	private WrhsBinInfo wb;
	private List<PurchItemBuilder1> piBuilderList;
	
	// -------------------------------------------------------------------------------
	@Override
	protected PurchBuilderAll appendBase() {
		/* base */
		// none
		
		/* data */
		piBuilderList = new ArrayList<>();
		
		return this;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public PurchBuilderAll appendTitle(String title) {
		return (PurchBuilderAll) super.appendTitle(title);
	}

	public PurchBuilderAll appendSupplierName(String supplierName) {
		return (PurchBuilderAll) super.appendSupplierName(supplierName);
	}

	public PurchBuilderAll appendSupplierBan(String supplierBan) {
		return (PurchBuilderAll) super.appendSupplierBan(supplierBan);
	}
	
	public PurchBuilderAll appendWb(WrhsBinInfo wb) {
		this.wb = wb;
		return this;
	}
	
	// -------------------------------------------------------------------------------
	public PurchItemBuilder1 addPiBuilder() {
		PurchItemBuilder1 piBuilder1 = new PurchItemBuilder1();
		piBuilderList.add(piBuilder1);
		return piBuilder1;
	}
	
	 
	
	// -------------------------------------------------------------------------------
	public WrhsBinInfo getWb() {
		return wb;
	}
	
	@Override
	protected List<PurchItemBuilder1> getPurchItemBuilderList() {
		return piBuilderList;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean result = true;
		if(getWb()==null) {
			_msg.append("未指定儲位。").append(System.lineSeparator());
			result = false;
		}
		
		if(getPurchItemBuilderList()==null || getPurchItemBuilderList().size()<=0) {
			_msg.append("未填寫採購品項。").append(System.lineSeparator());
			result = false;
		}
		
		return result;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	protected PurchInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		// 1. 建立購案、細項、狀態->待履約
		PurchInfo p = buildPurchBasic(tt);
		if (p == null) {
			log.error("buildPurchBasic return null");
			tt.travel();
			return null;
		}

		// 2. 購案履約（依Purch產生InvtOrder、InvtOrderItem含MaterialInst、MbsbStmt）
		InvtOrderBuilder11 iob = BpuFacade.getInstance().getBuilder(InvtBpuType.IO_11, p);
		iob.appendApplierId("USER1").appendApplierName("Min-Hua");
		iob.appendWb(getWb());
		InvtOrderInfo io = iob.build(_tt);
		if (io == null) {
			log.error("InvtOrderBuilder11.build return null");
			tt.travel();
			return null;
		}

		// 3.InvtOrder登帳
		IoBpuApprove ioBpuApprove = BpuFacade.getInstance().getBuilder(InvtBpuType.IO_$APPROVE, io);
		if (!ioBpuApprove.build(tt)) {
			log.error("IoBpuApprove.build return false");
			tt.travel();
			return null;
		}

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return p.reload();
	}

}
