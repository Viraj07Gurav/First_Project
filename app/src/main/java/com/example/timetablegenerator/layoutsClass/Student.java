package com.example.timetablegenerator.layoutsClass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetablegenerator.R;
import com.example.timetablegenerator.database.DBHelper;
import com.example.timetablegenerator.database.StudentConn;
import com.example.timetablegenerator.dbModel.StudentModel;
import com.example.timetablegenerator.tableHelper.DeptNCourseTableHelper;
import com.example.timetablegenerator.tableHelper.InstructorTableHelper;
import com.example.timetablegenerator.tableHelper.StudentTableHelper;
import com.example.timetablegenerator.tableHelper.TimetableTableHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class Student extends AppCompatActivity {

    ExtendedFloatingActionButton fab;
    TableLayout tl;
    Toolbar tb;
    SwipeRefreshLayout refreshLayout;
    StudentModel studentModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Drawable nav = tb.getNavigationIcon();
            if(nav != null) {
                nav.setTint(getResources().getColor(R.color.white));
            }
        }

        tl = findViewById(R.id.TableDesign);
        tl.setStretchAllColumns(true);
        studentTable();

        fab = findViewById(R.id.insertData);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertStudentDialog();
            }
        });

        refreshLayout = findViewById(R.id.swipeLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                studentTable();
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

    public void studentTable(){
        tl.removeAllViews();
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 40;

        String[][] records = new StudentTableHelper(getApplicationContext()).getRecords();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Students (" + records.length + ") ");

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
                tv.setText("Student Name");
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
                tv2.setText("Username");
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
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv3.setGravity(Gravity.CENTER);
            tv3.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv3.setText("Department");
                tv3.setBackgroundColor(Color.parseColor("#95f0e7"));
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3.setText(String.valueOf(row[2]));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            final TextView tv4 = new TextView(this);
            tv4.setGravity(Gravity.CENTER);
            tv4.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv4.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv4.setText("Section");
                tv4.setBackgroundColor(Color.parseColor("#95f0e7"));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                tv4.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv4.setBackgroundColor(Color.parseColor("#ffffff"));
                tv4.setTextColor(Color.parseColor("#000000"));
                tv4.setText(String.valueOf(row[3]));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
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
            tr.addView(tv4);

            if(tr.getId()== 0){
                tr.setClickable(false);
            }
            else {
                tr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu menu = new PopupMenu(Student.this, tr);
                        menu.getMenuInflater().inflate(R.menu.table_update_menu, menu.getMenu());
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @SuppressLint("NonConstantResourceId")
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                TableRow tableRow = (TableRow) v;
                                TextView uname = (TextView) tableRow.getChildAt(1); //to get uname of the corresponding clicked record
                                String unameText = uname.getText().toString();

                                switch (item.getItemId()) {
                                    case R.id.updateRecord:
                                        updateStudentDialog(unameText);
                                        break;
                                    case R.id.deleteRecord:
                                        AlertDialog.Builder delete = new AlertDialog.Builder(Student.this);
                                        delete.setTitle("Delete Record")
                                                .setMessage("Are you sure want to delete the record?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (new StudentConn(getApplicationContext()).deleteStudent(unameText)) {
                                                            studentTable();
                                                            Toast.makeText(Student.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(Student.this, "Failed to delete record", Toast.LENGTH_SHORT).show();
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
                final TableRow trSep = new TableRow(Student.this);
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
                tvSepLay.span = 4;
                tvSep.setLayoutParams(tvSepLay);
                tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                tvSep.setHeight(1);
                trSep.addView(tvSep);
                tl.addView(trSep, trParamsSep);
            }
        }
    }

    private void insertStudentDialog(){
        Dialog d = new Dialog(this, R.style.Dialog);
        d.setTitle("Student Data");
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.activity_student_register, null);
        d.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView studLabel = v.findViewById(R.id.studRegisterLabel);
        studLabel.setVisibility(View.GONE);
        TextView studAtClg = v.findViewById(R.id.studAtClg);
        studAtClg.setVisibility(View.GONE);
        TextView login = v.findViewById(R.id.logIn);
        login.setVisibility(View.GONE);
        TextInputEditText fullName = v.findViewById(R.id.fullName);
        TextInputEditText uname= v.findViewById(R.id.uname);
        TextInputEditText pwd = v.findViewById(R.id.pwd);
        TextInputEditText confirmPwd = v.findViewById(R.id.confirmPwd);
        AutoCompleteTextView dept = v.findViewById(R.id.deptName);
        AutoCompleteTextView section = v.findViewById(R.id.sectionName);
        Button addStudent = v.findViewById(R.id.signUpBtn);
        addStudent.setText("Insert");
        d.setContentView(v);

        String[] departments = new DeptNCourseTableHelper(this).getDepartmentName();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, departments);
        dept.setAdapter(adapter);

        dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dept.showDropDown();
            }
        });


        section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   ArrayList<String> sections = new SectionConn(StudentRegister.this).getDeptSections(dept.getText().toString());;

                if(dept.getText().toString().isEmpty()){
                    dept.requestFocus();
                    dept.setError("Select Department first");
                }
                else{
                    String[] sections = new TimetableTableHelper(Student.this).getSections(dept.getText().toString());
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Student.this, android.R.layout.simple_list_item_1, sections);
                    section.setAdapter(adapter1);
                    section.showDropDown();
                }
            }
        });

        final boolean[] validated = {false};
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Objects.requireNonNull(fullName.getText()).toString().isEmpty() || Objects.requireNonNull(uname.getText()).toString().isEmpty() || Objects.requireNonNull(pwd.getText()).toString().isEmpty() || Objects.requireNonNull(confirmPwd.getText()).toString().isEmpty() || dept.getText().toString().isEmpty() || section.getText().toString().isEmpty()) {
                    Toast.makeText(Student.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else if (Arrays.stream(departments).noneMatch(Predicate.isEqual(dept.getText().toString()))) {
                    dept.requestFocus();
                    dept.setError("No such Department");
                } else if (Arrays.stream(departments).anyMatch(Predicate.isEqual(dept.getText().toString()))) {
                    String[] sections = new TimetableTableHelper(Student.this).getSections(dept.getText().toString());
                    validated[0] = true;
                    if (Arrays.stream(sections).noneMatch(Predicate.isEqual(section.getText().toString()))) {
                        section.requestFocus();
                        section.setError("No such Section");
                        validated[0] = false;
                    }
                }
                if (validated[0]) {
                    if (Objects.requireNonNull(pwd.getText()).toString().equals(Objects.requireNonNull(confirmPwd.getText()).toString())) {

                        studentModel = new StudentModel();
                        studentModel.setStudFullName(fullName.getText().toString());
                        studentModel.setStudUname(uname.getText().toString());
                        studentModel.setStudPwd(pwd.getText().toString());
                        studentModel.setStudDept(dept.getText().toString());
                        studentModel.setStudSect(section.getText().toString());

                        if (new StudentConn(Student.this).insertStudent(studentModel)) {
                            fullName.setText("");
                            uname.setText("");
                            pwd.setText("");
                            confirmPwd.setText("");
                            dept.setText("");
                            section.setText("");
                            studentTable();
                            Toast.makeText(Student.this, "Data inserted successfully. Pull down to refresh", Toast.LENGTH_SHORT).show();
                            d.dismiss();
                        } else {
                            Toast.makeText(Student.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Student.this, "Password mismatch", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        d.show();
    }

    private void updateStudentDialog(String oldUname){
        Dialog d = new Dialog(this, R.style.Dialog);
        d.setTitle("Student Data");
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.activity_student_register, null);
        d.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView studLabel = v.findViewById(R.id.studRegisterLabel);
        studLabel.setVisibility(View.GONE);
        TextView studAtClg = v.findViewById(R.id.studAtClg);
        studAtClg.setVisibility(View.GONE);
        TextView login = v.findViewById(R.id.logIn);
        login.setVisibility(View.GONE);
        TextInputEditText fullName = v.findViewById(R.id.fullName);
        TextInputEditText uname= v.findViewById(R.id.uname);
        TextInputEditText pwd = v.findViewById(R.id.pwd);
        TextInputEditText confirmPwd = v.findViewById(R.id.confirmPwd);
        AutoCompleteTextView dept = v.findViewById(R.id.deptName);
        AutoCompleteTextView section = v.findViewById(R.id.sectionName);
        Button updateStudent = v.findViewById(R.id.signUpBtn);
        updateStudent.setText("Update");
        d.setContentView(v);

        String[] departments = new DeptNCourseTableHelper(this).getDepartmentName();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, departments);
        dept.setAdapter(adapter);

        dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dept.showDropDown();
            }
        });


        section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   ArrayList<String> sections = new SectionConn(StudentRegister.this).getDeptSections(dept.getText().toString());;

                if(dept.getText().toString().isEmpty()){
                    dept.requestFocus();
                    dept.setError("Select Department first");
                }
                else{
                    String[] sections = new TimetableTableHelper(Student.this).getSections(dept.getText().toString());
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Student.this, android.R.layout.simple_list_item_1, sections);
                    section.setAdapter(adapter1);
                    section.showDropDown();
                }
            }
        });

        //gets records from DB and stores in array
        String[][] records = new StudentTableHelper(getApplicationContext()).getRecords();
        for (String[] record : records) {
            try {
                //checks for a particular data with id
                if (record[1].equals(oldUname)) {
                    //if found sets the views text with data from DB
                    fullName.setText(record[0]);
                    uname.setText(record[1]);
                    pwd.setText(record[4]);
                    confirmPwd.setText(record[4]);
                    dept.setText(record[2]);
                    section.setText(record[3]);
                }
            }
            catch (SQLiteException e){
                e.printStackTrace();
            }
        }

        final boolean[] validated = {false};
        updateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Objects.requireNonNull(fullName.getText()).toString().isEmpty() || Objects.requireNonNull(uname.getText()).toString().isEmpty() || Objects.requireNonNull(pwd.getText()).toString().isEmpty() || Objects.requireNonNull(confirmPwd.getText()).toString().isEmpty() || dept.getText().toString().isEmpty() || section.getText().toString().isEmpty()) {
                    Toast.makeText(Student.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else if (Arrays.stream(departments).noneMatch(Predicate.isEqual(dept.getText().toString()))) {
                    dept.requestFocus();
                    dept.setError("No such Department");
                } else if (Arrays.stream(departments).anyMatch(Predicate.isEqual(dept.getText().toString()))) {
                    String[] sections = new TimetableTableHelper(Student.this).getSections(dept.getText().toString());
                    validated[0] = true;
                    if (Arrays.stream(sections).noneMatch(Predicate.isEqual(section.getText().toString()))) {
                        section.requestFocus();
                        section.setError("No such Section");
                        validated[0] = false;
                    }
                }

                if (validated[0]) {
                    if (Objects.requireNonNull(pwd.getText()).toString().equals(Objects.requireNonNull(confirmPwd.getText()).toString())) {

                        studentModel = new StudentModel();
                        studentModel.setStudFullName(fullName.getText().toString());
                        studentModel.setStudUname(uname.getText().toString());
                        studentModel.setStudPwd(pwd.getText().toString());
                        studentModel.setStudDept(dept.getText().toString());
                        studentModel.setStudSect(section.getText().toString());

                        if (new StudentConn(Student.this).updateStud(oldUname, studentModel)) {
                            fullName.setText("");
                            uname.setText("");
                            pwd.setText("");
                            confirmPwd.setText("");
                            dept.setText("");
                            section.setText("");
                            studentTable();
                            Toast.makeText(Student.this, "Data updated successfully. Pull down to refresh", Toast.LENGTH_SHORT).show();
                            d.dismiss();
                        } else {
                            Toast.makeText(Student.this, "Data not updated", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Student.this, "Password mismatch", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        d.show();
    }
}