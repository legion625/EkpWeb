package ekp.invt.bpu.wrhsLoc;

import ekp.data.InvtDataService;
import ekp.data.service.invt.WrhsBinCreateObj;
import ekp.data.service.invt.WrhsBinInfo;
import ekp.data.service.invt.WrhsLocInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class WrhsBinBuilder extends Bpu<WrhsBinInfo>{
	private static InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);
	
	/* base */
	private String wlUid;
	private String id;
	private String name;

	/* data */
	// none
	
	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected WrhsBinBuilder appendWlUid(String wlUid) {
		this.wlUid = wlUid;
		return this;
	}
	
	protected WrhsBinBuilder appendId(String id) {
		this.id = id;
		return this;
	}
	
	protected WrhsBinBuilder appendName(String name) {
		this.name = name;
		return this;
	}
	
	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getWlUid() {
		return wlUid;
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	// -------------------------------------------------------------------------------
	private WrhsBinCreateObj packWrhsBinCreateObj() {
		WrhsBinCreateObj dto = new WrhsBinCreateObj();
		dto.setWlUid(getWlUid());
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

		if(DataFO.isEmptyString(getId())) {
			_msg.append("ID should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		
		if(DataFO.isEmptyString(getName())) {
			_msg.append("Name should NOT be empty.").append(System.lineSeparator());
			v = false;
		}
		
		if (invtDataService.loadWrhsBin(getWlUid(), getId()) != null) {
			_msg.append("Duplicated ID.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	@Override
	protected WrhsBinInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		//
		WrhsBinInfo wb = invtDataService.createWrhsBin(packWrhsBinCreateObj());
		if (wb == null) {
			tt.travel();
			log.error("invtDataService.createWrhsBin return null.");
			return null;
		}
		tt.addSite("revert createWrhsBin", () -> invtDataService.deleteWrhsBin(wb.getUid()));
		log.info("invtDataService.createWrhsBin [{}][{}][{}][{}]", wb.getUid(), wb.getWlUid(), wb.getId(),
				wb.getName());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return wb;
	}

}
