package com.example.timetablegenerator.layoutsClass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timetablegenerator.R;
import com.example.timetablegenerator.database.StudentConn;
import com.example.timetablegenerator.dbModel.StudentModel;
import com.example.timetablegenerator.tableHelper.DeptNCourseTableHelper;
import com.example.timetablegenerator.tableHelper.TimetableTableHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class StudentRegister extends AppCompatActivity {

    TextInputEditText uname, pwd, confirmPwd, fullName;
    TextInputLayout unameLayout, pwdLayout, confirmPwdLayout;
    AutoCompleteTextView dept, section;
    TextView login;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        fullName = findViewById(R.id.fullName);
        uname = findViewById(R.id.uname);
        pwd = findViewById(R.id.pwd);
        confirmPwd = findViewById(R.id.confirmPwd);
        unameLayout = findViewById(R.id.unameLayout);
        pwdLayout = findViewById(R.id.pwdLayout);
        confirmPwdLayout = findViewById(R.id.confirmPwdLayout);
        login = findViewById(R.id.logIn);
        signUp = findViewById(R.id.signUpBtn);
        dept = findViewById(R.id.deptName);
        section = findViewById(R.id.sectionName);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

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
                    String[] sections = new TimetableTableHelper(StudentRegister.this).getSections(dept.getText().toString());
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(StudentRegister.this, android.R.layout.simple_list_item_1, sections);
                    section.setAdapter(adapter1);
                    section.showDropDown();
                }
            }
        });

        final boolean[] validated = {false};
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Objects.requireNonNull(fullName.getText()).toString().isEmpty() || Objects.requireNonNull(uname.getText()).toString().isEmpty() || Objects.requireNonNull(pwd.getText()).toString().isEmpty() || Objects.requireNonNull(confirmPwd.getText()).toString().isEmpty() || dept.getText().toString().isEmpty() || section.getText().toString().isEmpty()) {
                    Toast.makeText(StudentRegister.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else if (Arrays.stream(departments).noneMatch(Predicate.isEqual(dept.getText().toString()))) {
                    dept.requestFocus();
                    dept.setError("No such Department");
                } else if (Arrays.stream(departments).anyMatch(Predicate.isEqual(dept.getText().toString()))) {
                    String[] sections = new TimetableTableHelper(StudentRegister.this).getSections(dept.getText().toString());
                    validated[0] = true;
                    if (Arrays.stream(sections).noneMatch(Predicate.isEqual(section.getText().toString()))) {
                        section.requestFocus();
                        section.setError("No such Section");
                        validated[0] = false;
                    }
                }

                if (validated[0]) {
                    if (Objects.requireNonNull(pwd.getText()).toString().equals(Objects.requireNonNull(confirmPwd.getText()).toString())) {
                        StudentModel studentModel = new StudentModel();
                        studentModel.setStudFullName(fullName.getText().toString());
                        studentModel.setStudUname(uname.getText().toString());
                        studentModel.setStudPwd(pwd.getText().toString());
                        studentModel.setStudDept(dept.getText().toString());
                        studentModel.setStudSect(section.getText().toString());

                        if (new StudentConn(StudentRegister.this).insertStudent(studentModel)) {
                            fullName.setText("");
                            uname.setText("");
                            pwd.setText("");
                            confirmPwd.setText("");
                            dept.setText("");
                            section.setText("");

                            Toast.makeText(StudentRegister.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), StudentTimeTable.class);
                            intent.putExtra("deptName", dept.getText().toString());
                            intent.putExtra("section", section.getText().toString());
                            startActivity(intent);
                        }
                        else if (new StudentConn(StudentRegister.this).checkStudent(uname.getText().toString()) != null) {
                            Toast.makeText(StudentRegister.this, "Username already exists", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(StudentRegister.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(StudentRegister.this, "Password mismatch", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}