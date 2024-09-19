package com.example.timetablegenerator.tableHelper;

import android.content.Context;

import com.example.timetablegenerator.database.TimeConn;
import com.example.timetablegenerator.dbModel.TimeModel;

import java.util.ArrayList;

public class TimeTableHelper {
    Context c;

    public TimeTableHelper(Context c) {
        this.c = c;
    }

    public String[][] getRecords(){
        ArrayList<TimeModel> timeRecords = new TimeConn(c).retrieveRecords();
        TimeModel timeModel;

        String[][] records = new String[timeRecords.size()][3];

        for(int i=0; i<timeRecords.size();i++) {
            timeModel = timeRecords.get(i);

            records[i][0] = timeModel.getMid();
            records[i][1] = timeModel.getMtime();
            records[i][2] = timeModel.getMday();
        }
        return records;
    }
}
