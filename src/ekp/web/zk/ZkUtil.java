package ekp.web.zk;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import ekp.data.service.mbom.PartAcqInfo;

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
}
