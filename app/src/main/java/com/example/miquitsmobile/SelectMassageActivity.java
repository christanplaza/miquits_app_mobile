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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectMassageActivity extends AppCompatActivity implements RecyclerViewInterface {
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    String username, userKey, contactNumber, time, date;
    List<MassageModelClass> massageList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_massage);

        progressBar = findViewById(R.id.loading);
        sharedPreferences = getSharedPreferences("miquits_app", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "true");
        userKey = sharedPreferences.getString("userKey", "true");

        contactNumber = getIntent().getStringExtra("contact_number");
        time = getIntent().getStringExtra("time");
        date = getIntent().getStringExtra("date");

        massageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        MassageAdapter massageAdapter = new MassageAdapter(getApplicationContext(), massageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(massageAdapter);


        SimpleDateFormat df = new SimpleDateFormat("HH:mm");

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Global.RootIP + "mobile/get_massages.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("massages");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    MassageModelClass massageModel = new MassageModelClass();
                                    massageModel.setId(jsonObject1.getString("id"));
                                    massageModel.setTitle(jsonObject1.getString("title"));
                                    massageModel.setDescription(jsonObject1.getString("description"));
                                    massageModel.setDuration(jsonObject1.getString("duration"));
                                    massageModel.setPrice(jsonObject1.getString("price"));
                                    massageModel.setSeatType(jsonObject1.getString("seat_type"));

                                    massageList.add(massageModel);
                                }

                                recyclerView.invalidate();
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(massageAdapter);
                                massageAdapter.notifyDataSetChanged();
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
                paramV.put("time", time);
                paramV.put("date", date);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(int position) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = df.parse(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, Integer.parseInt(massageList.get(position).getDuration()));
        String newTime = df.format(cal.getTime());

        Intent intent = new Intent(getApplicationContext(), SelectTherapistActivity.class);

        intent.putExtra("contact_number", contactNumber);
        intent.putExtra("time", time);
        intent.putExtra("date", date);
        intent.putExtra("massage_id", massageList.get(position).getId());
        intent.putExtra("massage_name", massageList.get(position).getTitle());
        intent.putExtra("massage_price", massageList.get(position).getPrice());
        intent.putExtra("massage_duration", massageList.get(position).getDuration());
        intent.putExtra("massage_end_time", newTime);

        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }
}