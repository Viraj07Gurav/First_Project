package com.example.timetablegenerator.GeneticAlgorithm;

import android.content.Context;

import com.example.timetablegenerator.database.CourseNInstructorConn;
import com.example.timetablegenerator.dbModel.CourseNInstructorModel;
import com.example.timetablegenerator.dbModel.RoomModel;
import com.example.timetablegenerator.dbModel.TimeModel;

import java.util.ArrayList;

public class Class {
    Context c;
    Integer sectionId;
    String department;
    ArrayList<CourseNInstructorModel> course;
    String instructor;
    RoomModel room;
    String section;
    String maxNoOfStud;
    TimeModel meetingTime;

    public Class(Integer Id, String department, String section, String course, Context c) {
        this.c = c;
        this.sectionId = Id;
        this.department = department;
        this.course = new CourseNInstructorConn(c).getSpecificCourseRecords(course);
        this.section = section;
        this.instructor = null;
        this.meetingTime = null;
        this.room = null;
        this.maxNoOfStud = null;
    }

    public Integer get_Id() {
        return sectionId;
    }

    public ArrayList<CourseNInstructorModel> get_Course() {
        return course;
    }

    public String get_Instructor() {
        return instructor;
    }

    public RoomModel get_Room() {
        return room;
    }

    public String getSection() {
        return section;
    }

    public void set_Instructor(String instructor) {
        this.instructor = instructor;
    }

    public void set_Room(RoomModel room) {
        this.room = room;
    }

    public void set_MaxNoOfStud(String maxNoOfStud) {
        this.maxNoOfStud = maxNoOfStud;
    }

    public TimeModel get_MeetingTime() {
        return meetingTime;
    }

    public void set_MeetingTime(TimeModel meetingTime) {
        this.meetingTime = meetingTime;
    }

    public WeekDay getMDay(){
        return WeekDay.valueOf(meetingTime.getMday());
    }

    public String getMTime(){
        return meetingTime.getMtime();
    }
}




