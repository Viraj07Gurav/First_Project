package com.example.timetablegenerator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.timetablegenerator.dbModel.SectionModel;

import java.util.ArrayList;

public class SectionConn {
    Context c;
    DBHelper helper;
    SQLiteDatabase MyDB;

    public SectionConn(Context c) {
        this.c = c;
        helper = new DBHelper(c);
    }

    public boolean insertSection(SectionModel sectionModel){
        String deptName = sectionModel.getSecDeptName();
        Cursor c = null;
        try {
            MyDB = helper.getWritableDatabase();
            c =  MyDB.rawQuery("SELECT department.id FROM department WHERE department.dept_name= ? ", new String[]{deptName});

            c.moveToPosition(0);

            ContentValues values = new ContentValues();
            values.put("section_id", sectionModel.getSecId());
            values.put("department_id", c.getString(c.getColumnIndexOrThrow("id")));
            values.put("num_class_in_week", sectionModel.getNumOfClassPerWeek());

            long result = MyDB.insert("section", null, values);
            return result != -1;
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            helper.close();

        }
        return false;
    }

    public ArrayList<SectionModel> retrieveRecords() {
        ArrayList<SectionModel> secRecords = new ArrayList<>();

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.rawQuery("SELECT section.section_id, section.num_class_in_week, department.dept_name FROM section, department WHERE department.id=section.department_id", null);
            SectionModel sectionModel;
            if (c != null) {
                while (c.moveToNext()) {
                    String secId = c.getString(c.getColumnIndexOrThrow("section_id"));
                    String deptName = c.getString(c.getColumnIndexOrThrow("dept_name"));
                    String numClassInWeek = c.getString(c.getColumnIndexOrThrow("num_class_in_week"));

                    sectionModel = new SectionModel();
                    sectionModel.setSecId(secId);
                    sectionModel.setSecDeptName(deptName);
                    sectionModel.setNumOfClassPerWeek(numClassInWeek);
                    secRecords.add(sectionModel);
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
        return secRecords;
    }

    public boolean updateSection(String oldSecId, SectionModel sectionModel){
        String secId, deptName, numClassPerWeek, deptId;

        secId = sectionModel.getSecId();
        deptName = sectionModel.getSecDeptName();
        numClassPerWeek = sectionModel.getNumOfClassPerWeek();
        Cursor c, updateCursor;
        c = null; updateCursor = null;

        try{
            MyDB = helper.getWritableDatabase();
            c = MyDB.rawQuery("SELECT id FROM department WHERE dept_name = ?", new String[]{deptName});
            c.moveToPosition(0);
            deptId = c.getString(c.getColumnIndexOrThrow("id"));

            updateCursor = MyDB.rawQuery("UPDATE section SET section_id = ?, department_id = ?, num_class_in_week = ? WHERE section_id = ?", new String[]{secId, deptId, numClassPerWeek, oldSecId});
            updateCursor.moveToFirst();
            return true;
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
        finally {
            if(c!=null){
                c.close();
            }
            if(updateCursor != null){
                updateCursor.close();
            }
            helper.close();

        }
        return false;
    }
    public boolean deleteSection(String secId){
        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("DELETE FROM section WHERE section_id="+"'"+secId+"'");
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

    public ArrayList<String> getDeptSections(String deptName) {
        ArrayList<String> records = new ArrayList<>();

        Cursor c = null, c1 = null;
        try {
            MyDB = helper.getReadableDatabase();

            c = MyDB.rawQuery("SELECT id FROM department WHERE dept_name = ?", new String[]{deptName});
            c.moveToPosition(0);
            String deptId = c.getString(c.getColumnIndexOrThrow("id"));

            c1 = MyDB.rawQuery("SELECT section_id FROM section WHERE department_id = ?", new String[]{deptId});
            if (c1 != null) {
                while (c1.moveToNext()) {
                    String secId = c1.getString(c1.getColumnIndexOrThrow("section_id"));
                    Log.d("Timetable123", secId);
                    records.add(secId);
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            if (c1 != null) {
                c1.close();
            }
            helper.close();
        }
        return records;
    }
}
