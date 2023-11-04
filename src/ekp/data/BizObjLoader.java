package ekp.data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import ekp.data.service.invt.InvtOrderInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;
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
	private static InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);
	public final static Supplier<BizObjLoader<MaterialMasterInfo>> MM = () -> of(invtDataService::loadMaterialMaster);
	
	// -------------------------------------------------------------------------------
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);
	public final static Supplier<BizObjLoader<PartInfo>> PART = () -> of(mbomDataService::loadPart);
	public final static Supplier<BizObjLoader<PartAcqInfo>> PART_ACQ = () -> of(mbomDataService::loadPartAcquisition);
	public final static Supplier<BizObjLoader<ParsInfo>> PARS = () -> of(mbomDataService::loadPartAcqRoutingStep);
	public final static Supplier<BizObjLoader<PpartInfo>> PPART = () -> of(mbomDataService::loadParsPart);
	
	public final static Supplier<BizObjLoader<PartCfgInfo>> PART_CFG = () -> of(mbomDataService::loadPartCfg);
	
	public final static Supplier<BizObjLoader<ProdInfo>> PROD = () -> of(mbomDataService::loadProd);
	public final static Supplier<BizObjLoader<ProdCtlInfo>> PROD_CTL = () -> of(mbomDataService::loadProdCtl);
	
	public final static Supplier<BizObjLoader<InvtOrderInfo>> IO = ()-> of(invtDataService::loadInvtOrder);
	
	
	
}
