package com.codecool.queststore.model;

public class Quest {
    private Integer questId;
    private String questName;
    private String questDescription;
    private String questCategory;
    private Integer questPrice;

    public Quest(Integer questId, String questName, String questDescription,
                 String questCategory, Integer questPrice) {
        this.questId = questId;
        this.questName = questName;
        this.questDescription = questDescription;
        this.questCategory = questCategory;
        this.questPrice = questPrice;
    }

    public Integer getQuestId() {
        return questId;
    }

    public void setQuestId(Integer questId) {
        this.questId = questId;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public String getQuestDescription() {
        return questDescription;
    }

    public void setQuestDescription(String questDescription) {
        this.questDescription = questDescription;
    }

    public String getQuestCategory() {
        return questCategory;
    }

    public void setQuestCategory(String questCategory) {
        this.questCategory = questCategory;
    }

    public Integer getQuestPrice() {
        return questPrice;
    }

    public void setQuestPrice(Integer questPrice) {
        this.questPrice = questPrice;
    }

    public boolean equals (Object o) {
        if (o instanceof Quest) {
            if (questId.equals(((Quest) o).questId)) {
                return true;
            }
        }
        return false;
    }
}
