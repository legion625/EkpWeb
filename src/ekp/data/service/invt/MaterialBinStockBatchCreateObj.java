package ekp.data.service.invt;

public class MaterialBinStockBatchCreateObj {
	/* bizKey */
	private String mbsUid; // 主要ref的MaterialBinStock
	private String miUid; // material inst uid

	public String getMbsUid() {
		return mbsUid;
	}

	public void setMbsUid(String mbsUid) {
		this.mbsUid = mbsUid;
	}

	public String getMiUid() {
		return miUid;
	}

	public void setMiUid(String miUid) {
		this.miUid = miUid;
	}
}
