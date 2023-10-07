package ekp.web.control.zk.common;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import ekp.web.control.zk.pu.PuSearchConfig;
import ekp.web.control.zk.sd.SoSearchConfig;
import ekp.web.control.zk.wo.WoSearchConfig;
import legion.util.LogUtil;
import legion.util.query.QueryOperation;

public enum SearchConfigType {
	PU(PuSearchConfig.class), //
	SO(SoSearchConfig.class), //
	WO(WoSearchConfig.class), //
	
	
	;
	

	// -------------------------------------------------------------------------------
//	private Supplier<SearchConfig> fnGetSearchConfig;
	private Class searchConfigClass;

	// -------------------------------------------------------------------------------
//	private SearchConfigType(Supplier<SearchConfig> fnGetSearchConfig) {
//		this.fnGetSearchConfig = fnGetSearchConfig;
//	}

	private SearchConfigType(Class searchConfigClass) {
		this.searchConfigClass = searchConfigClass;
	}
	
	// -------------------------------------------------------------------------------
	public SearchConfig getSearchConfigInstance() {
//		ClassLoader.
		try {
			return (SearchConfig) searchConfigClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			LogUtil.log(e);
			return null;
		}
		
//		return fnGetSearchConfig.get();
	}

	
	
}
