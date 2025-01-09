package me.silvernine.tutorial.util;

import java.util.UUID;

public class TokenGenerator {

    public static String createToken(String mdn, String tid) {
        return UUID.randomUUID().toString().replace("-", "") + ":" + mdn + ":" + tid;
    }
}