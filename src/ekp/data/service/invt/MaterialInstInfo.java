package ekp.data.service.invt;

import ekp.invt.type.MaterialInstAcqChannel;
import legion.ObjectModelInfo;

public interface MaterialInstInfo extends ObjectModelInfo{

	String getMmUid();

	String getMisn();

	MaterialInstAcqChannel getMiac();
	
	String getMiacSrcNo();

	double getQty();

	double getValue();

	long getEffDate();

	long getExpDate();
	
	// -------------------------------------------------------------------------------
	default public String getMiacName() {
		return (getMiac()==null?MaterialInstAcqChannel.UNDEFINED:getMiac()).getName();
	}

}