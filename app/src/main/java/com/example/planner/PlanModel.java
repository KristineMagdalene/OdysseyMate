package com.example.planner;

public class PlanModel {
    String id = "";
    String title = "";
    String description = "";
    String image = "";
    String currentDate = "";
    String currentTime = "";

    public PlanModel(String id, String title, String description, String image, String currentDate, String currentTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
    }
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }
    public PlanModel() {
        // Default constructor required for calls to DataSnapshot.getValue(PlanModel.class)
    }


    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

}
