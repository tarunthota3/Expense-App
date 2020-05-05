package com.example.inclass11;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Expense implements Serializable {
    String id;
    String title;
    Double cost;
    String category;
    Date date;

    HashMap<String, Object> hMap;

    @Override
    public String toString() {
        return "Expense{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", cost=" + cost +
                ", category='" + category + '\'' +
                ", date=" + date +
                ", hMap=" + hMap +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Expense(String id, String title, Double cost, String category, Date date) {
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.category = category;
        this.date = date;
    }

    HashMap<String, Object> toHashMap(){
        hMap = new HashMap<>();
        hMap.put("title", getTitle());
        hMap.put("cost", getCost());
        hMap.put("category", getCategory());
        hMap.put("date", getDate());
        return hMap;
    }
}
