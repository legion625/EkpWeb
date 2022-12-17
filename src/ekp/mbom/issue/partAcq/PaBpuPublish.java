package ekp.mbom.issue.partAcq;

import java.util.List;
import java.util.stream.Collectors;

import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.mbom.type.PartAcqStatus;
import ekp.mbom.type.PartAcquisitionType;
import legion.util.TimeTraveler;

public class PaBpuPublish extends PaBpu {
	/* base */
	private PartAcqInfo pa;

	/* data */
	private long publishTime;

	// -------------------------------------------------------------------------------
	@Override
	protected PaBpuPublish appendBase() {
		/* base */
		pa = (PartAcqInfo) args[0];
		appendPa(pa);

		/* data */
		publishTime = System.currentTimeMillis(); // default now

		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public PaBpuPublish appendPublishTime(long publishTime) {
		this.publishTime = publishTime;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public long getPublishTime() {
		return publishTime;
	}

	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg) {
		boolean v = true;
		if (getPa() == null) {
			v = false;
			_msg.append("Part acquisition null.").append(System.lineSeparator());
		}

		if (PartAcqStatus.EDITING != getPa().getStatus()) {
			v = false;
			_msg.append("Part acquisition status error.").append(System.lineSeparator());
		}

		if (getPublishTime() <= 0) {
			v = false;
			_msg.append("publishTime error.").append(System.lineSeparator());
		}

		/* check type and ppartList */
		List<ParsInfo> parsList = getPa().getParsList(true);
		List<PpartInfo> ppartList = parsList.stream().flatMap(pars -> pars.getPpartList(true).stream())
				.collect(Collectors.toList());

		if (PartAcquisitionType.PURCHASING == getPa().getType()) {
			if (!parsList.isEmpty()) {
				v = false;
				_msg.append("Routing step list error.").append(System.lineSeparator());
			}
		} else if (PartAcquisitionType.OUTSOURCING == getPa().getType()
				|| PartAcquisitionType.SELF_PRODUCING == getPa().getType()) {
			if (parsList.isEmpty() || ppartList.isEmpty()) {
				v = false;
				_msg.append("Routing step list error.").append(System.lineSeparator());
			} else {
				for (PpartInfo ppart : ppartList) {
					if (!ppart.isAssignPart()) {
						v = false;
						_msg.append("Routing step part [" + ppart.getPartPin() + "][" + ppart.getPartName()
								+ "] has not assigned part yet.").append(System.lineSeparator());
					}
				}
			}
		} else {
			v = false;
			_msg.append("Part acquisition type error.").append(System.lineSeparator());
		}

		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		//
		if (!mbomDataService.partAcqPublish(getPa().getUid(), getPublishTime())) {
			tt.travel();
			log.error("mbomDataService.partAcqPublish return false. [{}][{}]", getPa().getUid(), getPublishTime());
			return false;
		}
		tt.addSite("revert partAcqPublish", () -> mbomDataService.partAcqRevertPublish(getPa().getUid()));
		log.info("mbomDataService.partAcqPublish [{}][{}]", getPa().getUid(), getPublishTime());

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return true;
	}

}
