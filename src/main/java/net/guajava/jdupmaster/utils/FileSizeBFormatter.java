package net.guajava.jdupmaster.utils;

import java.text.DecimalFormat;

class FileSizeBFormatter implements FileSizeFormatter {

    @Override
    public String format(long sizeInBytes) {
        return String.valueOf(sizeInBytes).concat(" B");
    }
}
