package cn.modo.sensitive.pojo;

import java.util.Date;

public class SensitiveWordStrategyRel {
    private Integer id;

    private Integer sensitiveId;

    private Integer strategyId;

    private Date createTime;

    private Date updateTime;

    public SensitiveWordStrategyRel(Integer id, Integer sensitiveId, Integer strategyId, Date createTime, Date updateTime) {
        this.id = id;
        this.sensitiveId = sensitiveId;
        this.strategyId = strategyId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public SensitiveWordStrategyRel() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSensitiveId() {
        return sensitiveId;
    }

    public void setSensitiveId(Integer sensitiveId) {
        this.sensitiveId = sensitiveId;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}