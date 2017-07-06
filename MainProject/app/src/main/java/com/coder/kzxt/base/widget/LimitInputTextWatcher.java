package com.coder.kzxt.base.widget;

/**
 * Created by MaShiZhao on 2017/6/28
 */

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 *
 * ediText 筛选提吹除了中文 英文 下划线的 其他字符
 */
public class LimitInputTextWatcher implements TextWatcher
{
    /**
     * et
     */
    private EditText et = null;
    /**
     * 筛选条件
     */
    private String regex;
    /**
     * 默认的筛选条件(正则:只能输入中文 英文)
     */
    private String DEFAULT_REGEX = "[^a-zA-Z\u4E00-\u9FA5_]";

    /**
     * 构造方法
     *
     * @param et
     */
    public LimitInputTextWatcher(EditText et) {
        this.et = et;
        this.regex = DEFAULT_REGEX;
    }

    /**
     * 构造方法
     *
     * @param et    et
     * @param regex 筛选条件
     */
    public LimitInputTextWatcher(EditText et, String regex) {
        this.et = et;
        this.regex = regex;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String str = editable.toString();
        String inputStr = clearLimitStr(regex, str);
        et.removeTextChangedListener(this);
        // et.setText方法可能会引起键盘变化,所以用editable.replace来显示内容
        editable.replace(0, editable.length(), inputStr.trim());
        et.addTextChangedListener(this);

    }

    /**
     * 清除不符合条件的内容
     *
     * @param regex
     * @return
     */
    private String clearLimitStr(String regex, String str) {
        return str.replaceAll(regex, "");
    }
}