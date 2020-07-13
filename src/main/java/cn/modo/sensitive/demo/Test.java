package cn.modo.sensitive.demo;


import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yjb
 **/
public class Test {
    private static boolean noWords(String str) {
        List<String> list = Arrays.asList(" ", "-", "_");
        List<String> strs = Arrays.asList(str.split(""));
        return strs.stream().allMatch(item -> list.contains(item));
    }

    public static void main(String[] args) {
        String tdc = "([!,.,,,#,$,%,&,*,(,),|,?,/,@,\",',;,\\[,\\],{,},+,~,-,_,=,^,<,>, ,！,。,，,￥,（,）,？,、,“,‘,；,【,】,——,……,《,》,`, ,]+)";
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";


        //  String tdc = "([!.,#$%&*()|?/@\"';[]{}+~-_=^<> ！。，￥（）？、“‘；【】——……《》`0 ]+)";
        // 短语匹配
        String testStr = " fuck |{}[] you hell are";
        String keyword = " hell ";
        List<String> strings = Arrays.asList("hell", "fuck", "nigger");
        testStr = testStr.concat(" ");
        for (String string : strings) {
            keyword = " " + string + " ";
            List<String> keywords = Arrays.asList(keyword.split(" "));
            System.out.println("敏感词列表" + JSON.toJSONString(keywords));
            if (keywords.size() > 1) {
                String join = StringUtils.join(keywords, tdc).concat(tdc);
                Pattern r = Pattern.compile(join);
                Matcher m = r.matcher(testStr);
                if (m.find()) {
                    Pattern pattern = Pattern.compile(regEx);
                    Matcher matcher = pattern.matcher(m.group());
                    String trim = matcher.replaceAll("").trim();
                    System.out.println("匹配到的 敏感词" + trim);
//                    System.out.println("join" + join);
                    testStr = testStr.replaceAll(join, getSymbol(m.group().trim().length()));
                }
            }
        }
        System.out.println(testStr);

    }

    public static String getSymbol(Integer size) {
        String symbol = " ";
        if (size < 1) {
            return " ";
        } else {
            for (Integer i = 0; i < size; i++) {
                symbol += "*";
            }
        }
        return symbol + " ";
    }

}
