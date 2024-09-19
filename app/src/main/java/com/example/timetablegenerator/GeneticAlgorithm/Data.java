package com.example.timetablegenerator.GeneticAlgorithm;

import android.content.Context;

import com.example.timetablegenerator.database.CourseNInstructorConn;
import com.example.timetablegenerator.database.DeptNCourseConn;
import com.example.timetablegenerator.database.InstructorConn;
import com.example.timetablegenerator.database.RoomConn;
import com.example.timetablegenerator.database.TimeConn;
import com.example.timetablegenerator.dbModel.CourseNInstructorModel;
import com.example.timetablegenerator.dbModel.DeptNCourseModel;
import com.example.timetablegenerator.dbModel.InstructorModel;
import com.example.timetablegenerator.dbModel.RoomModel;
import com.example.timetablegenerator.dbModel.TimeModel;

import java.util.ArrayList;

public class Data {
    Context c;
    ArrayList<RoomModel> rooms;
    ArrayList<TimeModel> meetingTimes;
    ArrayList<InstructorModel> instructors;
    ArrayList<CourseNInstructorModel> courses;
    ArrayList<DeptNCourseModel> depts;

    public Data(Context c) {
        this.c = c;
        this.rooms = new RoomConn(c).getAllRoomRecords();
        this.meetingTimes = new TimeConn(c).getAllTimeRecords();
        this.instructors = new InstructorConn(c).getAllInRecords();
        this.courses = new CourseNInstructorConn(c).getAllCourseRecords();
        this.depts = new DeptNCourseConn(c).getAllDeptRecords();
    }

    public ArrayList<RoomModel> getRooms() {
        return rooms;
    }

    public ArrayList<TimeModel> getMeetingTimes() {
        return meetingTimes;
    }

}
