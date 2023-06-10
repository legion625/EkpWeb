package ekp.chatbot;

public enum EkpIntent {
	UNDEFINED(-1, "UNDEFINED", ""), //
	NONE(0, "None", "None"), //
	// TODO
//	EK_DEF(1, "EK.Def", "Ask the definition."), EK_SEARCH_DOC(2, "EK.SearchDoc", "Search document on EKP."), //
	//
	;
	private int index;
	private String name;
	private String description;

	private EkpIntent(int index, String name, String description) {
		this.index = index;
		this.name = name;
		this.description = description;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public static EkpIntent get(int _index) {
		for (EkpIntent i : EkpIntent.values())
			if (i.index == _index)
				return i;
		return UNDEFINED;
	}
	
	public static EkpIntent get(String _name) {
		for (EkpIntent i : EkpIntent.values())
			if (i.name.equalsIgnoreCase(_name))
				return i;
		return UNDEFINED;
	}
}
