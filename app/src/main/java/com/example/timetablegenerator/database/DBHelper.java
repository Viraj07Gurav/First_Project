package com.example.timetablegenerator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static  final  String  DB_NAME = "timetable.db";
    public DBHelper(Context context) {
        super(context, DB_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE course (course_number varchar(5) NOT NULL PRIMARY KEY,  course_name  varchar(40) NOT NULL,  max_numb_students  varchar(65) NOT NULL)");
        MyDB.execSQL("CREATE TABLE course_instructors( id  integer NOT NULL PRIMARY KEY AUTOINCREMENT,  course_id  varchar(5) NOT NULL REFERENCES  course  ( course_number ) DEFERRABLE INITIALLY DEFERRED,  instructor_id  integer NOT NULL REFERENCES  instructor  ( id ) DEFERRABLE INITIALLY DEFERRED)");
        MyDB.execSQL("CREATE TABLE department( id  integer NOT NULL PRIMARY KEY AUTOINCREMENT,  dept_name  varchar(50) NOT NULL)");
        MyDB.execSQL("CREATE TABLE department_courses( id  integer NOT NULL PRIMARY KEY AUTOINCREMENT,  department_id  integer NOT NULL REFERENCES  department  ( id ) DEFERRABLE INITIALLY DEFERRED,  course_id  varchar(5) NOT NULL REFERENCES  course  ( course_number ) DEFERRABLE INITIALLY DEFERRED)");
        MyDB.execSQL("CREATE TABLE instructor(id  integer NOT NULL PRIMARY KEY AUTOINCREMENT, uid  varchar(6) NOT NULL UNIQUE,  name  varchar(25) NOT NULL,  pwd  varchar(10) NOT NULL)");
        MyDB.execSQL("CREATE TABLE meetingtime(pid  varchar(4) NOT NULL PRIMARY KEY,  time  varchar(50) NOT NULL,  day  varchar(15) NOT NULL)");
        MyDB.execSQL("CREATE TABLE room(id  integer NOT NULL PRIMARY KEY AUTOINCREMENT, room_number varchar(6) NOT NULL UNIQUE, seating_capacity integer NOT NULL)");
        MyDB.execSQL("CREATE TABLE section( section_id  varchar(25) NOT NULL PRIMARY KEY,  course_id  varchar(5) REFERENCES  course  ( course_number ) DEFERRABLE INITIALLY DEFERRED,  department_id  integer NOT NULL REFERENCES  department  ( id ) DEFERRABLE INITIALLY DEFERRED,  instructor_id  integer REFERENCES  instructor  ( id ) DEFERRABLE INITIALLY DEFERRED,  meeting_time_id  varchar(4)  REFERENCES  meetingtime  ( pid ) DEFERRABLE INITIALLY DEFERRED,  room_id  integer  REFERENCES  room  ( id ) DEFERRABLE INITIALLY DEFERRED,  num_class_in_week  integer NOT NULL)");
        MyDB.execSQL("CREATE TABLE students(uname varchar(25) NOT NULL PRIMARY KEY, fullName varchar(50) NOT NULL, pwd varchar(10) NOT NULL, dept varchar(50) NOT NULL, section varchar(25) NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("DROP TABLE IF EXISTS course");
        MyDB.execSQL("DROP TABLE IF EXISTS course_instructors");
        MyDB.execSQL("DROP TABLE IF EXISTS department");
        MyDB.execSQL("DROP TABLE IF EXISTS department_courses");
        MyDB.execSQL("DROP TABLE IF EXISTS instructor");
        MyDB.execSQL("DROP TABLE IF EXISTS meetingtime");
        MyDB.execSQL("DROP TABLE IF EXISTS room");
        MyDB.execSQL("DROP TABLE IF EXISTS section");
        MyDB.execSQL("DROP TABLE IF EXISTS students");
        onCreate(MyDB);
    }
}
