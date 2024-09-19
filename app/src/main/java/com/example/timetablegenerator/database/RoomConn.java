package com.example.timetablegenerator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.timetablegenerator.dbModel.RoomModel;

import java.util.ArrayList;

public class RoomConn {
    Context c;
    DBHelper helper;
    SQLiteDatabase MyDB;

    public RoomConn(Context c) {
        this.c = c;
        helper = new DBHelper(c);
    }

    public boolean insertRoom(RoomModel roomModel) {
        try {
            MyDB = helper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("room_number", roomModel.getRoomNo());
            values.put("seating_capacity", roomModel.getSeatingCap());

            long result = MyDB.insert("room", null, values);
            return result != -1;
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
        return false;
    }

    public ArrayList<RoomModel> retrieveRecords() {
        ArrayList<RoomModel> roomRecords = new ArrayList<>();
        String[] columns = {"room_number", "seating_capacity"};

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.query("room", columns, null, null, null, null, null);

            RoomModel roomModel;
            if (c != null) {
                while (c.moveToNext()) {
                    String rNo = c.getString(c.getColumnIndexOrThrow("room_number"));
                    String seatCap = c.getString(c.getColumnIndexOrThrow("seating_capacity"));

                    roomModel = new RoomModel();
                    roomModel.setRoomNo(rNo);
                    roomModel.setSeatingCap(seatCap);
                    roomRecords.add(roomModel);
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            helper.close();

        }
        return roomRecords;
    }

    public boolean updateRoom(String oldNo, RoomModel roomModel){
        String rNo, sCap;

        rNo = roomModel.getRoomNo();
        sCap = roomModel.getSeatingCap();

        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("UPDATE room SET room_number="+"'"+rNo+"'"+", seating_capacity="+"'"+sCap+"'"+"  WHERE room_number="+"'"+oldNo+"'");
            return true;
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
        finally {
            helper.close();
        }
        return false;
    }

    public boolean deleteRoom(String roomNo){
        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("DELETE FROM room WHERE room_number="+"'"+roomNo+"'");
            return true;
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
        finally {
            helper.close();
        }
        return false;
    }

    public ArrayList<RoomModel> getAllRoomRecords(){
        ArrayList<RoomModel> records =  new ArrayList<>();
        String[] columns = {"id", "room_number", "seating_capacity"};

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.query("room", columns, null, null, null, null, null);

            RoomModel roomModel;
            if (c != null) {
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndexOrThrow("id"));
                    String rNo = c.getString(c.getColumnIndexOrThrow("room_number"));
                    String seatingCap = c.getString(c.getColumnIndexOrThrow("seating_capacity"));

                    roomModel = new RoomModel();
                    roomModel.setrId(id);
                    roomModel.setRoomNo(rNo);
                    roomModel.setSeatingCap(seatingCap);
                    records.add(roomModel);
                }
            }
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            if (c != null) {
                c.close();
            }
            helper.close();

        }
        return records;
    }
}
