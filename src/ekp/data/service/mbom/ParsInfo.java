package ekp.data.service.mbom;

import java.util.List;

import legion.ObjectModelInfo;

public interface ParsInfo extends ObjectModelInfo {
	String getPartAcqUid();

	String getSeq();

	String getName();

	String getDesp();
	
	// -------------------------------------------------------------------------------
	PartAcqInfo getPa();
	
	// -------------------------------------------------------------------------------
	List<PprocInfo> getPprocList(boolean _reload);
	default List<PprocInfo> getPprocList(){
		return getPprocList(false);
	}
	List<PpartInfo> getPpartList(boolean _reload);
	default List<PpartInfo> getPpartList(){
		return getPpartList(false);
	}

}