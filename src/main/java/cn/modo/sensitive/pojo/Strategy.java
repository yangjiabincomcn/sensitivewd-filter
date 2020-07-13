package cn.modo.sensitive.pojo;

import java.util.Date;

public class Strategy {
    private Integer id;

    private String strategyKey;

    private String strategyName;

    private String strategyDesc;

    private Date createTime;

    private Date updateTime;

    public Strategy(Integer id, String strategyKey, String strategyName, String strategyDesc, Date createTime, Date updateTime) {
        this.id = id;
        this.strategyKey = strategyKey;
        this.strategyName = strategyName;
        this.strategyDesc = strategyDesc;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Strategy() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStrategyKey() {
        return strategyKey;
    }

    public void setStrategyKey(String strategyKey) {
        this.strategyKey = strategyKey == null ? null : strategyKey.trim();
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName == null ? null : strategyName.trim();
    }

    public String getStrategyDesc() {
        return strategyDesc;
    }

    public void setStrategyDesc(String strategyDesc) {
        this.strategyDesc = strategyDesc == null ? null : strategyDesc.trim();
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