package com.intelligentcarmanagement.carmanagementapp.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.intelligentcarmanagement.carmanagementapp.R;

public class ValidationTextWatcher implements TextWatcher {
    private static final String TAG = "ValidationTextWatcher";
    private View view;
    private TextInputLayout viewLayout;
    public ValidationTextWatcher(View view, TextInputLayout viewLayout) {
        this.view = view;
        this.viewLayout = viewLayout;
    }
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    public void afterTextChanged(Editable editable) {
        switch (view.getId()) {
            case R.id.login_password:
                PasswordValidator.validatePassword((TextInputEditText) view, viewLayout);
                break;
            case R.id.login_email:
                EmailValidator.validateEmail((TextInputEditText) view, viewLayout);
                break;
        }
    }
}
