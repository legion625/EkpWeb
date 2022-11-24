package ekp.web.control.zk.mbom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.RowRenderer;

import ekp.data.service.mbom.ParsInfo;
import ekp.data.service.mbom.PartAcqInfo;
import ekp.data.service.mbom.PartInfo;
import ekp.data.service.mbom.PpartInfo;
import ekp.data.service.mbom.PprocInfo;
import legion.util.LogUtil;
import legion.util.NumberFormatUtil;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;
import legion.web.zk.ZkNotification;
import legion.web.zk.ZkUtil;

public class PartInfoComposer extends SelectorComposer<Component> {
	public final static String URI = "/mbom/partInfo.zul";

	private Logger log = LoggerFactory.getLogger(PartInfoComposer.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Label lbPin, lbName;

	@Wire
	private Grid gridPartAcq, gridPars, gridPproc, gridPpart;

	// -------------------------------------------------------------------------------
	private FnCntProxy fnCntProxy;

	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			fnCntProxy = FnCntProxy.register(this);
			log.debug("fnCntProxy: {}", fnCntProxy);
			
			init();
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	// -------------------------------------------------------------------------------
	private void init() {
		/* PartAcq */
		RowRenderer<PartAcqInfo> paRowRenderer = (r, pa, i) -> {
			// id
			r.appendChild(new Label(pa.getId()));
			// name
			r.appendChild(new Label(pa.getName()));
			// type
			r.appendChild(new Label(pa.getTypeName()));
			
			// click event -> show pars
			r.addEventListener(Events.ON_CLICK, e -> {
				ListModelList<ParsInfo> parsModel = new ListModelList<>(pa.getParsList());
				gridPars.setModel(parsModel);
			});
		};
		gridPartAcq.setRowRenderer(paRowRenderer);
		
		/* Pars */
		RowRenderer<ParsInfo> parsRowRenderer = (r, pars, i) -> {
			// id
			r.appendChild(new Label(pars.getId()));
			// name
			r.appendChild(new Label(pars.getName()));
			// desp
			r.appendChild(new Label(pars.getDesp()));
			
			// click event -> show pproc and ppart
			r.addEventListener(Events.ON_CLICK, e -> {
				ListModelList<PprocInfo> pprocModel = new ListModelList<>(pars.getPprocList());
				gridPproc.setModel(pprocModel);
				
				ListModelList<PpartInfo> ppartModel = new ListModelList<>(pars.getPpartList());
				gridPpart.setModel(ppartModel);
				
			});
		};
		gridPars.setRowRenderer(parsRowRenderer);
		
		
		/* Pproc */
		RowRenderer<PprocInfo> pprocRowRenderer = (r, pproc, i) -> {
			// seq
			r.appendChild(new Label(pproc.getSeq()));
			// name
			r.appendChild(new Label(pproc.getName()));
			// desp
			r.appendChild(new Label(pproc.getDesp()));
		};
		gridPproc.setRowRenderer(pprocRowRenderer);

		/* Ppart */
		RowRenderer<PpartInfo> ppartRowRenderer = (r, ppart, i) -> {
			// part pin
			r.appendChild(new Label(ppart.getPartPin()));
			// part name
			r.appendChild(new Label(ppart.getPartName()));
			// part req qty
			r.appendChild(new Label(NumberFormatUtil.getDecimalString(ppart.getPartReqQty(), 0)));
		};
		gridPpart.setRowRenderer(ppartRowRenderer);

	}

	
	void refreshPartInfo(PartInfo _part) {
		if (_part == null)
			return;

		/* part */
		lbPin.setValue(_part.getPin());
		lbName.setValue(_part.getName());
		
		/* part acq */
		ListModelList<PartAcqInfo> paModel = new ListModelList<>(_part.getPaList(false));
		gridPartAcq.setModel(paModel);
	}

}
