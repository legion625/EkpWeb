package ekp.invt.bpu.material;

import ekp.invt.type.MaterialInstAcqChannel;

public class MaterialInstBuilder0 extends MaterialInstBuilder {

	/* base */
	// none

	/* data */
	// none

	// -------------------------------------------------------------------------------
	@Override
	protected MaterialInstBuilder0 appendBase() {
		// none
		return this;
	}

	// -------------------------------------------------------------------------------
	@Override
	public MaterialInstBuilder0 appendMmUid(String mmUid) {
		return (MaterialInstBuilder0) super.appendMmUid(mmUid);
	}

	@Override
	public MaterialInstBuilder0 appendMiac(MaterialInstAcqChannel miac) {
		return (MaterialInstBuilder0) super.appendMiac(miac);
	}
	
	@Override
	public MaterialInstBuilder0 appendMiacSrcNo(String miacSrcNo) {
		return (MaterialInstBuilder0) super.appendMiacSrcNo(miacSrcNo);
	}
	
	@Override
	public MaterialInstBuilder0 appendQty(double qty) {
		return (MaterialInstBuilder0)  super.appendQty(qty);
	}

	@Override
	public MaterialInstBuilder0 appendValue(double value) {
		return (MaterialInstBuilder0) super.appendValue(value);
	}

	@Override
	public MaterialInstBuilder0 appendEffDate(long effDate) {
		return (MaterialInstBuilder0) super.appendEffDate(effDate);
	}

	@Override
	public MaterialInstBuilder0 appendExpDate(long expDate) {
		return (MaterialInstBuilder0) super.appendExpDate(expDate);
	}
}
