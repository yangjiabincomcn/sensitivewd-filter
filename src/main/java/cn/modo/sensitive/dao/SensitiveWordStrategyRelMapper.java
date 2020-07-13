package cn.modo.sensitive.dao;

import cn.modo.sensitive.pojo.SensitiveWordStrategyRel;
import cn.modo.sensitive.pojo.Words;

import java.util.List;

public interface SensitiveWordStrategyRelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SensitiveWordStrategyRel record);

    int insertSelective(SensitiveWordStrategyRel record);

    SensitiveWordStrategyRel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SensitiveWordStrategyRel record);

    int updateByPrimaryKey(SensitiveWordStrategyRel record);

    List<Words> findByStrategy(Integer strategyId);
}