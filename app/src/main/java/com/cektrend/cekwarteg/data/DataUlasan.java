package com.cektrend.cekwarteg.data;

public class DataUlasan {
    private String name;
    private String reviewText;
    private String created_at;

    public DataUlasan(String name, String reviewText, String created_at) {
        this.name = name;
        this.reviewText = reviewText;
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
