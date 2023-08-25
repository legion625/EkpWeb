package ekp.data.service.pu;

import ekp.mbom.type.PartUnit;
import legion.ObjectModelInfo;

public interface PurchItemInfo extends ObjectModelInfo {

	String getPurchUid();

	String getMmUid();

	String getMmMano();

	String getMmName();

	String getMmSpecification();

	PartUnit getMmStdUnit();

	double getQty();

	double getValue();

	String getRemark();
	
	// -------------------------------------------------------------------------------
	PurchInfo getPurch();

}