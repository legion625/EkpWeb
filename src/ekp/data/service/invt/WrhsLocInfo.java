package ekp.data.service.invt;

import java.util.List;

import legion.ObjectModelInfo;

public interface WrhsLocInfo extends ObjectModelInfo {

	String getId();

	String getName();
	
	// -------------------------------------------------------------------------------
	List<WrhsBinInfo> getWrhsBinList(boolean _reload);
	
	default List<WrhsBinInfo> getWrhsBinList(){
		return getWrhsBinList(false);
	}

}