package com.intelligentcarmanagement.carmanagementapp.utils;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EmailValidator {
    public static boolean validateEmail(TextInputEditText emailEditText, TextInputLayout emailLayout) {
        if (emailEditText.getText().toString().trim().isEmpty()) {
            emailLayout.setErrorEnabled(false);
        } else {
            String emailId = emailEditText.getText().toString();
            Boolean  isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
            if (!isValid) {
                emailLayout.setError("Invalid Email address, ex: abc@example.com");
                emailLayout.requestFocus();
                return false;
            } else {
                emailLayout.setErrorEnabled(false);
            }
        }
        return true;
    }
}
