package ekp.mbom.issue.prodCtl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.ProdCtlCreateObj;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class ProdCtlBuilder extends Bpu<ProdCtlInfo> {
	protected Logger log = LoggerFactory.getLogger(ProdCtlBuilder.class);
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);

	/* base */
	private String id; // 型號 biz key
	private int lv; // 1:系統;2:次系統;3:模組 預設先展到第3階
	private String name; // 名稱
	private boolean req; // 是否為必要的

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected ProdCtlBuilder appendId(String id) {
		this.id = id;
		return this;
	}

	protected ProdCtlBuilder appendLv(int lv) {
		this.lv = lv;
		return this;
	}

	protected ProdCtlBuilder appendName(String name) {
		this.name = name;
		return this;
	}

	protected ProdCtlBuilder appendReq(boolean req) {
		this.req = req;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getId() {
		return id;
	}

	public int getLv() {
		return lv;
	}

	public String getName() {
		return name;
	}

	public boolean isReq() {
		return req;
	}

	// -------------------------------------------------------------------------------
	private ProdCtlCreateObj packProdCtlCreateObj() {
		ProdCtlCreateObj dto = new ProdCtlCreateObj();
		dto.setId(getId());
		dto.setLv(getLv());
		dto.setName(getName());
		dto.setReq(isReq());
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
		if (DataFO.isEmptyString(getId())) {
			_msg.append("Id should not be empty.").append(System.lineSeparator());
			v = false;
		} else {
//			if (mbomDataService.loadProdById(getId()) != null) { // FIXME
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
	protected ProdCtlInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		//
		ProdCtlInfo pc = mbomDataService.createProdCtl(packProdCtlCreateObj());
		if (pc == null) {
			tt.travel();
			log.error("mbomDataSerivce.createProdCtl return null.");
			return null;
		}
		tt.addSite("revert createProdCtl", () -> mbomDataService.deleteProdCtl(pc.getUid()));
		log.info("mbomDataService.createProdCtl [{}][{}]", pc.getUid(), pc.getId());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return pc;
	}
}
