package ekp.web.control.zk.wo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;

import ekp.web.control.zk.pu.PuFnComposer;
import legion.util.LogUtil;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;
import legion.web.zk.ZkMsgBox;

public class WoFnComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(WoFnComposer.class);

	private FnCntProxy fnCntProxy;

	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			fnCntProxy = FnCntProxy.register(this);
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}
	
	// -------------------------------------------------------------------------------
	@Listen(Events.ON_CLICK + "=#btnGotoSearchFnPage")
	public void btnGotoSearchFnPage_clicked() {
		fnCntProxy.refreshCntUri(WoSearchFnComposer.SRC);
	}

	@Listen(Events.ON_CLICK + "=#btnCreateWo")
	public void btnCreateWo_clicked() {
		// TODO not implemented yet...
		ZkMsgBox.info("施工中...");
	}
}
