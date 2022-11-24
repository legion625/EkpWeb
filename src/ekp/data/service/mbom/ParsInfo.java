package ekp.data.service.mbom;

import java.util.List;

import legion.ObjectModelInfo;

public interface ParsInfo extends ObjectModelInfo {
	String getPartAcqUid();

	String getId();

	String getName();

	String getDesp();
	
	// -------------------------------------------------------------------------------
	List<PprocInfo> getPprocList();
	List<PpartInfo> getPpartList();

}