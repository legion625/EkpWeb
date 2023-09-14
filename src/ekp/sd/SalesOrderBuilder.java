package ekp.sd;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.data.SdDataService;
import ekp.data.service.sd.SalesOrderCreateObj;
import ekp.data.service.sd.SalesOrderInfo;
import ekp.data.service.sd.SalesOrderItemInfo;
import ekp.util.DataUtil;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class SalesOrderBuilder extends Bpu<SalesOrderInfo> {
	
	private static SdDataService sdDataService = DataServiceFactory.getInstance().getService(SdDataService.class);

	/* base */
	private String title;
	private String customerName;
	private String customerBan;

	private String salerId;
	private String salerName;
	private long saleDate;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected SalesOrderBuilder appendTitle(String title) {
		this.title = title;
		return this;
	}

	protected SalesOrderBuilder appendCustomerName(String customerName) {
		this.customerName = customerName;
		return this;
	}

	protected SalesOrderBuilder appendCustomerBan(String customerBan) {
		this.customerBan = customerBan;
		return this;
	}

	protected SalesOrderBuilder appendSalerId(String salerId) {
		this.salerId = salerId;
		return this;
	}

	protected SalesOrderBuilder appendSalerName(String salerName) {
		this.salerName = salerName;
		return this;
	}

	protected SalesOrderBuilder appendSaleDate(long saleDate) {
		this.saleDate = saleDate;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getTitle() {
		return title;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getCustomerBan() {
		return customerBan;
	}

	public String getSalerId() {
		return salerId;
	}

	public String getSalerName() {
		return salerName;
	}

	public long getSaleDate() {
		return saleDate;
	}

	// -------------------------------------------------------------------------------
	protected abstract List<? extends SalesOrderItemBuilder> getSalesOrderItemBuilderList();
	
	// -------------------------------------------------------------------------------
	private SalesOrderCreateObj packSalesOrderCreateObj() {
		SalesOrderCreateObj dto = new SalesOrderCreateObj();
		dto.setTitle(getTitle());
		dto.setCustomerName(getCustomerName());
		dto.setCustomerBan(getCustomerBan());
		dto.setSalerId(getSalerId());
		dto.setSalerName(getSalerName());
		dto.setSaleDate(getSaleDate());
		return dto;
	}

	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;
		
		if(DataFO.isEmptyString(getTitle())) {
			_msg.append("Title should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		
		if (DataFO.isEmptyString(getCustomerName())) {
			_msg.append("Customer name should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		if (DataFO.isEmptyString(getCustomerBan())) {
			_msg.append("Customer BAN should NOT be empty.").append(System.lineSeparator());
			v = false;
		} else {
			if (!DataUtil.matchesBizAdminNum(getCustomerBan())) {
				_msg.append("統編有誤。").append(System.lineSeparator());
				v = false;
			}
		}

		//
		if (getSalesOrderItemBuilderList() == null || getSalesOrderItemBuilderList().size() <= 0) {
			_msg.append("銷售訂單項目必須>0。").append(System.lineSeparator());
			return false;
		}
		
		return v;
	}
	
	@Override
	protected abstract SalesOrderInfo buildProcess(TimeTraveler _tt);
	
	protected final SalesOrderInfo buildSalesOrderBasic(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		
		/* 1.SalesOrder */
		SalesOrderInfo so = sdDataService.createSalesOrder(packSalesOrderCreateObj());
		if(so==null) {
			tt.travel();
			log.error("sdDataService.createSalesOrder return null.");
			return null;
		}
		tt.addSite("revert createSalesOrder", () ->sdDataService.deleteSalesOrder(so.getUid()));
		log.info("sdDataService.createSalesOrder [{}][{}][{}][{}][{}]", so.getUid(), so.getSosn(), so.getTitle(), so.getCustomerName(), so.getCustomerBan());
		
		/* 2.SalesOrderItem */
		for (SalesOrderItemBuilder soiBuilder : getSalesOrderItemBuilderList()) {
			
			soiBuilder.appendSoUid(so.getUid());
			log.debug("soiBuilder.getSoUid(): {}", soiBuilder.getSoUid());
			SalesOrderItemInfo soi = soiBuilder.build(new StringBuilder(), tt);
			if (soi == null) {
				tt.travel();
				log.error("soiBuilder.build return null.");
				return null;
			} // copy sites inside
		}
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);
		
		return so.reload();
	}

}
