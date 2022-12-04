package ekp.data.service.mbom;

public class PpartSkewer {
	/* p */
	private String pUid;
	private String pPin;
	private String pName;

	/* pa */
	private String paUid;
	private String paId;
	private String paName;

	/* pars */
	private String parsSeq;
	private String parsName;
	private String parsDesp;

	/* ppart */
	private String uid;
	private long objectCreateTime;
	private long objectUpdateTime;
	private String parsUid; // ref data key

	// assign part
	private boolean assignPart;
	private String partUid; // ref data key
	private String partPin; // ref biz key
	private double partReqQty; // required quantity (allow decimal in certain conditions)

	/* ppart-p */
	private String partName;

	// -------------------------------------------------------------------------------
	// ---------------------------------getter&setter---------------------------------
	public String getpUid() {
		return pUid;
	}

	void setpUid(String pUid) {
		this.pUid = pUid;
	}

	public String getpPin() {
		return pPin;
	}

	void setpPin(String pPin) {
		this.pPin = pPin;
	}

	public String getpName() {
		return pName;
	}

	void setpName(String pName) {
		this.pName = pName;
	}

	public String getPaUid() {
		return paUid;
	}

	void setPaUid(String paUid) {
		this.paUid = paUid;
	}

	public String getPaId() {
		return paId;
	}

	void setPaId(String paId) {
		this.paId = paId;
	}

	public String getPaName() {
		return paName;
	}

	void setPaName(String paName) {
		this.paName = paName;
	}

	public String getParsSeq() {
		return parsSeq;
	}

	void setParsSeq(String parsSeq) {
		this.parsSeq = parsSeq;
	}

	public String getParsName() {
		return parsName;
	}

	void setParsName(String parsName) {
		this.parsName = parsName;
	}

	public String getParsDesp() {
		return parsDesp;
	}

	void setParsDesp(String parsDesp) {
		this.parsDesp = parsDesp;
	}

	public String getUid() {
		return uid;
	}

	void setUid(String uid) {
		this.uid = uid;
	}

	public long getObjectCreateTime() {
		return objectCreateTime;
	}

	void setObjectCreateTime(long objectCreateTime) {
		this.objectCreateTime = objectCreateTime;
	}

	public long getObjectUpdateTime() {
		return objectUpdateTime;
	}

	void setObjectUpdateTime(long objectUpdateTime) {
		this.objectUpdateTime = objectUpdateTime;
	}

	public String getParsUid() {
		return parsUid;
	}

	void setParsUid(String parsUid) {
		this.parsUid = parsUid;
	}

	public boolean isAssignPart() {
		return assignPart;
	}

	void setAssignPart(boolean assignPart) {
		this.assignPart = assignPart;
	}

	public String getPartUid() {
		return partUid;
	}

	void setPartUid(String partUid) {
		this.partUid = partUid;
	}

	public String getPartPin() {
		return partPin;
	}

	void setPartPin(String partPin) {
		this.partPin = partPin;
	}

	public double getPartReqQty() {
		return partReqQty;
	}

	void setPartReqQty(double partReqQty) {
		this.partReqQty = partReqQty;
	}

	public String getPartName() {
		return partName;
	}

	void setPartName(String partName) {
		this.partName = partName;
	}
}
