package ekp.web.control.zk.common;

import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

import ekp.DebugLogMark;
import legion.util.query.QueryOperation.CompareOp;
import legion.util.query.QueryOperation.QueryValue;
import legion.type.IdEnum;
import legion.type.IdxEnum;
import legion.util.DataFO;
import legion.util.query.QueryOperation;
import legion.util.query.QueryParam;
import legion.web.zk.ZkUtil;

public class NormalSearchLine<P extends QueryParam, T> {
	private static Logger log = LoggerFactory.getLogger(DebugLogMark.class);
	
	private String name;
	private InputElement inputElem;
	private Function<InputElement, Object> fnParseInput;
	private Function<Object, QueryValue<P, T>> fnParseInputQueryValue; // 若回傳null，代表不設定此查詢條件。

	private NormalSearchLine(String name, InputElement inputElem, Function<InputElement, Object> fnParseInput,
			Function<Object, QueryValue<P, T>> fnParseInputQueryValue) {
		this.name = name;
		this.inputElem = inputElem;
		this.fnParseInput = fnParseInput;
		this.fnParseInputQueryValue = fnParseInputQueryValue;
	}
	
	// -------------------------------------------------------------------------------
	public Label getLbName() {
		return new Label(name);
	}
	
	public InputElement getInputElem() {
		return inputElem;
	}

	public QueryValue<P,T> packQueryValue() {
		Object input = fnParseInput.apply(inputElem);
		return input == null ? null : fnParseInputQueryValue.apply(input);
	}

	
	
	
	// -------------------------------------------------------------------------------
	public static <P extends QueryParam, T> NormalSearchLine<P, T> ofTxbLine(String _name, P _p) {
		Function<InputElement, Object> fnParseInput = txb -> ((Textbox) txb).getValue();
		Function<Object, QueryValue<P, T>> fnParseInputQueryValue = o -> {
			log.debug("o: {}", o);
			
			if (o == null || !(o instanceof String) || DataFO.isEmptyString((String) o))
				return null; // 用null代表不設定此查詢條件
			else {
				log.debug("o: {}", o);
				return (QueryValue<P, T>) QueryOperation.value(_p, CompareOp.like, "%" + o + "%");
			}
				
		};

		//
		NormalSearchLine<P, T> nsl = new NormalSearchLine<>(_name, new Textbox(), fnParseInput, fnParseInputQueryValue);
		return nsl;
	}

	public static <P extends QueryParam, T> NormalSearchLine<P, T> ofCbbLine(String _name, IdEnum[] _idEnums, P _p) {

		// inputElement
		Combobox cbb = new Combobox();
		ZkUtil.initCbb(cbb, _idEnums, true);

		//
		Function<InputElement, Object> fnParseInput = ie -> {
			Comboitem cbi = ((Combobox) ie).getSelectedItem();
			if (cbi == null)
				return null;
			return cbb.getSelectedItem().getValue();
		};

		//
		Function<Object, QueryValue<P, T>> fnParseInputQueryValue = o -> {
			if (o == null || !(o instanceof IdEnum))
				return null; // 用null代表不設定此查詢條件
			else
				return (QueryValue<P, T>) QueryOperation.value(_p, CompareOp.equal, ((IdEnum) o).getId());
		};

		//
		NormalSearchLine<P, T> nsl = new NormalSearchLine<>(_name, cbb, fnParseInput, fnParseInputQueryValue);
		return nsl;
	}
	
	public static <P extends QueryParam, T> NormalSearchLine<P, T> ofCbbLine(String _name, IdxEnum[] _idxEnums, P _p) {

		// inputElement
		Combobox cbb = new Combobox();
		ZkUtil.initCbb(cbb, _idxEnums, true);

		//
		Function<InputElement, Object> fnParseInput = ie -> {
			Comboitem cbi = ((Combobox) ie).getSelectedItem();
			if (cbi == null)
				return null;
			return cbb.getSelectedItem().getValue();
		};

		//
		Function<Object, QueryValue<P, T>> fnParseInputQueryValue = o -> {
			if (o == null || !(o instanceof IdxEnum))
				return null; // 用null代表不設定此查詢條件
			else
				return (QueryValue<P, T>) QueryOperation.value(_p, CompareOp.equal, ((IdxEnum) o).getIdx());
		};

		//
		NormalSearchLine<P, T> nsl = new NormalSearchLine<>(_name, cbb, fnParseInput, fnParseInputQueryValue);
		return nsl;
	}
}
