package org.d3ifcool.nabung.base;

import java.text.NumberFormat;
import java.util.Locale;

public class MethodFunction {

    public String currencyIdr(int total){
        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        return format.format(total);
    }

}
