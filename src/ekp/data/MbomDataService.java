package ekp.data;

import ekp.data.service.mbom.PartCreateObj;
import ekp.data.service.mbom.PartInfo;
import legion.IntegrationService;

public interface MbomDataService extends IntegrationService{
	public boolean testEkpKernelServiceRemoteCallBack();

	// -------------------------------------------------------------------------------
	// -------------------------------------Part--------------------------------------
	public PartInfo createPart(PartCreateObj _dto);

	public boolean deletePart(String _uid);

	public PartInfo loadPart(String _uid);

	public PartInfo loadPartByPin(String _pin);

}
