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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateBookingActivity extends AppCompatActivity implements RecyclerViewInterface {
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    String username, userKey;
    List<MassageModelClass> massageList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);

        progressBar = findViewById(R.id.loading);
        sharedPreferences = getSharedPreferences("miquits_app", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "true");
        userKey = sharedPreferences.getString("userKey", "true");

        massageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        MassageAdapter massageAdapter = new MassageAdapter(getApplicationContext(), massageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(massageAdapter);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.11.195/miquits/mobile/get_massages.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                Log.e("inside", "test");
                                JSONArray jsonArray = jsonObject.getJSONArray("massages");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    MassageModelClass massageModel = new MassageModelClass();
                                    massageModel.setId(jsonObject1.getString("id"));
                                    massageModel.setTitle(jsonObject1.getString("title"));
                                    massageModel.setDescription(jsonObject1.getString("description"));
                                    massageModel.setDuration(jsonObject1.getString("duration"));
                                    massageModel.setPrice(jsonObject1.getString("price"));

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
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), SelectTherapistActivity.class);
        intent.putExtra("massage_id", massageList.get(position).getId());
        intent.putExtra("massage_name", massageList.get(position).getTitle());
        intent.putExtra("massage_price", massageList.get(position).getPrice());
        startActivity(intent);
    }
}