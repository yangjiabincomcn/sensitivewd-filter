package cn.modo.sensitive.dao;

import cn.modo.sensitive.pojo.SensitiveWord;
import cn.modo.sensitive.pojo.Words;
import cn.modo.sensitive.util.WordFilter;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SensitiveWordMapperTest {

    private final SensitiveWordMapper sensitiveWordMapper;


    @Test
    void stop() {
        List<String> collect = sensitiveWordMapper.getKeyword(null, null).stream().filter(item -> item.getType().equals("3")).map(Words::getWords).collect(Collectors.toList());
        String istrue = "([";
        for (String s : collect) {
            istrue += "," + s;
        }
        istrue = istrue + "]+)";
        System.out.println(istrue);

    }


    /*
        @Test
        void findByName() {
            List<String> names = new ArrayList<>();
            names.add("nigger");
            names.add("咨询");
            names.add("小姐");
            SensitiveWord byName = sensitiveWordMapper.findByName("nigger");
            System.out.println(byName);
        }

        @Test
        void updateDeleteFlag() {
            List<Integer> integers = new ArrayList<>();
            integers.add(2);
            integers.add(3);
            integers.add(1);
            int i = sensitiveWordMapper.updateDeleteFlag(integers);
            System.out.println(i);
        }*/
    @Test
    void WordFilter() {
        String tdc = "([!.,,,#,$,%,&,*,(,),|,?,/,@,\",',;,[,],{,},+,~,-,_,=,^,<,>, ,！,。,，,￥,（,）,？,、,“,‘,；,【,】,——,……,《,》,`, ]+)";
//        String tdc = "([!.,#$%&*()|?/@\"';[]{}+~-_=^<> ！。，￥（）？、“‘；【】——……《》`0 ]+)";
        // 短语匹配
        String testStr = "hello  hell fucking fuck nigger  hella hellb fuckyou";
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
                    System.out.println(m.group());
                    System.out.println("join" + join);
                    testStr = testStr.replaceAll(join, getSymbol(m.group().trim().length()));
                    System.out.println(testStr);
                }
            }
        }

    }

    @Test
    void WordFilter2() {
        String tdc = "([!.,,,#,$,%,&,*,(,),|,?,/,@,\",',;,[,],{,},+,~,-,_,=,^,<,>, ,！,。,，,￥,（,）,？,、,“,‘,；,【,】,——,……,《,》,`, ]+)";
//        String tdc = "([!.,#$%&*()|?/@\"';[]{}+~-_=^<> ！。，￥（）？、“‘；【】——……《》`0 ]+)";
        // 短语匹配
        String testStr = "hello  hell fucking fuck ";
        String keyword = " hell ";
        System.out.println(testStr);
        List<String> keywords = Arrays.asList(keyword.split(" "));
        System.out.println("敏感词列表" + JSON.toJSONString(keywords));
        if (keywords.size() > 1) {
            String join = StringUtils.join(keywords, tdc).concat(tdc);
//            System.out.println(join);
            Pattern r = Pattern.compile(join);
            Matcher m = r.matcher(testStr);
            System.out.println();
            if (m.find()) {
                System.out.println(m.group());
                System.out.println("join" + join);
                System.out.println(testStr.replaceAll(join, getSymbol(m.group().trim().length())));
            }
        }
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