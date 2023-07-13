package ekp.data;

import java.rmi.RemoteException;

import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import ekp.serviceFacade.rmi.EkpKernelServiceRemote;
import legion.datasource.manager.DSManager;
import legion.util.LogUtil;

public interface EkpKernelRmi {
//	default EkpKernelServiceRemote getEkpKernelRmi(String srcEkpKernelRmi) {
//		return (EkpKernelServiceRemote) DSManager.getInstance().getConn(srcEkpKernelRmi);
//	}
	
	public String getSrcEkpKernelRmi();
	
	default EkpKernelServiceRemote getEkpKernelRmi() {
		return (EkpKernelServiceRemote) DSManager.getInstance().getConn(getSrcEkpKernelRmi());
	}
	
	default boolean testEkpKernelServiceRemoteCallBack() {
		try {
			EkpKernelServiceRemote service = getEkpKernelRmi();
			LoggerFactory.getLogger(EkpKernelRmi.class).debug("getEkpKernelServiceRemote(): {}", service);
			return service.testCallBack();
		} catch (RemoteException e) {
			LogUtil.log( e, Level.ERROR);
			return false;
		}
	}
}
