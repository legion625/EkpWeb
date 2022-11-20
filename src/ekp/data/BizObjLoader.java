package ekp.data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import ekp.data.service.mbom.PartCfgInfo;
import legion.DataServiceFactory;

public class BizObjLoader<T> {

	private Function<String, T> fnLoad;
	private Map<String, T> map;

	private BizObjLoader(Function<String, T> fnLoad) {
		this.fnLoad = fnLoad;
		this.map = new HashMap<>();
	}

	public static <T> BizObjLoader<T> of(Function<String, T> _fnLoad) {
		return new BizObjLoader<>(_fnLoad);
	}
	
	// -------------------------------------------------------------------------------
	public final T getObj(String _key) {
		return getObj(_key, false);
	}

	public final T getObj(String _key, boolean _reload) {
		if (!map.containsKey(_key) || _reload)
			map.put(_key, fnLoad.apply(_key));
		return map.get(_key);
	}
	
	// -------------------------------------------------------------------------------
	private final static String DUMMY_KEY = "dummy_key";
	
	public static <T> BizObjLoader<T> of(Supplier<T> _splrLoad) {
		return new BizObjLoader<>(key -> _splrLoad.get());
	}
	
	public final T getObj() {
		return getObj(false);
	}

	public final T getObj(boolean _reload) {
		if (!map.containsKey(DUMMY_KEY) || _reload)
			map.put(DUMMY_KEY, fnLoad.apply(DUMMY_KEY));
		return map.get(DUMMY_KEY);
	}
	
	// -------------------------------------------------------------------------------
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);
	public final static Supplier<BizObjLoader<PartCfgInfo>> PART_CFG = () -> of(mbomDataService::loadPartCfg);
	
}