package ekp.data.service.mf;

public class WorkorderMaterialCreateObj {
	private String woUid;
	private String woNo;

	// 料件基本檔
	private String mmUid;
	private String mmMano;
	private String mmName;

	public String getWoUid() {
		return woUid;
	}

	public void setWoUid(String woUid) {
		this.woUid = woUid;
	}

	public String getWoNo() {
		return woNo;
	}

	public void setWoNo(String woNo) {
		this.woNo = woNo;
	}

	public String getMmUid() {
		return mmUid;
	}

	public void setMmUid(String mmUid) {
		this.mmUid = mmUid;
	}

	public String getMmMano() {
		return mmMano;
	}

	public void setMmMano(String mmMano) {
		this.mmMano = mmMano;
	}

	public String getMmName() {
		return mmName;
	}

	public void setMmName(String mmName) {
		this.mmName = mmName;
	}
}
