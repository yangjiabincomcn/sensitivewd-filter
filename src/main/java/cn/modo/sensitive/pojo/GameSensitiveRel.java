package cn.modo.sensitive.pojo;

import java.util.Date;

public class GameSensitiveRel {
    private Integer id;

    private Integer gameId;

    private Integer sensitiveId;

    private Date updateTime;

    private Date createTime;

    public GameSensitiveRel(Integer id, Integer gameId, Integer sensitiveId, Date updateTime, Date createTime) {
        this.id = id;
        this.gameId = gameId;
        this.sensitiveId = sensitiveId;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public GameSensitiveRel() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getSensitiveId() {
        return sensitiveId;
    }

    public void setSensitiveId(Integer sensitiveId) {
        this.sensitiveId = sensitiveId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}