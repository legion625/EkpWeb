package ekp;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import legion.SystemInfoDefault;
import legion.openai.ChatBot;

public class EkpWebTest extends AbstractEkpInitTest{
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);
	
	private static String apiKey;
	
	@BeforeClass
	public static void beforeClass() {
		apiKey = SystemInfoDefault.getInstance().getVersion();
		log.debug("api key: {}", apiKey);
	}
	
	@Test
	public void testOpenaiChatBot() {
		String input = "請幫我解析這段話的intent和entity:”55A3-Z0多少錢？”";
		log.debug("input: {}", input);
		String output = ChatBot.sendQuery(input, apiKey);
		log.debug("output: {}", output);

	}
}
