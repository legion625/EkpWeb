package ekp.mbom.issue.prod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.ProdCreateObj;
import ekp.data.service.mbom.ProdInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class ProdBuilder extends Bpu<ProdInfo> {
	protected Logger log = LoggerFactory.getLogger(ProdBuilder.class);
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);

	/* base */
	private String id;
	private String name;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected ProdBuilder appendId(String id) {
		this.id = id;
		return this;
	}

	protected ProdBuilder appendName(String name) {
		this.name = name;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	// -------------------------------------------------------------------------------
	private ProdCreateObj packProdCreateObj() {
		ProdCreateObj dto = new ProdCreateObj();
		dto.setId(getId());
		dto.setName(getName());
		return dto;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;
		//
		if (DataFO.isEmptyString(getId())) {
			_msg.append("Id should not be empty.").append(System.lineSeparator());
			v = false;
		} else {
			if (mbomDataService.loadProdById(getId()) != null) {
				_msg.append("Duplicated id.").append(System.lineSeparator());
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
	protected ProdInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		//
		ProdInfo p = mbomDataService.createProd(packProdCreateObj());
		if (p == null) {
			tt.travel();
			log.error("mbomDataSerivce.createProd return null.");
			return null;
		}
		tt.addSite("revert createProd", () -> mbomDataService.deleteProd(p.getUid()));
		log.info("mbomDataService.createProd [{}][{}]", p.getUid(), p.getId());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return p;
	}

}
