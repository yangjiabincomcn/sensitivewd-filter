package org.andy.sensitivewdfilter.test;

import cn.modo.sensitive.util.WordFilter;
import org.junit.Test;

/**
 * 创建时间：2016年8月30日 下午5:12:40
 *
 * @author andy
 * @version 2.2
 */

public class TestSensitiveWdFilter {
    @Test
    public void Test() {
        String s = "谁有棒 棒 糖卖啊，大约多少钱一克";
        System.out.println(s);
    }

//    @Test
//    public void TestFilter() {
//        //     String s = "你是逗比吗？ｆｕ ｃｋ！fU##cK,你竟#然用法$$轮功，法@!轮!%%%功";
//        String s = "fuck ing you are  f u c k, ";
//        //String s = "谁有棒 棒 糖卖啊，大约多少钱一克";
//        //String s = "nigger";
//        System.out.println("解析问题： " + s);
//        System.out.println("解析问题长度 : " + s.length());
//        String re;
//        long nano = System.nanoTime();
////        re = WordFilter.doFilter(s, null);
//        nano = (System.nanoTime() - nano);
//        System.out.println("解析时间 : " + nano + "ns");
//        System.out.println("解析时间 : " + nano / 1000000 + "ms");
//        System.out.println(re);
//        System.out.println();
//
//        nano = System.nanoTime();
//        System.out.println("是否包含敏感词： " + WordFilter.isContains(s, null));
//        nano = (System.nanoTime() - nano);
//        System.out.println("解析时间 : " + nano + "ns");
//        System.out.println("解析时间 : " + nano / 1000000 + "ms");
//    }
}
