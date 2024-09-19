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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.timetablegenerator.R;
import com.example.timetablegenerator.database.TimeConn;
import com.example.timetablegenerator.dbModel.TimeModel;
import com.example.timetablegenerator.tableHelper.TimeTableHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.Objects;


public class Time extends AppCompatActivity {

    ExtendedFloatingActionButton fab;
    TimeModel timeModel;
    TableLayout tl;
    Toolbar tb;
    SwipeRefreshLayout refreshLayout;

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
        timeTable();

        fab = findViewById(R.id.insertData);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertTimeDialog();
            }
        });

        refreshLayout = findViewById(R.id.swipeLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                timeTable();
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

    public void timeTable() {
        tl.removeAllViews();
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 40;

        timeModel = new TimeModel();
        String[][] records = new TimeTableHelper(getApplicationContext()).getRecords();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Meeting Timings (" + records.length + ") ");

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
                tv.setText("Meeting ID");
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
                tv2.setText("Meeting Time");
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
                tv3.setText("Meeting Day");
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
                        PopupMenu menu = new PopupMenu(Time.this, tr);
                        menu.getMenuInflater().inflate(R.menu.table_update_menu, menu.getMenu());
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @SuppressLint("NonConstantResourceId")
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                TableRow tableRow = (TableRow) v;
                                TextView mId = (TextView) tableRow.getChildAt(0);//to get meeting id of the corresponding clicked record
                                String mIdText = mId.getText().toString();

                                switch (item.getItemId()) {
                                    case R.id.updateRecord:
                                        updateTimeDialog(mIdText);
                                        break;

                                    case R.id.deleteRecord:
                                        AlertDialog.Builder delete = new AlertDialog.Builder(Time.this);
                                        delete.setTitle("Delete Record")
                                                .setMessage("Are you sure want to delete the record?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (new TimeConn(getApplicationContext()).deleteTime(mIdText)) {
                                                            timeTable();
                                                            Toast.makeText(Time.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(Time.this, "Failed to delete record", Toast.LENGTH_SHORT).show();
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

    public void insertTimeDialog() {
        Log.d("CheckButton", "Button clicked");
        Dialog d = new Dialog(this, R.style.Dialog);
        d.setTitle("Meeting Time Data");
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.time_dialog, null);
        d.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextInputEditText meetingId = v.findViewById(R.id.insertMeetingId);
        TimePicker startTime = v.findViewById(R.id.insertStartTime);
        TimePicker endTime = v.findViewById(R.id.insertEndTime);
        Spinner day = v.findViewById(R.id.dayDrpDwn);
        Button addTime = v.findViewById(R.id.addTimeBtn);
        d.setContentView(v);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.DaysOfTheWeek, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(adapter);

        //Getting start time and adding am/pm
        String[] sTime = new String[1];
        startTime.setIs24HourView(true);
        startTime.setHour(0);
        startTime.setMinute(0);
        startTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String period = "";
                if (hourOfDay < 12) {
                    period = "am";
                } else {
                    period = "pm";
                }
                Arrays.fill(sTime, String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute) + period);
                Log.d("MeetingTimeStart", sTime[0]);
            }
        });

        //Getting end time and adding am/pm
        String[] eTime = new String[1];
        endTime.setIs24HourView(true);
        endTime.setHour(0);
        endTime.setMinute(0);
        endTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                String period = "";
                if (hourOfDay < 12) {
                    period = "am";
                } else {
                    period = "pm";
                }
                Arrays.fill(eTime, String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute) + period);
                Log.d("MeetingTimeEnd", eTime[0]);

                if(hourOfDay<startTime.getHour() || minute<startTime.getMinute()){
                    Toast.makeText(Time.this, "Meeting cannot end before it starts", Toast.LENGTH_SHORT).show();
                }
            }
        });


        addTime.setText("Insert Time");
        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Final meeting time
                String[] mTime = {sTime[0]+ " - "  +eTime[0]};
                Log.d("MeetingTime", mTime[0]);
                if (meetingId.getText().toString().isEmpty() || day.getSelectedItem().equals("---------------------------")) {
                    Toast.makeText(Time.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    timeModel = new TimeModel();
                    timeModel.setMid(meetingId.getText().toString());
                    timeModel.setMtime(mTime[0]);
                    timeModel.setMday(day.getSelectedItem().toString());

                    if (new TimeConn(getApplicationContext()).insertTime(timeModel)) {
                        meetingId.setText("");
                        day.setSelection(0);
                        timeTable();
                        Toast.makeText(Time.this, "Data inserted. Pull down to refresh", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    } else {
                        Toast.makeText(Time.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        d.show();
    }

    public void updateTimeDialog(String oldMeetingId){
        Log.d("CheckButton", "Button clicked");
        Dialog d = new Dialog(this, R.style.Dialog);
        d.setTitle("Meeting Time Data");
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.time_dialog, null);
        d.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextInputEditText meetingId = v.findViewById(R.id.insertMeetingId);
        TimePicker startTime = v.findViewById(R.id.insertStartTime);
        TimePicker endTime = v.findViewById(R.id.insertEndTime);
        Spinner day = v.findViewById(R.id.dayDrpDwn);
        Button updateTime = v.findViewById(R.id.addTimeBtn);
        d.setContentView(v);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.DaysOfTheWeek, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(adapter);

        updateTime.setText("Update Time");

        //gets records from DB and stores in array
        String[][] records = new TimeTableHelper(getApplicationContext()).getRecords();
        for (String[] record : records) {
            try {
                //checks for a particular data with id
                if (record[0].equals(oldMeetingId)) {
                    //if found sets the views text with data from DB
                    meetingId.setText(record[0]);
                    //matching day drop down values with the array to set selection
                    String[] daysArray = {"---------------------------", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                    for(int i=0;i<daysArray.length;i++){
                        if(daysArray[i].equals(record[2])){
                            day.setSelection(i);
                        }
                    }
                }
            }
            catch (SQLiteException e){
                e.printStackTrace();
            }
        }

        //Getting start time and adding am/pm
        String[] sTime = new String[1];
        startTime.setIs24HourView(true);
        startTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String period = "";
                if (hourOfDay < 12) {
                    period = "am";
                } else {
                    period = "pm";
                }
                Arrays.fill(sTime, String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute) + period);
                Log.d("MeetingTimeStart", sTime[0]);
            }
        });

        //Getting end time and adding am/pm
        String[] eTime = new String[1];
        endTime.setIs24HourView(true);
        endTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                String period = "";
                if (hourOfDay < 12) {
                    period = "am";
                } else {
                    period = "pm";
                }
                Arrays.fill(eTime, String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute) + period);
                Log.d("MeetingTimeEnd", eTime[0]);

                if(hourOfDay<startTime.getHour() || minute<startTime.getMinute()){
                    Toast.makeText(Time.this, "Meeting cannot end before it starts", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Final meeting time
                String[] mTime = {sTime[0] + " - " + eTime[0]};
                Log.d("MeetingTime", mTime[0]);
                if (Objects.requireNonNull(meetingId.getText()).toString().isEmpty() || day.getSelectedItem().equals("---------------------------")) {
                    Toast.makeText(Time.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    timeModel = new TimeModel();
                    timeModel.setMid(meetingId.getText().toString());
                    timeModel.setMtime(mTime[0]);
                    timeModel.setMday(day.getSelectedItem().toString());

                    if(new TimeConn(getApplicationContext()).updateTime(oldMeetingId, timeModel)){
                        meetingId.setText("");
                        day.setSelection(0);
                        timeTable();
                        Toast.makeText(Time.this, "Data updated. Pull down to refresh", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    } else {
                        Toast.makeText(Time.this, "Data not updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        d.show();
    }
}