package com.example.timetablegenerator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.timetablegenerator.dbModel.TimetableModel;

import java.util.ArrayList;

public class TimetableConn {
    Context c;
    DBHelper helper;
    SQLiteDatabase MyDB;

    public TimetableConn(Context c) {
        this.c = c;
        helper = new DBHelper(c);
    }

    public boolean insertTimetable(TimetableModel model){

        try {
            MyDB = helper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("secId", model.getTsecId());
            values.put("deptName", model.getTdeptName());
            values.put("classId", model.getTclassId());
            values.put("courseName", model.getTcourseName());
            values.put("roomNo", model.getTroomNo());
            values.put("instructorName", model.getTinstructorName());
            values.put("mTime", model.getTmTime());
            values.put("mDay", model.getTmDay());

            long result = MyDB.insert("schedules", null, values);
            return result != -1;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            helper.close();
        }
        return false;
    }

    public ArrayList<TimetableModel> retrieveRecords(){
        ArrayList<TimetableModel> records = new ArrayList<>();
        String[] columns = {"secId", "deptName", "classId", "courseName", "roomNo", "instructorName", "mTime", "mDay"};

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.query("schedules", columns, null, null, null, null, null);

            TimetableModel model;
            if (c != null) {
                while (c.moveToNext()) {
                    String secId = c.getString(c.getColumnIndexOrThrow("secId"));
                    String deptName = c.getString(c.getColumnIndexOrThrow("deptName"));
                    String classId = c.getString(c.getColumnIndexOrThrow("classId"));
                    String courseName = c.getString(c.getColumnIndexOrThrow("courseName"));
                    String roomNo = c.getString(c.getColumnIndexOrThrow("roomNo"));
                    String instructorName = c.getString(c.getColumnIndexOrThrow("instructorName"));
                    String mTime = c.getString(c.getColumnIndexOrThrow("mTime"));
                    String mDay = c.getString(c.getColumnIndexOrThrow("mDay"));

                    model = new TimetableModel();
                    model.setTsecId(secId);
                    model.setTdeptName(deptName);
                    model.setTclassId(classId);
                    model.setTcourseName(courseName);
                    model.setTroomNo(roomNo);
                    model.setTinstructorName(instructorName);
                    model.setTmTime(mTime);
                    model.setTmDay(mDay);
                    records.add(model);
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


    public void deleteTable(){
        MyDB = helper.getWritableDatabase();
        try{
           MyDB.execSQL("DROP TABLE IF EXISTS schedules");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<TimetableModel> getInstructorsTT(String instructorName){
        ArrayList<TimetableModel> records = new ArrayList<>();

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.rawQuery("SELECT * FROM schedules WHERE instructorName = ? ORDER BY mDay", new String[]{instructorName});

            TimetableModel model;
            if (c != null) {
                while (c.moveToNext()) {
                    String secId = c.getString(c.getColumnIndexOrThrow("secId"));
                    String deptName = c.getString(c.getColumnIndexOrThrow("deptName"));
                    String courseName = c.getString(c.getColumnIndexOrThrow("courseName"));
                    String roomNo = c.getString(c.getColumnIndexOrThrow("roomNo"));
                    String mTime = c.getString(c.getColumnIndexOrThrow("mTime"));
                    String mDay = c.getString(c.getColumnIndexOrThrow("mDay"));

                    model = new TimetableModel();
                    model.setTsecId(secId);
                    model.setTdeptName(deptName);
                    model.setTcourseName(courseName);
                    model.setTroomNo(roomNo);
                    model.setTmTime(mTime);
                    model.setTmDay(mDay);
                    records.add(model);
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
        return  records;
    }

    public ArrayList<TimetableModel> getStudentsTT(String deptName, String section){
        ArrayList<TimetableModel> records = new ArrayList<>();

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.rawQuery("SELECT instructorName, courseName, roomNo, mTime, mDay FROM schedules WHERE deptName = ? AND secId= ? ORDER BY mDay", new String[]{deptName, section});

            TimetableModel model;
            if (c != null) {
                while (c.moveToNext()) {
                    String courseName = c.getString(c.getColumnIndexOrThrow("courseName"));
                    String instructorName = c.getString(c.getColumnIndexOrThrow("instructorName"));
                    String roomNo = c.getString(c.getColumnIndexOrThrow("roomNo"));
                    String mTime = c.getString(c.getColumnIndexOrThrow("mTime"));
                    String mDay = c.getString(c.getColumnIndexOrThrow("mDay"));

                    model = new TimetableModel();
                    model.setTcourseName(courseName);
                    model.setTinstructorName(instructorName);
                    model.setTroomNo(roomNo);
                    model.setTmTime(mTime);
                    model.setTmDay(mDay);
                    records.add(model);
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
        return  records;
    }
}
