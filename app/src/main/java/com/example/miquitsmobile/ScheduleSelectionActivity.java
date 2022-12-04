package com.example.miquitsmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduleSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    String massageId, massageName, massageDuration, contactNumber, massagePrice, username, userKey, massageEndtime;
    TextInputEditText textInputEditTextContactNumber;
    TextView textViewMassageDuration, textViewMassageName, textViewErrorMessage;
    Button btnDatePicker, btnTimePicker, submit;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute, startHour, endHour;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_selection);

        sharedPreferences = getSharedPreferences("miquits_app", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "true");
        userKey = sharedPreferences.getString("userKey", "true");

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.massage_date);
        txtTime=(EditText)findViewById(R.id.massage_time);
        textViewErrorMessage = findViewById(R.id.error_message);
        submit = findViewById(R.id.submit);
        textInputEditTextContactNumber = findViewById(R.id.contact_number);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        startHour = 9;
        endHour = 22;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactNumber = textInputEditTextContactNumber.getText().toString();

                Intent intent = new Intent(getApplicationContext(), SelectMassageActivity.class);

                intent.putExtra("contact_number", contactNumber);
                intent.putExtra("date", txtDate.getText().toString());
                intent.putExtra("time", txtTime.getText().toString());

                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            textViewErrorMessage.setVisibility(View.GONE);
                            SimpleDateFormat df = new SimpleDateFormat("HH:mm");

                            Calendar datetime = Calendar.getInstance();
                            Calendar openingTime = Calendar.getInstance();
                            Calendar closingTime = Calendar.getInstance();

                            openingTime.set(Calendar.HOUR_OF_DAY, startHour);
                            openingTime.set(Calendar.MINUTE, 00);
                            closingTime.set(Calendar.HOUR_OF_DAY, endHour);
                            closingTime.set(Calendar.MINUTE, 00);

                            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            datetime.set(Calendar.MINUTE, minute);

                            if ((datetime.getTimeInMillis() >= openingTime.getTimeInMillis() && datetime.getTimeInMillis() <= closingTime.getTimeInMillis())) {
                                txtTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                            } else {
                                textViewErrorMessage.setText("Invalid Time. Store hours 9:00AM-10:00PM");
                                textViewErrorMessage.setVisibility(View.VISIBLE);
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}