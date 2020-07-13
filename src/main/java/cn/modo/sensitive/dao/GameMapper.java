package cn.modo.sensitive.dao;

import cn.modo.sensitive.pojo.Game;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface GameMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Game record);

    int insertSelective(Game record);

    Game selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Game record);

    int updateByPrimaryKey(Game record);

    Game findByGameId(String gameId);

    List<Integer> getGameIds();

    @MapKey("id")
    Map<Integer, String> queryAll();

}