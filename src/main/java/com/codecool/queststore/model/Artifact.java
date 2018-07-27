package com.codecool.queststore.model;

public class Artifact {
    private Integer artifactId;
    private String artifactName;
    private String artifactDescription;
    private Integer artifactPrice;
    private String artifactCategory;
    private Integer artifactQuantity;

    public Artifact(Integer artifactId, String artifactName, String artifactDescription,
                    String artifactCategory, Integer quantity) {
        this.artifactId = artifactId;
        this.artifactName = artifactName;
        this.artifactDescription = artifactDescription;
        this.artifactCategory = artifactCategory;
        this.artifactQuantity = quantity;
    }

    public Artifact(Integer artifactId, String artifactName, String artifactDescription,
                    Integer artifactPrice, String artifactCategory) {
        this.artifactId = artifactId;
        this.artifactName = artifactName;
        this.artifactDescription = artifactDescription;
        this.artifactPrice = artifactPrice;
        this.artifactCategory = artifactCategory;
    }

    public Integer getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(Integer artifactId) {
        this.artifactId = artifactId;
    }

    public String getArtifactName() {
        return artifactName;
    }

    public void setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }

    public String getArtifactDescription() {
        return artifactDescription;
    }

    public void setArtifactDescription(String artifactDescription) {
        this.artifactDescription = artifactDescription;
    }

    public Integer getArtifactPrice() {
        return artifactPrice;
    }

    public void setArtifactPrice(Integer artifactPrice) {
        this.artifactPrice = artifactPrice;
    }

    public String getArtifactCategory() {
        return artifactCategory;
    }

    public void setArtifactCategory(String artifactCategory) {
        this.artifactCategory = artifactCategory;
    }

    public Integer getArtifactQuantity() {
        return artifactQuantity;
    }

    public void setArtifactQuantity(Integer quantity) {
        this.artifactQuantity = quantity;
    }
}
