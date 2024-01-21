package ekp.web.control.zk.invt.mm;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;

import ekp.data.service.invt.MaterialBinStockInfo;
import ekp.data.service.invt.MaterialMasterInfo;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.web.zk.ZkUtil;

public class Mm_mbsComposer extends SelectorComposer<Component>{
	private Logger log = LoggerFactory.getLogger(Mm_mbsComposer.class);
	
	private final static String SRC ="/invt/mm/mm_mbs.zul";
	
	public final static Mm_mbsComposer of(Include _icd) {
		return ZkUtil.of(_icd, SRC, "divMm_mbs");
	}
	
	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxMbs;
	
	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}
	
	void init() {
		/**/
		ListitemRenderer<MaterialBinStockInfo> mbsRenderer = (li, mbs, i)->{
			li.appendChild(new Listcell());
			li.appendChild(new Listcell(mbs.getWrhsLocId()));
			li.appendChild(new Listcell(mbs.getWrhsLocName()));
			li.appendChild(new Listcell(mbs.getWrhsBinId()));
			li.appendChild(new Listcell(mbs.getWrhsBinName()));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(mbs.getSumStockQty(),2)));
			li.appendChild(new Listcell(NumberFormatUtil.getDecimalString(mbs.getSumStockValue(),2)));
		};
		lbxMbs.setItemRenderer(mbsRenderer);
	}
	
	// -------------------------------------------------------------------------------
	void refreshMbsList(MaterialMasterInfo _mm) {
		List<MaterialBinStockInfo> mbsList = _mm.getMbsList();
		ListModelList<MaterialBinStockInfo> model = mbsList==null ? new ListModelList<>() : new ListModelList<>(mbsList);
		lbxMbs.setModel(model);
	}
	
	
	
}
