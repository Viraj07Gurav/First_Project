package com.example.timetablegenerator.tableHelper;

import android.content.Context;

import com.example.timetablegenerator.database.SectionConn;
import com.example.timetablegenerator.database.TimetableConn;
import com.example.timetablegenerator.dbModel.TimetableModel;

import java.util.ArrayList;

public class TimetableTableHelper {
    Context c;
    public TimetableTableHelper(Context c) {
        this.c = c;
    }

    public String[][] getRecords(String instructorName){
        ArrayList<TimetableModel> timetableRecords = new TimetableConn(c).getInstructorsTT(instructorName);
        String[][] records = new String[timetableRecords.size()][6];

        TimetableModel model;
        for(int i=0; i<timetableRecords.size();i++) {
            model = timetableRecords.get(i);

            records[i][0] = model.getTdeptName();
            records[i][1] = model.getTsecId();
            records[i][2] = model.getTcourseName();
            records[i][3] = model.getTroomNo();
            records[i][4] = model.getTmTime();
            records[i][5] = model.getTmDay();
        }
        return records;
    }

    public String[] getSections(String deptName){
        ArrayList<String> records = new SectionConn(c).getDeptSections(deptName);

        String[] sections = new String[records.size()];

        for(int i=0; i<records.size(); i++){
            sections[i] = records.get(i);
        }
        return sections;
    }

    public String[][] getRecords(String deptName, String section){
        ArrayList<TimetableModel> timetableRecords = new TimetableConn(c).getStudentsTT(deptName, section);
        String[][] records = new String[timetableRecords.size()][5];

        TimetableModel model;
        for(int i=0; i<timetableRecords.size();i++) {
            model = timetableRecords.get(i);

            records[i][0] = model.getTcourseName();
            records[i][1] = model.getTinstructorName();
            records[i][2] = model.getTroomNo();
            records[i][3] = model.getTmTime();
            records[i][4] = model.getTmDay();
        }
        return records;
    }
}
