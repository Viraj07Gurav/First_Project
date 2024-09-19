package com.example.timetablegenerator.GeneticAlgorithm;

import android.content.Context;

import java.util.ArrayList;

public class Population {
    int size;
    Data data;
    ArrayList<Schedule> schedules = new ArrayList<>();
    Context c;


    public Population(int size, Context c) {
        this.c = c;
        this.size = size;
        this.data = new Data(c);

        /*
        * The population takes size for example in our case 9 and adds 9 schedules
        * */
        for(int i=0; i<size; i++){
            Schedule schedule = new Schedule(c).initialize();
            schedules.add(schedule);
        }
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;

    }
}
