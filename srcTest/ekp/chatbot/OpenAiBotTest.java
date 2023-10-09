package ekp.chatbot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ekp.AbstractEkpInitTest;
import ekp.TestLogMark;
import ekp.chatbot.OpenAiBot.EntityType;
import ekp.chatbot.OpenAiBot.IntentType;
import ekp.util.DataUtil;
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
//		private String[] entityOutputs;
		private String[] response;

		private ResultLine(String utterance, IntentType expected) {
			super();
			this.utterance = utterance;
			this.expected = expected;
			this.actual = bot.getIntent(utterance);
//			this.actual = expected;
			this.match = expected == actual;
//			entityOutputs = new String[] { //
//					bot.getEntityResponse(utterance, EntityType.E11), //
////					bot.getEntityResponse(utterance, EntityType.E12), //
//			};
//			actual.getResponse(utterance, apiKey);
//			actual = bot.getIntent(utterance);
//			log.debug("{} start", utterance);
//			response = bot.getResponseNew(utterance);
//			for(String r: response)
//			log.debug("r: {}", r);
			
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

//		public String[] getEntityOutputs() {
//			return entityOutputs;
//		}
		
		public String[] getResponse() {
			return response;
		}
		
	}
	
	@Test
//	@Ignore
	public void test_run_01() {
		List<ResultLine> resultLineList = new ArrayList<>();
		
		/* 11 */
		resultLineList.add(new ResultLine("有多少種產品", I11));
		resultLineList.add(new ResultLine("賣哪些東西?", I11));
		resultLineList.add(new ResultLine("Show me the product family", I11));
		resultLineList.add(new ResultLine("Show me the product config family", I11));
		
		/* 12 */
		resultLineList.add(new ResultLine("What's the bom structure of A", I12));
		resultLineList.add(new ResultLine("What's the bom structure of Afa", I12));
		resultLineList.add(new ResultLine("A的料表?", I12));
		resultLineList.add(new ResultLine("Afa的料表?", I12));
		
		/* 13 */
		resultLineList.add(new ResultLine("What's the cost of PART_A", I13));
		resultLineList.add(new ResultLine("What's the cost of PART_Afa", I13));
		resultLineList.add(new ResultLine("PART_Afa多少錢", I13));
		
		/* 21.查詢銷售訂單 */
		resultLineList.add(new ResultLine("有哪些銷售訂單", I21));
		resultLineList.add(new ResultLine("有哪些客戶", I21));
		
		/* 31.查詢購案 */
		resultLineList.add(new ResultLine("有購買過哪件料件", I31));
		resultLineList.add(new ResultLine("有哪些供應商", I31));
		
		/* 41.查詢工令 */
		resultLineList.add(new ResultLine("有哪些自製工令?", I41));
		resultLineList.add(new ResultLine("有什麼工件正在製造?", I41));
		
		/* 90 */
		resultLineList.add(new ResultLine("這個chatbot有操作說明嗎?", I90));
		resultLineList.add(new ResultLine("What can you do?", I90));
		resultLineList.add(new ResultLine("How can you help me?", I90));
		/* 99 */
		resultLineList.add(new ResultLine("你有女朋友嗎", I99));
		resultLineList.add(new ResultLine("今天會下雨嗎", I99));

		for (ResultLine rl : resultLineList) {
//			log.debug("{}\t{}\t{}\t{}\t{}", rl.utterance, rl.expected, rl.actual, rl.isMatch(), rl.getEntityOutputs());
//			log.debug("{}\t{}\t{}\t{}\t{}", rl.utterance, rl.expected, rl.actual, rl.isMatch(), rl.getResponse());
			log.debug("{}\t{}\t{}\t{}", rl.utterance, rl.expected, rl.actual, rl.isMatch());
//			log.debug("{}\t{}", rl.utterance, rl.getResponse());
		}
	}
	
	// -------------------------------------------------------------------------------


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
	@Test
	@Ignore
	public void test2() {
//		[DEBUG] [06-12 22:37:57] [main] OpenAiBotTest[ 106]  => 有多少種產品	I11	I11	true	[["{產品編號} and {產品名稱}"]]
//		[DEBUG] [06-12 22:37:57] [main] OpenAiBotTest[ 106]  => 賣哪些東西?	I11	I11	true	[["{產品編號}：empty string\n{產品名稱}：empty string"]]
//		[DEBUG] [06-12 22:37:57] [main] OpenAiBotTest[ 106]  => Show me the product family	I11	I11	true	[["{empty string}"]]
//		[DEBUG] [06-12 22:37:57] [main] OpenAiBotTest[ 106]  => Show me the product config family	I11	I11	true	[["{empty string}"]]
//		[DEBUG] [06-12 22:37:57] [main] OpenAiBotTest[ 106]  => What's the bom structure of 55A3-Z0	I12	I12	true	[["Potential 產品編號 or 產品名稱: {55A3-Z0}"]]
//		[DEBUG] [06-12 22:37:57] [main] OpenAiBotTest[ 106]  => What's the structure of 55A3-Z0	I12	I12	true	[["Potential product code or name: {55A3-Z0}"]]
//		[DEBUG] [06-12 22:37:57] [main] OpenAiBotTest[ 106]  => What's the cost of 55A3-Z0	I13	I13	true	[["Potential 產品編號 or 產品名稱: {55A3-Z0}"]]
//		[DEBUG] [06-12 22:37:57] [main] OpenAiBotTest[ 106]  => What's the cost of big mac	I13	I13	true	[["Potential 產品名稱: {big mac}"]]
//		[DEBUG] [06-12 22:37:57] [main] OpenAiBotTest[ 106]  => 55A3-Z0多少錢？	I13	I13	true	[["Potential 產品編號 or 產品名稱: {55A3-Z0}"]]
//		[DEBUG] [06-12 22:37:57] [main] OpenAiBotTest[ 106]  => 這個chatbot有操作說明嗎?	I90	I90	true	[["{empty string}"]]
//		[DEBUG] [06-12 22:37:57] [main] OpenAiBotTest[ 106]  => 你有女朋友嗎	I99	I99	true	[Error: JSONObject["error"] not a string.]
		
//		String input = "This is {an example} string {with} curly {brackets}";
//		String input = "This is an example string with curly brackets";

//		String input = "Error sending request: JSONObject[\"error\"] not a string.";
		
//		String input = "{\"臺灣牛\"}";
//		String input = "{產品名稱: 臺灣牛}";
		String input = "\"產品編號: 無\n產品名稱: 臺灣牛\"";	
		
		for (String n : EntityType.E11.entityNames)
			input = input.replaceAll(n, "");

		List<String> entityValuesList = DataUtil.findInCurlyBrackets(input);
		log.debug("{}", entityValuesList.stream().collect(Collectors.joining(", ")));
		entityValuesList = entityValuesList.stream().map(DataUtil::sanitize).collect(Collectors.toList()); // 消毒
		log.debug("{}", entityValuesList.stream().collect(Collectors.joining(", ")));
		
//		// 定義正規表達式，匹配大括號的模式
//		String regex = "\\{([^\\}]+)\\}";
//		// 創建 Pattern 物件
//		Pattern pattern = Pattern.compile(regex);
//
//		// 創建 Matcher 物件，並應用正規表達式到輸入字串
//		Matcher matcher = pattern.matcher(input);
//
//		// 逐一尋找匹配的字串，並輸出結果
//		List<String> list = new ArrayList<>();
//		while (matcher.find()) {
//			String match = matcher.group(1); // 取得大括號內容
//			list.add(match);
//		}
//		log.debug("list.size(): {}", list.size());
//		log.debug("{}", list.stream().collect(Collectors.joining(", ")));
	}
}
