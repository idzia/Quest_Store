package com.codecool.queststore.model;

public class Level {
    private Integer levelId;
    private String levelName;
    private Integer levelTotalMoney;

    public Level(Integer levelId, String levelName, Integer levelTotalMoney) {
        this.levelId = levelId;
        this.levelName = levelName;
        this.levelTotalMoney = levelTotalMoney;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Integer getLevelTotalMoney() {
        return levelTotalMoney;
    }

    public void setLevelTotalMoney(Integer levelTotalMoney) {
        this.levelTotalMoney = levelTotalMoney;
    }
}
