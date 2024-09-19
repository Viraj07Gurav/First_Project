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
import com.example.timetablegenerator.database.DeptNCourseConn;
import com.example.timetablegenerator.dbModel.DeptNCourseModel;
import com.example.timetablegenerator.tableHelper.DeptNCourseTableHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class Department extends AppCompatActivity {

    ExtendedFloatingActionButton fab;
    TableLayout tl;
    DeptNCourseModel deptNCourseModel;
    Toolbar tb;
    SwipeRefreshLayout refreshLayout;
    boolean deptCourseIsClicked = false; //to check deptCourses Button click


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
        departmentTable();

        fab = findViewById(R.id.insertData);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDeptDialog();
            }
        });

        refreshLayout = findViewById(R.id.swipeLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                departmentTable();
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

    private void departmentTable() {
        tl.removeAllViews();
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 40;

        deptNCourseModel = new DeptNCourseModel();
        String[][] records = new DeptNCourseTableHelper(getApplicationContext()).getRecords();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Departments (" + records.length + ") ");

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
                tv.setText("No.");
                tv.setBackgroundColor(Color.parseColor("#95f0e7"));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv.setText(String.valueOf(i+1));
                Log.d("COUNT123", String.valueOf(i+1));
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
                tv2.setText("Department Name");
                tv2.setBackgroundColor(Color.parseColor("#95f0e7"));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                tv2.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                tv2.setTextColor(Color.parseColor("#000000"));
                tv2.setText(String.valueOf(row[0]));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
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

            if (tr.getId() == 0) {
                tr.setClickable(false);
            } else {
                tr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu menu = new PopupMenu(Department.this, tr);
                        menu.getMenuInflater().inflate(R.menu.update_delete_check_menu, menu.getMenu());
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @SuppressLint("NonConstantResourceId")
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                TableRow tableRow = (TableRow) v;
                                TextView deptName = (TextView) tableRow.getChildAt(1); //to get dept id of the corresponding clicked record
                                String deptNameText = deptName.getText().toString();
                                Log.d("DepartmentName", deptNameText);
                                switch (item.getItemId()) {
                                    case R.id.updateRecord:
                                        updateDeptDialog(deptNameText);
                                        break;

                                    case R.id.deleteRecord:
                                        AlertDialog.Builder delete = new AlertDialog.Builder(Department.this);
                                        delete.setTitle("Delete Record")
                                                .setMessage("Are you sure want to delete the record?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (new DeptNCourseConn(getApplicationContext()).deleteDeptCourses(deptNameText) && new DeptNCourseConn(getApplicationContext()).deleteDept(deptNameText)){
                                                            departmentTable();
                                                            Toast.makeText(Department.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(Department.this, "Failed to delete record", Toast.LENGTH_SHORT).show();
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
                                        checkCourses(deptNameText);
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
                final TableRow trSep = new TableRow(Department.this);
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
    }

    public  void insertDeptDialog(){
        Dialog d = new Dialog(this, R.style.Dialog);
        d.setTitle("Department Data");
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.dept_dialog, null);
        d.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextInputEditText deptName = v.findViewById(R.id.insertDeptName);
        Button deptCourses = v.findViewById(R.id.deptCourses);
        Button addDeptBtn = v.findViewById(R.id.addDeptBtn);
        d.setContentView(v);
        addDeptBtn.setText("Insert Department");
        String[] listCourses = new DeptNCourseTableHelper(getApplicationContext()).getCourseName(); //the list of courses
        boolean[] checkedItems; //to set boolean value for the items
        ArrayList<Integer> userCourses = new ArrayList<>(); //to get the position of selected item
        ArrayList<String> checkedCourses = new ArrayList<>(); //to store the selected item

        checkedItems = new boolean[listCourses.length];
        deptCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deptCourseIsClicked = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(Department.this);
                builder.setTitle("Assign courses for the department")
                        .setMultiChoiceItems(listCourses, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                if(isChecked){
                                    if(!userCourses.contains(position)){
                                        userCourses.add(position);
                                    }
                                }else if(userCourses.contains(position)){
                                    userCourses.remove(position);
                                }
                            }
                        })
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                for(int i=0; i<userCourses.size();i++){
                                 /*   item = item + listInstructors[userInstructor.get(i)];
                                    Log.d("InstructorList", item);

                                  */
                                    checkedCourses.add(listCourses[userCourses.get(i)]);
                                }
                                Log.d("checkedCourses", String.valueOf(checkedCourses));
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
                                    userCourses.clear();
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        addDeptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.requireNonNull(deptName.getText()).toString().isEmpty()){
                    Toast.makeText(Department.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(!deptCourseIsClicked){
                    Toast.makeText(Department.this, "Please select Course(s)", Toast.LENGTH_SHORT).show();
                }
                else {
                    DeptNCourseModel deptNCourseModel = new DeptNCourseModel();
                    deptNCourseModel.setDeptName(deptName.getText().toString());

                    if(new DeptNCourseConn(getApplicationContext()).insertDept(deptNCourseModel) && new DeptNCourseConn(getApplicationContext()).insertDeptCourse(checkedCourses, deptNCourseModel)){
                        deptName.setText("");
                        departmentTable();
                        Toast.makeText(Department.this, "Data inserted. Pull down to refresh", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    } else {
                        Toast.makeText(Department.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        d.show();
    }

    public void  updateDeptDialog(String oldDeptName){
        Dialog d = new Dialog(this, R.style.Dialog);
        d.setTitle("Department Data");
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.dept_dialog, null);
        d.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextInputEditText deptName = v.findViewById(R.id.insertDeptName);
        Button deptCourses = v.findViewById(R.id.deptCourses);
        Button updateDeptBtn = v.findViewById(R.id.addDeptBtn);
        d.setContentView(v);

        updateDeptBtn.setText("Update Department");
        boolean[] checkedItems; //to set boolean value for the items
        ArrayList<Integer> userCourses = new ArrayList<>(); //to get the position of selected item
        ArrayList<String> checkedCourses = new ArrayList<>(); //to store the selected item

        String[] listCourses = new DeptNCourseTableHelper(getApplicationContext()).getCourseName(); //the list of courses
        checkedItems = new boolean[listCourses.length];
        deptCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deptCourseIsClicked = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(Department.this);
                builder.setTitle("Assign courses for the department")
                        .setMultiChoiceItems(listCourses, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                if(isChecked){
                                    if(!userCourses.contains(position)){
                                        userCourses.add(position);
                                    }
                                }else if(userCourses.contains(position)){
                                    userCourses.remove(position);
                                }
                            }
                        })
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                for(int i=0; i<userCourses.size();i++){
                                 /*   item = item + listInstructors[userInstructor.get(i)];
                                    Log.d("InstructorList", item);

                                  */
                                    checkedCourses.add(listCourses[userCourses.get(i)]);
                                }
                                Log.d("checkedCourses", String.valueOf(checkedCourses));
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
                                    userCourses.clear();
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //gets records from DB and stores in array
        String[][] records = new DeptNCourseTableHelper(getApplicationContext()).getRecords();
        for (String[] record : records) {
            try {
                //checks for a particular data with id
                if (record[0].equals(oldDeptName)) {
                    //if found sets the views text with data from DB
                    deptName.setText(record[0]);
                }
            }
            catch (SQLiteException e){
                e.printStackTrace();
            }
        }
        updateDeptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.requireNonNull(deptName.getText()).toString().isEmpty()){
                    Toast.makeText(Department.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(!deptCourseIsClicked){
                    Toast.makeText(Department.this, "Please select Course(s)", Toast.LENGTH_SHORT).show();
                }
                else {
                    DeptNCourseModel deptNCourseModel = new DeptNCourseModel();
                    deptNCourseModel.setDeptName(deptName.getText().toString());

                    if(new DeptNCourseConn(getApplicationContext()).updateDept(oldDeptName, deptNCourseModel) && new DeptNCourseConn(getApplicationContext()).updateDeptCourses(oldDeptName, checkedCourses,deptNCourseModel)){
                        deptName.setText("");
                        departmentTable();
                        Toast.makeText(Department.this, "Data updated. Pull down to refresh", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    } else {
                        Toast.makeText(Department.this, "Data not updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        d.show();
    }

    public void checkCourses(String deptName) {
        AlertDialog.Builder checkCourses = new AlertDialog.Builder(Department.this);
        checkCourses.setTitle("Courses assigned to " + deptName)
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        View view = LayoutInflater.from(Department.this).inflate(R.layout.check_layout, null);
        TableLayout tl = view.findViewById(R.id.checkTableDesign);
        tl.setStretchAllColumns(true);
        checkCourses.setView(view);

        tl.removeAllViews();
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 40;

        deptNCourseModel = new DeptNCourseModel();
        ArrayList<DeptNCourseModel> assignedCourses = new DeptNCourseConn(getApplicationContext()).checkCoursesRecords(deptName);
        String[][] records = new String[assignedCourses.size()][2];

        for(int i=0; i<records.length;i++) {
            deptNCourseModel = assignedCourses.get(i);
            records[i][0] = deptNCourseModel.getDeptName();
            records[i][1] = deptNCourseModel.getCourseName();
        }


        TextView textSpacer;
        // -1 means heading row
        for (int i = -1; i < records.length; i++) {
            String[] row = null;
            if (i > -1) {
                row = records[i];
            } else {
                textSpacer = new TextView(Department.this);
                textSpacer.setText("");
            }
            // data columns
            final TextView tv = new TextView(Department.this);
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

            final TextView tv2 = new TextView(Department.this);
            tv2.setGravity(Gravity.CENTER);
            tv2.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv2.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv2.setText("Department Name");
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
                tv3.setText("Course Name");
                tv3.setBackgroundColor(Color.parseColor("#95f0e7"));
            } else {
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv3.setText(row[1]);
            }
            //add table row
            final TableRow tr = new TableRow(Department.this);
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
                final TableRow trSep = new TableRow(Department.this);
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