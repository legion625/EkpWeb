package ekp.data;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import ekp.data.service.mf.WorkorderCreateObj;
import ekp.data.service.mf.WorkorderInfo;
import ekp.data.service.mf.WorkorderMaterialCreateObj;
import ekp.data.service.mf.WorkorderMaterialInfo;
import ekp.data.service.mf.query.WorkorderQueryParam;
import ekp.serviceFacade.rmi.mf.WorkorderMaterialCreateObjRemote;
import ekp.serviceFacade.rmi.mf.WorkorderMaterialRemote;
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
	
	// -------------------------------------------------------------------------------
	// -------------------------------WorkorderMaterial-------------------------------
	public WorkorderMaterialInfo createWorkorderMaterial(WorkorderMaterialCreateObj _dto);
	public boolean deleteWorkorderMaterial(String _uid);
	public WorkorderMaterialInfo loadWorkorderMaterial(String _uid);
	public List<WorkorderMaterialInfo> loadWorkorderMaterialList(String _woUid);
	public boolean womAddQty0(String _uid, double _addQty);
	public boolean womQty0to1(String _uid, double _qty);

}
