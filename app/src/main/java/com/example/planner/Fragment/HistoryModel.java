package com.example.planner.Fragment;

public class HistoryModel {
    String id = "";
    String title = "";
    String description = "";

    public HistoryModel(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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
}
