package ekp.data.service.mbom;

public class ParsCreateObj {
	private String partAcqUid; // ref data key

	private String id; // routing step id
	private String name;
	private String desp;

	public String getPartAcqUid() {
		return partAcqUid;
	}

	public void setPartAcqUid(String partAcqUid) {
		this.partAcqUid = partAcqUid;
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
