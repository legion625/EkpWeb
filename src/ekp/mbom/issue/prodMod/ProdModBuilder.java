package ekp.mbom.issue.prodMod;

import java.util.List;

import ekp.data.MbomDataService;
import ekp.data.service.mbom.ProdModCreateObj;
import ekp.data.service.mbom.ProdModInfo;
import ekp.data.service.mbom.ProdModItemInfo;
import legion.DataServiceFactory;
import legion.biz.Bpu;
import legion.util.DataFO;
import legion.util.TimeTraveler;

public abstract class ProdModBuilder extends Bpu<ProdModInfo> {
	
	private static MbomDataService mbomDataService = DataServiceFactory.getInstance().getService(MbomDataService.class);

	/* base */
	private String prodUid; // 對應的產品項 biz key
	private String id; // 識別碼 biz key
	private String name;
	private String desp;

	/* data */
	// none

	// -------------------------------------------------------------------------------
	// -----------------------------------appender------------------------------------
	protected ProdModBuilder appendProdUid(String prodUid) {
		this.prodUid = prodUid;
		return this;
	}

	protected ProdModBuilder appendId(String id) {
		this.id = id;
		return this;
	}

	protected ProdModBuilder appendName(String name) {
		this.name = name;
		return this;
	}

	protected ProdModBuilder appendDesp(String desp) {
		this.desp = desp;
		return this;
	}

	// -------------------------------------------------------------------------------
	// ------------------------------------getter-------------------------------------
	public String getProdUid() {
		return prodUid;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDesp() {
		return desp;
	}

	// -------------------------------------------------------------------------------
	private ProdModCreateObj packProdModCreateObj() {
		ProdModCreateObj dto = new ProdModCreateObj();
		dto.setProdUid(getProdUid());
		dto.setId(getId());
		dto.setName(getName());
		dto.setDesp(getDesp());
		return dto;
	}

	// -------------------------------------------------------------------------------
	protected abstract List<ProdModItemBuilder> getProdModItemBuilderList();
	
	// -------------------------------------------------------------------------------
	@Override
	public boolean validate(StringBuilder _msg) {
		return true;
	}

	@Override
	public boolean verify(StringBuilder _msg, boolean _full) {
		boolean v = true;

		//
		if (DataFO.isEmptyString(getProdUid())) {
			_msg.append("prodUid should not be empty.").append(System.lineSeparator());
			v = false;
		}
		
		//
		if (DataFO.isEmptyString(getId())) {
			_msg.append("Id should not be empty.").append(System.lineSeparator());
			v = false;
		} else {
			if (mbomDataService.loadProdModById(getId()) != null) {
				_msg.append("Duplicated id.").append(System.lineSeparator());
				v = false;
			}
		}
		
		//
		if (DataFO.isEmptyString(getName())) {
			_msg.append("Name should not be empty.").append(System.lineSeparator());
			v = false;
		}
		
		return v;
	}
	
	// -------------------------------------------------------------------------------
	@Override
	protected ProdModInfo buildProcess(TimeTraveler _tt) {
		TimeTraveler tt = new TimeTraveler();

		//
		ProdModInfo prodMod = mbomDataService.createProdMod(packProdModCreateObj());
		if (prodMod == null) {
			tt.travel();
			log.error("mbomDataSerivce.createProdMod return null.");
			return null;
		}
		tt.addSite("revert createProdMod", () -> mbomDataService.deleteProdMod(prodMod.getUid()));
		log.info("mbomDataService.createProdMod [{}][{}]", prodMod.getUid(), prodMod.getId());
		
		//
		for (ProdModItemBuilder prodModItemBuilder : getProdModItemBuilderList()) {
			prodModItemBuilder.appendProdModUid(prodMod.getUid());
			ProdModItemInfo prodModItem = prodModItemBuilder.build(new StringBuilder(), tt);
			if (prodModItem == null) {
				tt.travel();
				log.error("prodModItemBuilder.build return null.");
				return null;
			}
			// copy sites inside
		}

		//
		if (_tt != null)
			_tt.copySitesFrom(tt);

		return prodMod;
	}
}
