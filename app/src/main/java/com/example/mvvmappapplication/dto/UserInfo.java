package com.example.mvvmappapplication.dto;

public class UserInfo {
    private String carNumber;
    private String id;
    private String password;

    public UserInfo(String carNumber, String id, String password) {
        this.carNumber = carNumber;
        this.id = id;
        this.password = password;
    }

    public UserInfo(String s, String s1) {
        this.id = s;
        this.password = s1;
    }
}
