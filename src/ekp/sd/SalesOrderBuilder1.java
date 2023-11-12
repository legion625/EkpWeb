package ekp.sd;

import java.util.ArrayList;
import java.util.List;

import ekp.data.service.sd.BizPartnerInfo;
import ekp.data.service.sd.SalesOrderInfo;
import legion.util.TimeTraveler;

public class SalesOrderBuilder1 extends SalesOrderBuilder {
	/* base */
	// none

	/* data */
	private List<SalesOrderItemBuilder1> soiBuilderList;

	// -------------------------------------------------------------------------------
	@Override
	protected SalesOrderBuilder1 appendBase() {
		/* base */
		// none

		/* data */
		soiBuilderList = new ArrayList<>();

		return this;
	}

	// -------------------------------------------------------------------------------
	@Override
	public SalesOrderBuilder1 appendTitle(String title) {
		return (SalesOrderBuilder1) super.appendTitle(title);
	}

	public SalesOrderBuilder1 appendCustomer(BizPartnerInfo customer) {
		appendCustomerUid(customer==null?"":customer.getUid());
		appendCustomerName(customer==null?"":customer.getName());
		appendCustomerBan(customer==null?"":customer.getBan());
		return this;
	}

	@Override
	public SalesOrderBuilder1 appendSalerId(String salerId) {
		return (SalesOrderBuilder1) super.appendSalerId(salerId);
	}

	@Override
	public SalesOrderBuilder1 appendSalerName(String salerName) {
		return (SalesOrderBuilder1) super.appendSalerName(salerName);
	}

	@Override
	public SalesOrderBuilder1 appendSaleDate(long saleDate) {
		return (SalesOrderBuilder1) super.appendSaleDate(saleDate);
	}
	
	// -------------------------------------------------------------------------------
	public SalesOrderItemBuilder1 addSoiBuilder() {
		SalesOrderItemBuilder1 soiBuilder1 = new SalesOrderItemBuilder1();
		soiBuilderList.add(soiBuilder1);
		return soiBuilder1;
	}
	
	@Override
	protected List<SalesOrderItemBuilder1> getSalesOrderItemBuilderList(){
		return soiBuilderList;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected SalesOrderInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		//
		SalesOrderInfo so = buildSalesOrderBasic(tt);

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return so;
	}

}
