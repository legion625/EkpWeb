package ekp.data.service.invt;

import ekp.invt.type.MaterialInstAcqChannel;
import legion.ObjectModelInfo;

public interface MaterialInstInfo extends ObjectModelInfo{

	String getMmUid();

	String getMisn();

	MaterialInstAcqChannel getMiac();

	double getQty();

	double getValue();

	long getEffDate();

	long getExpDate();

}