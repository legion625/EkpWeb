package legion.biz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import legion.util.LogUtil;
import legion.util.TimeTraveler;

public abstract class Bpu<U> {
	protected Logger log = LoggerFactory.getLogger(Bpu.class);

	protected Object[] args;

//	public final void init(Object[] _args) {
	public final void init(Object... _args) {
		args = _args;
		appendBase();
	}

	protected abstract Bpu<U> appendBase();

	/**
	 * 「確認」：合理性檢查（業務層級）
	 * 
	 * @param _msg
	 * @return
	 */
	public abstract boolean validate(StringBuilder _msg);

	/**
	 * 「驗證」：正確性檢查（資料結構層級）
	 * 
	 * @param _msg
	 * @param _full:有些應該檢查的屬性在build時才產生出來，這些屬性在_full為true時再檢查。若只是在Bpu的編輯階段，可透過設定_full為false作為輸入端的檢查條件。
	 * @return
	 */
	public abstract boolean verify(StringBuilder _msg, boolean _full);
	
	public final boolean verify(StringBuilder _msg) {
		return verify(_msg, false);
	}

	public final U build(StringBuilder _msg, TimeTraveler _tt) {
		if (!verify(_msg, true))
			return null;
		TimeTraveler tt = new TimeTraveler();
		try {
			U result = buildProcess(tt);
			if (result != null) {
				if (_tt != null)
					_tt.copySitesFrom(tt);
				return result;
			} else {
				tt.travel();
				return null;
			}
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
			tt.travel();
			return null;
		}
	}

	protected abstract U buildProcess(TimeTraveler _tt);

}
