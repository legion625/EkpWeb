package ekp.sd;

import ekp.data.SdDataService;
import ekp.data.service.sd.BizPartnerCreateObj;
import ekp.data.service.sd.BizPartnerInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public class BizPartnerBuilder extends Bpu<BizPartnerInfo> {

	private static SdDataService sdDataService = DataServiceFactory.getInstance().getService(SdDataService.class);
	
	/* base */
	// none
	
	/* data */
	private String name;
	private String ban;

	@Override
	protected Bpu<BizPartnerInfo> appendBase() {
		/* base */
		// none
		return this;
	}
	
	// -------------------------------------------------------------------------------
	public BizPartnerBuilder appendName(String name) {
		this.name = name;
		return this;
	}

	public BizPartnerBuilder appendBan(String ban) {
		this.ban = ban;
		return this;
	}

	// -------------------------------------------------------------------------------
	public String getName() {
		return name;
	}

	public String getBan() {
		return ban;
	}

	// -------------------------------------------------------------------------------
	private BizPartnerCreateObj parseBizPartnerCreateObj() {
		BizPartnerCreateObj dto = new BizPartnerCreateObj();
		dto.setName(getName());
		dto.setBan(getBan());
		return dto;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean result = true;
		
		if(DataFO.isEmptyString(getName())) {
			_msg.append("名稱未填。").append(System.lineSeparator());
			result = false;
		}
		
		if(DataFO.isEmptyString(getBan())) {
			_msg.append("統編未填。").append(System.lineSeparator());
			result = false;
		}
		
		return result;
	}

	@Override
	protected BizPartnerInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		BizPartnerInfo bp = sdDataService.createBizPartner(parseBizPartnerCreateObj());
		if (bp == null) {
			log.error("createBizPartner return null.");
			tt.travel();
			return null;
		}
		tt.addSite("revert createBizPartner", () -> sdDataService.deleteBizPartner(bp.getUid()));

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return bp;
	}

}
