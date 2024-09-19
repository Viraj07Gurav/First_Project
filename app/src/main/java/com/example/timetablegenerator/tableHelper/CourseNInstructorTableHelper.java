package com.example.timetablegenerator.tableHelper;

import android.content.Context;

import com.example.timetablegenerator.database.CourseNInstructorConn;
import com.example.timetablegenerator.database.InstructorConn;
import com.example.timetablegenerator.dbModel.CourseNInstructorModel;
import com.example.timetablegenerator.dbModel.InstructorModel;

import java.util.ArrayList;

public class CourseNInstructorTableHelper {
    Context c;
    public CourseNInstructorTableHelper(Context c){
        this.c = c;
    }

    CourseNInstructorModel courseNInstructorModel;
    public String[][] getCourseRecords() {
        ArrayList<CourseNInstructorModel> courseRecords = new CourseNInstructorConn(c).retrieveRecords();
        String[][] records = new String[courseRecords.size()][3];

        for(int i=0; i<courseRecords.size();i++) {
            courseNInstructorModel = courseRecords.get(i);

            records[i][0] = courseNInstructorModel.getCourseNo();
            records[i][1] = courseNInstructorModel.getCourseName();
            records[i][2] = courseNInstructorModel.getMaxNoOfStud();
        }
        return records;
    }

    InstructorModel instructorModel;
    public String[] getInstructorName(){
        ArrayList<InstructorModel> instructorRecords = new InstructorConn(c).retrieveRecords();
        String[] records = new String[instructorRecords.size()];

        for(int i=0; i<instructorRecords.size();i++) {
            instructorModel = instructorRecords.get(i);
            records[i] = instructorModel.getName();
        }
        return records;
    }

}
