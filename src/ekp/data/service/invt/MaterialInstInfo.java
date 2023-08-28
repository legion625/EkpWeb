package ekp.data.service.invt;

import ekp.invt.type.MaterialInstAcqChannel;
import ekp.invt.type.MaterialInstSrcStatus;
import legion.ObjectModelInfo;

public interface MaterialInstInfo extends ObjectModelInfo {

	String getMmUid();

	String getMisn();

	MaterialInstAcqChannel getMiac();

	String getMiacSrcNo();

	double getQty();

	double getValue();

	long getEffDate();

	long getExpDate();

	public MaterialInstSrcStatus getSrcStatus();

	// -------------------------------------------------------------------------------
	default public String getMiacName() {
		return (getMiac() == null ? MaterialInstAcqChannel.UNDEFINED : getMiac()).getName();
	}

}