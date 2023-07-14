package ekp.invt.bpu.wrhsLoc;

import ekp.data.InvtDataService;
import ekp.data.service.invt.WrhsLocCreateObj;
import ekp.data.service.invt.WrhsLocInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public abstract class WrhsLocBuilder extends Bpu<WrhsLocInfo> {
	private static InvtDataService invtDataService = DataServiceFactory.getInstance().getService(InvtDataService.class);
	
	/* base */
	private String id;
	private String name;

	

	/* data */
	// none
	
	// -------------------------------------------------------------------------------
		// -----------------------------------appender------------------------------------
	protected WrhsLocBuilder appendId(String id) {
		this.id = id;
		return this;
	}

	protected WrhsLocBuilder appendName(String name) {
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
	private WrhsLocCreateObj packWrhsLocCreateObj() {
		WrhsLocCreateObj dto = new WrhsLocCreateObj();
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
	public boolean verify(StringBuilder _msg) {
		boolean v = true;
		
		if(invtDataService.loadWrhsLocById(getId())!=null) {
			_msg.append("Duplicated ID.").append(System.lineSeparator());
			v = false;
		}
		
		return v;
	}
	
	@Override
	protected WrhsLocInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		
		//
		WrhsLocInfo wl = invtDataService.createWrhsLoc(packWrhsLocCreateObj());
		if(wl==null) {
			tt.travel();
			log.error("invtDataService.createWrhsLoc return null.");
			return null;
		}
		tt.addSite("revert createWrhsLoc", ()->invtDataService.deleteWrhsLoc(wl.getUid()));
		log.info("invtDataService.createWrhsLoc [{}][{}][{}]", wl.getUid(), wl.getId(), wl.getName());
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return wl;
	}
	
}
