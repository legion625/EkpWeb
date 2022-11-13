package ekp.web.control.zk.mbom;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Toolbarbutton;

import ekp.data.service.mbom.PartInfo;
import ekp.mbom.MbomService;
import legion.BusinessServiceFactory;
import legion.util.LogUtil;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;
import legion.web.zk.ZkMsgBox;
import legion.web.zk.ZkNotification;

public class MbomFnComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(MbomFnComposer.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Listbox lbxPart;
	
	// -------------------------------------------------------------------------------
	private MbomService mbomService = BusinessServiceFactory.getInstance().getService(MbomService.class);
	
	private FnCntProxy fnCntProxy;

	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			fnCntProxy = FnCntProxy.register(this);
			log.debug("fnCntProxy: {}", fnCntProxy);
			
			init();
			List<PartInfo> partList = new ArrayList<>();
			PartInfo partA1 =mbomService.loadPartByPin("A1");
			if(partA1!=null)
			partList.add(partA1);	
			setPartList(partList);
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	// -------------------------------------------------------------------------------
	private void init() {
		ListitemRenderer<PartInfo> partRenderer = (li, p, i) -> {
			Listcell lc;
			// delete
			lc = new Listcell();
			Toolbarbutton btn  =new Toolbarbutton();
			btn.setIconSclass("fa fa-minus");
			btn.addEventListener(Events.ON_CLICK, e -> {
				ZkMsgBox.confirm("test", () -> {
					ZkNotification.info("deleted!");
				});
			});
			lc.appendChild(btn);
			li.appendChild(lc);
			//
			li.appendChild(new Listcell(p.getPin()));
			//
			li.appendChild(new Listcell(p.getName()));

			li.addEventListener(Events.ON_CLICK, e -> {
				fnCntProxy.refreshCntUri(PartInfoComposer.URI);
				PartInfoComposer partComposer = fnCntProxy.getComposer(PartInfoComposer.class);
				partComposer.refreshPartInfo(p);
			});
		};
		lbxPart.setItemRenderer(partRenderer);
	}

	// -------------------------------------------------------------------------------
	@Listen(Events.ON_CLICK+"=#btnAddPart")
	public void btnAddPart_clicked() {
//		ZkMsgBox.confirm("btnAddPart?", _runAction);
		ZkNotification.info("not implemented yet..");
	}
	
	
	// -------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------
	private void setPartList(List<PartInfo> _partList) {
		ListModelList<PartInfo> model =_partList==null?new ListModelList<>(): new ListModelList<>(_partList);
		lbxPart.setModel(model);
	}
	
	

}
