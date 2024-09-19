package com.example.timetablegenerator.tableHelper;

import android.content.Context;

import com.example.timetablegenerator.database.SectionConn;
import com.example.timetablegenerator.dbModel.SectionModel;

import java.util.ArrayList;

public class SectionTableHelper {
    Context c;
    public  SectionTableHelper(Context c) {
        this.c = c;
    }

    public String[][] getRecords() {
        ArrayList<SectionModel> secRecords = new SectionConn(c).retrieveRecords();
        SectionModel sectionModel;
        String[][] records = new String[secRecords.size()][3];

        for (int i = 0; i < secRecords.size(); i++) {
            sectionModel = secRecords.get(i);

            records[i][0] = sectionModel.getSecId();
            records[i][1] = sectionModel.getSecDeptName();
            records[i][2] = sectionModel.getNumOfClassPerWeek();
        }
        return records;
    }
}
