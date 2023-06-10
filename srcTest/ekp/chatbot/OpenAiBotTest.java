package ekp.chatbot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.AbstractEkpInitTest;
import ekp.TestLogMark;

public class OpenAiBotTest extends AbstractEkpInitTest {
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);
	
	@Test
	@Ignore
	public void testA0() {
		OpenAiBot bot = new OpenAiBot();
		String utterance = "你有女朋友嗎";
//		String utterance = "55A3-Z0多少錢？";
		bot.getResponseNew(utterance);
	}
	
	
	
	@Test
	@Ignore
	public void testA1() {
		OpenAiBot bot = new OpenAiBot();
		String utterance = "有多少種產品";
//		String utterance = "55A3-Z0多少錢？";
		bot.getResponseNew(utterance);
	}
	
	@Test
//	@Ignore
	public void testA2() {
		OpenAiBot bot = new OpenAiBot();
		String utterance = "What's the structure of 55A3-Z0";
//		String utterance = "55A3-Z0多少錢？";
		bot.getResponseNew(utterance);
	}
	
	@Test
	@Ignore
	public void testA3() {
		OpenAiBot bot = new OpenAiBot();
		String utterance = "What's the cost of 55A3-Z0";
//		String utterance = "55A3-Z0多少錢？";
		bot.getResponseNew(utterance);
	}
	
	@Test
	@Ignore
	public void test1() {
//		String outputStr = "[\"Intent: Inquiry of Product Cost\\nEntity: Product Model - 55A3-Z0\"]";
//		JSONObject jsonObj = new JSONObject(outputStr);
//		log.debug("jsonObj: {}", jsonObj);
		String select = "[\"1.詢問指定產品的價格。\"]";
//		Pattern pattern = Pattern.compile("\\d+\\.\\d+");  // 定义匹配小数数字的正则表达式
		Pattern pattern = Pattern.compile("\\d+");  // 匹配一个或多个数字
        Matcher matcher = pattern.matcher(select);
        if (matcher.find()) {
            String numberString = matcher.group();  // 获取匹配到的字符串
            int number = Integer.parseInt(numberString);  // 转换为整数类型
//            System.out.println(number);  // 输出结果：42
            log.debug("number: {}", number);
        }
	}
}
