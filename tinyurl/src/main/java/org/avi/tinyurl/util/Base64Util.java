package org.avi.tinyurl.util;

import java.util.Base64;

public class Base64Util {
    public static String encodeUrl(String url) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(url.getBytes());
    }

    public static String decodeUrl(String slug) {
        byte[] decoded = Base64.getUrlDecoder().decode(slug);
        return new String(decoded);
    }

    public static void main(String[] args) {
        String longUrl = "https://example.com/some/very/long/path?x=1";
        String slug = encodeUrl(longUrl);
        String restored = decodeUrl(slug);

        System.out.println("Slug: " + slug);
        System.out.println("Restored: " + restored);
    }
}
