package ekp.invt.bpu.invtOrder;

import java.util.ArrayList;
import java.util.List;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.invt.bpu.material.MbsbStmtBuilder2;
import ekp.invt.type.InvtOrderType;
import ekp.invt.type.IoiTargetType;
import legion.util.TimeTraveler;

public class InvtOrderItemBuilder0 extends InvtOrderItemBuilder {

	/* base */
	// none

	/* data */
	private List<MbsbStmtBuilder2> mbsbStmtBuilderList;
	// none

	@Override
	protected InvtOrderItemBuilder0 appendBase() {
		
		mbsbStmtBuilderList = new ArrayList<>();
		
		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public InvtOrderItemBuilder0 appendMmUid(String mmUid) {
		return (InvtOrderItemBuilder0) super.appendMmUid(mmUid);
	}

	public InvtOrderItemBuilder0 appendIoType(InvtOrderType ioType) {
		return (InvtOrderItemBuilder0) super.appendIoType(ioType);
	}

	public InvtOrderItemBuilder0 appendTargetType(IoiTargetType targetType) {
		return (InvtOrderItemBuilder0) super.appendTargetType(targetType);
	}
	public InvtOrderItemBuilder0 appendTargetUid(String targetUid) {
		return (InvtOrderItemBuilder0) super.appendTargetUid(targetUid);
	}
	public InvtOrderItemBuilder0 appendTargetBizKey(String targetBizKey) {
		return (InvtOrderItemBuilder0) super.appendTargetBizKey(targetBizKey);
	}
	
	public InvtOrderItemBuilder0 appendOrderQty(double orderQty) {
		return (InvtOrderItemBuilder0) super.appendOrderQty(orderQty);
	}

	public InvtOrderItemBuilder0 appendOrderValue(double orderValue) {
		return (InvtOrderItemBuilder0) super.appendOrderValue(orderValue);
	}
	
	public MbsbStmtBuilder2 addMbsbStmtBuilder(String _mbsbUid, double _stmtQty, double _stmtValue) {
		MbsbStmtBuilder2 b = new MbsbStmtBuilder2();
		b.init();
		b.appendMbsbUid(_mbsbUid);
		b.appendStmtQty(_stmtQty).appendStmtValue(_stmtValue);
		mbsbStmtBuilderList.add(b);
		return b;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public List<MbsbStmtBuilder2> getMbsbStmtBuilderList() {
		return mbsbStmtBuilderList;
	}
	public double getSumMbsbStmtBuilderQty() {
		return getMbsbStmtBuilderList().stream().mapToDouble(b -> b.getStmtQty()).sum();
	}

	public double getSumMbsbStmtBuilderValue() {
		return getMbsbStmtBuilderList().stream().mapToDouble(b -> b.getStmtValue()).sum();
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}
	
	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		return verifyThis(_msg, _full);
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public InvtOrderItemInfo buildProcess(TimeTraveler _tt) {
		return buildInvtOrderItem(_tt);
	}

	

	

}
