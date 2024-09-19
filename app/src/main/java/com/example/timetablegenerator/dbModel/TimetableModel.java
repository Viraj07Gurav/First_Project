package com.example.timetablegenerator.dbModel;

import com.example.timetablegenerator.GeneticAlgorithm.WeekDay;

public class TimetableModel {
    String tsecId, tdeptName, tclassId, tcourseName, troomNo, tinstructorName, tmTime, tmDay;

    public String getTsecId() {
        return tsecId;
    }

    public void setTsecId(String tsecId) {
        this.tsecId = tsecId;
    }

    public String getTdeptName() {
        return tdeptName;
    }

    public void setTdeptName(String tdeptName) {
        this.tdeptName = tdeptName;
    }

    public String getTclassId() {
        return tclassId;
    }

    public void setTclassId(String tclassId) {
        this.tclassId = tclassId;
    }

    public String getTcourseName() {
        return tcourseName;
    }

    public void setTcourseName(String tcourseName) {
        this.tcourseName = tcourseName;
    }

    public String getTroomNo() {
        return troomNo;
    }

    public void setTroomNo(String troomNo) {
        this.troomNo = troomNo;
    }

    public String getTinstructorName() {
        return tinstructorName;
    }

    public void setTinstructorName(String tinstructorName) {
        this.tinstructorName = tinstructorName;
    }

    public String getTmTime() {
        return tmTime;
    }

    public void setTmTime(String tmTime) {
        this.tmTime = tmTime;
    }

    public String getTmDay() {
        return tmDay;
    }

    public void setTmDay(String tmDay) {
        this.tmDay = tmDay;
    }

    public WeekDay getMDay(){
        return WeekDay.valueOf(getTmDay());
    }

}
