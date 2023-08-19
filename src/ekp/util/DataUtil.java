package ekp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import legion.util.DataFO;

public class DataUtil {
	public final static String NO_DATA = "(No data)";
	
	public static String nodataIfEmpty(String _str) {
		return DataFO.isEmptyString(_str) ? NO_DATA : _str;
	}

	public static <T> String nodataIfEmpty(T _t, Function<T, String> _fnGetValue) {
		if (_t == null || _fnGetValue == null)
			return NO_DATA;
		return nodataIfEmpty(_fnGetValue.apply(_t));
	}
	
	// -------------------------------------------------------------------------------
	public static List<String> findInCurlyBrackets(String _s) {
		// 定義正規表達式，匹配大括號的模式
		String regex = "\\{([^\\}]+)\\}";
		// 創建 Pattern 物件
		Pattern pattern = Pattern.compile(regex);

		// 創建 Matcher 物件，並應用正規表達式到輸入字串
		Matcher matcher = pattern.matcher(_s);

		// 逐一尋找匹配的字串，並輸出結果
		List<String> list = new ArrayList<>();
		while (matcher.find()) {
			String match = matcher.group(1); // 取得大括號內容
			list.add(match);
		}
		return list;
	}
	
	public static int findInt(String _s) throws NumberFormatException {
		Pattern pattern = Pattern.compile("\\d+"); // 匹配一个或多个数字
		Matcher matcher = pattern.matcher(_s);
		if (matcher.find()) {
			String numberString = matcher.group(); // 获取匹配到的字符串
			int number = Integer.parseInt(numberString); // 转换为整数类型
//            System.out.println(number);  // 输出结果：42
			return number;
		} else
			throw new NumberFormatException("Integer not found");
	}
	
	public static String sanitize(String original) {
		 // 定义正则表达式，匹配可能导致 SQL 异常的特殊字符
        String regex = "[';\";:]";
        // 替换特殊字符为空字符串
        return  original.replaceAll(regex, "").strip();
	}

	public static boolean matchesBizAdminNum(String _ban) {
		Pattern TWBID_PATTERN = Pattern.compile("^[0-9]{8}$");
		boolean result = false;
		String weight = "12121241";
		boolean type2 = false; // 第七個數是否為七
		if (TWBID_PATTERN.matcher(_ban).matches()) {
			int tmp = 0, sum = 0;
			for (int i = 0; i < 8; i++) {
//		tmp = (Integer.parseInt (String.valueOf(_ban.charAt(i))))* (weight.charAt(i) – '0');
				tmp = (Integer.parseInt(String.valueOf(_ban.charAt(i))))
						* (Integer.parseInt(String.valueOf(weight.charAt(i))));
				sum += (int) (tmp / 10) + (tmp % 10); // 取出十位數和個位數相加
				if (i == 6 && _ban.charAt(i) == '7') {
					type2 = true;
				}
			}
			if (type2) {
				if ((sum % 10) == 0 || ((sum + 1) % 10) == 0) { // 如果第七位數為7
					result = true;
				}
			} else {
				if ((sum % 10) == 0) {
					result = true;
				}
			}
		}
		return result;
	}
}
