package ch.keller.camel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public final class MemoryFormater {

    private MemoryFormater() {
        // empty
    }

    public static String format(long l) {
        final DecimalFormat df = new DecimalFormat("###,###");
        final DecimalFormatSymbols symb = new DecimalFormatSymbols();
        symb.setGroupingSeparator('\'');
        df.setDecimalFormatSymbols(symb);
        return df.format(l);
    }

}
