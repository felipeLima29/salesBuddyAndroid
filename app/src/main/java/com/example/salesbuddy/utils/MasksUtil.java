package com.example.salesbuddy.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MasksUtil {
    public static final String FORMAT_CPF = "###.###.###-##";
    public static final String FORMAT_DATE = "##/##/####";

    public static TextWatcher mask(final String mask, final EditText et) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                et.setText(mascara);
                et.setSelection(mascara.length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    public static String unmask(String s) {
        return s.replaceAll("[^0-9]*", "");
    }
}
