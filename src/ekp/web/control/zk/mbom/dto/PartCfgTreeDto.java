package ekp.web.control.zk.mbom.dto;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.logging.Log;
import org.zkoss.zul.DefaultTreeNode;

import ekp.DebugLogMark;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartCfgInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.invt.type.InvtOrderType;
import ekp.util.DataUtil;
import legion.util.NumberFormatUtil;

public class PartCfgTreeDto {
	private static Logger log = LoggerFactory.getLogger(PartCfgTreeDto.class);
	
	private PpartInfo ppart; // root: no ppart
	private PartInfo p;
	private PartAcqInfo pa;
	private List<PartCfgTreeDto> childrenList;

	private PartCfgTreeDto(PpartInfo ppart, PartInfo p, PartAcqInfo pa, List<PartCfgTreeDto> childrenList) {
		this.p = p;
		this.pa = pa;
		this.ppart = ppart;
		this.childrenList = childrenList;
	}

	public static PartCfgTreeDto of(PartCfgInfo _partCfg) {
		return of(_partCfg, _partCfg.getRootPart());
	}
	
	public static PartCfgTreeDto of(PartCfgInfo _partCfg, PartInfo _thisPart) {
		log.debug("PartCfgTreeDto.of");
//		PartInfo rootPart = _partCfg.getRootPart();
		PartInfo thisPart = _thisPart;
		log.debug("thisPart: {}\t{}", thisPart.getPin(), thisPart.getName());
		if(_thisPart==null) {
			thisPart = _partCfg.getRootPart();
			log.debug("thisPart: {}\t{}", thisPart.getPin(), thisPart.getName());
		}
			
//		PartAcqInfo rootPa = rootPart.getPa(_partCfg);
		
		PartAcqInfo thisPa = thisPart.getPa(_partCfg, true);
		log.debug("thisPa: {}", thisPa);
		PpartInfo ppart = null;
		List<PartCfgTreeDto> childrenList = new ArrayList<>();

//		for (PpartInfo childPpart : rootPa.getPpartList()) {
		for (PpartInfo childPpart : thisPa.getPpartList()) {
			childrenList.add(of(_partCfg, childPpart));
		}

//		return new PartCfgTreeDto(ppart, rootPart, rootPa, childrenList);
		return new PartCfgTreeDto(ppart, thisPart, thisPa, childrenList);
	}
	
//	public static PartCfgTreeDto of(PartCfgInfo _partCfg) {
//		PartInfo rootPart = _partCfg.getRootPart();
//		PartAcqInfo rootPa = rootPart.getPa(_partCfg);
//		PpartInfo ppart = null;
//		List<PartCfgTreeDto> childrenList = new ArrayList<>();
//
//		for (PpartInfo childPpart : rootPa.getPpartList()) {
//			childrenList.add(of(_partCfg, childPpart));
//		}
//
//		return new PartCfgTreeDto(ppart, rootPart, rootPa, childrenList);
//	}

	private static PartCfgTreeDto of(PartCfgInfo _partCfg, PpartInfo _ppart) {
		PpartInfo ppart = _ppart;
		PartInfo p = ppart.getPart();
		PartAcqInfo pa = p == null ? null : p.getPa(_partCfg);
		List<PartCfgTreeDto> childrenList = new ArrayList<>();
		if (pa != null)
			for (PpartInfo childPpart : pa.getPpartList()) {
				childrenList.add(of(_partCfg, childPpart));
			}
		return new PartCfgTreeDto(ppart, p, pa, childrenList);
	}

	// ---------------------------------------------------------------------------
	public PartInfo getP() {
		return p;
	}

	public PartAcqInfo getPa() {
		return pa;
	}

	public PpartInfo getPpart() {
		return ppart;
	}

	public List<PartCfgTreeDto> getChildrenList() {
		return childrenList;
	}

	// ---------------------------------------------------------------------------
	public void reloadPa() {
		this.pa = pa.reload();
	}
	
	// ---------------------------------------------------------------------------
	public String getPartPin() {
		return DataUtil.nodataIfEmpty(getP(), PartInfo::getPin);
	}
	
	public String getPartName() {
		return DataUtil.nodataIfEmpty(getP(), PartInfo::getName);
	}
	
	public String getPartUnitName() {
		return DataUtil.nodataIfEmpty(getP(), PartInfo::getUnitName);
	}
	
