package com.example.salesbuddy.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MasksUtil {

    public static final String FORMAT_CPF = "###.###.###-##";

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    public static String unmask(String s) {
        return s.replaceAll("[^0-9]*", "");
    }

    // --- MÁSCARA DE DINHEIRO SEM R$ ---
    public static TextWatcher money(final EditText editText) {
        return new TextWatcher() {
            private boolean isUpdating = false;

            // Força o padrão brasileiro: Ponto para milhar, Vírgula para decimal
            private final DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                isUpdating = true;
                String str = s.toString();

                // 1. Limpa tudo que não for dígito numérico
                str = str.replaceAll("[^\\d]", "");

                try {
                    // Se estiver vazio, evita erro
                    if (str.isEmpty()) {
                        editText.setText("");
                        isUpdating = false;
                        return;
                    }

                    // 2. Transforma em BigDecimal e divide por 100 (centavos)
                    BigDecimal valor = new BigDecimal(str).divide(new BigDecimal(100));

                    // 3. Formata (Ex: 1500 vira 15,00)
                    String formatado = df.format(valor);

                    editText.setText(formatado);
                    editText.setSelection(formatado.length()); // Move cursor pro final
                } catch (NumberFormatException e) {
                    // Em caso de erro, limpa
                    editText.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    // Transforma "1.200,50" em "1200.50"
    public static String unmaskPrice(String valorFormatado) {
        if (valorFormatado == null || valorFormatado.isEmpty()) return "";

        String semMilhar = valorFormatado.replaceAll("\\.", "");
        return semMilhar.replace(",", ".");
    }
}