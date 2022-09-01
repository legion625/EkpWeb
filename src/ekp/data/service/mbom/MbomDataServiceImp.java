package ekp.data.service.mbom;

import java.rmi.RemoteException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import ekp.data.MbomDataService;
import ekp.serviceFacade.rmi.EkpKernelServiceRemote;
import legion.datasource.manager.DSManager;
import legion.util.LogUtil;

public class MbomDataServiceImp implements MbomDataService {
	private Logger log = LoggerFactory.getLogger(MbomDataServiceImp.class);

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

	// -------------------------------------------------------------------------------
	private EkpKernelServiceRemote getEkpKernelServiceRemote() {
		return (EkpKernelServiceRemote) DSManager.getInstance().getConn(srcEkpKernelRmi);
	}

	@Override
	public boolean testEkpKernelServiceRemoteCallBack() {
		try {
			EkpKernelServiceRemote service = getEkpKernelServiceRemote();
			log.debug("getEkpKernelServiceRemote(): {}", service);
			return service.testCallBack();
		} catch (RemoteException e) {
			LogUtil.log(log, e, Level.ERROR);
			return false;
		}
	}

	// -------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------

}
