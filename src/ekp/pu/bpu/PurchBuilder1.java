package ekp.pu.bpu;

import java.util.ArrayList;
import java.util.List;

import ekp.data.service.pu.PurchInfo;
import legion.util.TimeTraveler;

public class PurchBuilder1 extends PurchBuilder{
	/* base */
	// none

	/* data */
	private List<PurchItemBuilder1> piBuilderList;
	
	// -------------------------------------------------------------------------------
	@Override
	protected PurchBuilder1 appendBase() {
		/* base */
		// none
		
		/* data */
		piBuilderList = new ArrayList<>();
		
		return this;
	}

	// -------------------------------------------------------------------------------
	public PurchBuilder1 appendTitle(String title) {
		return (PurchBuilder1) super.appendTitle(title);
	}

	public PurchBuilder1 appendSupplierName(String supplierName) {
		return (PurchBuilder1) super.appendSupplierName(supplierName);
	}

	public PurchBuilder1 appendSupplierBan(String supplierBan) {
		return (PurchBuilder1) super.appendSupplierBan(supplierBan);
	}
	
	// -------------------------------------------------------------------------------
	public PurchItemBuilder1 addPiBuilder() {
		PurchItemBuilder1 piBuilder1 = new PurchItemBuilder1();
		piBuilderList.add(piBuilder1);
		return piBuilder1;
	}
	
	@Override
	protected List<PurchItemBuilder1> getPurchItemBuilderList() {
		return piBuilderList;
	}
		
	// -------------------------------------------------------------------------------
	@Override
	protected PurchInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		//
		PurchInfo p = buildPurchBasic(tt);
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return p;
		
	}

	

}
