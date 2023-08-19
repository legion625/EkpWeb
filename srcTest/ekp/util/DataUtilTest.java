package ekp.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ekp.AbstractEkpInitTest;

public class DataUtilTest extends AbstractEkpInitTest {
	@Test
	public void testMatchesBizAdminNum() {
		assertTrue(DataUtil.matchesBizAdminNum("45002910")); // 中科院
		assertTrue(DataUtil.matchesBizAdminNum("22099131")); // 台積電
		assertTrue(DataUtil.matchesBizAdminNum("20954200")); // 群光電子
		assertTrue(DataUtil.matchesBizAdminNum("30435973")); // 飛宏
		assertTrue(DataUtil.matchesBizAdminNum("16863116")); // 新鶴達貿易股份有限公司
		assertTrue(DataUtil.matchesBizAdminNum("12357481")); // 傑地有限公司
		//
		assertFalse(DataUtil.matchesBizAdminNum("45002911"));
		assertFalse(DataUtil.matchesBizAdminNum("12345678"));
		
		
	}

}
