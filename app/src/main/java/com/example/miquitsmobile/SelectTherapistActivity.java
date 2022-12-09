package com.example.miquitsmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectTherapistActivity extends AppCompatActivity implements RecyclerViewInterface {
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    String username, userKey, massageId, massageName, massagePrice, time, date, massageDuration, massageEndTime, contactNumber, name;
    List<TherapistModelClass> therapistList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_therapist);

        progressBar = findViewById(R.id.loading);
        sharedPreferences = getSharedPreferences("miquits_app", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "true");
        name = sharedPreferences.getString("name", "true");
        userKey = sharedPreferences.getString("userKey", "true");
        massageId = getIntent().getStringExtra("massage_id");
        massageName = getIntent().getStringExtra("massage_name");
        massagePrice = getIntent().getStringExtra("massage_price");
        massageDuration = getIntent().getStringExtra("massage_duration");
        time = getIntent().getStringExtra("time");
        date = getIntent().getStringExtra("date");
        massageEndTime = getIntent().getStringExtra("massage_end_time");
        contactNumber = getIntent().getStringExtra("contact_number");

        therapistList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        TherapistAdapter therapistAdapter = new TherapistAdapter(getApplicationContext(), therapistList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(therapistAdapter);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Global.RootIP + "miquits/mobile/get_therapists.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("therapists");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    TherapistModelClass therapistModel = new TherapistModelClass();
                                    therapistModel.setId(jsonObject1.getString("id"));
                                    therapistModel.setName(jsonObject1.getString("name"));

                                    therapistList.add(therapistModel);
                                }

                                recyclerView.invalidate();
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(therapistAdapter);
                                therapistAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            Log.e("log", e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }){
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("username", username);
                paramV.put("userKey", userKey);
                paramV.put("date", date);
                paramV.put("timeStart", time);
                paramV.put("timeEnd", massageEndTime);
                paramV.put("massage", massageId);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(int position) {
        String therapistId = therapistList.get(position).getId();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Global.RootIP + "miquits/mobile/add_bookings.php";

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
                                overridePendingTransition(R.anim.enter, R.anim.exit);
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
                paramV.put("name", name);
                paramV.put("contact_number", contactNumber);
                paramV.put("date", date);
                paramV.put("time", time);
                paramV.put("therapist", therapistId);
                paramV.put("massage", massageId);
                return paramV;
            }
        };

        queue.add(stringRequest);
    }
}