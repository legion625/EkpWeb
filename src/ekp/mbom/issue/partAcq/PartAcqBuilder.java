package ekp.mbom.issue.partAcq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.PartAcquisitionCreateObj;
import ekp.data.service.mbom.PartAcquisitionInfo;
import ekp.mbom.type.PartAcquisitionType;
import legion.DataServiceFactory;
import legion.biz.BizObjBuilder;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class PartAcqBuilder extends BizObjBuilder<PartAcquisitionInfo> {
	protected Logger log = LoggerFactory.getLogger(PartAcqBuilder.class);
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);

	/* base */
	private String partUid; // ref data key
	private String partPin; // ref biz key

	private String id; // biz key
	private String name;
	private PartAcquisitionType type;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected PartAcqBuilder appendPartUid(String partUid) {
		this.partUid = partUid;
		return this;
	}

	protected PartAcqBuilder appendPartPin(String partPin) {
		this.partPin = partPin;
		return this;
	}

	protected PartAcqBuilder appendId(String id) {
		this.id = id;
		return this;
	}

	protected PartAcqBuilder appendName(String name) {
		this.name = name;
		return this;
	}

	protected PartAcqBuilder appendType(PartAcquisitionType type) {
		this.type = type;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getPartUid() {
		return partUid;
	}

	public String getPartPin() {
		return partPin;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public PartAcquisitionType getType() {
		return type;
	}

	// -------------------------------------------------------------------------------
	private PartAcquisitionCreateObj packPartAcquisitionCreateObj() {
		PartAcquisitionCreateObj dto = new PartAcquisitionCreateObj();
		dto.setPartUid(getPartUid());
		dto.setPartPin(getPartPin());
		dto.setId(getId());
		dto.setName(getName());
		dto.setType(getType());
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

		// partUid
		if(DataFO.isEmptyString(getPartUid())) {
			_msg.append("PartUid should not be empty.").append(System.lineSeparator());
			v = false;
		}

		// partPin
		if (DataFO.isEmptyString(getPartPin())) {
			_msg.append("PartPin should not be empty.").append(System.lineSeparator());
			v = false;
		}


		// id
		if (DataFO.isEmptyString(getId())) {
			_msg.append("Id should not be empty.").append(System.lineSeparator());
			v = false;
		} else {
//			if (mbomDataService.loadPartAcquisition(getPartPin(), getId()) != null) { // FIXME
//				_msg.append("Duplicated id.").append(System.lineSeparator());
//				v = false;
//			}
		}

		// name
		if (DataFO.isEmptyString(getName())) {
			_msg.append("Name should not be empty.").append(System.lineSeparator());
			v = false;
		}

		// type
		if (getType() == null || PartAcquisitionType.UNDEFINED == getType()) {
			_msg.append("Type error.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}


	@Override
	protected PartAcquisitionInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		//
		PartAcquisitionInfo pa = mbomDataService.createPartAcquisition(packPartAcquisitionCreateObj());
		if (pa == null) {
			tt.travel();
			log.error("mbomDataSerivce.createPartAcquisition return null.");
			return null;
		}
		tt.addSite("revert createPartAcquisition", () -> mbomDataService.deletePartAcquisition(pa.getUid()));
		log.info("mbomDataService.createPartAcquisition [{}][{}][{}][{}]", pa.getUid(), pa.getPartUid(),
				pa.getPartPin(), pa.getId());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return pa;
	}



}
