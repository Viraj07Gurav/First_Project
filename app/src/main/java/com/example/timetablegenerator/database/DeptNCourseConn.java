package com.example.timetablegenerator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.timetablegenerator.dbModel.DeptNCourseModel;

import java.util.ArrayList;

public class DeptNCourseConn {
    Context c;
    DBHelper helper;
    SQLiteDatabase MyDB;

    public DeptNCourseConn(Context c) {
        this.c = c;
        helper = new DBHelper(c);
    }

    public boolean insertDept( DeptNCourseModel deptNCourseModel){
        try{
            MyDB = helper.getWritableDatabase();
            ContentValues deptValues = new ContentValues();

            deptValues.put("dept_name", deptNCourseModel.getDeptName());
            long result = MyDB.insert("department", null, deptValues);
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
    
    public ArrayList<String> retrieveRecords(){
        ArrayList<String> departmentRecords = new ArrayList<>();

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.query("department", new String[]{"dept_name"}, null, null, null, null, null);
            
            if (c != null) {
                while (c.moveToNext()) {
                    String deptName = c.getString(c.getColumnIndexOrThrow("dept_name"));
                    departmentRecords.add(deptName);
                }
            }
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
        finally {
            if (c != null) {
                c.close();
            }
            helper.close();

        }
        return departmentRecords;
    }


    public boolean updateDept(String oldDeptName, DeptNCourseModel deptNCourseModel){
        String deptName = deptNCourseModel.getDeptName();

        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("UPDATE department SET dept_name="+"'"+deptName+"'"+" WHERE dept_name="+"'"+oldDeptName+"'");
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

    public boolean deleteDept(String deptName){
        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("DELETE FROM department WHERE dept_name="+"'"+deptName+"'");
            MyDB.execSQL("UPDATE sqlite_sequence SET seq = (SELECT max(id) FROM department) WHERE name = 'department';");

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

    //for department_courses table:
    public  boolean insertDeptCourse(ArrayList<String> courses, DeptNCourseModel deptNCourseModel){
        String deptName = deptNCourseModel.getDeptName();
        String courseName;

        MyDB = helper.getWritableDatabase();
        try {
            for (int i = 0; i < courses.size(); i++) {
                courseName = courses.get(i);
                MyDB.execSQL("INSERT INTO department_courses(department_id, course_id) VALUES ((SELECT department.id FROM department WHERE department.dept_name="+"'"+deptName+"'), (SELECT course.course_number FROM course WHERE course.course_name="+"'"+courseName+"'))");
                Log.d("CoursesArrayListDB", courseName);
            }
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

    public boolean updateDeptCourses(String oldDeptName, ArrayList<String> courses, DeptNCourseModel deptNCourseModel){
        String deptName = deptNCourseModel.getDeptName();
        String courseName;

        MyDB = helper.getWritableDatabase();
        try {
            MyDB.execSQL("DELETE FROM department_courses WHERE department_id=(Select department.id from department WHERE department.dept_name="+"'"+oldDeptName+"')");
            for (int i = 0; i < courses.size(); i++) {
                courseName = courses.get(i);
                MyDB.execSQL("INSERT INTO department_courses(department_id, course_id) VALUES ((SELECT department.id FROM department WHERE department.dept_name="+"'"+deptName+"'), (SELECT course.course_number FROM course WHERE course.course_name="+"'"+courseName+"'))");
                //  MyDB.execSQL("UPDATE department_courses SET department_id = (SELECT department.id from department WHERE department.dept_name="+"'"+deptName+"'), course_id = (SELECT course.course_number FROM course WHERE course.course_name="+"'"+courseName+"') WHERE department_id = (Select department.id from department WHERE department.dept_name="+"'"+oldDeptName+"')");
                Log.d("CoursesArrayListDB", courses.get(i));
            }
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

    public boolean deleteDeptCourses(String deptName){
        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("DELETE FROM department_courses WHERE department_id=(Select department.id from department WHERE department.dept_name="+"'"+deptName+"')");
            MyDB.execSQL("UPDATE sqlite_sequence SET seq = (SELECT max(department_id) FROM department_courses) WHERE name = 'department_courses';");
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

    public ArrayList<DeptNCourseModel> checkCoursesRecords(String deptName){
        ArrayList<DeptNCourseModel> assignedCoursesRecords = new ArrayList<>();

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.rawQuery("SELECT department.dept_name, course.course_name FROM department, course JOIN department_courses on department.id = department_courses.department_id AND department.id =(SELECT department.id FROM department WHERE department.dept_name = ?) AND course.course_number = department_courses.course_id", new String[]{deptName});

            DeptNCourseModel deptNCourseModel;
            if (c != null) {
                while (c.moveToNext()) {
                    String departmentName = c.getString(c.getColumnIndexOrThrow("dept_name"));
                    String courseName = c.getString(c.getColumnIndexOrThrow("course_name"));

                    deptNCourseModel = new DeptNCourseModel();
                    deptNCourseModel.setDeptName(departmentName);
                    deptNCourseModel.setCourseName(courseName);
                    assignedCoursesRecords.add(deptNCourseModel);
                }
            }
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
        finally {
            if (c != null) {
                c.close();
            }
            helper.close();
        }
        return assignedCoursesRecords;
    }



    public ArrayList<DeptNCourseModel> getAllDeptRecords(){
        ArrayList<DeptNCourseModel> records = new ArrayList<>();
        String[] deptColumns = {"id", "dept_name"};
        String[] deptCoursesColumns = {"id", "department_id", "course_id"};

        Cursor c = null;
        Cursor c1 = null;

        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.query("department", deptColumns, null, null, null, null, null);
            c1 = MyDB.query("department_courses", deptCoursesColumns, null, null, null, null, null);

            DeptNCourseModel deptNCourseModel = new DeptNCourseModel();
            if (c != null) {
                while (c.moveToNext()) {
                    String deptId = c.getString(c.getColumnIndexOrThrow("id"));
                    String deptName = c.getString(c.getColumnIndexOrThrow("dept_name"));

                    deptNCourseModel.setDeptId(deptId);
                    deptNCourseModel.setDeptName(deptName);
                    records.add(deptNCourseModel);
                }
            }
            if(c1 != null){
                while (c1.moveToNext()) {
                    String dcId = c1.getString(c1.getColumnIndexOrThrow("id"));
                    String dcDeptId = c1.getString(c1.getColumnIndexOrThrow("department_id"));
                    String dcCourseId = c1.getString(c1.getColumnIndexOrThrow("course_id"));

                    deptNCourseModel.setDcId(dcId);
                    deptNCourseModel.setDcDeptId(dcDeptId);
                    deptNCourseModel.setDcCourseId(dcCourseId);
                    records.add(deptNCourseModel);
                }
            }
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
        finally {
            if (c != null ) {
                c.close();
            }
            if(c1 != null){
                c1.close();
            }
            helper.close();
        }
        return records;
    }
}
