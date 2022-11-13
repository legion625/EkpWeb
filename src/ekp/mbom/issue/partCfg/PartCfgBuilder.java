package ekp.mbom.issue.partCfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.PartCfgCreateObj;
import ekp.data.service.mbom.PartCfgInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class PartCfgBuilder extends Bpu<PartCfgInfo> {
	protected Logger log = LoggerFactory.getLogger(PartCfgBuilder.class);
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);

	/* base */
	private String rootPartUid; // ref data key
	private String rootPartPin; // ref biz key

	private String id; // biz key
	private String name;
	private String desp;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected PartCfgBuilder appendRootPartUid(String rootPartUid) {
		this.rootPartUid = rootPartUid;
		return this;
	}

	protected PartCfgBuilder appendRootPartPin(String rootPartPin) {
		this.rootPartPin = rootPartPin;
		return this;
	}

	protected PartCfgBuilder appendId(String id) {
		this.id = id;
		return this;
	}

	protected PartCfgBuilder appendName(String name) {
		this.name = name;
		return this;
	}

	protected PartCfgBuilder appendDesp(String desp) {
		this.desp = desp;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getRootPartUid() {
		return rootPartUid;
	}

	public String getRootPartPin() {
		return rootPartPin;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDesp() {
		return desp;
	}
	
	// -------------------------------------------------------------------------------
	private PartCfgCreateObj packPartCfgCreateObj() {
		PartCfgCreateObj dto = new PartCfgCreateObj();
		dto.setRootPartUid(getRootPartUid());
		dto.setRootPartPin(getRootPartPin());
		dto.setId(getId());
		dto.setName(getName());
		dto.setDesp(getDesp());
		return dto;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = true;
		
		// rootPartUid
		if(DataFO.isEmptyString(getRootPartUid())) {
			_msg.append("RootPartUid should not be empty.").append(System.lineSeparator());
			v = false;
		}
		
		// rootPartPin
		if (DataFO.isEmptyString(getRootPartPin())) {
			_msg.append("RootPartPin should not be empty.").append(System.lineSeparator());
			v = false;
		}
		
		//
		if (DataFO.isEmptyString(getId())) {
			_msg.append("Id should not be empty.").append(System.lineSeparator());
			v = false;
		} else {
//			if (mbomDataService.loadPartCfgById(getId()) != null) { // FIXME
//				_msg.append("Duplicated id.").append(System.lineSeparator());
//				v = false;
//			}
		}

		if (DataFO.isEmptyString(getName())) {
			_msg.append("Name should not be empty.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}
	
	@Override
	protected PartCfgInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		
		// 1. createPartCfg
		PartCfgInfo pc = mbomDataService.createPartCfg(packPartCfgCreateObj());
		if(pc==null) {
			tt.travel();
			log.error("mbomDataSerivce.createPartCfg return null.");
			return null;
		}
		tt.addSite("revert createPartCfg", () -> mbomDataService.deletePartCfg(pc.getUid()));
		log.info("mbomDataService.createPartCfg [{}][{}][{}][{}]", pc.getUid(), pc.getRootPartUid(),
				pc.getRootPartPin(), pc.getId());

		// 2. startEditing
		if (!mbomDataService.partCfgStartEditing(pc.getUid())) {
			tt.travel();
			log.error("mbomDataSerivce.partCfgStartEditing return false.");
			return null;
		}
		tt.addSite("revert partCfgStartEditing", () -> mbomDataService.partCfgRevertStartEditing(pc.getUid()));
		log.info("mbomDataService.partCfgStartEditing [{}]", pc.getUid());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return pc.reload();
	}
	
	

}
