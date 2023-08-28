package ekp.invt.bpu.invtOrder;

import ekp.data.InvtDataService;
import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.InvtOrderItemCreateObj;
import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.invt.type.InvtOrderType;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class InvtOrderItemBuilder extends Bpu<InvtOrderItemInfo> {
	protected static InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);

	/* base */
	private String ioUid;

	private String mmUid;
	private InvtOrderType ioType;
	private double orderQty;
	private double orderValue;

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected InvtOrderItemBuilder appendIoUid(String ioUid) {
		this.ioUid = ioUid;
		return this;
	}

	protected InvtOrderItemBuilder appendMmUid(String mmUid) {
		this.mmUid = mmUid;
		return this;
	}

	protected InvtOrderItemBuilder appendIoType(InvtOrderType ioType) {
		this.ioType = ioType;
		return this;
	}

	protected InvtOrderItemBuilder appendOrderQty(double orderQty) {
		this.orderQty = orderQty;
		return this;
	}

	protected InvtOrderItemBuilder appendOrderValue(double orderValue) {
		this.orderValue = orderValue;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getIoUid() {
		return ioUid;
	}

	public String getMmUid() {
		return mmUid;
	}

	


	public InvtOrderType getIoType() {
		return ioType;
	}

	public double getOrderQty() {
		return orderQty;
	}

	public double getOrderValue() {
		return orderValue;
	}

	// -------------------------------------------------------------------------------
	private InvtOrderItemCreateObj packInvtOrderItemCreateObj() {
		InvtOrderItemCreateObj dto = new InvtOrderItemCreateObj();
		dto.setIoUid(getIoUid());
		dto.setMmUid(getMmUid());
		dto.setIoType(getIoType());
		dto.setOrderQty(getOrderQty());
		dto.setOrderValue(getOrderValue());
		return dto;
	}
	
	// -------------------------------------------------------------------------------
	protected final boolean verifyThis(StringBuilder _msg) {
		boolean v = true;

//		if (DataFO.isEmptyString(getIoUid())) {
//			_msg.append("ioUid should NOT be empty.").append(System.lineSeparator());
//			v = false;
//		}

		if (DataFO.isEmptyString(getMmUid())) {
			_msg.append("mmUid should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		if (getIoType() == null || InvtOrderType.UNDEFINED == getIoType()) {
			_msg.append("InvtOrderType error.").append(System.lineSeparator());
			v = false;
		}
		// 數量和帳值不能同時都是0
		if (getOrderQty() == 0 && getOrderValue() == 0) {
			_msg.append("OrderQty/OrderValue error.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}
	
	// -------------------------------------------------------------------------------
	protected abstract InvtOrderItemInfo buildProcess(TimeTraveler _tt);
	
	protected final InvtOrderItemInfo buildInvtOrderItem(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		InvtOrderItemInfo ioi = invtDataService.createInvtOrderItem(packInvtOrderItemCreateObj());
		if (ioi == null) {
			tt.travel();
			log.error("invtDataService.createInvtOrderItem return null.");
			return null;
		}
		tt.addSite("revert createInvtOrderItem", () -> invtDataService.deleteInvtOrderItem(ioi.getUid()));
		log.info("invtDataService.createInvtOrderItem");
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return ioi;
	}
	
}
