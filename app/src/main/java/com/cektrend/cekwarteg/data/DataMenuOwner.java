package com.cektrend.cekwarteg.data;

public class DataMenuOwner {
    private Integer id;
    private String code;
    private String name;
    private String description;
    private Integer warteg_id;
    private Integer price;
    private Boolean is_have_stock;
    private String created_at;
    private String updated_at;
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public DataMenuOwner(Integer id, String code, String name, Integer warteg_id, Integer price, Boolean is_have_stock, String created_at, String updated_at, String photo , String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.warteg_id = warteg_id;
        this.price = price;
        this.is_have_stock = is_have_stock;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.photo = photo;
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWarteg_id() {
        return warteg_id;
    }

    public void setWarteg_id(Integer warteg_id) {
        this.warteg_id = warteg_id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getIs_have_stock() {
        return is_have_stock;
    }

    public void setIs_have_stock(Boolean is_have_stock) {
        this.is_have_stock = is_have_stock;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
