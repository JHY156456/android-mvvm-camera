package com.example.mvvmappapplication.dto;

public class UserInfo {

    private final String id;
    private final String password;
    private  String carNumber;
    private String phone;

    public UserInfo( String id, String password,String carNumber,String phone) {
        this.id = id;
        this.password = password;
        this.carNumber = carNumber;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
