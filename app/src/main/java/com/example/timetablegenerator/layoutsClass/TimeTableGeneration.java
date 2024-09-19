package com.example.timetablegenerator.layoutsClass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.timetablegenerator.GeneticAlgorithm.Class;
import com.example.timetablegenerator.GeneticAlgorithm.GeneticAlgorithm;
import com.example.timetablegenerator.GeneticAlgorithm.Population;
import com.example.timetablegenerator.GeneticAlgorithm.Schedule;
import com.example.timetablegenerator.GeneticAlgorithm.Variables;
import com.example.timetablegenerator.R;
import com.example.timetablegenerator.database.SectionConn;
import com.example.timetablegenerator.dbModel.SectionModel;

import java.util.ArrayList;
import java.util.Comparator;

public class TimeTableGeneration extends AppCompatActivity {

    Toolbar tb;
    Button getTT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_time_table_generation);
        getTT = findViewById(R.id.generateTT);
        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Drawable nav = tb.getNavigationIcon();
            if (nav != null) {
                nav.setTint(getResources().getColor(R.color.white));
            }
        }
        getTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Class> schedules = new ArrayList<>();
                ArrayList<SectionModel> sections = new SectionConn(TimeTableGeneration.this).retrieveRecords();
                Population population = new Population(new Variables().POPULATION_SIZE,TimeTableGeneration.this);
                int generationNum = 0;
                population.getSchedules().sort(Comparator.comparing(Schedule::getFitness).reversed());
                GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(TimeTableGeneration.this);

                while(population.getSchedules().get(1).getFitness() != 1.0){
                    Toast.makeText(TimeTableGeneration.this, "in while loop", Toast.LENGTH_SHORT).show();
                    generationNum +=1;
                    Log.d("Timetable123", "Fitness: "+population.getSchedules().get(0).getFitness());
                    Log.d("Timetable123", "Generation #" + generationNum);
                    population = geneticAlgorithm.evolve(population);
                    population.getSchedules().sort(Comparator.comparing(Schedule::getFitness).reversed());
                    schedules = population.getSchedules().get(0).getClasses();
                }
                for(SectionModel section:sections){
                    Toast.makeText(TimeTableGeneration.this, "in section for loop", Toast.LENGTH_SHORT).show();

                    Log.d("Timetable123", "section " + section.getSecId() + " " + section.getSecDeptName());
                    Log.d("Timetable123", "----------------------------------------------------------------------------------");
                    for (Class aClass : schedules) {
                        int i = 0;
                        if (aClass.getSection().equals(section.getSecId())) {
                            Log.d("Timetable123", "ClassID: "+(aClass.get_Id()));
                            Log.d("Timetable123", "Course Name: "+aClass.get_Course().get(i).getCourseName());
                            Log.d("Timetable123", "Room no: "+aClass.get_Room().getRoomNo());
                         //   Log.d("Timetable123", aClass.get_RoomNo());
                            Log.d("Timetable123", "Instructor Name: "+ aClass.get_Instructor());
                            Log.d("Timetable123", "Meeting: " +aClass.get_MeetingTime().getMtime()+"    "+aClass.get_MeetingTime().getMday());
                            i += 1;
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public void timetable(){
        ArrayList<Class> schedule = new ArrayList<>();
        ArrayList<SectionModel> sections = new SectionConn(TimeTableGeneration.this).retrieveRecords();
        Population population = new Population(new Variables().POPULATION_SIZE,TimeTableGeneration.this);
        int generationNum = 0;
        population.getSchedules().sort(Comparator.comparing(Schedule::getFitness).reversed());
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(TimeTableGeneration.this);
        while(population.getSchedules().get(0).getFitness() != 1.0) {
            generationNum += 1;
            Log.d("Timetable123", "Generation# " + generationNum);
            population = geneticAlgorithm.evolve(population);
            population.getSchedules().sort(Comparator.comparing(Schedule::getFitness).reversed());
            schedule = population.getSchedules().get(0).getClasses();
        }

    }

      /*  public void getTimetable () {
            ArrayList<Class> schedule = new ArrayList<>();
            ArrayList<SectionModel> sections = new SectionConn(TimeTableGeneration.this).getAllSectionRecords();
            Population population = new Population(new Variables().POPULATION_SIZE, TimeTableGeneration.this);
            int generationNum = 0;
            population.getSchedules().sort(Comparator.comparing(Schedule::getFitness).reversed());
            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(TimeTableGeneration.this);

            while (population.getSchedules().get(0).getFitness() != 1.0) {
                generationNum += 1;
                Log.d("Timetable123", "Generation #" + generationNum);
                population = geneticAlgorithm.evolve(population);
                population.getSchedules().sort(Comparator.comparing(Schedule::getFitness).reversed());
                schedule = population.getSchedules().get(0).getClasses();

                for (SectionModel section : sections) {
                    Log.d("Timetable123", "section " + section.getSecId() + " " + section.getSecDeptName());
                    Log.d("Timetable123", "----------------------------------------------------------------------------------");
                    int i = 0;
                    for (Class aClass : schedule) {
                        if (aClass.getSection().equals(section.getSecId())) {
                            Log.d("Timetable123", String.valueOf(aClass.get_Id()));
                            Log.d("Timetable123", aClass.get_Course().get(i).getCourseName());
                            Log.d("Timetable123", aClass.get_Room().getRoomNo());
                            Log.d("Timetable123", aClass.get_Instructor());
                            Log.d("Timetable123", aClass.get_MeetingTime());
                            i += 1;
                        }
                    }
                }
            }
            Log.d("Timetable123", sections.get(0).getSecId());

        }

       */
}