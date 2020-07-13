package cn.modo.sensitive.pojo;

import lombok.Data;

@Data
public class Game {
    private Integer id;

    private String gameId;

    private String gameName;

    private String gameDesc;

    private String createTime;

    private String updateTime;

}