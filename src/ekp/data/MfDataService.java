package ekp.data;

import java.util.Map;

import ekp.data.service.mf.WorkorderCreateObj;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.query.WorkorderQueryParam;
import legion.IntegrationService;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public interface MfDataService extends IntegrationService, EkpKernelRmi {

	// -------------------------------------------------------------------------------
	// -----------------------------------Workorder-----------------------------------
	public WorkorderInfo createWorkorder(WorkorderCreateObj _dto);

	public boolean deleteWorkorder(String _uid);

	public WorkorderInfo loadWorkorder(String _uid);

	public QueryOperation<WorkorderQueryParam, WorkorderInfo> searchWorkorder(
			QueryOperation<WorkorderQueryParam, WorkorderInfo> _param,
			Map<WorkorderQueryParam, QueryValue[]> _existsDetailMap);

	public boolean woToStart(String _uid);

	public boolean woRevertToStart(String _uid);

	public boolean woStartWork(String _uid, long _startWorkTime);

	public boolean woRevertStartWork(String _uid);

	public boolean woFinishWork(String _uid, long _finishWorkTime);

	public boolean woRevertFinishWork(String _uid);

	public boolean woOver(String _uid, long _overTime);

	public boolean woRevertOver(String _uid);

}
