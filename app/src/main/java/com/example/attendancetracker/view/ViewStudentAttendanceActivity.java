package com.example.attendancetracker.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancetracker.R;
import com.example.attendancetracker.model.AttendanceRecord;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewStudentAttendanceActivity extends AppCompatActivity {

    private ListView lvStudentAttendanceRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_attendance);

        lvStudentAttendanceRecords = findViewById(R.id.lvStudentAttendanceRecords);

        fetchStudentAttendance();
    }

    private void fetchStudentAttendance() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String studentId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("attendance")
                .whereEqualTo("studentId", studentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<AttendanceRecord> records = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                AttendanceRecord record = document.toObject(AttendanceRecord.class);
                                records.add(record);
                            }
                            populateListView(records);
                        } else {
                            Toast.makeText(ViewStudentAttendanceActivity.this, "Error fetching records.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void populateListView(List<AttendanceRecord> records) {
        ArrayAdapter<AttendanceRecord> adapter = new ArrayAdapter<AttendanceRecord>(this, android.R.layout.simple_list_item_1, records) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                AttendanceRecord record = records.get(position);
                textView.setText("Date: " + record.getDate() + " - Present: " + (record.isPresent() ? "Yes" : "No"));
                return view;
            }
        };
        lvStudentAttendanceRecords.setAdapter(adapter);
    }
}
