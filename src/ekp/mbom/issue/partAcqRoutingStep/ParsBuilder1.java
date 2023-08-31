package ekp.mbom.issue.partAcqRoutingStep;

import ekp.data.service.mbom.PartAcqInfo;
import ekp.mbom.type.PartAcqStatus;

public class ParsBuilder1 extends ParsBuilder{
	/* base */
	private PartAcqInfo pa;
	
	/* data */
	// none
	
	// -------------------------------------------------------------------------------
	@Override
	protected ParsBuilder1 appendBase() {
		/* base */
		pa = (PartAcqInfo) args[0];
		appendPartAcqUid(pa.getUid());
		
		/* data */
		// none
		
		return this;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public ParsBuilder1 appendSeq(String seq) {
		return (ParsBuilder1) super.appendSeq(seq);
	}

	@Override
	public ParsBuilder1 appendName(String name) {
		return (ParsBuilder1) super.appendName(name);
	}

	@Override
	public ParsBuilder1 appendDesp(String desp) {
		return (ParsBuilder1) super.appendDesp(desp);
	}
	
	// -------------------------------------------------------------------------------
	public PartAcqInfo getPa() {
		return pa;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = super.verify(_msg);

		if (PartAcqStatus.EDITING != getPa().getStatus()) {
			_msg.append("The status of part acquisition should not be EDITING.").append(System.lineSeparator());
			v = false;
		}

		return v;
	}
	
	
}
