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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetablegenerator.R;
import com.example.timetablegenerator.database.RoomConn;
import com.example.timetablegenerator.dbModel.RoomModel;
import com.example.timetablegenerator.tableHelper.RoomTableHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class Room extends AppCompatActivity {

    ExtendedFloatingActionButton fab;
    RoomModel roomModel;
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
        roomTable();

        fab = findViewById(R.id.insertData);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertRoomDialog();
            }
        });

        refreshLayout = findViewById(R.id.swipeLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                roomTable();
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

    @SuppressLint({"SetTextI18n", "ResourceType"})
    public void roomTable() {
        tl.removeAllViews();
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 40;

        roomModel = new RoomModel();
        String[][] records = new RoomTableHelper(getApplicationContext()).getRecords();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Rooms (" + records.length + ") ");

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
                tv.setText("Room Number");
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
                tv2.setText("Seating Capacity");
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

            if(tr.getId()== 0){
                tr.setClickable(false);
            }
            else {
                tr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu menu = new PopupMenu(Room.this, tr);
                        menu.getMenuInflater().inflate(R.menu.table_update_menu, menu.getMenu());
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                TableRow tableRow = (TableRow) v;
                                TextView roomNo = (TextView) tableRow.getChildAt(0); //to get room no of the corresponding clicked record
                                String roomNoText = roomNo.getText().toString();

                                switch (item.getItemId()) {
                                    case R.id.updateRecord:
                                        updateRoomDialog(roomNoText);
                                        break;
                                    case R.id.deleteRecord:
                                        AlertDialog.Builder delete = new AlertDialog.Builder(Room.this);
                                        delete.setTitle("Delete Record")
                                                .setMessage("Are you sure want to delete the record?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (new RoomConn(getApplicationContext()).deleteRoom(roomNoText)) {
                                                            roomTable();
                                                            Toast.makeText(Room.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(Room.this, "Failed to delete record", Toast.LENGTH_SHORT).show();
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
                final TableRow trSep = new TableRow(Room.this);
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

    public void insertRoomDialog() {
        Dialog d = new Dialog(this, R.style.Dialog);
        d.setTitle("Room Data");
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.room_dialog, null);
        d.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextInputEditText roomNo = v.findViewById(R.id.insertRoomNo);
        TextInputEditText seatingCap = v.findViewById(R.id.insertSeatCap);
        Button addRoom = v.findViewById(R.id.addRoomBtn);
        d.setContentView(v);

        addRoom.setText("Insert Room");
        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(roomNo.getText()).toString().isEmpty() || Objects.requireNonNull(seatingCap.getText()).toString().isEmpty()) {
                    Toast.makeText(Room.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    roomModel = new RoomModel();
                    roomModel.setRoomNo(roomNo.getText().toString());
                    roomModel.setSeatingCap(seatingCap.getText().toString());

                    if (new RoomConn(getApplicationContext()).insertRoom(roomModel)) {
                        roomNo.setText("");
                        seatingCap.setText("");
                        roomTable();
                        Toast.makeText(Room.this, "Data inserted. Pull down to refresh", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    } else {
                        Toast.makeText(Room.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        d.show();
    }

    public void updateRoomDialog(String oldRoomNo) {
        Dialog d = new Dialog(this, R.style.Dialog);
        d.setTitle("Room Data");
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.room_dialog, null);
        d.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextInputEditText roomNo = v.findViewById(R.id.insertRoomNo);
        TextInputEditText seatingCap = v.findViewById(R.id.insertSeatCap);
        Button updateRoom = v.findViewById(R.id.addRoomBtn);
        d.setContentView(v);
        updateRoom.setText("Update Room");

        //gets records from DB and stores in array
        String[][] records = new RoomTableHelper(getApplicationContext()).getRecords();
        for (String[] record : records) {
            try {
                //checks for a particular data with id
                if (record[0].equals(oldRoomNo)) {
                    //if found sets the views text with data from DB
                    roomNo.setText(record[0]);
                    seatingCap.setText(record[1]);
                }
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }

        updateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(roomNo.getText()).toString().isEmpty() || Objects.requireNonNull(seatingCap.getText()).toString().isEmpty()) {
                    Toast.makeText(Room.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    roomModel = new RoomModel();
                    roomModel.setRoomNo(roomNo.getText().toString());
                    roomModel.setSeatingCap(seatingCap.getText().toString());

                    if (new RoomConn(getApplicationContext()).updateRoom(oldRoomNo, roomModel)) {
                        roomNo.setText("");
                        seatingCap.setText("");

                        tl.removeAllViews();
                        roomTable();
                        Toast.makeText(Room.this, "Data updated. Pull down to refresh", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    } else {
                        Toast.makeText(Room.this, "Data not updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        d.show();
    }
}