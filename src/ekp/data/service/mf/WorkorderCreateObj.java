package ekp.data.service.mf;

public class WorkorderCreateObj {
	private String partUid;
	private String partPin;
	private String partAcqUid;
	private String partAcqId;
	private String partAcqMmMano;

	private String partCfgUid;
	private String partCfgId;
	private double rqQty; // 需求數量

	public String getPartUid() {
		return partUid;
	}

	public void setPartUid(String partUid) {
		this.partUid = partUid;
	}

	public String getPartPin() {
		return partPin;
	}

	public void setPartPin(String partPin) {
		this.partPin = partPin;
	}

	public String getPartAcqUid() {
		return partAcqUid;
	}

	public void setPartAcqUid(String partAcqUid) {
		this.partAcqUid = partAcqUid;
	}

	public String getPartAcqId() {
		return partAcqId;
	}

	public void setPartAcqId(String partAcqId) {
		this.partAcqId = partAcqId;
	}

	public String getPartAcqMmMano() {
		return partAcqMmMano;
	}

	public void setPartAcqMmMano(String partAcqMmMano) {
		this.partAcqMmMano = partAcqMmMano;
	}

	public String getPartCfgUid() {
		return partCfgUid;
	}

	public void setPartCfgUid(String partCfgUid) {
		this.partCfgUid = partCfgUid;
	}

	public String getPartCfgId() {
		return partCfgId;
	}

	public void setPartCfgId(String partCfgId) {
		this.partCfgId = partCfgId;
	}

	public double getRqQty() {
		return rqQty;
	}

	public void setRqQty(double rqQty) {
		this.rqQty = rqQty;
	}

}
