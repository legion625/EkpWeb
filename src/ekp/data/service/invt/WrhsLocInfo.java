package ekp.data.service.invt;

import java.util.List;

import legion.ObjectModelInfo;

public interface WrhsLocInfo extends ObjectModelInfo {

	String getId();

	String getName();
	
	// -------------------------------------------------------------------------------
	List<WrhsBinInfo> getWrhsBinList(); 

}