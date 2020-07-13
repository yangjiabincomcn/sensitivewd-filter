package cn.modo.sensitive.util;

import cn.modo.sensitive.pojo.WordNode;
import cn.modo.sensitive.pojo.Words;
import cn.modo.sensitive.pojo.WordsList;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 创建时间：2016年8月30日 下午3:01:12
 * <p>
 * 思路： 创建一个FilterSet，枚举了0~65535的所有char是否是某个敏感词开头的状态
 * <p>
 * 判断是否是 敏感词开头 | | 是 不是 获取头节点 OK--下一个字 然后逐级遍历，DFA算法
 *
 * @author andy
 * @version 2.2
 */
@Slf4j
public class WordFilter {

    // 存储首字
    private static final Map<Integer, FilterSet> set = new ConcurrentHashMap<>();
    private static final Map<Integer, FilterSet> wait = new ConcurrentHashMap<>();
    // 存储节点
    private static final Map<Integer, Map<Integer, WordNode>> nodes = new HashMap<>(1024, 1);
    private static final Map<Integer, Map<Integer, WordNode>> waitNodes = new HashMap<>(1024, 1);
    // 停顿词
    private static final Map<Integer, Set<Integer>> stopSetMap = new ConcurrentHashMap<>();

    //精确匹配 连接词
    private static final Map<Integer, List<String>> stopListMap = new ConcurrentHashMap<>();
    // 精确匹配存储首字
    private static final Map<Integer, List<String>> preciseSet = new ConcurrentHashMap<>();
    // 精确匹配 白名单
    private static final Map<Integer, List<String>> preciseSetWhite = new ConcurrentHashMap<>();

    // 敏感词过滤替换
    private static final char SIGN = '*';
    private Map<String, List<Words>> map = new HashMap<>();


/*    private static final List<Words> wordsArrayList = new ArrayList<>();
    private static final List<Words> stopArrayList = new ArrayList<>();*/

    static {

    }

    public static void init(List<Words> words) {
        log.info("初始化  敏感词库");
        List<WordsList> wordsList = new ArrayList<>();
        Map<Integer, List<Words>> blackMap = words.stream().filter(item -> item.getGameId() != null).collect(Collectors.groupingBy(Words::getGameId));
        blackMap.forEach((k, v) -> {
            WordsList list = new WordsList();
            list.setGameId(k);
            list.setBlacks(v.stream().filter(item -> "1".equals(item.getType()) && item.getStrategy().equals(1)).map(Words::getWords).collect(Collectors.toList()));
            list.setWhites(v.stream().filter(item -> "2".equals(item.getType()) && item.getStrategy().equals(1)).map(Words::getWords).collect(Collectors.toList()));
            list.setStops(v.stream().filter(item -> "3".equals(item.getType()) && item.getStrategy().equals(1)).map(Words::getWords).collect(Collectors.toList()));
            list.setPreciseWords(v.stream().filter(item -> item.getStrategy().equals(2) && "1".equals(item.getType())).map(Words::getWords).collect(Collectors.toList()));
            list.setPreciseSetWhites(v.stream().filter(item -> item.getStrategy().equals(2) && "2".equals(item.getType())).map(Words::getWords).collect(Collectors.toList()));
            wordsList.add(list);
        });
        wordsList.forEach(item -> {
                    FilterSet blackSet = new FilterSet();
                    Map<Integer, WordNode> blackNodes = new HashMap<>(1024, 1);
                    addSensitiveWord(item.getBlacks(), blackSet, blackNodes);
                    set.put(item.getGameId(), blackSet);
                    nodes.put(item.getGameId(), blackNodes);
                    FilterSet whitesSet = new FilterSet();
                    Map<Integer, WordNode> whitesNodes = new HashMap<>(1024, 1);
                    addWait(item.getWhites(), whitesSet, whitesNodes);
                    waitNodes.put(item.getGameId(), whitesNodes);
                    wait.put(item.getGameId(), whitesSet);
                    Set<Integer> stopSet = new HashSet<>();
                    addStopWord(item.getStops(), stopSet);
                    stopSetMap.put(item.getGameId(), stopSet);
                    stopListMap.put(item.getGameId(), item.getStops());
                    preciseSet.put(item.getGameId(), item.getPreciseWords());
                    preciseSetWhite.put(item.getGameId(), item.getPreciseSetWhites());
                }
        );
    }

