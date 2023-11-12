package ekp.invt.bpu.invtOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.InvtOrderItemInfo;
import ekp.data.service.invt.MaterialInstInfo;
import ekp.data.service.invt.MbsbStmtInfo;
//import ekp.invt.bpu.material.MbsbStmtBuilderByIoi;
import ekp.invt.type.InvtOrderStatus;
import ekp.invt.type.InvtOrderType;
import legion.util.TimeTraveler;

public class IoBpuApprove extends IoBpu {
	/* base */
	private InvtOrderInfo io;

	/* data */
	private List<MbsbStmtInfo> mbsbStmtList;
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected IoBpuApprove appendBase() {
		/* base */
		io = (InvtOrderInfo) args[0];
		appendIo(io);

		/* data */
		mbsbStmtList = io.getMbsbStmtList();

		return this;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;

		/*  */
		if (InvtOrderStatus.TO_APV != io.getStatus()) {
			_msg.append("InvtOrderStatus error.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		/* 1.Io Apv */
		if (!invtDataService.invtOrderApprove(io.getUid(), System.currentTimeMillis())) {
			tt.travel();
			log.error("invtDataService.invtOrderApprove return false.");
			return false;
		}
		tt.addSite("revert invtOrderApprove", () -> invtDataService.invtOrderRevertApprove(io.getUid()));

		/* 2.MbsbStmtPost */
		for (MbsbStmtInfo mbsbStmt : mbsbStmtList) {
			if (!invtDataService.mbsbStmtPost(mbsbStmt.getUid())) {
				tt.travel();
				log.error("invtDataService.mbsbStmtPost return false.");
				return false;
			}
			tt.addSite("revert mbsbStmtPost", () -> invtDataService.mbsbStmtRevertPost(mbsbStmt.getUid()));
		}

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return true;
	}

	
	
}
