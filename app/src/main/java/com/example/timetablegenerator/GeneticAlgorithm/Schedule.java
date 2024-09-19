package com.example.timetablegenerator.GeneticAlgorithm;

import android.content.Context;

import com.example.timetablegenerator.database.CourseNInstructorConn;
import com.example.timetablegenerator.database.DeptNCourseConn;
import com.example.timetablegenerator.database.SectionConn;
import com.example.timetablegenerator.database.TimeConn;
import com.example.timetablegenerator.dbModel.CourseNInstructorModel;
import com.example.timetablegenerator.dbModel.DeptNCourseModel;
import com.example.timetablegenerator.dbModel.SectionModel;
import com.example.timetablegenerator.dbModel.TimeModel;

import java.util.ArrayList;

import java.util.Random;

public class Schedule {
    Context c;
    Data data;
    ArrayList<Class> classes;
    Integer numberOfConflicts;
    double fitness;
    Integer classNumb;
    Boolean isFitnessChanged;
    ArrayList<SectionModel> sections;
    ArrayList<TimeModel> meetingTime;
    ArrayList<DeptNCourseModel> courses;
    ArrayList<CourseNInstructorModel> crsInst;

    public Schedule(Context c) {
        this.c = c;
        this.data = new Data(c);
        this.classes = new ArrayList<>();
        this.numberOfConflicts = 0;
        this.fitness = -1;
        this.classNumb = 1;
        this.isFitnessChanged = true;
        this.sections = new SectionConn(c).retrieveRecords();
        this.meetingTime = new TimeConn(c).getAllTimeRecords();
    }

    public ArrayList<Class> getClasses() {
        isFitnessChanged = true;
        return classes;
    }

    public Integer getNumberOfConflicts() {
        return numberOfConflicts;
    }

    public double getFitness() {
        if (isFitnessChanged) {
            this.fitness = this.calculate_fitness();
            isFitnessChanged = false;
        }
        return fitness;
    }

    /*
    * For total sections the schedule function makes a class with random genes
    * It takes into account the number of classes in a section, total courses available for the section
    * The instructors and other details are selected randomly
    */
    public Schedule initialize() {
        for (SectionModel section : sections) {
            String dept = section.getSecDeptName();
            int n = Integer.parseInt(section.getNumOfClassPerWeek());
            if (n <= meetingTime.size()) {
                courses = new DeptNCourseConn(c).checkCoursesRecords(dept);
                for (DeptNCourseModel course : courses) {
                    String courseName = course.getCourseName();
                    for (int i = 0; i < (Math.floorDiv(n, courses.size())); i++) {
                        //crsInst = new CourseNInstructorConn(c).getCourseInstructorsRecords(courseName);
                        crsInst = new CourseNInstructorConn(c).getCourseInstructorsRecords(new CourseNInstructorConn(c).getCourseId(courseName));
                        Class newClass = new Class(classNumb, dept, section.getSecId(), course.getCourseName(), c);
                        classNumb += 1;
                        Random random = new Random();
                        int randomMeeting = random.nextInt(meetingTime.size());
                        newClass.set_MeetingTime(data.getMeetingTimes().get(randomMeeting));

                        int randomRoom = random.nextInt(data.getRooms().size());
                       // newClass.set_RoomNo((data.getRooms().get(randomRoom).getRoomNo()));
                       // newClass.set_rSeatingCap(data.getRooms().get(randomRoom).getSeatingCap());
                        newClass.set_Room(data.getRooms().get(randomRoom));

                        int randomInstructor = random.nextInt(crsInst.size());
                        newClass.set_Instructor((crsInst.get(randomInstructor).getInstructorName()));
                        newClass.set_MaxNoOfStud(crsInst.get(randomInstructor).getMaxNoOfStud());

                        classes.add(newClass);
                    }
                }
            } else {
                n = meetingTime.size();
               courses = new DeptNCourseConn(c).checkCoursesRecords(dept);
                for (DeptNCourseModel course : courses) {
                    String courseName = course.getCourseName();
                    for (int i = 0; i < (Math.floorDiv(n, courses.size())); i++) {
                        //ArrayList<CourseNInstructorModel> crsInst = new CourseNInstructorConn(c).getCourseInstructorsRecords(courseName);
                        ArrayList<CourseNInstructorModel> crsInst = new CourseNInstructorConn(c).getCourseInstructorsRecords(new CourseNInstructorConn(c).getCourseId(courseName));
                        Class newClass = new Class(classNumb, dept, section.getSecId(), course.getCourseName(), c);

                        classNumb += 1;
                        Random random = new Random();
                        int randomMeeting = random.nextInt(meetingTime.size());
                        newClass.set_MeetingTime(data.getMeetingTimes().get(randomMeeting));

                        int randomRoom = random.nextInt(data.getRooms().size());
                       // newClass.set_RoomNo(data.getRooms().get(randomRoom).getRoomNo());
                      //  newClass.set_rSeatingCap(data.getRooms().get(randomRoom).getSeatingCap());
                        newClass.set_Room(data.getRooms().get(randomRoom));

                        int randomInstructor = random.nextInt(crsInst.size());
                        newClass.set_Instructor(crsInst.get(randomInstructor).getInstructorName());
                        newClass.set_MaxNoOfStud(crsInst.get(randomInstructor).getMaxNoOfStud());

                        classes.add(newClass);
                    }
                }

            }
        }
        return this;
    }


    /*
    *
    *  Calculate fitness checks for the constraints such as
    * max no of students in a course and the seating capacity of the room to be allocated
    * two classes getting assigned the same meeting with different sections
    * getting assigned the same room or instructor
    *
    */
    private double calculate_fitness() {

        numberOfConflicts  = 0;
        classes = getClasses();
        for(int i=0; i<classes.size(); i++) {
            if (Integer.parseInt(classes.get(i).room.getSeatingCap()) < Integer.parseInt(classes.get(i).maxNoOfStud.trim())) {
                numberOfConflicts += 1;
            } else {
                for (int j = 0; j < classes.size(); j++) {
                    if (j >= i) {
                        if ((classes.get(i).meetingTime.equals(classes.get(j).meetingTime)) && (!classes.get(i).sectionId.equals(classes.get(j).sectionId)) && (classes.get(i).section.equals(classes.get(j).section))) {
                            if (classes.get(i).room == classes.get(j).room)
                                numberOfConflicts += 1;
                            if (classes.get(i).instructor.equals(classes.get(j).instructor))
                                numberOfConflicts += 1;
                        }
                    }
                }
            }
        }
        return  1/(1.0 * numberOfConflicts + 1);
    }
}
