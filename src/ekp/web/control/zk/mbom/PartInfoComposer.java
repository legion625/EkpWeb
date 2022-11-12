package ekp.web.control.zk.mbom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;

import legion.util.LogUtil;

public class PartInfoComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(PartInfoComposer.class);

	// -------------------------------------------------------------------------------
	@Wire
	private Grid gridPartAcq;
	
	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			
			
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}
	
	// -------------------------------------------------------------------------------
	
	
}
