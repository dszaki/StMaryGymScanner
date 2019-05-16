package com.example.stmarygymscanner.model;

import com.example.stmarygymscanner.model.StatusEnum;

public class Member {

    int id;
    String name;


    StatusEnum.status StatAnEnum;

    public Member(int id, String name, StatusEnum.status statAnEnum) {
        this.id = id;
        this.name = name;
        StatAnEnum = statAnEnum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatAnEnum(StatusEnum.status statAnEnum) {
        StatAnEnum = statAnEnum;
    }

    public StatusEnum.status getStatAnEnum() {
        return StatAnEnum;
    }


}
