package ekp.invt.bpu.invtOrder;

import java.util.List;

import ekp.data.InvtDataService;
import ekp.data.service.invt.InvtOrderCreateObj;
import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.InvtOrderItemInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.DateFormatUtil;
import legion.util.TimeTraveler;

public abstract class InvtOrderBuilder extends Bpu<InvtOrderInfo> {
	private static InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);

	/* base */
	private String applierId;
	private String applierName;
	private long applyTime; // apply time
	private String remark; //

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public InvtOrderBuilder appendApplierId(String applierId) {
		this.applierId = applierId;
		return this;
	}

	public InvtOrderBuilder appendApplierName(String applierName) {
		this.applierName = applierName;
		return this;
	}

	public InvtOrderBuilder appendApplyTime(long applyTime) {
		this.applyTime = applyTime;
		return this;
	}

	protected InvtOrderBuilder appendRemark(String remark) {
		this.remark = remark;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getApplierId() {
		return applierId;
	}

	public String getApplierName() {
		return applierName;
	}

	public long getApplyTime() {
		return applyTime;
	}

	public String getRemark() {
		return remark;
	}

	// -------------------------------------------------------------------------------
	protected abstract List<? extends InvtOrderItemBuilder> getInvtOrderItemBuilderList();
	
	// -------------------------------------------------------------------------------
	private InvtOrderCreateObj packInvtOrderCreateObj() {
		InvtOrderCreateObj dto = new InvtOrderCreateObj();
		dto.setApplierId(getApplierId());
		dto.setApplierName(getApplierName());
		dto.setApvTime(getApplyTime());
		dto.setRemark(getRemark());
		return dto;
	}

	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = true;
		
		if(DataFO.isEmptyString(getApplierId())) {
			_msg.append("Applier ID should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		
		if(DataFO.isEmptyString(getApplierName())) {
			_msg.append("Applier name should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		
		if(getApplyTime()<=0) {
			_msg.append("Applied time should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		
		return v;
	}

	
	@Override
	protected abstract InvtOrderInfo buildProcess(TimeTraveler _tt);
	
	protected final InvtOrderInfo buildInvtOrderBasic(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		/* 1.InvtOrder */
		InvtOrderInfo io = invtDataService.createInvtOrder(packInvtOrderCreateObj());
		if (io == null) {
			tt.travel();
			log.error("invtDataService.createInvtOrder return null.");
			return null;
		}
		tt.addSite("revert createInvtOrder", () -> invtDataService.deleteInvtOrder(io.getUid()));
		log.info("invtDataService.createInvtOrder [{}][{}][{}][{}][{}][{}]", io.getUid(), io.getIosn(),
				io.getApplierId(), io.getApplierName(), DateFormatUtil.transToTime(io.getApvTime()), io.getRemark());
		
		/* 2.InvtOrderItem */
		for (InvtOrderItemBuilder ioiBuilder : getInvtOrderItemBuilderList()) {
			InvtOrderItemInfo ioi = ioiBuilder.build(new StringBuilder(), tt);
			if (ioi == null) {
				tt.travel();
				log.error("ioiBuilder.build return null.");
				return null;
			} // copy sites inside
		}
		log.info("create InvtOrderItem finished [{}][{}]", io.getUid(), io.getIosn());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return io;
	}
	
}
