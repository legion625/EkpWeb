package ekp.data.service.mbom;

import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;
import legion.util.query.QueryOperation;
import legion.util.query.QueryOperation.CompareOp;
import legion.util.query.QueryOperation.QueryValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ekp.data.BizObjLoader;
import ekp.data.MbomDataService;
import ekp.data.service.mbom.query.PartCfgQueryParam;
import ekp.data.service.mbom.query.PpartSkewerQueryParam;
import ekp.mbom.type.PartCfgStatus;

public class PartCfgInfoDto extends ObjectModelInfoDto implements PartCfgInfo {

	protected PartCfgInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String rootPartUid; // ref data key
	private String rootPartPin; // ref biz key

	private PartCfgStatus status;

	private String id; // biz key
	private String name;
	private String desp;
	
	private long publishTime;

	@Override
	public String getRootPartUid() {
		return rootPartUid;
	}

	void setRootPartUid(String rootPartUid) {
		this.rootPartUid = rootPartUid;
	}

	@Override
	public String getRootPartPin() {
		return rootPartPin;
	}

	void setRootPartPin(String rootPartPin) {
		this.rootPartPin = rootPartPin;
	}

	@Override
	public PartCfgStatus getStatus() {
		return status;
	}

	void setStatus(PartCfgStatus status) {
		this.status = status;
	}

	@Override
	public String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDesp() {
		return desp;
	}

	void setDesp(String desp) {
		this.desp = desp;
	}
	
	@Override
	public long getPublishTime() {
		return publishTime;
	}

	void setPublishTime(long publishTime) {
		this.publishTime = publishTime;
	}

	
	// -------------------------------------------------------------------------------
	@Override
	public PartCfgInfo reload() {
		return DataServiceFactory.getInstance().getService(MbomDataService.class).loadPartCfg(this.getUid());
	}

	private BizObjLoader<PartInfo> partLoader = BizObjLoader.PART.get();

	@Override
	public PartInfo getRootPart() {
		return partLoader.getObj(getRootPartUid());
	}

	private BizObjLoader<List<PpartSkewer>> ppartSkewerListLoader = BizObjLoader.of(() -> {
		QueryOperation<PpartSkewerQueryParam, PpartSkewer> param = new QueryOperation<>();
		Map<PpartSkewerQueryParam, QueryValue[]> existsQvMap = new HashMap<>();

		param.appendCondition(QueryOperation.value(PpartSkewerQueryParam.B_OF_PC$_PA_EXISTS, CompareOp.equal, true));
		existsQvMap.put(PpartSkewerQueryParam.B_OF_PC$_PA_EXISTS, new QueryValue[] { //
				QueryOperation.value(PartCfgQueryParam.ID, CompareOp.equal, getId()), //
		});
		param = DataServiceFactory.getInstance().getService(MbomDataService.class).searchPpartSkewer(param,
				existsQvMap);
		return param.getQueryResult();
	});

	@Override
	public List<PpartSkewer> getPpartSkewerList(boolean _reload) {
		return ppartSkewerListLoader.getObj(_reload);
	}
}
