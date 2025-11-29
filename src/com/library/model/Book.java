package com.library.model;

import java.io.Serializable;

public class Book implements Serializable {
    private int id;
    private String title;
    private String author;
    private String category;
    private boolean referenceOnly;
    private boolean issued;
    private int timesIssued; // for popularity stats

    public Book(int id, String title, String author, String category, boolean referenceOnly) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.referenceOnly = referenceOnly;
        this.issued = false;
        this.timesIssued = 0;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public boolean isReferenceOnly() {
        return referenceOnly;
    }

    public boolean isIssued() {
        return issued;
    }

    public void setIssued(boolean issued) {
        this.issued = issued;
    }

    public int getTimesIssued() {
        return timesIssued;
    }

    public void incrementTimesIssued() {
        this.timesIssued++;
    }
}
