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
	public MaterialInstBuilder appendMiac(MaterialInstAcqChannel miac) {
		return super.appendMiac(miac);
	}

	@Override
	public MaterialInstBuilder appendQty(double qty) {
		return super.appendQty(qty);
	}

	@Override
	public MaterialInstBuilder appendValue(double value) {
		return super.appendValue(value);
	}

	@Override
	public MaterialInstBuilder appendEffDate(long effDate) {
		return super.appendEffDate(effDate);
	}

	@Override
	public MaterialInstBuilder appendExpDate(long expDate) {
		return super.appendExpDate(expDate);
	}
}
