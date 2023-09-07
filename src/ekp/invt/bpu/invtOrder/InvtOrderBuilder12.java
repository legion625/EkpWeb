package ekp.invt.bpu.invtOrder;

import java.util.Arrays;
import java.util.List;

import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.mf.WorkorderInfo;
import ekp.mf.type.WorkorderStatus;
import legion.util.TimeTraveler;

public class InvtOrderBuilder12 extends InvtOrderBuilder {
	/* base */
	private WorkorderInfo wo;

	/**/
	private InvtOrderItemBuilder12 ioiBuilder;
	private WrhsBinInfo wb;

	// -------------------------------------------------------------------------------
	@Override
	protected InvtOrderBuilder12 appendBase() {
		/* base */
		wo = (WorkorderInfo) args[0];

		/* data */
		// to append applierId, applierName, applyTime
		appendRemark("工令" + wo.getWoNo() + "完工入庫");

		ioiBuilder = new InvtOrderItemBuilder12();
		ioiBuilder.init(wo);

		return this;
	}

	// -------------------------------------------------------------------------------
	public InvtOrderBuilder12 appendWb(WrhsBinInfo wb) {
		this.wb = wb;
		ioiBuilder.appendWb(wb);
		return this;
	}

	// -------------------------------------------------------------------------------

	public WorkorderInfo getWo() {
		return wo;
	}

	@Override
	protected List<InvtOrderItemBuilder12> getInvtOrderItemBuilderList() {
		return Arrays.asList(ioiBuilder);
	}
	

	public InvtOrderItemBuilder12 getIoiBuilder() {
		return ioiBuilder;
	}

	public WrhsBinInfo getWb() {
		return wb;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = super.verify(_msg, _full);
		
		if (WorkorderStatus.FINISH_WORK != getWo().getStatus()) {
			_msg.append("WorkorderStatus should be FINISH_WORK, NOT ").append(getWo().getStatusName())
					.append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	// -------------------------------------------------------------------------------
	@Override
	protected InvtOrderInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		/* 1. buildInvtOrderBasic */
		InvtOrderInfo io = buildInvtOrderBasic(tt);
		if (io == null) {
			tt.travel();
			log.error("buildInvtOrderBasic return null.");
			return null;
		}

		/* 2.工令入庫 */
		// none 工令無為此動作跳狀態

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return io;
	}
}
