package com.example.ble;

public class Student {
    private String email;

    private String name;

    private String status;

    private String address;

    private int absent_time;

    private String student_id;

    public String getEmail() {
        return email;
    }

    public String getAddress(){
        return address;
    }
    public String getStudent_id(){
        return student_id;
    }
    public int getAbsent_time(){
        return absent_time;
    }

    public String getName() {
        return name;
    }

    public String getStatus(){return status;}

    public void setStatus(String status){
        this.status=status;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setAddress(String address){this.address=address;}
    public void setName(String name){this.name=name;}
    public void setAbsent_time(int absent_time){this.absent_time=absent_time;}
    public void setStudent_id(String student_id){this.student_id=student_id;}
}
