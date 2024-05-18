package com.example.attendancetracker.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.attendancetracker.R;
import com.example.attendancetracker.model.AttendanceRecord;

import java.util.List;

public class AttendanceRecordAdapter extends ArrayAdapter<AttendanceRecord> {

    public AttendanceRecordAdapter(@NonNull Context context, @NonNull List<AttendanceRecord> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AttendanceRecord record = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_attendance_record, parent, false);
        }

        TextView tvStudentName = convertView.findViewById(R.id.tvStudentName);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);

        tvStudentName.setText(record.getStudentId());
        tvDate.setText(record.getDate());
        tvStatus.setText("true");

        return convertView;
    }
}
