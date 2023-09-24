package ekp.invt.bpu.invtOrder;

import java.util.ArrayList;
import java.util.List;

import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MbsbStmtInfo;
import ekp.invt.bpu.material.MbsbStmtBuilder2;
import ekp.invt.type.InvtOrderType;
import ekp.invt.type.IoiTargetType;
import legion.util.TimeTraveler;

public class InvtOrderItemBuilder21 extends InvtOrderItemBuilder {

	/* base */
	// none

	/* data */
	private List<MbsbStmtBuilder2> mbsbStmtBuilderList;
	// none

	@Override
	protected InvtOrderItemBuilder21 appendBase() {
		
		mbsbStmtBuilderList = new ArrayList<>();
		
		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public InvtOrderItemBuilder21 appendMmUid(String mmUid) {
		return (InvtOrderItemBuilder21) super.appendMmUid(mmUid);
	}

	public InvtOrderItemBuilder21 appendIoType(InvtOrderType ioType) {
		return (InvtOrderItemBuilder21) super.appendIoType(ioType);
	}

	public InvtOrderItemBuilder21 appendTargetType(IoiTargetType targetType) {
		return (InvtOrderItemBuilder21) super.appendTargetType(targetType);
	}
	public InvtOrderItemBuilder21 appendTargetUid(String targetUid) {
		return (InvtOrderItemBuilder21) super.appendTargetUid(targetUid);
	}
	public InvtOrderItemBuilder21 appendTargetBizKey(String targetBizKey) {
		return (InvtOrderItemBuilder21) super.appendTargetBizKey(targetBizKey);
	}
	
	public InvtOrderItemBuilder21 appendOrderQty(double orderQty) {
		return (InvtOrderItemBuilder21) super.appendOrderQty(orderQty);
	}

	public InvtOrderItemBuilder21 appendOrderValue(double orderValue) {
		return (InvtOrderItemBuilder21) super.appendOrderValue(orderValue);
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
	@Override
	public double getOrderValue() {
		return getSumMbsbStmtBuilderValue();
	}
	
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
		TimeTraveler tt = new TimeTraveler();
		/* 1.InvtOrderItem */
		InvtOrderItemInfo ioi = buildInvtOrderItem(tt);
		if (ioi == null) {
			tt.travel();
			log.error("buildInvtOrderItem return null.");
			return null;
		} // copy sites inside
		
		/* 2.MbsbStmt */
		for (MbsbStmtBuilder2 mbsbStmtBuilder : getMbsbStmtBuilderList()) {
			mbsbStmtBuilder.appendIoi(ioi);
			StringBuilder msg = new StringBuilder();
			MbsbStmtInfo mbsbStmt = mbsbStmtBuilder.build(msg, tt);
			if (mbsbStmt == null) {
				tt.travel();
				log.error("mbsbStmtBuilder.build return null. {}", msg.toString());
				return null;
			} // copy sites inside
		}
		
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return ioi.reload();
	}

	

	

}
