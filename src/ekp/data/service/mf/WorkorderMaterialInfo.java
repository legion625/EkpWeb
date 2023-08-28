package ekp.data.service.mf;

import legion.ObjectModelInfo;

public interface WorkorderMaterialInfo extends ObjectModelInfo{

	String getWoUid();

	String getWoNo();

	String getMmUid();

	String getMmMano();

	String getMmName();

	double getQty0();

	double getQty1();

}