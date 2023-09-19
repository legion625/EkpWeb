package ekp.mbom.issue.prodCtl;

import ekp.data.service.mbom.PartInfo;

public class ProdCtlBuilder1 extends ProdCtlBuilder {

	/* base */
	// none

	/* data */
	private PartInfo part;

	// -------------------------------------------------------------------------------
	@Override
	protected ProdCtlBuilder1 appendBase() {
		// none
		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
//	@Override
//	public ProdCtlBuilder0 appendId(String id) {
//		return (ProdCtlBuilder0) super.appendId(id);
//	}

	@Override
	public ProdCtlBuilder1 appendLv(int lv) {
		return (ProdCtlBuilder1) super.appendLv(lv);
	}

//	@Override
//	public ProdCtlBuilder0 appendName(String name) {
//		return (ProdCtlBuilder0) super.appendName(name);
//	}

	@Override
	public ProdCtlBuilder1 appendReq(boolean req) {
		return (ProdCtlBuilder1) super.appendReq(req);
	}
	
	public ProdCtlBuilder1 appendPart(PartInfo part) {
		this.part = part;
		appendPartUid(part.getUid()).appendPartPin(part.getPin()).appendPartName(part.getName());
		return this;
	}

}
