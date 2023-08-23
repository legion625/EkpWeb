package ekp.pu.bpu;

import java.util.List;

import ekp.data.PuDataService;
import ekp.data.service.pu.PurchCreateObj;
import ekp.data.service.pu.PurchInfo;
import ekp.data.service.pu.PurchItemInfo;
import ekp.util.DataUtil;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class PurchBuilder extends Bpu<PurchInfo> {
	private static PuDataService puDataService = DataServiceFactory.getInstance().getService(PuDataService.class);

	/* base */
	private String title; // 名稱
	private String supplierName;
	private String supplierBan; // 供應商統編（臺灣）

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected PurchBuilder appendTitle(String title) {
		this.title = title;
		return this;
	}

	protected PurchBuilder appendSupplierName(String supplierName) {
		this.supplierName = supplierName;
		return this;
	}

	protected PurchBuilder appendSupplierBan(String supplierBan) {
		this.supplierBan = supplierBan;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getTitle() {
		return title;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public String getSupplierBan() {
		return supplierBan;
	}

	// -------------------------------------------------------------------------------
	protected abstract List<? extends PurchItemBuilder> getPurchItemBuilderList();
	
	// -------------------------------------------------------------------------------
	private PurchCreateObj packPurchCreateObj() {
		PurchCreateObj dto = new PurchCreateObj();
		dto.setTitle(getTitle());
		dto.setSupplierName(getSupplierName());
		dto.setSupplierBan(getSupplierBan());
		return dto;
	}
	
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = true;

		if (DataFO.isEmptyString(getTitle())) {
			_msg.append("Title should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		if (DataFO.isEmptyString(getSupplierName())) {
			_msg.append("Supplier name should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		if (DataFO.isEmptyString(getSupplierBan())) {
			_msg.append("Supplier BAN should NOT be empty.").append(System.lineSeparator());
			v = false;
		} else {
			if (!DataUtil.matchesBizAdminNum(getSupplierBan())) {
				_msg.append("統編有誤。").append(System.lineSeparator());
				v = false;
			}
		}

		//
		if (getPurchItemBuilderList() == null || getPurchItemBuilderList().size() <= 0) {
			_msg.append("採購項目必須>0。").append(System.lineSeparator());
			return false;
		}

		return v;
	}

	@Override
	protected abstract PurchInfo buildProcess(TimeTraveler _tt);
	
	protected final PurchInfo buildPurchBasic(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		/* 1.Purch */
		PurchInfo p = puDataService.createPurch(packPurchCreateObj());
		if (p == null) {
			tt.travel();
			log.error("puDataService.createPurch return null.");
			return null;
		}
		tt.addSite("revert createPurch", () -> puDataService.deletePurch(p.getUid()));
		log.info("puDataService.createPurch [{}][{}][{}][{}][{}]", p.getUid(), p.getPuNo(), p.getTitle(),
				p.getSupplierName(), p.getSupplierName());

		/* 2.PurchItem */
		for (PurchItemBuilder piBuilder : getPurchItemBuilderList()) {
			piBuilder.appendPurchUid(p.getUid()); //
			PurchItemInfo pi = piBuilder.build(new StringBuilder(), tt);
			if (pi == null) {
				tt.travel();
				log.error("piBuilder.build return null.");
				return null;
			} // copy sites inside
		}
		log.info("Create PurchItem finished [{}][{}]", p.getUid(), p.getPuNo());

		/* 3.購案履約狀態->待履約 */
		if (!puDataService.purchToPerf(p.getUid())) {
			tt.travel();
			log.error("puDataService.purchToPerf return false.");
			return null;
		}
		tt.addSite("revert puDataService.purchToPerf", ()->puDataService.purchRevertToPerf(p.getUid()));
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return p.reload();
	}
	
	
	
}
