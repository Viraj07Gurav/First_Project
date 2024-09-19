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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetablegenerator.R;
import com.example.timetablegenerator.database.DeptNCourseConn;
import com.example.timetablegenerator.database.SectionConn;
import com.example.timetablegenerator.dbModel.SectionModel;
import com.example.timetablegenerator.tableHelper.SectionTableHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Section extends AppCompatActivity {

    ExtendedFloatingActionButton fab;
    TableLayout tl;
    SectionModel sectionModel;
    Toolbar tb;
    SwipeRefreshLayout refreshLayout;
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
        sectionTable();

        fab = findViewById(R.id.insertData);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertSectionDialog();

            }
        });

        refreshLayout = findViewById(R.id.swipeLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sectionTable();
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

     public void sectionTable() {
        tl.removeAllViews();
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 40;

        sectionModel = new SectionModel();
        String[][] records = new SectionTableHelper(getApplicationContext()).getRecords();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Sections (" + records.length + ") ");

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
                tv.setText("Section ID");
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
                tv2.setText("Department Name");
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
                tv3.setText("Num Class/Week");
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
                        PopupMenu menu = new PopupMenu(Section.this, tr);
                        menu.getMenuInflater().inflate(R.menu.table_update_menu, menu.getMenu());
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @SuppressLint("NonConstantResourceId")
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                TableRow tableRow = (TableRow) v;
                                TextView secId = (TextView) tableRow.getChildAt(0);//to get meeting id of the corresponding clicked record
                                String secIdText = secId.getText().toString();

                                switch (item.getItemId()) {
                                    case R.id.updateRecord:
                                        updateSection(secIdText);
                                        break;

                                    case R.id.deleteRecord:
                                        AlertDialog.Builder delete = new AlertDialog.Builder(Section.this);
                                        delete.setTitle("Delete Record")
                                                .setMessage("Are you sure want to delete the record?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (new SectionConn(getApplicationContext()).deleteSection(secIdText)) {
                                                            sectionTable();
                                                            Toast.makeText(Section.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(Section.this, "Failed to delete record", Toast.LENGTH_SHORT).show();
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
                final TableRow trSep = new TableRow(this);
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

    public void insertSectionDialog(){
        Dialog d = new Dialog(this, R.style.Dialog);
        d.setTitle("Department Data");
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.section_dialog, null);
        d.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextInputEditText secId = v.findViewById(R.id.insertSecId);
        Spinner deptName = v.findViewById(R.id.deptNameDrpDwn);
        Button addSecBtn = v.findViewById(R.id.addSecBtn);
        NumberPicker np = v.findViewById(R.id.numClassPerWeek);
        d.setContentView(v);

        ArrayList<String> spinnerRec = new DeptNCourseConn(Section.this).retrieveRecords();
        spinnerRec.add(0, "Select Dept");
        String[] departmentName = new String[spinnerRec.size()];
        for(int i = 0; i<spinnerRec.size(); i++){
            departmentName[i] = spinnerRec.get(i);
        }

        final String[] selectedDept = new String[1];
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departmentName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deptName.setAdapter(adapter);

        deptName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(Section.this, "Please select department", Toast.LENGTH_SHORT).show();
                }else{
                    selectedDept[0] = departmentName[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Section.this, "Please select department", Toast.LENGTH_SHORT).show();
            }

        });

        np.setMinValue(0);
        np.setMaxValue(20);

        addSecBtn.setText("Insert Section");
        addSecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.requireNonNull(secId.getText()).toString().isEmpty() || deptName.getSelectedItem().equals("Select Dept") || np.getValue()==0){
                    Toast.makeText(Section.this, "Please complete all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    sectionModel = new SectionModel();
                    sectionModel.setSecId(secId.getText().toString());
                    sectionModel.setSecDeptName(selectedDept[0]);
                    sectionModel.setNumOfClassPerWeek(String.valueOf(np.getValue()));

                    if (new SectionConn(getApplicationContext()).insertSection(sectionModel)) {
                        secId.setText("");
                        deptName.setSelection(0);
                        np.setValue(0);

                        sectionTable();
                        Toast.makeText(Section.this, "Data inserted. Pull down to refresh", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    } else {
                        Toast.makeText(Section.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        d.show();
    }

    public void updateSection(String oldSecId){
        Dialog d = new Dialog(this, R.style.Dialog);
        d.setTitle("Department Data");
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.section_dialog, null);
        d.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextInputEditText secId = v.findViewById(R.id.insertSecId);
        Spinner deptName = v.findViewById(R.id.deptNameDrpDwn);
        Button updateSecBtn = v.findViewById(R.id.addSecBtn);
        NumberPicker np = v.findViewById(R.id.numClassPerWeek);
        d.setContentView(v);

        ArrayList<String> spinnerRec = new DeptNCourseConn(Section.this).retrieveRecords();
        spinnerRec.add(0, "Select Dept");
        String[] departmentName = new String[spinnerRec.size()];
        for(int i = 0; i<spinnerRec.size(); i++){
            departmentName[i] = spinnerRec.get(i);
        }

        final String[] selectedDept = new String[1];
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departmentName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deptName.setAdapter(adapter);

        deptName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(Section.this, "Please select department", Toast.LENGTH_SHORT).show();
                }else{
                    selectedDept[0] = departmentName[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Section.this, "Please select department", Toast.LENGTH_SHORT).show();
            }

        });

        np.setMinValue(0);
        np.setMaxValue(20);

        //gets records from DB and stores in array
        String[][] sectionRecords = new SectionTableHelper(getApplicationContext()).getRecords();
        for (String[] record : sectionRecords) {
            try {
                Log.d("RECORDARRAY", Arrays.toString(record));
                //checks for a particular data with id
                if (record[0].equals(oldSecId)) {
                    //if found sets the views text with data from DB
                    secId.setText(record[0]);
                     int i;
                     for(i=0; i<departmentName.length;i++){
                         if(record[1].equals(departmentName[i])){
                             deptName.setSelection(i);
                         }
                     }
                     np.setValue(Integer.parseInt(record[2]));
                }
            }
            catch (SQLiteException e){
                e.printStackTrace();
            }
        }

        updateSecBtn.setText("Update Section");
        updateSecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.requireNonNull(secId.getText()).toString().isEmpty() || deptName.getSelectedItem().equals("Select Dept") || np.getValue()==0){
                    Toast.makeText(Section.this, "Please complete all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    sectionModel = new SectionModel();
                    sectionModel.setSecId(secId.getText().toString());
                    sectionModel.setSecDeptName(selectedDept[0]);
                    sectionModel.setNumOfClassPerWeek(String.valueOf(np.getValue()));

                    if (new SectionConn(getApplicationContext()).updateSection(oldSecId,sectionModel)) {
                        secId.setText("");
                        deptName.setSelection(0);
                        np.setValue(0);

                        sectionTable();
                        Toast.makeText(Section.this, "Data updated. Pull down to refresh", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    } else {
                        Toast.makeText(Section.this, "Data not updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        d.show();
    }

}