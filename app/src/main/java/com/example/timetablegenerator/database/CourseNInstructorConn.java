package com.example.timetablegenerator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.timetablegenerator.dbModel.CourseNInstructorModel;

import java.util.ArrayList;

public class CourseNInstructorConn {
    Context c;
    DBHelper helper;
    SQLiteDatabase MyDB;

    public CourseNInstructorConn(Context c) {
        this.c = c;
        helper = new DBHelper(c);
    }

    public boolean insertCourse(CourseNInstructorModel courseNInstructorModel){
        try{
            MyDB = helper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("course_number", courseNInstructorModel.getCourseNo());
            values.put("course_name", courseNInstructorModel.getCourseName());
            values.put("max_numb_students", courseNInstructorModel.getMaxNoOfStud());

            long result = MyDB.insert("course", null, values);
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

    public ArrayList<CourseNInstructorModel> retrieveRecords() {
        ArrayList<CourseNInstructorModel> courseRecords = new ArrayList<>();
        String[] columns = {"course_number", "course_name", "max_numb_students"};

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.query("course", columns, null, null, null, null, null);

            CourseNInstructorModel courseNInstructorModel;
            if (c != null) {
                while (c.moveToNext()) {
                    String courseNumber = c.getString(c.getColumnIndexOrThrow("course_number"));
                    String courseName = c.getString(c.getColumnIndexOrThrow("course_name"));
                    String maxNumbStudents = c.getString(c.getColumnIndexOrThrow("max_numb_students"));

                    courseNInstructorModel = new CourseNInstructorModel();
                    courseNInstructorModel.setCourseNo(courseNumber);
                    courseNInstructorModel.setCourseName(courseName);
                    courseNInstructorModel.setMaxNoOfStud(maxNumbStudents);
                    courseRecords.add(courseNInstructorModel);
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
        return courseRecords;
    }

    public boolean updateCourse(String oldCourseNum, CourseNInstructorModel courseNInstructorModel){
        String courseNo, courseName, maxStuds;

        courseNo = courseNInstructorModel.getCourseNo();
        courseName = courseNInstructorModel.getCourseName();
        maxStuds = courseNInstructorModel.getMaxNoOfStud();

        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("UPDATE course SET course_number="+"'"+courseNo+"'"+", course_name="+"'"+courseName+"'"+", max_numb_students="+"'"+maxStuds+"'"+" WHERE course_number="+"'"+oldCourseNum+"'");
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

    public boolean deleteCourse(String courseNo){
        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("DELETE FROM course WHERE course_number="+"'"+courseNo+"'");
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

    //for course_instructor table:
    public  boolean insertCourseInstructors(ArrayList<String> instructors, CourseNInstructorModel courseNInstructorModel){
        String courseId = courseNInstructorModel.getCourseId();
        String instructorName;
        MyDB = helper.getWritableDatabase();
        try {
            for (int i = 0; i < instructors.size(); i++) {
                instructorName = instructors.get(i);
                MyDB.execSQL("INSERT INTO course_instructors(course_id, instructor_id) VALUES ("+"'"+courseId+"', (SELECT instructor.id FROM instructor WHERE instructor.name="+"'"+instructorName+"'))");
                Log.d("InstructorArrayListDB", instructorName);
            }
            return true;
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
        finally {
            helper.close();
        }

        Log.d("InstructorArrayListDB", String.valueOf(instructors.size()));
        return  false;
    }

    public boolean updateCourseInstructors(String oldCourseId, ArrayList<String> instructors, CourseNInstructorModel courseNInstructorModel){
        String courseId = courseNInstructorModel.getCourseId();
        String instructorName;
        MyDB = helper.getWritableDatabase();
        try{
            MyDB.execSQL("DELETE from course_instructors WHERE course_id="+"'"+oldCourseId+"'");
            for (int i = 0; i < instructors.size(); i++) {
                instructorName = instructors.get(i);
                Log.d("InstructorsArrayListDB",instructorName);

                MyDB.execSQL("INSERT INTO course_instructors(course_id, instructor_id) VALUES ("+"'"+courseId+"', (SELECT instructor.id FROM instructor WHERE instructor.name="+"'"+instructorName+"'))");
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

    public boolean deleteCourseInstructors(String courseNo){
        try{
            MyDB = helper.getWritableDatabase();
            MyDB.execSQL("DELETE from course_instructors WHERE course_id="+"'"+courseNo+"'");
            MyDB.execSQL("UPDATE sqlite_sequence SET seq = (SELECT max(id) FROM course_instructors) WHERE name = 'course_instructors';");
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


    public String getCourseId(String courseName){
        String id = "";

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.rawQuery("SELECT course.course_number FROM course WHERE course.course_name=?", new String[]{courseName});

            if (c != null) {
                while (c.moveToNext()) {
                     id = c.getString(c.getColumnIndexOrThrow("course_number"));
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
        return id;
    }

    public ArrayList<CourseNInstructorModel> checkInstructorsRecords(String courseId){
        ArrayList<CourseNInstructorModel> assignedInstructorsRecords = new ArrayList<>();

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.rawQuery("SELECT DISTINCT course.course_name, instructor.name FROM course, instructor JOIN course_instructors ON course.course_number = course_instructors.course_id AND course_instructors.course_id= ? AND instructor.id = course_instructors.instructor_id", new String[]{courseId});

            CourseNInstructorModel courseNInstructorModel;
            if (c != null) {
                while (c.moveToNext()) {
                    String courseName = c.getString(c.getColumnIndexOrThrow("course_name"));
                    String instructorName = c.getString(c.getColumnIndexOrThrow("name"));

                    courseNInstructorModel = new CourseNInstructorModel();
                    courseNInstructorModel.setCourseName(courseName);
                    courseNInstructorModel.setInstructorName(instructorName);
                    assignedInstructorsRecords.add(courseNInstructorModel);
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
        return assignedInstructorsRecords;
    }

    public ArrayList<CourseNInstructorModel> getAllCourseRecords() {
        ArrayList<CourseNInstructorModel> records = new ArrayList<>();
        String[] courseColumns = {"course_number", "course_name", "max_numb_students"};
        String[] courseInColumns = {"id", "course_id", "instructor_id"};

        Cursor c = null;
        Cursor c1 = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.query("course", courseColumns, null, null, null, null, null);
            c1 = MyDB.query("course_instructors", courseInColumns, null, null, null, null, null);

            CourseNInstructorModel courseNInstructorModel = new CourseNInstructorModel();
            if (c != null) {
                while (c.moveToNext()) {
                    String courseNumber = c.getString(c.getColumnIndexOrThrow("course_number"));
                    String courseName = c.getString(c.getColumnIndexOrThrow("course_name"));
                    String maxNumbStudents = c.getString(c.getColumnIndexOrThrow("max_numb_students"));

                    courseNInstructorModel.setCourseNo(courseNumber);
                    courseNInstructorModel.setCourseName(courseName);
                    courseNInstructorModel.setMaxNoOfStud(maxNumbStudents);
                    records.add(courseNInstructorModel);
                }
            }
            if (c1 != null) {
                while (c1.moveToNext()){
                    String cinId = c1.getString(c1.getColumnIndexOrThrow("id"));
                    String courseId = c1.getString(c1.getColumnIndexOrThrow("course_id"));
                    String instructorId = c1.getString(c1.getColumnIndexOrThrow("instructor_id"));

                    courseNInstructorModel.setCiId(cinId);
                    courseNInstructorModel.setCourseId(courseId);
                    courseNInstructorModel.setInstructorId(instructorId);
                    records.add(courseNInstructorModel);
                }
            }
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            if (c != null && c1 != null) {
                c.close();
                c1.close();
            }
            helper.close();
        }
        return records;
    }

    public ArrayList<CourseNInstructorModel> getCourseInstructorsRecords(String courseId){
        ArrayList<CourseNInstructorModel> records = new ArrayList<>();

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.rawQuery("SELECT DISTINCT course.course_name, course.max_numb_students, instructor.name FROM course, instructor JOIN course_instructors ON course.course_number = course_instructors.course_id AND course_instructors.course_id= ? AND instructor.id = course_instructors.instructor_id", new String[]{courseId});

            CourseNInstructorModel courseNInstructorModel;
            if (c != null) {
                while (c.moveToNext()) {
                    String cName = c.getString(c.getColumnIndexOrThrow("course_name"));
                    String instructorName = c.getString(c.getColumnIndexOrThrow("name"));
                    String maxStuds = c.getString(c.getColumnIndexOrThrow("max_numb_students"));

                    courseNInstructorModel = new CourseNInstructorModel();
                    courseNInstructorModel.setCourseName(cName);
                    courseNInstructorModel.setInstructorName(instructorName);
                    courseNInstructorModel.setMaxNoOfStud(maxStuds);

                    Log.d("courseNInstructorModel ",courseNInstructorModel.getCourseName()+" "+courseNInstructorModel.getInstructorName());
                    records.add(courseNInstructorModel);
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
            if(helper!=null){
                helper.close();
            }
        }
        return records;
    }

    public ArrayList<CourseNInstructorModel> getSpecificCourseRecords(String cName) {
        ArrayList<CourseNInstructorModel> records = new ArrayList<>();

        Cursor c = null;
        try {
            MyDB = helper.getReadableDatabase();
            c = MyDB.rawQuery("SELECT * FROM course WHERE course.course_name = ?", new String[]{cName});

            CourseNInstructorModel courseNInstructorModel = new CourseNInstructorModel();
            if (c != null) {
                while (c.moveToNext()) {
                    String courseNumber = c.getString(c.getColumnIndexOrThrow("course_number"));
                    String courseName = c.getString(c.getColumnIndexOrThrow("course_name"));
                    String maxNumbStudents = c.getString(c.getColumnIndexOrThrow("max_numb_students"));

                    courseNInstructorModel.setCourseNo(courseNumber);
                    courseNInstructorModel.setCourseName(courseName);
                    courseNInstructorModel.setMaxNoOfStud(maxNumbStudents);
                    records.add(courseNInstructorModel);
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
