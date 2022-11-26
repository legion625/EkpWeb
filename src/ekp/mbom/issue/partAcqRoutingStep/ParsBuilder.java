package ekp.mbom.issue.partAcqRoutingStep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.ParsCreateObj;
import ekp.data.service.mbom.ParsInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class ParsBuilder extends Bpu<ParsInfo> {
	protected Logger log = LoggerFactory.getLogger(ParsBuilder.class);
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);

	/* base */
	private String partAcqUid; // ref data key

	private String id; // routing step id
	private String name;
	private String desp;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected ParsBuilder appendPartAcqUid(String partAcqUid) {
		this.partAcqUid = partAcqUid;
		return this;
	}

	protected ParsBuilder appendId(String id) {
		this.id = id;
		return this;
	}

	protected ParsBuilder appendName(String name) {
		this.name = name;
		return this;
	}

	protected ParsBuilder appendDesp(String desp) {
		this.desp = desp;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getPartAcqUid() {
		return partAcqUid;
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
	private ParsCreateObj packPartAcqRoutingStepCreateObj() {
		ParsCreateObj dto = new ParsCreateObj();
		dto.setPartAcqUid(getPartAcqUid());
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

		// partAcqUid
		if (DataFO.isEmptyString(getPartAcqUid())) {
			_msg.append("PartAcqUid should not be empty.").append(System.lineSeparator());
			v = false;
		}

		// id
		if (DataFO.isEmptyString(getId())) {
			_msg.append("Id should not be empty.").append(System.lineSeparator());
			v = false;
		} else {
			if (mbomDataService.loadPartAcqRoutingStep(getPartAcqUid(), getId()) != null) {
				_msg.append("Duplicated id.").append(System.lineSeparator());
				v = false;
			}
		}

		// name
		if (DataFO.isEmptyString(getName())) {
			_msg.append("Name should not be empty.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	@Override
	protected ParsInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		//
		ParsInfo pars = mbomDataService.createPartAcqRoutingStep(packPartAcqRoutingStepCreateObj());
		if (pars == null) {
			tt.travel();
			log.error("mbomDataSerivce.createPartAcqRoutingStep return null.");
			return null;
		}
		tt.addSite("revert createPartAcqRoutingStep", () -> mbomDataService.deletePartAcqRoutingStep(pars.getUid()));
		log.info("mbomDataService.createPartAcqRoutingStep [{}][{}][{}]", pars.getUid(), pars.getPartAcqUid(),
				pars.getId());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return pars;
	}

}
