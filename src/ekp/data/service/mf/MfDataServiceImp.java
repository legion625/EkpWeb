package ekp.data.service.mf;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import ekp.data.MfDataService;
import ekp.data.service.mf.query.WorkorderQueryParam;
import ekp.serviceFacade.rmi.mf.WorkorderCreateObjRemote;
import ekp.serviceFacade.rmi.mf.WorkorderRemote;
import legion.util.LogUtil;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.QueryValue;

public class MfDataServiceImp implements MfDataService {
	private Logger log = LoggerFactory.getLogger(MfDataServiceImp.class);

	private String srcEkpKernelRmi;

	@Override
	public void register(Map<String, String> _params) {
		if (_params == null || _params.isEmpty())
			return;
		srcEkpKernelRmi = _params.get("srcEkpKernelRmi");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getSrcEkpKernelRmi() {
		return srcEkpKernelRmi;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------Workorder-----------------------------------
	@Override
	public WorkorderInfo createWorkorder(WorkorderCreateObj _dto) {
		try {
			WorkorderCreateObjRemote dto = MfFO.parseWorkorderCreateObjRemote(_dto);
			return MfFO.parseWorkorder(getEkpKernelRmi().createWorkorder(dto));
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean deleteWorkorder(String _uid) {
		try {
			return getEkpKernelRmi().deleteWorkorder(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public WorkorderInfo loadWorkorder(String _uid) {
		try {
			WorkorderRemote remote = getEkpKernelRmi().loadWorkorder(_uid);
			return remote == null ? null : MfFO.parseWorkorder(remote);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public QueryOperation<WorkorderQueryParam, WorkorderInfo> searchWorkorder(
			QueryOperation<WorkorderQueryParam, WorkorderInfo> _param,
			Map<WorkorderQueryParam, QueryValue[]> _existsDetailMap) {
		try {
			QueryOperation<WorkorderQueryParam, WorkorderRemote> paramRemote = (QueryOperation<WorkorderQueryParam, WorkorderRemote>) _param
					.copy();
			paramRemote = getEkpKernelRmi().searchWorkorder(paramRemote, _existsDetailMap);
			List<WorkorderInfo> list = paramRemote.getQueryResult().stream().map(MfFO::parseWorkorder)
					.collect(Collectors.toList());
			_param.setQueryResult(list);
			_param.setTotal(paramRemote.getTotal());
			return _param;
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return null;
		}
	}

	@Override
	public boolean woToStart(String _uid) {
		try {
			return getEkpKernelRmi().woToStart(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean woRevertToStart(String _uid) {
		try {
			return getEkpKernelRmi().woRevertToStart(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean woStartWork(String _uid, long _startWorkTime) {
		try {
			return getEkpKernelRmi().woStartWork(_uid, _startWorkTime);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean woRevertStartWork(String _uid) {
		try {
			return getEkpKernelRmi().woRevertStartWork(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean woFinishWork(String _uid, long _finishWorkTime) {
		try {
			return getEkpKernelRmi().woFinishWork(_uid, _finishWorkTime);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean woRevertFinishWork(String _uid) {
		try {
			return getEkpKernelRmi().woRevertFinishWork(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean woOver(String _uid, long _overTime) {
		try {
			return getEkpKernelRmi().woOver(_uid, _overTime);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	@Override
	public boolean woRevertOver(String _uid) {
		try {
			return getEkpKernelRmi().woRevertOver(_uid);
		} catch (Throwable e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

}
