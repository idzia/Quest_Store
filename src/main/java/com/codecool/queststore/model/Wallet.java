package com.codecool.queststore.model;

import java.util.List;

public class Wallet {
    private Integer currentMoney;
    private Integer totalMoney;
    private List<Artifact> artifactsList;
    //private Integer level;


    public Wallet(Integer currentMoney, Integer totalMoney, List<Artifact> artifactsList) {
        this.currentMoney = currentMoney;
        this.totalMoney = totalMoney;
        this.artifactsList = artifactsList;
    }

    public Integer getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(Integer currentMoney) {
        this.currentMoney = currentMoney;
    }

    public Integer getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<Artifact> getArtifactsList() {
        return artifactsList;
    }

    public void setArtifactsList(List<Artifact> artifactsList) {
        this.artifactsList = artifactsList;
    }
}
