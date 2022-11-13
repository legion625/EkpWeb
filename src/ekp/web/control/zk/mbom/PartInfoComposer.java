package ekp.web.control.zk.mbom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;

import ekp.data.service.mbom.PartInfo;
import legion.util.LogUtil;
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
	private Grid gridPartAcq;

	// -------------------------------------------------------------------------------
	private FnCntProxy fnCntProxy;

	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			fnCntProxy = FnCntProxy.register(this);
			log.debug("fnCntProxy: {}", fnCntProxy);
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

	// -------------------------------------------------------------------------------
	void refreshPartInfo(PartInfo _part) {
		if (_part == null)
			return;

		/* part */
		lbPin.setValue(_part.getPin());
		lbName.setValue(_part.getName());
	}

}
