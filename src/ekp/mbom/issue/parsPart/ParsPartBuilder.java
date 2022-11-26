package ekp.mbom.issue.parsPart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.PpartInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class ParsPartBuilder extends Bpu<PpartInfo> {
	protected Logger log = LoggerFactory.getLogger(ParsPartBuilder.class);
	protected static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);

	/* base */
	private String parsUid; // ref data key

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected ParsPartBuilder appendParsUid(String parsUid) {
		this.parsUid = parsUid;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getParsUid() {
		return parsUid;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = true;

		// parsUid
		if (DataFO.isEmptyString(getParsUid())) {
			_msg.append("ParsUid should not be empty.").append(System.lineSeparator());
			v = false;
		}
		return v;
	}


	@Override
	protected abstract PpartInfo buildProcess(TimeTraveler _tt);
	
//	@Override
//	protected ParsPartInfo buildProcess(TimeTraveler _tt) {
	protected PpartInfo buildPpart(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		PpartInfo parsPart = mbomDataService.createParsPart(getParsUid());
		if (parsPart == null) {
			tt.travel();
			log.error("mbomDataSerivce.createParsPart return null.");
			return null;
		}
		tt.addSite("revert createParsPart", () -> mbomDataService.deleteParsPart(parsPart.getUid()));
		log.info("mbomDataService.createParsPart [{}][{}]", parsPart.getUid(), parsPart.getParsUid());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return parsPart;
	}

}
