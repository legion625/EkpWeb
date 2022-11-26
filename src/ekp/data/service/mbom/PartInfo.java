package ekp.data.service.mbom;

import java.util.List;

import legion.ObjectModelInfo;

public interface PartInfo extends ObjectModelInfo{

	String getPin();

	String getName();

	// -------------------------------------------------------------------------------
	PartInfo reload();
	
	List<PartAcqInfo> getPaList(boolean _reload);
	
	List<PpartInfo> getPpartList(boolean _reload);
	
	List<PartCfgInfo> getPartCfgList(boolean _reload);
}