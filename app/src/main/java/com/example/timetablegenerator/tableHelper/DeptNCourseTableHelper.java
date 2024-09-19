package com.example.timetablegenerator.tableHelper;

import android.content.Context;

import com.example.timetablegenerator.database.CourseNInstructorConn;
import com.example.timetablegenerator.database.DeptNCourseConn;
import com.example.timetablegenerator.dbModel.CourseNInstructorModel;

import java.util.ArrayList;

public class DeptNCourseTableHelper {
    Context c;
    public DeptNCourseTableHelper(Context c){
        this.c = c;
    }

    public String[][] getRecords() {
        ArrayList<String> deptRecords = new DeptNCourseConn(c).retrieveRecords();
        String[][] records = new String[deptRecords.size()][1];

        for(int i=0; i<deptRecords.size();i++) {
            records[i][0] = deptRecords.get(i);
        }
        return records;
    }

    CourseNInstructorModel courseModel;
    public String[] getCourseName(){
        ArrayList<CourseNInstructorModel> courseRecords = new CourseNInstructorConn(c).retrieveRecords();
        String[] records = new String[courseRecords.size()];
        for(int i=0; i<courseRecords.size();i++) {
            courseModel = courseRecords.get(i);
            records[i] = courseModel.getCourseName();
        }
        return records;
    }

    public String[] getDepartmentName() {
        ArrayList<String> deptRecords = new DeptNCourseConn(c).retrieveRecords();
        String[] records = new String[deptRecords.size()];

        for(int i=0; i<deptRecords.size();i++) {
            records[i] = deptRecords.get(i);
        }
        return records;
    }
}
