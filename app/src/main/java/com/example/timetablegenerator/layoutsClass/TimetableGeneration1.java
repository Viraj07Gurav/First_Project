package com.example.timetablegenerator.layoutsClass;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.timetablegenerator.GeneticAlgorithm.Class;
import com.example.timetablegenerator.GeneticAlgorithm.GeneticAlgorithm;
import com.example.timetablegenerator.GeneticAlgorithm.Population;
import com.example.timetablegenerator.GeneticAlgorithm.Schedule;
import com.example.timetablegenerator.GeneticAlgorithm.Variables;
import com.example.timetablegenerator.R;
import com.example.timetablegenerator.database.DBHelper;
import com.example.timetablegenerator.database.SectionConn;
import com.example.timetablegenerator.database.TimetableConn;
import com.example.timetablegenerator.dbModel.RoomModel;
import com.example.timetablegenerator.dbModel.SectionModel;
import com.example.timetablegenerator.dbModel.TimeModel;
import com.example.timetablegenerator.dbModel.TimetableModel;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TimetableGeneration1 extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Class>> {

    Toolbar tb;
    Button generateTimetable;
    Button cancelGeneration;
    SwipeRefreshLayout refreshLayout;
    LinearLayout linearLayout;
    ProgressBar progressBar;
    private static final int LOADER_ID = 1;
    boolean cancelled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_generation);

        linearLayout = findViewById(R.id.linearLayoutfortt);
        generateTimetable = findViewById(R.id.generateTimetableBtn);
        cancelGeneration = findViewById(R.id.cancelGenerationBtn);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        tb = findViewById(R.id.toolbar);
   //     getSupportLoaderManager().initLoader(LOADER_ID, null, this);

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

        refreshLayout = findViewById(R.id.swipeLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });

        cancelGeneration.setEnabled(false);
        generateTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGenerate();
            }
        });

        cancelGeneration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCancel();
            }
        });

        ArrayList<TimetableModel> timetableModel = new TimetableConn(this).retrieveRecords();
        ArrayList<Class> timetableRecords = new ArrayList<>();
        Class oneSchedule = null;
        TimeModel timeModel;
        RoomModel roomModel;
        int classNumb = 0;
        for (int i = 0; i<timetableModel.size(); i++) {
            classNumb++;
            String dept = timetableModel.get(i).getTdeptName();
            String section = timetableModel.get(i).getTsecId();
            String course = timetableModel.get(i).getTcourseName();
            oneSchedule = new Class(classNumb, dept, section, course, this);
            oneSchedule.set_Instructor(timetableModel.get(i).getTinstructorName());

            timeModel = new TimeModel();
            roomModel = new RoomModel();
            timeModel.setMtime(timetableModel.get(i).getTmTime());
            timeModel.setMday(timetableModel.get(i).getTmDay());
            oneSchedule.set_MeetingTime(timeModel);

            roomModel.setRoomNo(timetableModel.get(i).getTroomNo());
            oneSchedule.set_Room(roomModel);
            timetableRecords.add(oneSchedule);
        }
        getTimetableLayout(timetableRecords);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClickGenerate(){
        generateTimetable.setEnabled(false);
        cancelGeneration.setEnabled(true);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<String>> asyncLoader = loaderManager.getLoader(LOADER_ID);

        if(asyncLoader == null){
            loaderManager.initLoader(LOADER_ID, null, this);
        }
        else{
            loaderManager.restartLoader(LOADER_ID, null, this);
        }
    }

    private void onClickCancel(){
        generateTimetable.setEnabled(true);
        cancelGeneration.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<String>> asyncLoader = loaderManager.getLoader(LOADER_ID);

        if(asyncLoader != null)  {
            cancelled = asyncLoader.cancelLoad();
            Log.d("Timetable123", "Cancelled: "+cancelled);

        }
    }

    private void getTimetableLayout(ArrayList<Class> data) {
        //ArrayList<TimetableModel> sections = new TimetableConn(getApplicationContext()).getSections();
        ArrayList<SectionModel> sections = new SectionConn(getApplicationContext()).retrieveRecords();
       /* sections.sort(Comparator.comparing(SectionModel::getSecId));
        sections.sort(Comparator.comparing(SectionModel::getSecDeptName));*/
       // sections.sort(Comparator.comparing(TimetableModel::getTsecId));
       // sections.sort(Comparator.comparing(TimetableModel::getTdeptName));
        Objects.requireNonNull(getSupportActionBar()).setTitle("Timetables (" + sections.size() + ") ");

        int tableId = 1;
        int textId = 1;


        for (SectionModel section : sections) {
            TableLayout tl = new TableLayout(getApplicationContext());
            tl.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TableLayout.LayoutParams tlParams = (TableLayout.LayoutParams) tl.getLayoutParams();
            tlParams.setMargins(0, 0, 0, 45);
            tl.setStretchAllColumns(true);
            tl.setId(tableId);
            tableId++;

            TextView title = new TextView(getApplicationContext());
            title.setText(String.format("%s                 Section: %s", section.getSecDeptName(), section.getSecId()));
            title.setId(textId);
            textId++;
            if (title.getParent() != null) {
                ((ViewGroup) title.getParent()).removeView(title);
            }
            linearLayout.addView(title);

            int leftRowMargin = 0;
            int topRowMargin = 0;
            int rightRowMargin = 0;
            int bottomRowMargin = 0;
            int textSize = 40;

            int i = -1;
            final TableRow trHeader = new TableRow(getApplicationContext());
            trHeader.setId(-1);
            i++;
            TableLayout.LayoutParams trHeaderParams = new
                    TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            trHeaderParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin,
                    bottomRowMargin);
            trHeader.setPadding(0, 0, 0, 0);
            trHeader.setLayoutParams(trHeaderParams);

            TextView textSpacer  = new TextView(getApplicationContext());
            textSpacer.setText("");

            final TextView header1 = new TextView(getApplicationContext());
            header1.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            header1.setGravity(Gravity.CENTER);
            header1.setPadding(5, 15, 0, 15);
            header1.setText("Class#");
            header1.setBackgroundColor(Color.parseColor("#95f0e7"));
            header1.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            final TextView header2 = new TextView(getApplicationContext());
            header2.setGravity(Gravity.CENTER);
            header2.setPadding(5, 15, 0, 15);
            header2.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
            header2.setText("Course");
            header2.setBackgroundColor(Color.parseColor("#95f0e7"));
            header2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            final TextView header3 = new TextView(getApplicationContext());
            header3.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            header3.setPadding(5, 0, 0, 5);
            header3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            header3.setGravity(Gravity.CENTER);
            header3.setText("Room");
            header3.setBackgroundColor(Color.parseColor("#95f0e7"));

            final TextView header4 = new TextView(getApplicationContext());
            header4.setGravity(Gravity.CENTER);
            header4.setPadding(5, 15, 0, 15);
            header4.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
            header4.setText("Instructor");
            header4.setBackgroundColor(Color.parseColor("#95f0e7"));
            header4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            final TextView header5 = new TextView(getApplicationContext());
            header5.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            header5.setPadding(5, 0, 0, 5);
            header5.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            header5.setGravity(Gravity.CENTER);
            header5.setText("Timing");
            header5.setBackgroundColor(Color.parseColor("#95f0e7"));

            trHeader.addView(header1);
            trHeader.addView(header2);
            trHeader.addView(header3);
            trHeader.addView(header4);
            trHeader.addView(header5);

            tl.addView(trHeader, trHeaderParams);

            int classNumb= 0;
            for (Class aClass : data) {
                if (aClass.getSection().equals(section.getSecId())) {
                    classNumb++;
                    if (i == -1) {
                        textSpacer = new TextView(getApplicationContext());
                        textSpacer.setText("");
                    }
                    // data columns
                    final TextView tv = new TextView(getApplicationContext());
                    tv.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv.setGravity(Gravity.CENTER);
                    tv.setPadding(5, 15, 0, 15);
                    if (i == -1) {
                        tv.setText("Class#");
                        tv.setBackgroundColor(Color.parseColor("#95f0e7"));
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                    tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    tv.setText(String.valueOf(classNumb));
                    tv.setTextColor(Color.parseColor("#000000"));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);


                    final TextView tv2 = new TextView(getApplicationContext());
                    tv2.setGravity(Gravity.CENTER);
                    tv2.setPadding(5, 15, 0, 15);
                    if (i == -1) {
                        tv2.setLayoutParams(new
                                TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv2.setText("Course");
                        tv2.setBackgroundColor(Color.parseColor("#95f0e7"));
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                    tv2.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.MATCH_PARENT));
                    tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                    tv2.setTextColor(Color.parseColor("#000000"));
                    tv2.setText(String.valueOf(aClass.get_Course().get(0).getCourseName()));
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);


                    final TextView tv3 = new TextView(getApplicationContext());
                    tv3.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT));
                    tv3.setPadding(5, 0, 0, 5);
                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    tv3.setGravity(Gravity.CENTER);
                    if (i == -1) {
                        tv3.setText("Room");
                        tv3.setBackgroundColor(Color.parseColor("#95f0e7"));
                    }
                    tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    tv3.setTextColor(Color.parseColor("#000000"));
                    tv3.setText(aClass.get_Room().getRoomNo());


                    final TextView tv4 = new TextView(getApplicationContext());
                    tv4.setGravity(Gravity.CENTER);
                    tv4.setPadding(5, 15, 0, 15);
                    if (i == -1) {
                        tv4.setLayoutParams(new
                                TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv4.setText("Instructor");
                        tv4.setBackgroundColor(Color.parseColor("#95f0e7"));
                        tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                    tv4.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.MATCH_PARENT));
                    tv4.setBackgroundColor(Color.parseColor("#ffffff"));
                    tv4.setTextColor(Color.parseColor("#000000"));
                    tv4.setText(String.valueOf(aClass.get_Instructor()));
                    tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

                    final TextView tv5 = new TextView(getApplicationContext());
                    tv5.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT));
                    tv5.setPadding(5, 0, 0, 5);
                    tv5.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    tv5.setGravity(Gravity.CENTER);
                    if (i == -1) {
                        tv5.setText("Timing");
                        tv5.setBackgroundColor(Color.parseColor("#95f0e7"));
                    }
                    tv5.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    tv5.setTextColor(Color.parseColor("#000000"));
                    tv5.setText(String.format("%s   %s", aClass.get_MeetingTime().getMday(), aClass.get_MeetingTime().getMtime()));

                    final TableRow tr = new TableRow(getApplicationContext());
                    tr.setId(i + 1);
                    i++;
                    TableLayout.LayoutParams trParams = new
                            TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT);
                    trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin,
                            bottomRowMargin);
                    tr.setPadding(0, 0, 0, 0);
                    tr.setLayoutParams(trParams);
                    tr.addView(tv);
                    tr.addView(tv2);
                    tr.addView(tv3);
                    tr.addView(tv4);
                    tr.addView(tv5);

                    tl.addView(tr, trParams);

                        final TableRow trSep = new TableRow(getApplicationContext());
                        TableLayout.LayoutParams trParamsSep = new
                                TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT);
                        trParamsSep.setMargins(leftRowMargin, topRowMargin,
                                rightRowMargin, bottomRowMargin);
                        trSep.setLayoutParams(trParamsSep);
                        TextView tvSep = new TextView(getApplicationContext());
                        TableRow.LayoutParams tvSepLay = new
                                TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT);
                        tvSepLay.span = 5;
                        tvSep.setLayoutParams(tvSepLay);
                        tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                        tvSep.setHeight(1);
                        trSep.addView(tvSep);

                    tl.addView(trSep, trParamsSep);
                 }
            }
            linearLayout.addView(tl, tlParams);
        }
    }

    @NonNull
    @Override
    public Loader<ArrayList<Class>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<ArrayList<Class>>(this) {

            @Override
            protected void onStartLoading() {
                progressBar.setVisibility(View.VISIBLE);
                forceLoad();
                super.onStartLoading();
            }

            @Nullable
            @Override
            public ArrayList<Class> loadInBackground() {

                ArrayList<Class> schedules = new ArrayList<>();
                ArrayList<SectionModel> sections = new SectionConn(getApplicationContext()).retrieveRecords();
                Population population = new Population(new Variables().POPULATION_SIZE, getApplicationContext());
                int generationNum = 0;
                population.getSchedules().sort(Comparator.comparing(Schedule::getFitness).reversed());
                Log.d("Timetable123Pop", Arrays.toString(population.getSchedules().toArray()));
                GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(getApplicationContext());

                /*
                *Fitness of 1 is considered to be the fittest solution here
                * New set of population is made by evolving the existing population
                * */
                while (population.getSchedules().get(0).getFitness() != 1.0) {
                    if (isLoadInBackgroundCanceled()) {
                        break;
                    } else {
                        generationNum += 1;
                        Log.d("Timetable123", "Fitness: " + population.getSchedules().get(0).getFitness());
                        Log.d("Timetable123", "Generation #" + generationNum);
                        population = geneticAlgorithm.evolve(population);
                        population.getSchedules().sort(Comparator.comparing(Schedule::getFitness).reversed());
                        schedules = population.getSchedules().get(0).getClasses();
                    }
                }
                if (!isLoadInBackgroundCanceled()) {
                    for (SectionModel section : sections) {
                        Log.d("Timetable123", "section " + section.getSecId() + " " + section.getSecDeptName());
                        Log.d("Timetable123", "----------------------------------------------------------------------------------");
                        for (Class aClass : schedules) {
                            int i = 0;
                            if (aClass.getSection().equals(section.getSecId())) {
                                Log.d("Timetable123", String.valueOf(aClass.get_Course().size()));
                                Log.d("Timetable123", "ClassID: " + (aClass.get_Id()));
                                Log.d("Timetable123", "Course Name: " + aClass.get_Course().get(i).getCourseName());
                                Log.d("Timetable123", "Room no: " + aClass.get_Room().getRoomNo());
                                Log.d("Timetable123", "Instructor Name: " + aClass.get_Instructor());
                                Log.d("Timetable123", "Meeting: " + aClass.get_MeetingTime().getMtime() + "    " + aClass.get_MeetingTime().getMday());
                                i += 1;
                            }
                        }
                    }
                }
                return schedules;
            }

            @Override
            protected void onReset() {
                onStopLoading();
                super.onReset();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Class>> loader, ArrayList<Class> data) {

        linearLayout.removeAllViews();
        progressBar.setVisibility(View.INVISIBLE);
        generateTimetable.setEnabled(true);
        cancelGeneration.setEnabled(false);

        ArrayList<SectionModel> sections = new SectionConn(getApplicationContext()).retrieveRecords();
        sections.sort(Comparator.comparing(SectionModel::getSecDeptName));
        DBHelper helper = new DBHelper(getApplicationContext());
        SQLiteDatabase MyDB = helper.getWritableDatabase();

        MyDB.execSQL("DROP TABLE IF EXISTS schedules");
        try {
            MyDB.execSQL("CREATE TABLE IF NOT EXISTS schedules(secId varchar(25) NOT NULL, deptName varchar(50) NOT NULL,classId varchar(6) NOT NULL, courseName varchar(40) NOT NULL, roomNo varchar(6) NOT Null, instructorName varchar(25) NOT NULL, mTime varchar(50) NOT NULL, mDay varchar(15) NOT NULL )");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        TimetableModel model = new TimetableModel();

        for (SectionModel section : sections) {
          data.sort(Comparator.comparing(Class::getSection));
          data.sort(Comparator.comparing(Class::getMDay));
            for (Class d : data) {
                if (section.getSecId().equals(d.getSection())) {
                    model.setTsecId(d.getSection());
                    model.setTdeptName(section.getSecDeptName());
                    model.setTclassId(d.get_Id().toString());
                    model.setTcourseName(d.get_Course().get(0).getCourseName());
                    model.setTinstructorName(d.get_Instructor());
                    model.setTroomNo(d.get_Room().getRoomNo());
                    model.setTmTime(d.get_MeetingTime().getMtime());
                    model.setTmDay(d.get_MeetingTime().getMday());

                    if(new TimetableConn(getApplicationContext()).insertTimetable(model)){
                        Log.d("Timetable123", "Timetable uploaded to DB");
                    }
                    else {
                        Log.d("Timetable123", "Failed to upload timetable to DB");
                    }
                }
            }
        }
        getTimetableLayout(data);
        Log.d("Timetable123 Child count", String.valueOf(linearLayout.getChildCount()));

        Toast.makeText(this, "Generated Timetable!", Toast.LENGTH_SHORT).show();
        Log.d("Timetable123", "Finished background task");
        getSupportLoaderManager().destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Class>> loader) {
        progressBar.setVisibility(View.INVISIBLE);
        generateTimetable.setEnabled(true);
        cancelGeneration.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}
