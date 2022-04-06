package com.intelligentcarmanagement.carmanagementapp.utils;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PasswordValidator {
    public static boolean validatePassword(TextInputEditText passwordEditText, TextInputLayout passwordLayout) {
        if (passwordEditText.getText().toString().trim().isEmpty()) {
            passwordLayout.setError("Password is required");
            passwordLayout.requestFocus();
            return false;
        }else if(passwordEditText.getText().toString().length() < 3){
            passwordLayout.setError("Password can't be less than 3 digit");
            passwordLayout.requestFocus();
            return false;
        }
        else {
            passwordLayout.setErrorEnabled(false);
        }
        return true;
    }
}
