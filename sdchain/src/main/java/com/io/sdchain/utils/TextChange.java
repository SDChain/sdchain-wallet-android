package com.io.sdchain.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by xiey on 2017/11/7.
 */

public class TextChange implements TextWatcher {

    public interface OnTextChange {
        void afterTextChange(Editable s);
    }

    public void setOnTextChange(OnTextChange onTextChange) {
        this.onTextChange = onTextChange;
    }

    private OnTextChange onTextChange;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        onTextChange.afterTextChange(s);
    }
}
