package ekp.mbom.issue.prodMod;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.DebugLogMark;
import ekp.data.service.mbom.ProdCtlInfo;
import ekp.data.service.mbom.ProdInfo;

public class ProdModBuilder1 extends ProdModBuilder{
	private Logger log =LoggerFactory.getLogger(DebugLogMark.class);
	
	/* base */
	private ProdInfo prod;
	// none

	/* data */
	private List<ProdModItemBuilder> pmiBuilderList;

	// -------------------------------------------------------------------------------
	@Override
	protected ProdModBuilder1 appendBase() {
		/* base */
		prod = (ProdInfo) args[0];

		appendProdUid(prod.getUid());

		// pmiBuilderList
		pmiBuilderList = new ArrayList<>();
		for (ProdCtlInfo prodCtlLv1 : prod.getProdCtlListLv1())
			flatPmiBuilderList( prodCtlLv1, pmiBuilderList);
		log.debug("pmiBuilderList.size(): {}", pmiBuilderList.size());

		return this;
	}

	private void flatPmiBuilderList(ProdCtlInfo _prodCtl, List<ProdModItemBuilder> _pmiBuilderList) {
		ProdModItemBuilder pmiBuilder = new ProdModItemBuilder();
		pmiBuilder.appendProdCtlUid(_prodCtl.getUid());
		_pmiBuilderList.add(pmiBuilder);
		for (ProdCtlInfo _childProdCtl : _prodCtl.getChildrenList())
			flatPmiBuilderList(_childProdCtl, _pmiBuilderList);
	}

	// -------------------------------------------------------------------------------
	@Override
	public ProdModBuilder1 appendId(String id) {
		return (ProdModBuilder1) super.appendId(id);
	}

	@Override
	public ProdModBuilder1 appendName(String name) {
		return (ProdModBuilder1) super.appendName(name);
	}

	@Override
	public ProdModBuilder1 appendDesp(String desp) {
		return (ProdModBuilder1) super.appendDesp(desp);
	}
	
	// -------------------------------------------------------------------------------
	@Override
	protected List<ProdModItemBuilder> getProdModItemBuilderList() {
		return pmiBuilderList;
	}
}
