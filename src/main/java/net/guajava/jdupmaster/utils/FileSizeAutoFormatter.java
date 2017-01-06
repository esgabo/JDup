package net.guajava.jdupmaster.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.NumberFormat;

public class FileSizeAutoFormatter implements FileSizeFormatter {

    private static final int BYTES_PER_KB = 1024;
    private static final double BYTES_PER_MB = 1048576d;

    private final FileSizeFormatter bFormatter;
    private final FileSizeFormatter kbFormatter;
    private final FileSizeFormatter mbFormatter;

    public FileSizeAutoFormatter(NumberFormat decimalFormat) {
        this.bFormatter = new FileSizeBFormatter();
        this.kbFormatter = new FileSizeKBFormatter(decimalFormat);
        this.mbFormatter = new FileSizeMBFormatter(decimalFormat);
    }

    @Override
    public String format(long sizeInBytes) {
        FileSizeFormatter formatter;
        if (sizeInBytes <= BYTES_PER_KB) {
            formatter = bFormatter;
        } else if (sizeInBytes <= BYTES_PER_MB) {
            formatter = kbFormatter;
        } else {
            formatter = mbFormatter;
        }

        return formatter.format(sizeInBytes);
    }
}
