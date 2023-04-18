package com.lhb.player.playerapp.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by howe.zhong
 * on 2022/8/16  17:39
 */
public class UrlUtils {

    /**
     * 根据传入的URL获取一级域名
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        String domain = "";
        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            try {
                String host = Uri.parse(url).getHost();
                String scheme = Uri.parse(url).getScheme();
                domain = scheme + "://" + host;
                int port = Uri.parse(url).getPort();
                if (port != -1) {
                    domain = scheme + "://" + host + ":" + port;
                }
            } catch (Exception ex) {
            }
        }
        Log.d("getDomain", "getDomain: domain is " + domain);
        return domain;
    }

    /**
     * Unicode码转为汉字
     */
    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U'))) {
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                } else {
                    retBuf.append(unicodeStr.charAt(i));
                }
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }

    public static String stringToUnicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            //"\\u只是代号，请根据具体所需添加相应的符号"
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }
}
