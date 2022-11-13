package legion.web.control.zk.legionmodule.pageTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zul.Include;

public class FnCntProxy {
	private static Logger log = LoggerFactory.getLogger(FnCntProxy.class);

	private final static String ATT_FN_CNT_PROXY = "ATT_FN_CNT_PROXY";

	private FnCntTemplateComposer composer;

	private ConcurrentHashMap<Class<?>, SelectorComposer<Component>> composers = new ConcurrentHashMap<>();

	private FnCntProxy(FnCntTemplateComposer composer) {
		this.composer = composer;
	}

	public <T> T getComposer(Class<T> _composerClass) {
		return (T) composers.get(_composerClass);
	}

	public void setComposer(SelectorComposer<Component> _composer) {
		composers.put(_composer.getClass(), _composer);
	}

	public static FnCntProxy register(SelectorComposer<Component> _c) {
		log.debug("register::Start");
		Page page = Executions.getCurrent().getDesktop().getPage("fnCntTemplateRoot");
//		System.out.println("page: " + page);
		Component rootCpn = page.getFellowIfAny("fnCntTemplateRoot");
//		System.out.println("rootCpn: " + rootCpn);
		FnCntTemplateComposer composer = (FnCntTemplateComposer) rootCpn.getAttribute("$composer");
//		System.out.println("composer: " + composer);
//		System.out.println("composer.getClass().getSimpleName(): " + composer.getClass().getSimpleName());
//		log.debug("Executions.getCurrent().getAttributes..........................");
//		log.debug("Executions.getCurrent(): {}", Executions.getCurrent());
//		log.debug("{}", Executions.getCurrent().getAttributes());
//		log.debug("Executions.getCurrent().getDesktop().getAttributes..........................");
//		log.debug("{}", Executions.getCurrent().getDesktop().getAttributes());

//		FnCntProxy proxy = (FnCntProxy) Executions.getCurrent().getAttribute(ATT_FN_CNT_PROXY);
		FnCntProxy proxy = (FnCntProxy) page.getAttribute(ATT_FN_CNT_PROXY);
		if (proxy == null) {
			proxy = new FnCntProxy(composer);
			log.debug("new proxy");
			log.debug("Executions.getCurrent(): {}", Executions.getCurrent());
//			Executions.getCurrent().setAttribute(ATT_FN_CNT_PROXY, proxy);
			page.setAttribute(ATT_FN_CNT_PROXY, proxy);
		}
		log.debug("proxy: {}", proxy);
		proxy.setComposer(_c);
		return proxy;
	}

//	@Deprecated
//	public static FnCntProxy register() {
//		Page page = Executions.getCurrent().getDesktop().getPage("fnCntTemplateRoot");
////		System.out.println("page: " + page);
//		Component rootCpn = page.getFellowIfAny("fnCntTemplateRoot");
////		System.out.println("rootCpn: " + rootCpn);
//		FnCntTemplateComposer composer = (FnCntTemplateComposer) rootCpn.getAttribute("$composer");
////		System.out.println("composer: " + composer);
////		System.out.println("composer.getClass().getSimpleName(): " + composer.getClass().getSimpleName());
//
//		FnCntProxy proxy = new FnCntProxy(composer);
//		return proxy;
//	}

	// -------------------------------------------------------------------------------
	public void setFnOpen(boolean _open) {
		composer.setFnOpen(_open);
	}

	// -------------------------------------------------------------------------------
	public void refreshFnUri(String _uri) {
		composer.refreshFnUri(_uri);
	}

//	public void refreshFnUri(String _uri, Map<String, Object> _dynamicProperties) {
//		composer.refreshFnUri(_uri, _dynamicProperties);
//	}

	/** @deprecated _r may not be required? */
	public void refreshFnUri(String _uri, Runnable _r) {
		composer.refreshFnUri(_uri, _r);
	}

	public void refreshCntUri(String _uri) {
		composer.refreshCntUri(_uri);
	}

//	public void refreshCntUri(String _uri, Map<String, Object> _dynamicProperties) {
//		composer.refreshCntUri(_uri, _dynamicProperties);
//	}
	/** @deprecated _r may not be required? */
	public void refreshCntUri(String _uri, Runnable _r) {
		composer.refreshCntUri(_uri, _r);
	}

	public void refreshPage(Include _iclSubpage, String _uri) {
		composer.refreshPage(_iclSubpage, _uri);
	}

//	public void refreshPage(Include _iclSubpage, String _uri, Map<String, Object> _dynamicProperties) {
//		composer.refreshPage(_iclSubpage, _uri, _dynamicProperties);
//	}
	/** @deprecated _r may not be required? */
	public void refreshPage(Include _iclSubpage, String _uri, Runnable _r) {
		composer.refreshPage(_iclSubpage, _uri, _r);
	}
}
