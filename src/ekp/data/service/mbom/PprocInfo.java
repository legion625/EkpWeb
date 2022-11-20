package ekp.data.service.mbom;

import legion.ObjectModelInfo;

public interface PprocInfo extends ObjectModelInfo{

	String getParsUid();

	String getSeq();

	String getName();

	String getDesp();

	boolean isAssignProc();

	String getProcUid();

	String getProcId();

}