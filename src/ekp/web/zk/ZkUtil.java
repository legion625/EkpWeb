package ekp.web.zk;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.sd.BizPartnerInfo;

public class ZkUtil {
	public static void initCbb(Combobox _cbb, PartAcqInfo[] _partAcqs, boolean _containsBlank) {
		if (_cbb == null)
			return;
		_cbb.getChildren().clear();
		
		if (_containsBlank) {
			_cbb.appendChild(new Comboitem());
		}
		
		for(PartAcqInfo _pa: _partAcqs) {
			Comboitem cbi = new Comboitem(_pa.getPartPinWithId());
			cbi.setValue(_pa);
			cbi.setDescription(_pa.getName());
			_cbb.appendChild(cbi);
		}
	}
	
	public static void initCbb(Combobox _cbb, BizPartnerInfo[] _bps, boolean _containsBlank) {
		if (_cbb == null)
			return;
		_cbb.getChildren().clear();

		if (_containsBlank) {
			_cbb.appendChild(new Comboitem());
		}

		for (BizPartnerInfo _bp : _bps) {
			Comboitem cbi = new Comboitem(_bp.getName());
			cbi.setValue(_bp);
			cbi.setDescription(_bp.getBpsn() + " | " + _bp.getBan());
			_cbb.appendChild(cbi);
		}
	}
}
