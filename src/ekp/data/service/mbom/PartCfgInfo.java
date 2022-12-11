package ekp.data.service.mbom;

import legion.ObjectModelInfo;

import java.util.List;

import ekp.mbom.type.PartCfgStatus;

public interface PartCfgInfo extends ObjectModelInfo {

	String getRootPartUid();

	String getRootPartPin();

	PartCfgStatus getStatus();

	String getId();

	String getName();

	String getDesp();
	
	long getPublishTime();

	// -------------------------------------------------------------------------------
	default String getStatusName() {
		return (getStatus() == null ? PartCfgStatus.UNDEFINED : getStatus()).getName();
	}

	// -------------------------------------------------------------------------------
	PartCfgInfo reload();
	
	PartInfo getRootPart();
	
	List<PpartSkewer> getPpartSkewerList(boolean _reload);

}