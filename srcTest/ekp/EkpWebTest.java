package ekp;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import legion.SystemInfoDefault;
import legion.openai.ChatBot;

public class EkpWebTest extends AbstractEkpInitTest{
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);
	@Test
	public void test0() {
		log.debug("test0");
		log.debug("SystemInfoDefault.getInstance().getVersion(): {}", SystemInfoDefault.getInstance().getVersion());
	}

	@Test
	public void testOpenaiChatBot() {
//		String apiKey = "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";
//		String apiKey = "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";
		String apiKey = "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";
		
		
		String input = "請幫我解析這段話的intent和entity:”55A3-Z0多少錢？”";
		log.debug("input: {}", input);
		String output = ChatBot.sendQuery(input, apiKey);
		log.debug("output: {}", output);

	}
	
	@Test
	public void parseJson() {
		
	}
}
