package ekp.chatbot;

public enum EkpEntity {
	UNDEFINED("UNDEFINED"), RFI("request for information");

	private String resolution;

	private EkpEntity(String resolution) {
		this.resolution = resolution;
	}

	public static EkpEntity get(String _resolution) {
		for (EkpEntity e : values())
			if (e.resolution.equalsIgnoreCase(_resolution))
				return e;
		return UNDEFINED;
	}
}
