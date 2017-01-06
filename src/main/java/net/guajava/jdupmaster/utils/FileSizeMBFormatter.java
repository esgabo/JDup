package net.guajava.jdupmaster.utils;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.NumberFormat;

class FileSizeMBFormatter implements FileSizeFormatter {
    private static final int BYTES_PER_MB = 1048576;
    private NumberFormat decimalFormat;

    public FileSizeMBFormatter(NumberFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    @Override
    public String format(long sizeInBytes) {
        double size = (double) sizeInBytes / BYTES_PER_MB;
        return decimalFormat.format(size).concat(" MB");
    }
}