	public String getPaId() {
		return DataUtil.nodataIfEmpty(getPa(), PartAcqInfo::getId);
	}
	public String getPaName() {
		return DataUtil.nodataIfEmpty(getPa(), PartAcqInfo::getName);
	}
	public String getPaTypeName() {
		return DataUtil.nodataIfEmpty(getPa(), PartAcqInfo::getTypeName);
	}
	public String getPaStatusName() {
		return DataUtil.nodataIfEmpty(getPa(), PartAcqInfo::getStatusName);
	}
	public String getPaMmMano() {
		return DataUtil.nodataIfEmpty(getPa(), PartAcqInfo::getMmMano);
	}
	
	public String getPpartReqQtyDisplay() {
		return DataUtil.nodataIfEmpty(getPpart(), pp->NumberFormatUtil.getDecimalString(pp.getPartReqQty(), 3));
	}
	
	public String getPaRefUnitCostDisplay() {
		double d = getPa() == null ? 0 : getPa().getRefUnitCost();
		String dStr = d <= 0 || Double.isNaN(d) ? null : NumberFormatUtil.getDecimalString(d, 3);
		return DataUtil.nodataIfEmpty(dStr);
	}
	
	public String getPaMmAvgStockValueDisplay() {
		double d = getPa() == null ? 0 : (getPa().getMm() == null ? 0 : getPa().getMm().getAvgStockValue());
		String dStr = d <= 0 || Double.isNaN(d) ? null : NumberFormatUtil.getDecimalString(d, 3);
		return DataUtil.nodataIfEmpty(dStr);
	}
	
	public double getPaMmIoiAvgOrderValue(InvtOrderType _ioType) {
		return getPa() == null ? 0 : (getPa().getMm() == null ? 0 : getPa().getMm().getIoiAvgOrderValue(_ioType));
	}
	
	public String getPaMmIoiAvgOrderValueDisplay(InvtOrderType _ioType) {
		double d = getPaMmIoiAvgOrderValue(_ioType);
		String dStr = d <= 0 || Double.isNaN(d) ? null : NumberFormatUtil.getDecimalString(d, 3);
		return DataUtil.nodataIfEmpty(dStr);
	}
	
	public String getPaMmIoiAvgOrderValueDisplayI1Display() {
		return getPaMmIoiAvgOrderValueDisplay(InvtOrderType.I1);
	}
	public String getPaMmIoiAvgOrderValueDisplayI2Display() {
		return getPaMmIoiAvgOrderValueDisplay(InvtOrderType.I2);
	}
	public String getPaMmIoiAvgOrderValueDisplayO2Display() {
		return getPaMmIoiAvgOrderValueDisplay(InvtOrderType.O2);
	}
	public String getPaMmIoiAvgOrderValueDisplayO9Display() {
		return getPaMmIoiAvgOrderValueDisplay(InvtOrderType.O9);
	}

	public String getPaMmSoiAvgValueDisplay() {
		double d = getPa() == null ? 0 : (getPa().getMm() == null ? 0 : getPa().getMm().getSoiAvgValue());
		String dStr = d <= 0 || Double.isNaN(d) ? null : NumberFormatUtil.getDecimalString(d, 3);
		return DataUtil.nodataIfEmpty(dStr);
	}
	

	// ---------------------------------------------------------------------------
	public void reloadPa(PartCfgInfo _partCfg) {
		pa = p.getPa(_partCfg, true);
	}

	// ---------------------------------------------------------------------------
	public DefaultTreeNode<PartCfgTreeDto> packRootTreeNode4Model() {
		return new DefaultTreeNode<PartCfgTreeDto>(this, new DefaultTreeNode[] { packTreeNode() });
	}

	public DefaultTreeNode<PartCfgTreeDto> packTreeNode() {
		if (getChildrenList() == null || getChildrenList().isEmpty()) {
			return new DefaultTreeNode<PartCfgTreeDto>(this);
		} else {
			List<DefaultTreeNode<PartCfgTreeDto>> childrenTreeNodeList = new ArrayList<>();
			for (PartCfgTreeDto childDto : getChildrenList())
				childrenTreeNodeList.add(childDto.packTreeNode());
			return new DefaultTreeNode<PartCfgTreeDto>(this, childrenTreeNodeList);
		}
	}

}
