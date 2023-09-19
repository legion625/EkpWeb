package ekp.data.service.mbom;

public class ProdCtlCreateObj {
//	private String id; // 型號 biz key
	private int lv; // 1:系統;2:次系統;3:模組 預設先展到第3階
//	private String name; // 名稱
	private String partUid;
	private String partPin;
	private String partName;
	private boolean req; // 是否為必要的

//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}

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

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	
	
	public boolean isReq() {
		return req;
	}

	
	public void setReq(boolean req) {
		this.req = req;
	}
}
