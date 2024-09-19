package com.example.timetablegenerator.layoutsClass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetablegenerator.R;
import com.example.timetablegenerator.database.CourseNInstructorConn;
import com.example.timetablegenerator.dbModel.CourseNInstructorModel;
import com.example.timetablegenerator.tableHelper.CourseNInstructorTableHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class Course extends AppCompatActivity {

    ExtendedFloatingActionButton fab;
    TableLayout tl;
    CourseNInstructorModel courseNInstructorModel;
    Toolbar tb;
    SwipeRefreshLayout refreshLayout;
    boolean courseInstructorsIsClicked = false; //to check courseInstructors Button click

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
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

        tl = findViewById(R.id.TableDesign);
        tl.setStretchAllColumns(true);
        courseTable();

        fab = findViewById(R.id.insertData);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertCourseDialog();
            }
        });

        refreshLayout = findViewById(R.id.swipeLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                courseTable();
                refreshLayout.setRefreshing(false);
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

    public void courseTable() {
        tl.removeAllViews();
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 40;

        courseNInstructorModel = new CourseNInstructorModel();
        String[][] records = new CourseNInstructorTableHelper(getApplicationContext()).getCourseRecords();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Courses (" + records.length + ") ");

        TextView textSpacer;
        // -1 means heading row
        for (int i = -1; i < records.length; i++) {
            String[] row = null;
            if (i > -1) {
                row = records[i];
            } else {
                textSpacer = new TextView(this);
                textSpacer.setText("");
            }
            // data columns
            final TextView tv = new TextView(this);
            tv.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv.setText("Course Number");
                tv.setBackgroundColor(Color.parseColor("#95f0e7"));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv.setText(String.valueOf(row[0]));
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            final TextView tv2 = new TextView(this);
            tv2.setGravity(Gravity.CENTER);
            tv2.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv2.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv2.setText("Course Name");
                tv2.setBackgroundColor(Color.parseColor("#95f0e7"));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                tv2.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                tv2.setTextColor(Color.parseColor("#000000"));
                tv2.setText(String.valueOf(row[1]));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            final TextView tv3 = new TextView(this);
            tv3.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            tv3.setPadding(5, 0, 0, 5);
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv3.setGravity(Gravity.CENTER);
            if (i == -1) {
                tv3.setText("Max Students");
                tv3.setBackgroundColor(Color.parseColor("#95f0e7"));
            } else {
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv3.setText(row[2]);
            }

            //add table row
            final TableRow tr = new TableRow(this);
            tr.setId(i + 1);
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

            if (tr.getId() == 0) {
                tr.setClickable(false);
            } else {
                tr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu menu = new PopupMenu(Course.this, tr);
                        menu.getMenuInflater().inflate(R.menu.update_delete_check_course, menu.getMenu());
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @SuppressLint("NonConstantResourceId")
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                TableRow tableRow = (TableRow) v;
                                TextView courseNo = (TextView) tableRow.getChildAt(0); //to get course no of the corresponding clicked record
                                String courseNoText = courseNo.getText().toString();
                                TextView courseName= (TextView) tableRow.getChildAt(1);
                                String courseNameText = courseName.getText().toString();

                                switch (item.getItemId()) {
                                    case R.id.updateRecord:
                                        updateCourseDialog(courseNoText);
                                        break;
                                    case R.id.deleteRecord:
                                        AlertDialog.Builder delete = new AlertDialog.Builder(Course.this);
                                        delete.setTitle("Delete Record")
                                                .setMessage("Are you sure want to delete the record?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (new CourseNInstructorConn(getApplicationContext()).deleteCourse(courseNoText) && new CourseNInstructorConn(getApplicationContext()).deleteCourseInstructors(courseNoText)) {
                                                            courseTable();
                                                            Toast.makeText(Course.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(Course.this, "Failed to delete record", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                })
                                                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                })
                                                .create()
                                                .show();
                                        break;
                                    case R.id.checkRecords:
                                        checkInstructors(courseNoText,courseNameText);
                                    default:
                                }
                                return false;
                            }
                        });
                        menu.show();
                    }
                });
            }

            tl.addView(tr, trParams);
            if (i > -1) {
                // add separator row
                final TableRow trSep = new TableRow(Course.this);
                TableLayout.LayoutParams trParamsSep = new
                        TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParamsSep.setMargins(leftRowMargin, topRowMargin,
                        rightRowMargin, bottomRowMargin);
                trSep.setLayoutParams(trParamsSep);
                TextView tvSep = new TextView(this);
                TableRow.LayoutParams tvSepLay = new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                tvSepLay.span = 3;
                tvSep.setLayoutParams(tvSepLay);
                tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                tvSep.setHeight(1);
                trSep.addView(tvSep);
                tl.addView(trSep, trParamsSep);
            }
        }
    }

    public void insertCourseDialog() {
        Dialog d = new Dialog(this, R.style.Dialog);
        d.setTitle("Course Data");
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.course_dialog, null);
        d.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextInputEditText courseNo = v.findViewById(R.id.insertCourseNo);
        TextInputEditText courseName = v.findViewById(R.id.insertCourseName);
        TextInputEditText maxStudNo = v.findViewById(R.id.insertMaxStud);
        Button courseInstructors = v.findViewById(R.id.courseInstructor);
        Button addCourseBtn = v.findViewById(R.id.addCourseBtn);
        d.setContentView(v);

        addCourseBtn.setText("Insert Course");
        String[] listInstructors; //the list of instructors
        boolean[] checkedItems; //to set boolean value for the items
        ArrayList<Integer> userInstructor = new ArrayList<>(); //to get the position of selected item
        ArrayList<String> checkedInstructors = new ArrayList<>(); //to store the selected item

        listInstructors = new CourseNInstructorTableHelper(getApplicationContext()).getInstructorName();
        checkedItems = new boolean[listInstructors.length];

        courseInstructors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Log.d("Instructor", Arrays.toString(listInstructors));
                courseInstructorsIsClicked = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(Course.this);
                builder.setTitle("Assign teachers for the course")
                        .setMultiChoiceItems(listInstructors, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                if(isChecked){
                                    if(!userInstructor.contains(position)){
                                        userInstructor.add(position);
                                    }
                                }else if(userInstructor.contains(position)){
                                    userInstructor.remove(listInstructors[position]);
                                }
                            }
                        })
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(int i=0; i<userInstructor.size();i++){
                                    checkedInstructors.add(listInstructors[userInstructor.get(i)]);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(int i =0; i<checkedItems.length;i++){
                                    checkedItems[i] = false;
                                    userInstructor.clear();
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(courseNo.getText()).toString().isEmpty() || Objects.requireNonNull(courseName.getText()).toString().isEmpty() || Objects.requireNonNull(maxStudNo.getText()).toString().isEmpty()) {
                    Toast.makeText(Course.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(!courseInstructorsIsClicked || checkedInstructors.size() == 0){
                    Toast.makeText(Course.this, "Please select Instructor(s)", Toast.LENGTH_SHORT).show();
                }
                else {
                    //for course table
                    CourseNInstructorModel courseNInstructorModel = new CourseNInstructorModel();
                    courseNInstructorModel.setCourseNo(courseNo.getText().toString());
                    courseNInstructorModel.setCourseName(courseName.getText().toString());
                    courseNInstructorModel.setMaxNoOfStud(maxStudNo.getText().toString());
                    courseNInstructorModel.setCourseId(courseNo.getText().toString());

                    if (new CourseNInstructorConn(getApplicationContext()).insertCourse(courseNInstructorModel) && new CourseNInstructorConn(getApplicationContext()).insertCourseInstructors(checkedInstructors, courseNInstructorModel)) {
                        courseNo.setText("");
                        courseName.setText("");
                        maxStudNo.setText("");
                        courseTable();
                        Toast.makeText(Course.this, "Data inserted. Pull down to refresh", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    } else {
                        Toast.makeText(Course.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        d.show();
    }

    public void updateCourseDialog(String oldCourseNo) {
        Dialog d = new Dialog(this, R.style.Dialog);
        d.setTitle("Course Data");
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.course_dialog, null);
        d.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextInputEditText courseNo = v.findViewById(R.id.insertCourseNo);
        TextInputEditText courseName = v.findViewById(R.id.insertCourseName);
        TextInputEditText maxStudNo = v.findViewById(R.id.insertMaxStud);
        Button courseInstructors = v.findViewById(R.id.courseInstructor);
        Button updateCourseBtn = v.findViewById(R.id.addCourseBtn);
        d.setContentView(v);

        //gets records from DB and stores in array
        String[][] courseRecords = new CourseNInstructorTableHelper(getApplicationContext()).getCourseRecords();

        for (String[] record : courseRecords) {
            try {
                //checks for a particular data with id
                if (record[0].equals(oldCourseNo)) {
                    //if found sets the views text with data from DB
                    courseNo.setText(record[0]);
                    courseName.setText(record[1]);
                    maxStudNo.setText(record[2]);
                }
            }
            catch (SQLiteException e){
                e.printStackTrace();
            }
        }

        updateCourseBtn.setText("Update Course");
        String[] listInstructors; //the list of instructors
        boolean[] checkedItems; //to set boolean value for the items
        ArrayList<Integer> userInstructor = new ArrayList<>(); //to get the position of selected item
        ArrayList<String> checkedInstructors = new ArrayList<>(); //to store the selected item

        listInstructors = new CourseNInstructorTableHelper(getApplicationContext()).getInstructorName();
        checkedItems = new boolean[listInstructors.length];

        courseInstructors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Log.d("Instructor", Arrays.toString(listInstructors));
                AlertDialog.Builder builder = new AlertDialog.Builder(Course.this);
                builder.setTitle("Assign teachers for the course")
                        .setMultiChoiceItems(listInstructors, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                if(isChecked){
                                    if(!userInstructor.contains(position)){
                                        userInstructor.add(position);
                                    }
                                }else if(userInstructor.contains(position)){
                                    userInstructor.remove(position);
                                }
                            }
                        })
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(int i=0; i<userInstructor.size();i++){
                                    checkedInstructors.add(listInstructors[userInstructor.get(i)]);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(int i =0; i<checkedItems.length;i++){
                                    checkedItems[i] = false;
                                    userInstructor.clear();
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        updateCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(courseNo.getText()).toString().isEmpty() || Objects.requireNonNull(courseName.getText()).toString().isEmpty() || Objects.requireNonNull(maxStudNo.getText()).toString().isEmpty()) {
                    Toast.makeText(Course.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    //for course table
                    CourseNInstructorModel courseNInstructorModel = new CourseNInstructorModel();
                    courseNInstructorModel.setCourseNo(courseNo.getText().toString());
                    courseNInstructorModel.setCourseName(courseName.getText().toString());
                    courseNInstructorModel.setMaxNoOfStud(maxStudNo.getText().toString());

                    courseNInstructorModel.setCourseId(courseNo.getText().toString());

                    if(new CourseNInstructorConn(getApplicationContext()).updateCourse(oldCourseNo, courseNInstructorModel) && new CourseNInstructorConn(getApplicationContext()).updateCourseInstructors(oldCourseNo, checkedInstructors, courseNInstructorModel)){
                        courseNo.setText("");
                        courseName.setText("");
                        maxStudNo.setText("");
                        courseTable();
                        Toast.makeText(Course.this, "Data updated. Pull down to refresh", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    } else {
                        Toast.makeText(Course.this, "Data not updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        d.show();
    }

    public void checkInstructors(String courseNo, String courseName) {
        AlertDialog.Builder checkCourses = new AlertDialog.Builder(Course.this);
        checkCourses.setTitle("Instructors assigned to " + courseName)
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        View view = LayoutInflater.from(Course.this).inflate(R.layout.check_layout, null);
        TableLayout tl = view.findViewById(R.id.checkTableDesign);
        tl.setStretchAllColumns(true);
        checkCourses.setView(view);

        tl.removeAllViews();
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 40;

        courseNInstructorModel = new CourseNInstructorModel();
        ArrayList<CourseNInstructorModel> assignedCourses = new CourseNInstructorConn(getApplicationContext()).checkInstructorsRecords(courseNo);
        String[][] records = new String[assignedCourses.size()][2];

        for(int i=0; i<records.length;i++) {
            courseNInstructorModel = assignedCourses.get(i);
            records[i][0] = courseNInstructorModel.getCourseName();
            records[i][1] = courseNInstructorModel.getInstructorName();
        }


        TextView textSpacer;
        // -1 means heading row
        for (int i = -1; i < records.length; i++) {
            String[] row = null;
            if (i > -1) {
                row = records[i];
            } else {
                textSpacer = new TextView(Course.this);
                textSpacer.setText("");
            }
            // data columns
            final TextView tv = new TextView(Course.this);
            tv.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv.setText("No.");
                tv.setBackgroundColor(Color.parseColor("#95f0e7"));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv.setText(String.valueOf(i + 1));
                Log.d("COUNT123", String.valueOf(i + 1));
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            final TextView tv2 = new TextView(Course.this);
            tv2.setGravity(Gravity.CENTER);
            tv2.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv2.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv2.setText("Course Name");
                tv2.setBackgroundColor(Color.parseColor("#95f0e7"));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                tv2.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                tv2.setTextColor(Color.parseColor("#000000"));
                tv2.setText(row[0]);
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            final TextView tv3 = new TextView(this);
            tv3.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            tv3.setPadding(5, 0, 0, 5);
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv3.setGravity(Gravity.CENTER);
            if (i == -1) {
                tv3.setText("Instructor Name");
                tv3.setBackgroundColor(Color.parseColor("#95f0e7"));
            } else {
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv3.setText(row[1]);
            }
            //add table row
            final TableRow tr = new TableRow(Course.this);
            tr.setId(i + 1);
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

            tl.addView(tr, trParams);
            if (i > -1) {
                // add separator row
                final TableRow trSep = new TableRow(Course.this);
                TableLayout.LayoutParams trParamsSep = new
                        TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParamsSep.setMargins(leftRowMargin, topRowMargin,
                        rightRowMargin, bottomRowMargin);
                trSep.setLayoutParams(trParamsSep);
                TextView tvSep = new TextView(this);
                TableRow.LayoutParams tvSepLay = new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                tvSepLay.span = 2;
                tvSep.setLayoutParams(tvSepLay);
                tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                tvSep.setHeight(1);
                trSep.addView(tvSep);
                tl.addView(trSep, trParamsSep);
            }
        }
        AlertDialog dialog = checkCourses.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

    }
}