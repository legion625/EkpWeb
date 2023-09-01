package ekp.mf.bpu;

import ekp.data.MfDataService;
import ekp.data.service.mf.WorkorderMaterialCreateObj;
import ekp.data.service.mf.WorkorderMaterialInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class WomBuilder extends Bpu<WorkorderMaterialInfo> {
	protected static MfDataService mfDataService = DataServiceFactory.getInstance().getService(MfDataService.class);

	/* base */
	private String woUid;
	private String woNo;

	// 料件基本檔
	private String mmUid;
	private String mmMano;
	private String mmName;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected WomBuilder appendWoUid(String woUid) {
		this.woUid = woUid;
		return this;
	}

	protected WomBuilder appendWoNo(String woNo) {
		this.woNo = woNo;
		return this;
	}

	protected WomBuilder appendMmUid(String mmUid) {
		this.mmUid = mmUid;
		return this;
	}

	protected WomBuilder appendMmMano(String mmMano) {
		this.mmMano = mmMano;
		return this;
	}

	protected WomBuilder appendMmName(String mmName) {
		this.mmName = mmName;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getWoUid() {
		return woUid;
	}

	public String getWoNo() {
		return woNo;
	}

	public String getMmUid() {
		return mmUid;
	}

	public String getMmMano() {
		return mmMano;
	}

	public String getMmName() {
		return mmName;
	}

	protected WorkorderMaterialCreateObj packWorkorderMaterialCreateObj() {
		WorkorderMaterialCreateObj dto = new WorkorderMaterialCreateObj();
		dto.setWoUid(getWoUid());
		dto.setWoNo(getWoNo());
		dto.setMmUid(getMmUid());
		dto.setMmMano(getMmMano());
		dto.setMmName(getMmName());
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

//		if (DataFO.isEmptyString(getWoUid())) {
//			_msg.append("WoUid should NOT be empty.").append(System.lineSeparator());
//			v = false;
//		}
//
//		if (DataFO.isEmptyString(getWoNo())) {
//			_msg.append("WoNo should NOT be empty.").append(System.lineSeparator());
//			v = false;
//		}

		if (DataFO.isEmptyString(getMmUid())) {
			_msg.append("MmUid should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		if (DataFO.isEmptyString(getMmMano())) {
			_msg.append("MmMano should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		if (DataFO.isEmptyString(getMmName())) {
			_msg.append("MmName should NOT be empty.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}

	@Override
	protected abstract WorkorderMaterialInfo buildProcess(TimeTraveler _tt);

}
