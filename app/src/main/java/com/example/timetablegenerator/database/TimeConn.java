package com.example.timetablegenerator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.timetablegenerator.dbModel.TimeModel;

import java.util.ArrayList;

public class TimeConn {
    Context c;
    DBHelper helper;
    SQLiteDatabase MyDB;

    public TimeConn(Context c) {
        this.c = c;
        helper = new DBHelper(c);
    }

    public boolean insertTime(TimeModel timeModel) {
        try {
            MyDB = helper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("pid", timeModel.getMid());
            values.put("time", timeModel.getMtime());
            values.put("day", timeModel.getMday());

            long result = MyDB.insert("meetingtime", null, values);
            return result != -1;
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
        return false;
    }

    public ArrayList<TimeModel> retrieveRecords() {
        ArrayList<TimeModel> timeRecords = new ArrayList<>();
        String[] columns = {"pid", "time", "day"};

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.query("meetingtime", columns, null, null, null, null, null);

            TimeModel timeModel;
            if (c != null) {
                while (c.moveToNext()) {
                    String meetingId = c.getString(c.getColumnIndexOrThrow("pid"));
                    String meetingTime = c.getString(c.getColumnIndexOrThrow("time"));
                    String meetingDay = c.getString(c.getColumnIndexOrThrow("day"));

                    timeModel = new TimeModel();
                    timeModel.setMid(meetingId);
                    timeModel.setMtime(meetingTime);
                    timeModel.setMday(meetingDay);
                    timeRecords.add(timeModel);
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
        return timeRecords;
    }

    public boolean updateTime(String oldMId, TimeModel timeModel){
        String meetingId, meetingTime, meetingDay;

        meetingId = timeModel.getMid();
        meetingTime = timeModel.getMtime();
        meetingDay = timeModel.getMday();

        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("UPDATE meetingtime SET pid="+"'"+meetingId+"'"+", time="+"'"+meetingTime+"'"+", day="+"'"+meetingDay+"'"+"  WHERE pid="+"'"+oldMId+"'");
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

    public boolean deleteTime(String meetingId){
        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("DELETE FROM meetingtime WHERE pid="+"'"+meetingId+"'");
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

    public ArrayList<TimeModel> getAllTimeRecords() {
        ArrayList<TimeModel> records = new ArrayList<>();
        String[] columns = {"pid", "time", "day"};

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.query("meetingtime", columns, null, null, null, null, null);

            TimeModel timeModel;
            if (c != null) {
                while (c.moveToNext()) {
                    String meetingId = c.getString(c.getColumnIndexOrThrow("pid"));
                    String meetingTime = c.getString(c.getColumnIndexOrThrow("time"));
                    String meetingDay = c.getString(c.getColumnIndexOrThrow("day"));

                    timeModel = new TimeModel();
                    timeModel.setMid(meetingId);
                    timeModel.setMtime(meetingTime);
                    timeModel.setMday(meetingDay);
                    records.add(timeModel);
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
        return records;
    }

}