    public static void addWait(List<String> waitList, FilterSet whitesSet, Map<Integer, WordNode> whitesNodes) {
        if (waitList != null && waitList.size() > 0) {
            char[] chs;
            int fchar;
            int lastIndex;
            WordNode fnode; // 首字母节点
            for (String curr : waitList) {
                chs = curr.toCharArray();
                fchar = charConvert(chs[0]);
                // 没有首字定义
                if (!whitesSet.contains(fchar)) {
                    // 首字标志位 可重复add,反正判断了，不重复了
                    whitesSet.add(fchar);
                    fnode = new WordNode(fchar, chs.length == 1);
                    whitesNodes.put(fchar, fnode);
                } else {
                    fnode = whitesNodes.get(fchar);
                    if (!fnode.isLast() && chs.length == 1) {
                        fnode.setLast(true);
                    }
                }
                lastIndex = chs.length - 1;
                for (int i = 1; i < chs.length; i++) {
                    fnode = fnode.addIfNoExist(charConvert(chs[i]), i == lastIndex);
                }
            }
        }
    }

    /**
     * 增加敏感词
     *
     * @param path
     * @return
     */
    private static List<String> readWordFromFile(String path) {
        List<String> words;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(WordFilter.class.getClassLoader().getResourceAsStream(path)));
            words = new ArrayList<String>(1200);
            for (String buf = ""; (buf = br.readLine()) != null; ) {
                if (buf == null || buf.trim().equals("")) {
                    continue;
                }
                words.add(buf);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }
        return words;
    }

    /**
     * 增加停顿词
     *
     * @param words
     * @param stopSet
     */
    public static void addStopWord(final List<String> words, Set<Integer> stopSet) {
        if (words != null && words.size() > 0) {
            char[] chs;
            for (String curr : words) {
                chs = curr.toCharArray();
                for (char c : chs) {
                    stopSet.add(charConvert(c));
                }
            }
        }
    }

    /**
     * 添加DFA节点
     *
     * @param words
     * @param set
     * @param nodes
     */
    public static void addSensitiveWord(final List<String> words, FilterSet set, Map<Integer, WordNode> nodes) {
     /*   set.add(32);
        nodes.put(32, new WordNode(32, false));*/
        if (words != null && words.size() > 0) {
            char[] chs;
            int fchar;
            int lastIndex;
            WordNode fnode; // 首字母节点
            for (String curr : words) {
                if (Strings.isEmpty(curr)) {
                    continue;
                }
                chs = curr.toCharArray();
                fchar = charConvert(chs[0]);
                // 没有首字定义
                if (!set.contains(fchar)) {
                    // 首字标志位 可重复add,反正判断了，不重复了
                    set.add(fchar);
                    fnode = new WordNode(fchar, chs.length == 1);
                    nodes.put(fchar, fnode);
                } else {
                    fnode = nodes.get(fchar);
                    if (!fnode.isLast() && chs.length == 1) {
                        fnode.setLast(true);
                    }

                }
                lastIndex = chs.length - 1;
                for (int i = 1; i < chs.length; i++) {
                    fnode = fnode.addIfNoExist(charConvert(chs[i]), i == lastIndex);
                }
            }
        }
    }


    /**
     * 过滤判断 将敏感词转化为成屏蔽词
     *
     * @param src
     * @return
     */
    public static Map<String, Object> doFilter(final String src, Integer gameId) {
        FilterSet blackSet = set.get(gameId);
        Map<Integer, WordNode> blackNodes = nodes.get(gameId);
        FilterSet waitSet = wait.get(gameId);
        Map<Integer, WordNode> waitNode = waitNodes.get(gameId);
        Set<Integer> stopSet = stopSetMap.get(gameId);
        char[] chs = src.toCharArray();
        int length = chs.length;
        int currc;
        int k;
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", src);
        List<Integer> indexList = new ArrayList<>();
        WordNode node;
        for (int i = 0; i < length; i++) {
            currc = charConvert(chs[i]);
            if (blackSet == null) {
                return resultMap;
            }
            if (!blackSet.contains(currc)) {
                continue;
            }
            // 日 2
            if (blackNodes == null) {
                return resultMap;
            }
            node = blackNodes.get(currc);
            if (node == null) {
                continue;
            }// 其实不会发生，习惯性写上了
            boolean couldMark = false;
            int markNum = -1;
            if (node.isLast()) {// 单字匹配（日）
                couldMark = true;
                markNum = 0;
            }
            // 继续匹配（日你/日你妹），以长的优先
            // 你-3 妹-4 夫-5
            k = i;
            while (++k < length) {
                int temp = charConvert(chs[k]);
                if (stopSet.contains(temp)) {
                    continue;
                }
                node = node.querySub(temp);
                if (node == null) {
                    // 没有了
                    break;
                }
                if (node.isLast()) {
                    couldMark = true;
                    markNum = k - i;// 3-2
                }
            }
            if (couldMark && waitSet.contains(currc)) {
                node = waitNode.get(currc);
                if (node.isLast()) {
                    couldMark = false;
                }
                // 继续匹配（日你/日你妹），以长的优先
                // 你-3 妹-4 夫-5
                k = i;
                while (++k < length) {
                    int temp = charConvert(chs[k]);
                    if (stopSet.contains(temp)) {
                        continue;
                    }
                    node = node.querySub(temp);
                    if (node == null) {
                        // 没有了
                        break;
                    }
                    if (node.isLast()) {
                        couldMark = false;
                    }
                }
            }
            if (couldMark) {
                for (k = 0; k <= markNum; k++) {
                    int s = charConvert(chs[k + i]);
                    if (stopSet.contains(s)) {
                        continue;
                    }
                    indexList.add(k + i);
//                    log.info("要替换的 char:{}", k + i);
                    chs[k + i] = SIGN;
                }
                i = i + markNum;
            }
        }
        resultMap.put("index", indexList);
        resultMap.put("result", new String(chs));
        log.info("indexList :{}", JSON.toJSONString(indexList));
        return resultMap;
    }

    private static String getSIGN(Integer size) {
        String sign = "";
        for (Integer i = 0; i < size; i++) {
            sign += "*";
        }
        return sign;
    }

    private static boolean waitWord(WordNode node, boolean couldMark, int k, int i, int length, char[] chs) {

        return couldMark;
    }

    public static String precise(String text, Integer gameId) {
        // 用于过滤正则 过滤出带符号敏感词的正则表达式
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        List<String> strings = stopListMap.get(gameId);
        List<String> blacks = preciseSet.get(gameId);
        List<String> whites = preciseSetWhite.get(gameId);
        // 根据 连接词拼接 正则表达式
        String tdc = "([";
        for (String s : strings) {
            if ("[".equals(s) || "]".equals(s) || "-".equals(s)) {
                tdc += ",\\" + s;
            } else {
                tdc += "," + s;
            }

        }
        tdc = tdc + "]+)";
        // 短语匹配
        text = " ".concat(text).concat(" ");
        String keyword = "";
        http:
        for (String string : blacks) {
            keyword = " ".concat(string).concat(" ");
            List<String> keywords = Arrays.asList(keyword.split(" "));
            System.out.println("敏感词列表" + JSON.toJSONString(keywords));
            if (keywords.size() > 1) {
                String join = StringUtils.join(keywords, tdc).concat(tdc);
                log.info("keyword text join result :{}", join);
                Pattern r = Pattern.compile(join);
                Matcher m = r.matcher(text);
                if (m.find()) {
                    Pattern pattern = Pattern.compile(regEx);
                    Matcher matcher = pattern.matcher(m.group());
                    String trim = matcher.replaceAll("").trim();
                    if (whites.contains(trim)) {
                        log.info("敏感词白名单:{} ", JSON.toJSONString(whites));
                        log.info("该词存在忽略库,continue :{}", m.group().trim());
                        continue;
                    }
                    text = text.replaceAll(join, getSymbol(m.group().trim().length()));
                }
            }
        }
        log.info("精确匹配 result:{}", text);
        return text;
    }

    /**
     * 是否包含敏感词
     *
     * @param src
     * @return
     */
    public static final boolean isContains(final String src, Integer gameId) {
        char[] chs = src.toCharArray();
        FilterSet blackSet = set.get(gameId);
        Map<Integer, WordNode> nodeMap = nodes.get(gameId);
        Set<Integer> stopSet = stopSetMap.get(gameId);
        int length = chs.length;
        int currc;
        int k;
        WordNode node;
        for (int i = 0; i < length; i++) {
            currc = charConvert(chs[i]);
            if (!blackSet.contains(currc)) {
                continue;
            }
            node = nodeMap.get(currc);// 日 2
            if (node == null) {
                // 其实不会发生，习惯性写上了
                continue;
            }
            boolean couldMark = false;
            if (node.isLast()) {// 单字匹配（日）
                couldMark = true;
            }
            // 继续匹配（日你/日你妹），以长的优先
            // 你-3 妹-4 夫-5
            k = i;
            for (; ++k < length; ) {
                int temp = charConvert(chs[k]);
                if (stopSet.contains(temp)) {
                    continue;
                }

                node = node.querySub(temp);
                if (node == null) {
                    // 没有了
                    break;
                }

                if (node.isLast()) {
                    couldMark = true;
                }
            }
            if (couldMark) {
                return true;
            }
        }

        return false;
    }

    /**
     * 大写转化为小写 全角转化为半角
     *
     * @param src
     * @return
     */
    private static int charConvert(char src) {
        int r = BCConvert.qj2bj(src);
        return (r >= 'A' && r <= 'Z') ? r + 32 : r;
    }

    public static Map<String, Object> preciseFilter(String text, Integer gameId) {
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {

        }
        return null;
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