package ekp.chatbot;

public class Line {
	private BotChatStatus chatStatus;
	private BotSpeaker speaker;
	private String utterance;

	Line(BotChatStatus chatStatus, BotSpeaker speaker, String utterance) {
		this.chatStatus = chatStatus;
		this.speaker = speaker;
		this.utterance = utterance;
	}

	public BotChatStatus getChatStatus() {
		return chatStatus;
	}

	public BotSpeaker getSpeaker() {
		return speaker;
	}

	public String getUtterance() {
		return utterance;
	}
}
