package com.raph_furniture.utils;

//Add your annotations here

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class PaymentUtils {

    public static String generateTimestamp() {

        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public static String generatePassword(String shortcode, String passkey, String timestamp) {
        String data = shortcode + passkey + timestamp;

        return Base64.getEncoder().encodeToString(data.getBytes());
    }
}
