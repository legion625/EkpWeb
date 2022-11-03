package ekp.data.service.mbom;

public class PartCfgCreateObj {
	private String rootPartUid; // ref data key
	private String rootPartPin; // ref biz key

	private String id; // biz key
	private String name;
	private String desp;

	public String getRootPartUid() {
		return rootPartUid;
	}

	public void setRootPartUid(String rootPartUid) {
		this.rootPartUid = rootPartUid;
	}

	public String getRootPartPin() {
		return rootPartPin;
	}

	public void setRootPartPin(String rootPartPin) {
		this.rootPartPin = rootPartPin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}
}
