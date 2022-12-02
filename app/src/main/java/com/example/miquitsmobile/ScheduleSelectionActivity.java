package com.example.miquitsmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ScheduleSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    String massageId, therapistId, therapistName, massageName, contactNumber, username, userKey;
    TextInputEditText textInputEditTextContactNumber;
    TextView textViewTherapistName, textViewMassageName;
    Button btnDatePicker, btnTimePicker, submit;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_selection);

        massageId = getIntent().getStringExtra("massage_id");
        therapistId = getIntent().getStringExtra("therapist_id");
        massageName = getIntent().getStringExtra("massage_name");
        therapistName = getIntent().getStringExtra("therapist_name");

        sharedPreferences = getSharedPreferences("miquits_app", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "true");
        userKey = sharedPreferences.getString("userKey", "true");


        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.massage_date);
        txtTime=(EditText)findViewById(R.id.massage_time);
        submit = findViewById(R.id.submit);
        textInputEditTextContactNumber = findViewById(R.id.contact_number);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        textViewMassageName = findViewById(R.id.massage_chosen);
        textViewTherapistName = findViewById(R.id.therapist_chosen);
        textViewMassageName.setText(massageName);
        textViewTherapistName.setText(therapistName);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactNumber = textInputEditTextContactNumber.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.11.195/miquits/mobile/add_bookings.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if (status.equals("success")) {
                                    Toast.makeText(getApplicationContext(), "Booking Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    } else {
                                        Log.e("error", response);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("username", username);
                        paramV.put("userKey", userKey);
                        paramV.put("name", username);
                        paramV.put("contact_number", contactNumber);
                        paramV.put("date", txtDate.getText().toString());
                        paramV.put("time", txtTime.getText().toString());
                        paramV.put("therapist", therapistId);
                        paramV.put("massage", massageId);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
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

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}