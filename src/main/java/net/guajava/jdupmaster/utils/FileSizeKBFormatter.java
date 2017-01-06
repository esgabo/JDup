package net.guajava.jdupmaster.utils;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.NumberFormat;

class FileSizeKBFormatter implements FileSizeFormatter {
    private static final int BYTES_PER_KB = 1024;
    private NumberFormat decimalFormat;

    public FileSizeKBFormatter(NumberFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    @Override
    public String format(long sizeInBytes) {
        double size = (double) sizeInBytes / BYTES_PER_KB;
        return decimalFormat.format(size).concat(" KB");
    }
}
