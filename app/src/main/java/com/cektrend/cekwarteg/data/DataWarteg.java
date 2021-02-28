package com.cektrend.cekwarteg.data;

public class DataWarteg {
    private String id;
    private String code;
    private String name;
    private String email;
    private String owner_name;
    private String address;
    private String phone;
    private String description;
    private String photo_profile;

    public DataWarteg(String id, String code, String name, String email, String owner_name, String address, String phone, String description, String photo_profile) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.email = email;
        this.owner_name = owner_name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.photo_profile = photo_profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }
}
