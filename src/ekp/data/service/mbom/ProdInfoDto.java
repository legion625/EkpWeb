package ekp.data.service.mbom;

import legion.ObjectModelInfoDto;

public class ProdInfoDto extends ObjectModelInfoDto implements ProdInfo {

	protected ProdInfoDto(String uid, long objectCreateTime, long objectUpdateTime) {
		super(uid, objectCreateTime, objectUpdateTime);
	}

	private String id; // 型號 biz key
	private String name; // 名稱

	@Override
	public String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}
}
