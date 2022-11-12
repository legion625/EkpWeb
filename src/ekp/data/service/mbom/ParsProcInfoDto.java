package ekp.data.service.mbom;

import ekp.ObjectModelInfoDto;

public class ParsProcInfoDto extends ObjectModelInfoDto implements ParsProcInfo {

	protected ParsProcInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------attribute-----------------------------------
	private String parsUid; // ref data key

	//
	private String seq; //
	private String name;
	private String desp;

	// process
	private boolean assignProc;
	private String procUid; // ref data key
	private String procId; // ref biz key

	// -------------------------------------------------------------------------------
	// ---------------------------------getter&setter---------------------------------
	@Override
	public String getParsUid() {
		return parsUid;
	}

	void setParsUid(String parsUid) {
		this.parsUid = parsUid;
	}

	@Override
	public String getSeq() {
		return seq;
	}

	void setSeq(String seq) {
		this.seq = seq;
	}

	@Override
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDesp() {
		return desp;
	}

	void setDesp(String desp) {
		this.desp = desp;
	}

	@Override
	public boolean isAssignProc() {
		return assignProc;
	}

	void setAssignProc(boolean assignProc) {
		this.assignProc = assignProc;
	}

	@Override
	public String getProcUid() {
		return procUid;
	}

	void setProcUid(String procUid) {
		this.procUid = procUid;
	}

	@Override
	public String getProcId() {
		return procId;
	}

	void setProcId(String procId) {
		this.procId = procId;
	}

}
