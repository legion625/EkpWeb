package ekp.data.service.pu;

import ekp.serviceFacade.rmi.pu.PurchCreateObjRemote;
import ekp.serviceFacade.rmi.pu.PurchItemCreateObjRemote;
import ekp.serviceFacade.rmi.pu.PurchItemRemote;
import ekp.serviceFacade.rmi.pu.PurchRemote;

public class PuFO {

	// -------------------------------------------------------------------------------
	// -------------------------------------Purch-------------------------------------
	public static PurchInfo parsePurch(PurchRemote _remote) {
		PurchInfoDto dto = new PurchInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setPuNo(_remote.getPuNo());
		dto.setTitle(_remote.getTitle());
		dto.setSupplierName(_remote.getSupplierName());
		dto.setSupplierBan(_remote.getSupplierBan());
		dto.setPerfStatus(_remote.getPerfStatus());
		dto.setPerfTime(_remote.getPerfTime());
		return dto;
	}

	public static PurchCreateObjRemote parsePurchCreateObjRemote(PurchCreateObj _dto) {
		PurchCreateObjRemote remote = new PurchCreateObjRemote();
		remote.setPuNo(_dto.getPuNo());
		remote.setTitle(_dto.getTitle());
		remote.setSupplierName(_dto.getSupplierName());
		remote.setSupplierBan(_dto.getSupplierBan());
		return remote;
	}
	
	// -------------------------------------------------------------------------------
	// -----------------------------------PurchItem-----------------------------------
	public static PurchItemInfo parsePurchItem(PurchItemRemote _remote) {
		PurchItemInfoDto dto = new PurchItemInfoDto(_remote.getUid(), _remote.getObjectCreateTime(),
				_remote.getObjectUpdateTime());
		dto.setPurchUid(_remote.getPurchUid());
		dto.setMmUid(_remote.getMmUid());
		dto.setMmMano(_remote.getMmMano());
		dto.setMmName(_remote.getMmName());
		dto.setMmSpecification(_remote.getMmSpecification());
		dto.setMmStdUnit(_remote.getMmStdUnit());
		dto.setRefPa(_remote.isRefPa());
		dto.setRefPaUid(_remote.getRefPaUid());
		dto.setRefPaType(_remote.getRefPaType());
		dto.setQty(_remote.getQty());
		dto.setValue(_remote.getValue());
		dto.setRemark(_remote.getRemark());
		return dto;
	}
	
	public static PurchItemCreateObjRemote parsePurchItemCreateObjRemote(PurchItemCreateObj _dto) {
		PurchItemCreateObjRemote remote = new PurchItemCreateObjRemote();
		remote.setPurchUid(_dto.getPurchUid());
		remote.setMmUid(_dto.getMmUid());
		remote.setMmMano(_dto.getMmMano());
		remote.setMmName(_dto.getMmName());
		remote.setMmSpecification(_dto.getMmSpecification());
		remote.setMmStdUnit(_dto.getMmStdUnit());
		remote.setRefPa(_dto.isRefPa());
		remote.setRefPaUid(_dto.getRefPaUid());
		remote.setRefPaType(_dto.getRefPaType());
		remote.setQty(_dto.getQty());
		remote.setValue(_dto.getValue());
		remote.setRemark(_dto.getRemark());
		return remote;
	}
}
