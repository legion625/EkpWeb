package ekp.mbom.issue.parsProc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.ParsProcCreateObj;
import ekp.data.service.mbom.ParsProcInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class ParsProcBuilder extends Bpu<ParsProcInfo> {
	protected Logger log = LoggerFactory.getLogger(ParsProcBuilder.class);
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);

	/* base */
	private String parsUid; // ref data key

	//
	private String seq; //
	private String name;
	private String desp;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected ParsProcBuilder appendParsUid(String parsUid) {
		this.parsUid = parsUid;
		return this;
	}

	protected ParsProcBuilder appendSeq(String seq) {
		this.seq = seq;
		return this;
	}

	protected ParsProcBuilder appendName(String name) {
		this.name = name;
		return this;
	}

	protected ParsProcBuilder appendDesp(String desp) {
		this.desp = desp;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getParsUid() {
		return parsUid;
	}

	public String getSeq() {
		return seq;
	}

	public String getName() {
		return name;
	}

	public String getDesp() {
		return desp;
	}

	// -------------------------------------------------------------------------------
	private ParsProcCreateObj packParsProcCreateObj() {
		ParsProcCreateObj dto = new ParsProcCreateObj();
		dto.setParsUid(getParsUid());
		dto.setSeq(getSeq());
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

		// parsUid
		if (DataFO.isEmptyString(getParsUid())) {
			_msg.append("ParsUid should not be empty.").append(System.lineSeparator());
			v = false;
		}

		// seq
		if (DataFO.isEmptyString(getSeq())) {
			_msg.append("Seq should not be empty.").append(System.lineSeparator());
			v = false;
		}

		if (DataFO.isEmptyString(getName())) {
			_msg.append("Name should not be empty.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}
	
	@Override
	protected ParsProcInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		//
		ParsProcInfo parsProc = mbomDataService.createParsProc(packParsProcCreateObj());
		if (parsProc == null) {
			tt.travel();
			log.error("mbomDataSerivce.createParsProc return null.");
			return null;
		}
		tt.addSite("revert createParsProc", () -> mbomDataService.deleteParsProc(parsProc.getUid()));
		log.info("mbomDataService.createParsProc [{}][{}][{}][{}]", parsProc.getUid(), parsProc.getParsUid(),
				parsProc.getSeq(), parsProc.getName());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return parsProc;
	}
}
