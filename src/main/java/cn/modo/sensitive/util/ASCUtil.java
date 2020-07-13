package cn.modo.sensitive.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yjb
 **/
@Slf4j
public class ASCUtil {
    private static int ascNum;
    private static char strChar;

    public static void main(String[] args) {
        String helloASC = stringToAscii("hello");
        System.out.println(helloASC);
        String s = asciiToString(helloASC);
        System.out.println(s);
//        System.out.println(backchar(98));
    }

    /**
     * 字符串转换为Ascii
     *
     * @param value
     * @return
     */
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }

    /**
     * Ascii转换为字符串
     *
     * @param value
     * @return
     */
    public static String asciiToString(String value) {
        log.info("asciiToString value :{}", value);
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

}
