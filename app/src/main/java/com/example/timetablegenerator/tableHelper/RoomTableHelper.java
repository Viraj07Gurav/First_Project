package com.example.timetablegenerator.tableHelper;

import android.content.Context;

import com.example.timetablegenerator.database.RoomConn;
import com.example.timetablegenerator.dbModel.RoomModel;

import java.util.ArrayList;

public class RoomTableHelper {
    Context c;
    public  RoomTableHelper(Context c) {
        this.c = c;
    }

    public String[][] getRecords() {
        ArrayList<RoomModel> roomRecords = new RoomConn(c).retrieveRecords();
        RoomModel roomModel;
        String[][] records = new String[roomRecords.size()][2];

        for(int i=0; i<roomRecords.size();i++) {
            roomModel = roomRecords.get(i);

            records[i][0] = roomModel.getRoomNo();
            records[i][1] = roomModel.getSeatingCap();
        }
        return records;
    }
}
