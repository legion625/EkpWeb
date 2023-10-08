package ekp.web.control.zk.mbom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;

import ekp.web.control.zk.wo.WoSearchFnComposer;
import legion.util.LogUtil;
import legion.web.control.zk.legionmodule.pageTemplate.FnCntProxy;
import legion.web.zk.ZkMsgBox;

public class MbomFnComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(MbomFnComposer.class);
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
	@Listen(Events.ON_CLICK + "=#btnGotoSearchPartFnPage")
	public void btnGotoSearchPartFnPage_clicked() {
		fnCntProxy.refreshCntUri(PartSearchFnComposer.SRC);
		
	}
	
	@Listen(Events.ON_CLICK + "=#btnGotoSearchPartCfgFnPage")
	public void btnGotoSearchPartCfgFnPage_clicked() {
		fnCntProxy.refreshCntUri(PartCfgSearchFnComposer.SRC);
	}
	
	@Listen(Events.ON_CLICK+"=#btnGotoSearchPpartFnPage")
	public void btnGotoSearchPpartFnPage_clicked() {
		fnCntProxy.refreshCntUri(PpartSearchFnComposer.SRC);
	}
	
	
	
}
