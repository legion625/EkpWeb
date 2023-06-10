package ekp.util;

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
}
