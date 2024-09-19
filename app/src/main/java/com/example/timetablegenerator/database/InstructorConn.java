package com.example.timetablegenerator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.timetablegenerator.dbModel.InstructorModel;

import java.util.ArrayList;

public class InstructorConn {
    Context c;
    DBHelper helper;
    SQLiteDatabase MyDB;

    public InstructorConn(Context c) {
        this.c = c;
        helper = new DBHelper(c);
    }

    public boolean insertInstructor(InstructorModel instructorModel){
        try{
            MyDB = helper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("uid", instructorModel.getUid());
            values.put("name", instructorModel.getName());
            values.put("pwd", instructorModel.getPwd());

            long result = MyDB.insert("instructor", null, values);
            return result != -1;
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
        finally {
            helper.close();
        }
        return false;
    }

    public ArrayList<InstructorModel> retrieveRecords() {
        ArrayList<InstructorModel> instructorRecords = new ArrayList<>();
        String[] columns = {"uid", "name", "pwd"};

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.query("instructor", columns, null, null, null, null, null);

            InstructorModel instructorModel;
            if (c != null) {
                while (c.moveToNext()) {
                    String uid = c.getString(c.getColumnIndexOrThrow("uid"));
                    String name = c.getString(c.getColumnIndexOrThrow("name"));
                    String pwd = c.getString(c.getColumnIndexOrThrow("pwd"));

                    instructorModel = new InstructorModel();
                    instructorModel.setUid(uid);
                    instructorModel.setName(name);
                    instructorModel.setPwd(pwd);
                    instructorRecords.add(instructorModel);
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
        return instructorRecords;
    }

    public boolean updateInstructor(String oldId, InstructorModel instructorModel){
        String id, name, pwd;

        id = instructorModel.getUid();
        name = instructorModel.getName();
        pwd = instructorModel.getPwd();

        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("UPDATE instructor SET uid="+"'"+id+"'"+", name="+"'"+name+"'"+", pwd="+"'"+pwd+"'"+" WHERE uid="+"'"+oldId+"'");
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

    public boolean deleteInstructor(String instructorId){
        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("DELETE FROM instructor WHERE uid="+"'"+instructorId+"'");
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


    public ArrayList<InstructorModel> getAllInRecords(){
        ArrayList<InstructorModel> records =  new ArrayList<>();
        String[] columns = {"id", "uid", "name", "pwd"};

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.query("instructor", columns, null, null, null, null, null);

            InstructorModel instructorModel;
            if (c != null) {
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndexOrThrow("id"));
                    String uid = c.getString(c.getColumnIndexOrThrow("uid"));
                    String name = c.getString(c.getColumnIndexOrThrow("name"));
                    String pwd = c.getString(c.getColumnIndexOrThrow("pwd"));

                    instructorModel = new InstructorModel();
                    instructorModel.setInId(id);
                    instructorModel.setUid(uid);
                    instructorModel.setName(name);
                    instructorModel.setPwd(pwd);
                    records.add(instructorModel);
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
