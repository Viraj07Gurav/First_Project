package com.example.timetablegenerator.layoutsClass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.timetablegenerator.R;
import com.example.timetablegenerator.dbModel.TimetableModel;
import com.example.timetablegenerator.tableHelper.TimetableTableHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Objects;

public class InstructorTimeTable extends AppCompatActivity {

    TableLayout tl;
    TimetableModel model;
    Toolbar tb;
    SwipeRefreshLayout refreshLayout;
    ExtendedFloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        fab = findViewById(R.id.insertData);
        fab.setClickable(false);
        fab.setVisibility(View.GONE);
        refreshLayout = findViewById(R.id.swipeLayout);
        refreshLayout.setEnabled(false);

        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        //White overflow icon
        tb.getOverflowIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        tl = findViewById(R.id.TableDesign);
        tl.setStretchAllColumns(true);

        Intent intent = getIntent();
        String instructorName = intent.getStringExtra("instructorName");
        timetable(instructorName);

    }

    private void timetable(String instructorName){
        tl.removeAllViews();
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 40;

        model = new TimetableModel();
        String[][] records = new TimetableTableHelper(this).getRecords(instructorName);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Your lectures (" + records.length + ") ");
        TextView textSpacer;
        int srNo = 1;
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
                tv.setText("Sr No.");
                tv.setBackgroundColor(Color.parseColor("#95f0e7"));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv.setText(String.valueOf(srNo));
                srNo++;
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
                tv2.setText("Department");
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

            final TextView tv3 = new TextView(this);
            tv3.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            tv3.setPadding(5, 0, 0, 5);
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv3.setGravity(Gravity.CENTER);
            if (i == -1) {
                tv3.setText("Section");
                tv3.setBackgroundColor(Color.parseColor("#95f0e7"));
            } else {
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv3.setText(row[1]);
            }

            final TextView tv4 = new TextView(this);
            tv4.setGravity(Gravity.CENTER);
            tv4.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv4.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv4.setText("Course");
                tv4.setBackgroundColor(Color.parseColor("#95f0e7"));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                tv4.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv4.setBackgroundColor(Color.parseColor("#ffffff"));
                tv4.setTextColor(Color.parseColor("#000000"));
                tv4.setText(String.valueOf(row[2]));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            final TextView tv5 = new TextView(this);
            tv5.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            tv5.setPadding(5, 0, 0, 5);
            tv5.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv5.setGravity(Gravity.CENTER);
            if (i == -1) {
                tv5.setText("Room");
                tv5.setBackgroundColor(Color.parseColor("#95f0e7"));
            } else {
                tv5.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv5.setTextColor(Color.parseColor("#000000"));
                tv5.setText(row[3]);
            }

            final TextView tv6 = new TextView(this);
            tv6.setGravity(Gravity.CENTER);
            tv6.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv6.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv6.setText("Time");
                tv6.setBackgroundColor(Color.parseColor("#95f0e7"));
                tv6.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                tv6.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv6.setBackgroundColor(Color.parseColor("#ffffff"));
                tv6.setTextColor(Color.parseColor("#000000"));
                tv6.setText(String.format("%s   %s", (row[4]), row[5]));
                tv6.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
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
            tr.addView(tv5);
            tr.addView(tv6);

            tl.addView(tr, trParams);
            if (i > -1) {
                // add separator row
                final TableRow trSep = new TableRow(InstructorTimeTable.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }
}