package com.example.timetablegenerator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.timetablegenerator.dbModel.StudentModel;

import java.util.ArrayList;

public class StudentConn {
    Context c;
    DBHelper helper;
    SQLiteDatabase MyDB;

    public StudentConn(Context c) {
        this.c = c;
        helper = new DBHelper(c);
    }

    public boolean insertStudent(StudentModel studentModel){
        String studUname = studentModel.getStudUname();
        String studFullName = studentModel.getStudFullName();
        String studPwd = studentModel.getStudPwd();
        String studDept = studentModel.getStudDept();
        String studSect = studentModel.getStudSect();

        try{
            MyDB = helper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("fullName", studFullName);
            values.put("uname", studUname);
            values.put("pwd", studPwd);
            values.put("dept", studDept);
            values.put("section", studSect);

            long result = MyDB.insert("students", null, values);
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

    public ArrayList<StudentModel> retrieveRecords() {
        ArrayList<StudentModel> studRecords = new ArrayList<>();
        String[] columns = {"uname", "fullName", "pwd", "dept", "section"};

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.query("students", columns, null, null, null, null, null);

            StudentModel studentModel;
            if (c != null) {
                while (c.moveToNext()) {
                    String uname = c.getString(c.getColumnIndexOrThrow("uname"));
                    String fullName = c.getString(c.getColumnIndexOrThrow("fullName"));
                    String pwd = c.getString(c.getColumnIndexOrThrow("pwd"));
                    String dept = c.getString(c.getColumnIndexOrThrow("dept"));
                    String sect = c.getString(c.getColumnIndexOrThrow("section"));

                    studentModel = new StudentModel();
                    studentModel.setStudUname(uname);
                    studentModel.setStudFullName(fullName);
                    studentModel.setStudPwd(pwd);
                    studentModel.setStudDept(dept);
                    studentModel.setStudSect(sect);
                    studRecords.add(studentModel);
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
        return studRecords;
    }

    public boolean updateStud(String oldStudUname, StudentModel studentModel){
        String studUname = studentModel.getStudUname();
        String studFullName = studentModel.getStudFullName();
        String studPwd = studentModel.getStudPwd();
        String studDept = studentModel.getStudDept();
        String studSect = studentModel.getStudSect();

        try{
            MyDB = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("fullName", studFullName);
            values.put("uname", studUname);
            values.put("pwd", studPwd);
            values.put("dept", studDept);
            values.put("section", studSect);

            long result = MyDB.update("students", values, "uname=?", new String[]{oldStudUname});
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

    public String checkStudent(String uname){
        String username = null;
        Cursor c = null;
        try{
            MyDB = helper.getReadableDatabase();
          //  MyDB.execSQL("SELECT uname FROM students WHERE uname ="+"'"+uname+"'");
            c = MyDB.rawQuery("SELECT uname FROM students WHERE uname = ?", new String[]{uname});
            c.moveToNext();
            username = c.getString(c.getColumnIndexOrThrow("uname"));
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
     return username;
    }

    public boolean deleteStudent(String uname){
        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("DELETE FROM students WHERE uname="+"'"+uname+"'");
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
}
