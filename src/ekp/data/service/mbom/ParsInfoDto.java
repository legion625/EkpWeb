package ekp.data.service.mbom;

import java.util.List;

import ekp.data.BizObjLoader;
import ekp.data.MbomDataService;
import legion.DataServiceFactory;
import legion.ObjectModelInfoDto;

public class ParsInfoDto extends ObjectModelInfoDto implements ParsInfo {

	protected ParsInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------attribute-----------------------------------
	private String partAcqUid; // ref data key

	private String seq; // routing step id
	private String name;
	private String desp;

	// -------------------------------------------------------------------------------
	// ---------------------------------getter&setter---------------------------------
	@Override
	public String getPartAcqUid() {
		return partAcqUid;
	}

	void setPartAcqUid(String partAcqUid) {
		this.partAcqUid = partAcqUid;
	}

	@Override
	public String getSeq() {
		return seq;
	}

	void setSeq(String seq) {
		this.seq = seq;
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
	
	// -------------------------------------------------------------------------------
	private BizObjLoader<PartAcqInfo> paLoader = BizObjLoader.PART_ACQ.get();

	@Override
	public PartAcqInfo getPa() {
		return paLoader.getObj(getPartAcqUid());
	}

	// -------------------------------------------------------------------------------
	private BizObjLoader<List<PprocInfo>> pprocListLoader = BizObjLoader.of(
			() -> DataServiceFactory.getInstance().getService(MbomDataService.class).loadParsProcList(getUid()));
	
	@Override
	public List<PprocInfo> getPprocList(boolean _reload){
		return pprocListLoader.getObj(_reload);
	}
	
	private BizObjLoader<List<PpartInfo>> ppartListLoader = BizObjLoader.of(
			() -> DataServiceFactory.getInstance().getService(MbomDataService.class).loadParsPartList(getUid()));
	
	@Override
	public List<PpartInfo> getPpartList(boolean _reload){
		return ppartListLoader.getObj(_reload);
	}

}
