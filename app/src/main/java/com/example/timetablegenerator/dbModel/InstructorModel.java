package com.example.timetablegenerator.dbModel;

import androidx.annotation.NonNull;

public class InstructorModel {
    public String uid, name, pwd, inId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setInId(String inId) {
        this.inId = inId;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
