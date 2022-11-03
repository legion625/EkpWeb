package ekp.data.service.mbom;

import ekp.mbom.type.PartCfgStatus;
import legion.ObjectModelInfo;

public interface PartCfgInfo extends ObjectModelInfo {

	String getRootPartUid();

	String getRootPartPin();

	PartCfgStatus getStatus();

	String getId();

	String getName();

	String getDesp();

}