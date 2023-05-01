package org.example.util;

import java.time.Instant;
import java.util.Random;

public class CommonUtil {
    public static String getTimeStamps(){
        return String.valueOf(Instant.now().getEpochSecond());
    }
    public static long getTimeStampsWithLong(){
        return Instant.now().getEpochSecond();
    }
    public static String randomString(int length) {
        String source = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        char[] sourceChars = source.toCharArray();
        Random random = new Random();
        for (int i = sourceChars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = sourceChars[i];
            sourceChars[i] = sourceChars[j];
            sourceChars[j] = temp;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(sourceChars[i]);
        }
        return result.toString();
    }
}
