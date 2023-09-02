package ekp.mbom.issue.part;

import org.apache.catalina.core.ApplicationMapping;

import ekp.data.service.mbom.PartInfo;
import ekp.mbom.type.PartAcquisitionType;
import ekp.mbom.type.PartUnit;
import legion.biz.Bpu;
import legion.util.TimeTraveler;

public class PartBpuUpdate extends PartBpu {
	/* base */
	private PartInfo part;
	
	/* data */
	private String pin;
	private String name;
	private PartUnit unit;
	
	
	// -------------------------------------------------------------------------------
	@Override
	protected PartBpuUpdate appendBase() {
		/* base */
		part = (PartInfo) args[0];
		appendPart(part);
		
		/* data */
		appendPin(part.getPin()).appendName(part.getName()).appendUnit(part.getUnit());
		
		return this;
	}

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	public PartBpuUpdate appendPin(String pin) {
		this.pin = pin;
		return this;
	}

	public PartBpuUpdate appendName(String name) {
		this.name = name;
		return this;
	}

	public PartBpuUpdate appendUnit(PartUnit unit) {
		this.unit = unit;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getPin() {
		return pin;
	}

	public String getName() {
		return name;
	}

	public PartUnit getUnit() {
		return unit;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;
		if(getPart()==null) {
			v = false;
			_msg.append("Part null").append(System.lineSeparator());
		}
		
		return v;
	}

	@Override
	protected Boolean buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();
		//
		String origPin = getPart().getPin();
		String origName = getPart().getName();
		PartUnit origUnit = getPart().getUnit();
		if(!mbomDataService.partUpdate(getPart().getUid(), getPin(), getName(), getUnit())) {
			tt.travel();
			log.error("mbomDataService.partUpdate return false. [{}][{}][{}][{}]", getPart().getUid(),getPin(), getName(), getUnit());
			return false;
		}
		tt.addSite("revert partUpdate", ()->mbomDataService.partUpdate(getPart().getUid(), origPin, origName, origUnit));
		log.info("mbomDataService.partUpdate [{}][{}][{}][{}]", getPart().getUid(),getPin(), getName(), getUnit());
		
		//
		if (_tt != null)
			_tt.copySitesFrom(tt);
		
		return true;
	}

	
}
