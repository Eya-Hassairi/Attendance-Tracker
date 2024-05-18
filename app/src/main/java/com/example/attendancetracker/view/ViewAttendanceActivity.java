package com.example.attendancetracker.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancetracker.R;
import com.example.attendancetracker.controller.AttendanceRecordAdapter;
import com.example.attendancetracker.model.AttendanceRecord;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAttendanceActivity extends AppCompatActivity {

    private ListView lvAttendanceRecords;
    private AttendanceRecordAdapter adapter;
    private List<AttendanceRecord> records;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        lvAttendanceRecords = findViewById(R.id.lvAttendanceRecords);
        progressBar = findViewById(R.id.progressBar);
        records = new ArrayList<>();
        adapter = new AttendanceRecordAdapter(this, records);

        // Inflate and add header to ListView
        View headerView = LayoutInflater.from(this).inflate(R.layout.attendance_list_header, lvAttendanceRecords, false);
        lvAttendanceRecords.addHeaderView(headerView);

        lvAttendanceRecords.setAdapter(adapter);

        // Show the progress bar while fetching data
        progressBar.setVisibility(View.VISIBLE);

        // Fetch and display attendance records from Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("attendance").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);  // Hide the progress bar
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        AttendanceRecord record = document.toObject(AttendanceRecord.class);
                        records.add(record);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ViewAttendanceActivity.this, "Error fetching records.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
