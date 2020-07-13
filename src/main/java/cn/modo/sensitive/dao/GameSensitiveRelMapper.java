package cn.modo.sensitive.dao;

import cn.modo.sensitive.pojo.GameSensitiveRel;

public interface GameSensitiveRelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GameSensitiveRel record);

    int insertSelective(GameSensitiveRel record);

    GameSensitiveRel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GameSensitiveRel record);

    int updateByPrimaryKey(GameSensitiveRel record);
}