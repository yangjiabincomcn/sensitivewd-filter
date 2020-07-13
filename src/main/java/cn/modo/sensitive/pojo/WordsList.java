package cn.modo.sensitive.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author yjb
 **/
@Data
public class WordsList {
    private Integer gameId;
    List<String> blacks;
    List<String> whites;
    List<String> stops;
    List<String> preciseWords;
    List<String> preciseSetWhites;
}
