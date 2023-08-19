package ekp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import legion.SystemInfoDefault;
import legion.openai.ChatBot;
import legion.util.DateFormatUtil;

public class EkpWebTest extends AbstractEkpInitTest{
	private static Logger log = LoggerFactory.getLogger(TestLogMark.class);
	
	private static String apiKey;
	
	@BeforeClass
	public static void beforeClass() {
		apiKey = SystemInfoDefault.getInstance().getVersion();
		log.debug("api key: {}", apiKey);
	}
	
//	@Test
//	public void testOpenaiChatBot() {
//		String input = "請幫我解析這段話的intent和entity:”55A3-Z0多少錢？”";
//		log.debug("input: {}", input);
//		String output = ChatBot.sendQuery(input, apiKey);
//		log.debug("output: {}", output);
//
//	}
	
//	@Test
//	public void testDate() {
//		
//		Date now = new Date(System.currentTimeMillis());
//		log.debug("{}, {}, {}",now.getTime(), DateFormatUtil.transToDate(now), DateFormatUtil.getEarliestTimeInDate(now).getTime());
//		
//		LocalDate ld = DateFormatUtil.parseLocalDate(now);
//		LocalDate ld2 = ld.plusYears(2);
////		ld2.getLong(ChronoField.)
////		Date.from(ld.)
//		long l = ld.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
//		log.debug("l: {}", l);
//		
////		DateFormatUtil.
//	}
}
