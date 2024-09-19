package com.example.timetablegenerator.tableHelper;

import android.content.Context;

import com.example.timetablegenerator.database.InstructorConn;
import com.example.timetablegenerator.dbModel.InstructorModel;
import com.example.timetablegenerator.layoutsClass.Instructor;

import java.util.ArrayList;

public class InstructorTableHelper {
    Context c;
    public  InstructorTableHelper(Context c){
        this.c = c;
    }

    InstructorModel instructorModel;
    public String[][] getRecords() {
        ArrayList<InstructorModel> instructorRecords = new InstructorConn(c).retrieveRecords();
        String[][] records = new String[instructorRecords.size()][3];

        for(int i=0; i<instructorRecords.size();i++) {
            instructorModel = instructorRecords.get(i);

            records[i][0] = instructorModel.getUid();
            records[i][1] = instructorModel.getName();
            records[i][2] = instructorModel.getPwd();
        }
        return records;
    }

}
