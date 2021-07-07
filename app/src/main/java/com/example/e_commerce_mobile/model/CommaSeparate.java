package com.example.e_commerce_mobile.model;

import java.text.NumberFormat;
import java.util.Locale;

public class CommaSeparate {

    public static String getFormatedNumber(String number){
        if(number!=null) {
            try {
                double val = Double.parseDouble(number);
                return NumberFormat.getNumberInstance(Locale.US).format(val);
            }catch (Exception e){
                return "Erreur";
            }

        }else{
            return "XX,XXX";
        }
    }
}

