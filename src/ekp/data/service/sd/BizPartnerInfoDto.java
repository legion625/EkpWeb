package ekp.data.service.sd;

import legion.ObjectModelInfoDto;

public class BizPartnerInfoDto extends ObjectModelInfoDto implements BizPartnerInfo {
	protected BizPartnerInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String bpsn;
	private String name;
	private String ban;

	@Override
	public String getBpsn() {
		return bpsn;
	}

	void setBpsn(String bpsn) {
		this.bpsn = bpsn;
	}

	@Override
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Override
	public String getBan() {
		return ban;
	}

	void setBan(String ban) {
		this.ban = ban;
	}
}
