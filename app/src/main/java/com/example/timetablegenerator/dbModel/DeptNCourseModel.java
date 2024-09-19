package com.example.timetablegenerator.dbModel;

public class DeptNCourseModel {
    String deptId, deptName, courseName, dcId, dcDeptId, dcCourseId;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public void setDcDeptId(String dcDeptId) {
        this.dcDeptId = dcDeptId;
    }

    public void setDcCourseId(String dcCourseId) {
        this.dcCourseId = dcCourseId;
    }
}
