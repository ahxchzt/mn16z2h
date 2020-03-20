package com.example.ble;

import java.io.Serializable;

class Lecture implements Serializable {
    private String lecture_code;
    private String lecture_name;
    private String Data;
    private String Time;
    public Lecture(String code,String name,String data,String time){
        this.lecture_name=name;
        this.lecture_code=code;
        this.Data=data;
        this.Time=time;
    }
    public String getName() {
        return lecture_name;
    }
    public String getCode() {
        return lecture_code;
    }
    public String getData() {
        return Data;
    }
    public String getTime() {
        return Time;
    }
}
