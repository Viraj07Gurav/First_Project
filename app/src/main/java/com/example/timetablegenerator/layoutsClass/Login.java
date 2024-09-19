package com.example.timetablegenerator.layoutsClass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetablegenerator.R;
import com.example.timetablegenerator.database.DBHelper;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    TextInputEditText uname;
    TextInputEditText pwd;
    Button loginBtn;
    Spinner role;
    TextView signUp;
    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(Color.BLACK);
        helper = new DBHelper(getApplicationContext());

        uname= findViewById(R.id.loginUname);
        pwd = findViewById(R.id.loginPwd);
        loginBtn = findViewById(R.id.loginBtn);
        role = findViewById(R.id.loginRole);
        signUp = findViewById(R.id.signUp);

        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.login_role, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(adapter);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StudentRegister.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {

            //private final String user = uname.getText().toString();
            //private final String pass = pwd.getText().toString();

            @Override
            public void onClick(View view) {
                String roles = role.getSelectedItem().toString();
                final String user = uname.getText().toString();
                final String pass = pwd.getText().toString();

                if(user.equals("") || pass.equals("")){
                    Toast.makeText(Login.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    switch (roles) {
                        case "Select an option":
                            Toast.makeText(Login.this, "Select an option to continue", Toast.LENGTH_SHORT).show();
                            break;

                        case "Admin":
                            if (user.equals("admin") && pass.equals("admin")) {
                                Intent adminPage = new Intent(getApplicationContext(), Admin.class);
                                startActivity(adminPage);
                                uname.setText("");
                                pwd.setText("");
                            } else {
                                Toast.makeText(Login.this, "Username or password incorrect", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Faculty":
                            SQLiteDatabase MyDB = helper.getReadableDatabase();
                            String instructorId = null, instructorPwd = null, instructorName = null;
                            Cursor c = null;
                            try {
                                c = MyDB.rawQuery("SELECT instructor.uid,instructor.name, instructor.pwd FROM instructor WHERE instructor.uid= ?", new String[]{user});
                                c.moveToNext();
                                instructorId = c.getString(c.getColumnIndexOrThrow("uid"));
                                instructorPwd = c.getString(c.getColumnIndexOrThrow("pwd"));
                                instructorName = c.getString(c.getColumnIndexOrThrow("name"));

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (c != null){
                                    c.close();
                                }
                            }
                            if(user.equals(instructorId) && pass.equals(instructorPwd)){
                                Intent intent = new Intent(getApplicationContext(), InstructorTimeTable.class);
                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                intent.putExtra("instructorName", instructorName);
                                startActivity(intent);
                                uname.setText("");
                                pwd.setText("");
                            }
                            else if(user.equals(instructorId)){
                                Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Login.this, "No such Instructor", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "Student":
                            String studName = null, studPwd = null, studDept = null, studSect = null;
                            Cursor c1 = null;
                            try {
                                 MyDB = helper.getReadableDatabase();
                                c1 = MyDB.rawQuery("SELECT * FROM students WHERE uname = ?", new String[]{user});
                                c1.moveToNext();
                                studName = c1.getString(c1.getColumnIndexOrThrow("uname"));
                                studPwd = c1.getString(c1.getColumnIndexOrThrow("pwd"));
                                studDept = c1.getString(c1.getColumnIndexOrThrow("dept"));
                                studSect = c1.getString(c1.getColumnIndexOrThrow("section"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (c1 != null){
                                    c1.close();
                                }
                            }
                            if(user.equals(studName) && pass.equals(studPwd)){
                                Intent intent = new Intent(getApplicationContext(), StudentTimeTable.class);
                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                intent.putExtra("deptName", studDept);
                                intent.putExtra("section", studSect);
                                startActivity(intent);
                                uname.setText("");
                                pwd.setText("");
                            }
                            else if(user.equals(studName)){
                                Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Login.this, "No such Student", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        default:
                            Toast.makeText(Login.this, "Select an option from the dropdown box", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}