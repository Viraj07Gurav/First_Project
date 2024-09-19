package com.example.timetablegenerator.tableHelper;

import android.content.Context;

import com.example.timetablegenerator.database.StudentConn;
import com.example.timetablegenerator.dbModel.StudentModel;

import java.util.ArrayList;

public class StudentTableHelper {
    Context c;
    public  StudentTableHelper(Context c){
        this.c = c;
    }

    StudentModel studModel;
    public String[][] getRecords() {
        ArrayList<StudentModel> studRecords = new StudentConn(c).retrieveRecords();
        String[][] records = new String[studRecords.size()][5];

        for(int i=0; i<studRecords.size();i++) {
            studModel = studRecords.get(i);

            records[i][0] = studModel.getStudFullName();
            records[i][1] = studModel.getStudUname();
            records[i][2] = studModel.getStudDept();
            records[i][3] = studModel.getStudSect();
            records[i][4] = studModel.getStudPwd();

        }
        return records;
    }
}
