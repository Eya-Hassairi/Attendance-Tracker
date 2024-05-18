package com.example.attendancetracker.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancetracker.R;
import com.example.attendancetracker.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Button btnRecordAttendance, btnViewAttendance, btnViewStudentAttendance;
    private boolean isTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        btnRecordAttendance = findViewById(R.id.btnRecordAttendance);
        btnViewAttendance = findViewById(R.id.btnViewAttendance);
        btnViewStudentAttendance = findViewById(R.id.btnViewStudentAttendance);

        checkUserType();

        btnRecordAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTeacher) {
                    startActivity(new Intent(MainActivity.this, AttendanceActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Access Denied", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnViewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTeacher) {
                    startActivity(new Intent(MainActivity.this, ViewAttendanceActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Access Denied", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnViewStudentAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTeacher) {
                    startActivity(new Intent(MainActivity.this, ViewStudentAttendanceActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Access Denied", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkUserType() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                isTeacher = user.isTeacher();
                                adjustViewBasedOnUserType();
                            } else {
                                Toast.makeText(MainActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error fetching user details.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void adjustViewBasedOnUserType() {
        if (isTeacher) {
            btnViewStudentAttendance.setVisibility(View.GONE);
        } else {
            btnRecordAttendance.setVisibility(View.GONE);
            btnViewAttendance.setVisibility(View.GONE);
        }
    }
}

