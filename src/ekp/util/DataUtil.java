package ekp.util;

import java.util.function.Function;

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
}
