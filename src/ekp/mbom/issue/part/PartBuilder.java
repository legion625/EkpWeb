package ekp.mbom.issue.part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.PartCreateObj;
import ekp.data.service.mbom.PartInfo;
import legion.DataServiceFactory;
import legion.biz.BizObjBuilder;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class PartBuilder extends BizObjBuilder<PartInfo> {
	protected Logger log = LoggerFactory.getLogger(PartBuilder.class);
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);

	/* base */
	private String pin;
	private String name;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected PartBuilder appendPin(String pin) {
		this.pin = pin;
		return this;
	}

	protected PartBuilder appendName(String name) {
		this.name = name;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getPin() {
		return pin;
	}

	public String getName() {
		return name;
	}
	
	// -------------------------------------------------------------------------------
	private PartCreateObj packPartCreateObj() {
		PartCreateObj dto = new PartCreateObj();
		dto.setPin(getPin());
		dto.setName(getName());
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
		//
		if (DataFO.isEmptyString(getPin())) {
			_msg.append("Pin should not be empty.").append(System.lineSeparator());
			v = false;
		} else {
			if (mbomDataService.loadPartByPin(getPin()) != null) {
				_msg.append("Duplicated pin.").append(System.lineSeparator());
				v = false;
			}
		}

		if (DataFO.isEmptyString(getName())) {
			_msg.append("Name should not be empty.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}


	@Override
	protected PartInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		//
		PartInfo p = mbomDataService.createPart(packPartCreateObj());
		if (p == null) {
			tt.travel();
			log.error("mbomDataSerivce.createPart return null.");
			return null;
		}
		tt.addSite("revert createPart", () -> mbomDataService.deletePart(p.getUid()));
		log.info("mbomDataService.createPart [{}][{}]", p.getUid(), p.getPin());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return p;
	}

}
