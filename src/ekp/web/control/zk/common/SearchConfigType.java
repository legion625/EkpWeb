package ekp.web.control.zk.common;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import ekp.web.control.zk.invt.io.IoSearchConfig;
import ekp.web.control.zk.mbom.PartCfgSearchConfig;
import ekp.web.control.zk.mbom.PartSearchConfig;
import ekp.web.control.zk.mbom.PpartSkewerSearchConfig;
import ekp.web.control.zk.pu.PuSearchConfig;
import ekp.web.control.zk.sd.SoSearchConfig;
import ekp.web.control.zk.wo.WoSearchConfig;
import legion.util.LogUtil;
import legion.util.query.QueryOperation;

public enum SearchConfigType {
	IO(IoSearchConfig.class), //
	PART(PartSearchConfig.class), //
	PART_CFG(PartCfgSearchConfig.class), //
	PPART_SKEWER(PpartSkewerSearchConfig.class), //
	PU(PuSearchConfig.class), //
	SO(SoSearchConfig.class), //
	WO(WoSearchConfig.class), //
	;
	

	// -------------------------------------------------------------------------------
	private Class searchConfigClass;

	// -------------------------------------------------------------------------------
	private SearchConfigType(Class searchConfigClass) {
		this.searchConfigClass = searchConfigClass;
	}
	
	// -------------------------------------------------------------------------------
	public SearchConfig getSearchConfigInstance() {
		try {
			return (SearchConfig) searchConfigClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			LogUtil.log(e);
			return null;
		}
	}

	
	
}
