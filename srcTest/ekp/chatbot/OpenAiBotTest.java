package ekp.chatbot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.AbstractEkpInitTest;
import ekp.TestLogMark;
import ekp.chatbot.OpenAiBot.IntentType;
import legion.SystemInfoDefault;

import static ekp.chatbot.OpenAiBot.IntentType.*;

public class OpenAiBotTest extends AbstractEkpInitTest {
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);

//	private static String apiKey;

	private static OpenAiBot bot;
	
	@BeforeClass
	public static void beforeClass() {
//		apiKey = SystemInfoDefault.getInstance().getAttribute("openai.apiKey");
//		log.debug("api key: {}", apiKey);
		bot = new OpenAiBot();
	}
	
	
	class ResultLine {
		private String utterance;
		private IntentType expected, actual;
		private boolean match;

		private ResultLine(String utterance, IntentType expected) {
			super();
			this.utterance = utterance;
			this.expected = expected;
			this.actual = bot.getIntent(utterance);
			this.match = expected == actual;
		}

		public String getUtterance() {
			return utterance;
		}

		public IntentType getExpected() {
			return expected;
		}

		public IntentType getActual() {
			return actual;
		}

		public boolean isMatch() {
			return match;
		}
		
		
	}
	
	@Test
	public void test_run_01() {
		List<ResultLine> resultLineList = new ArrayList<>();
		
		/* 11 */
		resultLineList.add(new ResultLine("有多少種產品", I11));
		resultLineList.add(new ResultLine("賣哪些東西?", I11));
		resultLineList.add(new ResultLine("Show me the product family", I11));
		resultLineList.add(new ResultLine("Show me the product config family", I11));
		
		/* 12 */
		resultLineList.add(new ResultLine("What's the bom structure of 55A3-Z0", I12));
		resultLineList.add(new ResultLine("What's the structure of 55A3-Z0", I12));
		
		/* 13 */
		resultLineList.add(new ResultLine("What's the cost of 55A3-Z0", I13));
		resultLineList.add(new ResultLine("55A3-Z0多少錢？", I13));
		
		/* 90 */
		resultLineList.add(new ResultLine("這個chatbot有操作說明嗎?", I90));
		/* 99 */
		resultLineList.add(new ResultLine("你有女朋友嗎", I99));
		

		for (ResultLine rl : resultLineList) {
			log.debug("{}\t{}\t{}\t{}", rl.utterance, rl.expected, rl.actual, rl.isMatch());
		}
	}
	


	@Test
	@Ignore
	public void test1() {
//		String outputStr = "[\"Intent: Inquiry of Product Cost\\nEntity: Product Model - 55A3-Z0\"]";
//		JSONObject jsonObj = new JSONObject(outputStr);
//		log.debug("jsonObj: {}", jsonObj);
		String select = "[\"1.詢問指定產品的價格。\"]";
//		Pattern pattern = Pattern.compile("\\d+\\.\\d+");  // 定义匹配小数数字的正则表达式
		Pattern pattern = Pattern.compile("\\d+"); // 匹配一个或多个数字
		Matcher matcher = pattern.matcher(select);
		if (matcher.find()) {
			String numberString = matcher.group(); // 获取匹配到的字符串
			int number = Integer.parseInt(numberString); // 转换为整数类型
//            System.out.println(number);  // 输出结果：42
			log.debug("number: {}", number);
		}
	}
}
