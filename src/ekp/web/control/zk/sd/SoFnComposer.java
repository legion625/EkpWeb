package ekp.web.control.zk.sd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;

import ekp.web.control.zk.sd.bp.BizPartnerComposer;
import legion.util.LogUtil;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;
import legion.web.zk.ZkMsgBox;

public class SoFnComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(SoFnComposer.class);

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
		fnCntProxy.refreshCntUri(SoSearchFnComposer.SRC);
	}

	@Listen(Events.ON_CLICK + "=#btnGotoBizPartnerFnPage")
	public void btnGotoBizPartnerFnPage_clicked() {
		fnCntProxy.refreshCntUri(BizPartnerComposer.SRC);
	}
	
	
	
	// -------------------------------------------------------------------------------
	@Listen(Events.ON_CLICK + "=#btnCreateSo")
	public void btnCreateSo_clicked() {
		// TODO not implemented yet...
		ZkMsgBox.info("施工中...");
	}
}
