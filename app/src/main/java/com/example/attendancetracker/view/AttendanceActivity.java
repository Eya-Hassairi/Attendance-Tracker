package com.example.attendancetracker.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancetracker.R;
import com.example.attendancetracker.model.AttendanceRecord;
import com.example.attendancetracker.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AttendanceActivity extends AppCompatActivity {

    private static final String TAG = "AttendanceActivity";
    private ListView lvStudents;
    private Button btnSubmitAttendance;
    private List<User> students;
    private List<Boolean> attendanceStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        lvStudents = findViewById(R.id.lvStudents);
        btnSubmitAttendance = findViewById(R.id.btnSubmitAttendance);
        students = new ArrayList<>();
        attendanceStatus = new ArrayList<>();

        fetchStudents();

        btnSubmitAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAttendance();
            }
        });
    }

    private void fetchStudents() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("teacher", false) // Update the field name here
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && !task.getResult().isEmpty()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    try {
                                        User student = document.toObject(User.class);
                                        if (student != null) {
                                            students.add(student);
                                            attendanceStatus.add(false); // Default attendance status
                                            Log.d(TAG, "Student fetched: " + student.getUsername());
                                        } else {
                                            Log.e(TAG, "Error converting document to User object");
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error parsing document: " + document.getData(), e);
                                    }
                                }
                                populateListView();
                            } else {
                                Log.e(TAG, "No students found");
                                Toast.makeText(AttendanceActivity.this, "No students found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Error fetching students", task.getException());
                            Toast.makeText(AttendanceActivity.this, "Error fetching students.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private void populateListView() {
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_multiple_choice, students) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.student_list_item, parent, false);
                }

                TextView studentNameTextView = convertView.findViewById(R.id.studentName);
                CheckBox attendanceCheckBox = convertView.findViewById(R.id.studentAttendanceCheckbox);

                User student = students.get(position);
                studentNameTextView.setText(student.getUsername());

                // Set the checkbox state
                attendanceCheckBox.setChecked(attendanceStatus.get(position));

                // Add a listener to update the attendance status
                attendanceCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    attendanceStatus.set(position, isChecked);
                });

                return convertView;
            }
        };
        lvStudents.setAdapter(adapter);
        lvStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private void submitAttendance() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String teacherId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        for (int i = 0; i < students.size(); i++) {
            if (attendanceStatus.get(i)) { // Only submit if the student is present
                AttendanceRecord record = new AttendanceRecord(UUID.randomUUID().toString(), students.get(i).getUserId(), teacherId, date, true);
                db.collection("attendance").document(record.getRecordId()).set(record)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Attendance submitted successfully"))
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error submitting attendance", e);
                            Toast.makeText(AttendanceActivity.this, "Error submitting attendance.", Toast.LENGTH_SHORT).show();
                        });
            }
        }
        Toast.makeText(AttendanceActivity.this, "Attendance Submitted.", Toast.LENGTH_SHORT).show();
    }




}
