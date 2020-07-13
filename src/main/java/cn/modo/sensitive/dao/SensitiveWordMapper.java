package cn.modo.sensitive.dao;

import cn.modo.sensitive.pojo.SensitiveWord;
import cn.modo.sensitive.pojo.Words;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface SensitiveWordMapper {

    int insert(SensitiveWord record);

    int updateByPrimaryKeySelective(SensitiveWord record);

    List<Words> getKeyword(@Param("type") Integer type, @Param("gameId") String gameId);

    List<SensitiveWord> findAll(@Param("wordType") Integer wordType, @Param("keyword") String keyword,@Param("strategy") Integer strategy);

    SensitiveWord findByName(String name);

    SensitiveWord selectByParamKey(Integer id);

    int updateDeleteFlag(@Param("list") List<Integer> list);
}