package ekp.data.service.mbom;

import ekp.serviceFacade.rmi.mbom.PartCreateObjRemote;
import ekp.serviceFacade.rmi.mbom.PartRemote;

public class MbomFO {
	// -------------------------------------------------------------------------------
	// -------------------------------------Part--------------------------------------
	public static PartInfo parsePart(PartRemote _remote) {
		PartInfoDto dto = new PartInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setPin(_remote.getPin());
		dto.setName(_remote.getName());
		return dto;
	}
	
	public static PartCreateObjRemote parsePartCreateObjRemote(PartCreateObj _dto) {
		PartCreateObjRemote remote = new PartCreateObjRemote();
		remote.setPin(_dto.getPin());
		remote.setName(_dto.getName());
		return remote;
	}
}
