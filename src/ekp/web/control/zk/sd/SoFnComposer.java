package ekp.web.control.zk.sd;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import ekp.data.service.invt.MaterialMasterInfo;
import ekp.data.service.sd.BizPartnerInfo;
import ekp.sd.BizPartner;
import ekp.sd.SdService;
import ekp.util.DataUtil;
import ekp.web.control.zk.sd.bp.BizPartnerComposer;
import ekp.web.zk.ZkUtil;
import legion.BusinessServiceFactory;
import legion.util.LogUtil;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;
import legion.web.zk.ZkMsgBox;

public class SoFnComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(SoFnComposer.class);

	private FnCntProxy fnCntProxy;

	// -------------------------------------------------------------------------------
	private SdService sdService = BusinessServiceFactory.getInstance().getService(SdService.class);
	
	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			fnCntProxy = FnCntProxy.register(this);
			
			init();
			
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	private void init() {
		/* Customer */
		ZkUtil.initCbb(cbbCreateSoCustomer, sdService.loadBizPartnerList().toArray(new BizPartnerInfo[0]), false);
		
		/* MmQvData */
		ListitemRenderer<MmQvData> mmQvDataRenderer = (li, d, i) -> {
			Listcell lc;
			//
			lc = new Listcell();
			Toolbarbutton btnRemove = new Toolbarbutton();
			btnRemove.setIconSclass("fas fa-trash-alt");
			btnRemove.addEventListener(Events.ON_CLICK, e -> {
				ListModelList<MmQvData> model = (ListModelList) lbxCreateSoMmQvData.getModel();
				model.remove(d);
			});
			lc.appendChild(btnRemove);
			li.appendChild(lc);

			//
			li.appendChild(new Listcell(d.getMmMano()));
			li.appendChild(new Listcell(d.getMmName()));
			li.appendChild(new Listcell(d.getMmSpec()));
			//
			lc = new Listcell();
			Doublebox dbbQty = new Doublebox(d.getQty());
			dbbQty.setHflex("1");
			dbbQty.addEventListener(Events.ON_CHANGE, e -> d.setQty(dbbQty.getValue()));
			lc.appendChild(dbbQty);
			li.appendChild(lc);
			//
			lc = new Listcell();
			Doublebox dbbValue = new Doublebox(d.getValue());
			dbbValue.setHflex("1");
			dbbValue.addEventListener(Events.ON_CHANGE, e -> d.setValue(dbbValue.getValue()));
			lc.appendChild(dbbValue);
			li.appendChild(lc);
			
		};
		lbxCreateSoMmQvData.setItemRenderer(mmQvDataRenderer);
		
		
		
		
	}
	
	// -------------------------------------------------------------------------------
	@Listen(Events.ON_CLICK + "=#btnGotoSearchFnPage")
	public void btnGotoSearchFnPage_clicked() {
		fnCntProxy.refreshCntUri(SoSearchFnComposer.SRC);
	}

	@Listen(Events.ON_CLICK + "=#btnGotoBizPartnerFnPage")
	public void btnGotoBizPartnerFnPage_clicked() {
		fnCntProxy.refreshCntUri(BizPartnerComposer.SRC);
	}
	
	// -------------------------------------------------------------------------------
	@Wire
	private Window wdCreateSo;
	@Wire("#wdCreateSo #cbbCustomer")
	private Combobox cbbCreateSoCustomer;
	@Wire("#wdCreateSo #lbxMmQvData")
	private Listbox lbxCreateSoMmQvData;
	
	@Listen(Events.ON_CLICK + "=#btnCreateSo")
	public void btnCreateSo_clicked() {
		wdCreateSo_btnResetBlanks_clicked();
		wdCreateSo.setVisible(true);
	}
	
	@Listen(Events.ON_CLICK + "=#wdCreateSo #btnAddMmQvData")
	public void wdCreateSo_btnAddMmQvData_clicked() {
		ListModelList<MmQvData> model = (ListModelList) lbxCreateSoMmQvData.getModel();
		model.add(new MmQvData());
	}
	
	@Listen(Events.ON_CLICK + "=#wdCreateSo #btnResetBlanks")
	public void wdCreateSo_btnResetBlanks_clicked() {
		cbbCreateSoCustomer.setValue("");
		refreshLbxCreateSoSoib(null);
	}

	private void refreshLbxCreateSoSoib(List<MmQvData> _dataList) {
		ListModelList<MmQvData> model = _dataList == null ? new ListModelList<>() : new ListModelList<>(_dataList);
		lbxCreateSoMmQvData.setModel(model);
	}
	
	private class MmQvData {
		private MaterialMasterInfo mm;
		private double qty, value;

		public MaterialMasterInfo getMm() {
			return mm;
		}

		public void setMm(MaterialMasterInfo mm) {
			this.mm = mm;
		}

		public double getQty() {
			return qty;
		}

		public void setQty(double qty) {
			this.qty = qty;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}
		
		// ---------------------------------------------------------------------------
		public String getMmMano() {
			return getMm()==null?DataUtil.NO_DATA: getMm().getMano();
		}
		public String getMmName() {
			return getMm()==null?DataUtil.NO_DATA: getMm().getName();
		}
		public String getMmSpec() {
			return getMm()==null?DataUtil.NO_DATA: getMm().getSpecification();
		}
		
		
		

	}

}
