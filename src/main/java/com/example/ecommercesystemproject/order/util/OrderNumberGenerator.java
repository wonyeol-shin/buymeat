package com.example.ecommercesystemproject.order.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class OrderNumberGenerator {
    private OrderNumberGenerator(){
    }

    public static String generate() {
        String date = LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE);

        String randomValue = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        return date + "-" + randomValue;
    }

}
