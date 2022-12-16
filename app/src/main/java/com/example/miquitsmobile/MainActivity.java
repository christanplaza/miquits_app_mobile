package com.example.miquitsmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    TextView massageName, massageDate, massageTime, massageStatus, recentMassage;
    FloatingActionButton floatingActionButton;
    Button logout, rateButton;
    String username, userKey, massageNameStr, therapistNameStr;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    List<MassageModelClass> massageList;
    ProgressBar progressBar;
    MassageModelClass recentMassageObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.fab);
        logout = findViewById(R.id.logout);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.loading);
        recentMassage = findViewById(R.id.recent_massage_title);
        rateButton = findViewById(R.id.rate_now);

        sharedPreferences = getSharedPreferences("miquits_app", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "true");
        userKey = sharedPreferences.getString("userKey", "true");
        massageList = new ArrayList<>();
        recentMassageObject = new MassageModelClass();

        recyclerView = findViewById(R.id.recycler_view);
        UpcomingMassageAdapter massageAdapter = new UpcomingMassageAdapter(getApplicationContext(), massageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(massageAdapter);

        String url = Global.RootIP + "mobile/user_dashboard.php";
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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
                                    massageModel.setTitle(jsonObject1.getString("massageTitle"));
                                    massageModel.setDate(jsonObject1.getString("date"));
                                    massageModel.setTime(jsonObject1.getString("time"));
                                    massageModel.setStatus(jsonObject1.getString("status"));
                                    massageModel.setTherapistName(jsonObject1.getString("therapistName"));

                                    massageList.add(massageModel);
                                }

                                recyclerView.invalidate();
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(massageAdapter);
                                massageAdapter.notifyDataSetChanged();
                            } else {
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
                paramV.put("username", sharedPreferences.getString("username", ""));
                paramV.put("userKey",  sharedPreferences.getString("userKey", ""));
                return paramV;
            }
        };
        queue.add(stringRequest);

        url = Global.RootIP + "mobile/get_user_rating.php";
        progressBar.setVisibility(View.VISIBLE);

        queue = Volley.newRequestQueue(getApplicationContext());
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("booking");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                recentMassageObject.setId(jsonObject1.getString("id"));
                                recentMassageObject.setTherapistName(jsonObject1.getString("therapistName"));
                                recentMassageObject.setMassageName(jsonObject1.getString("massageTitle"));
                                recentMassageObject.setDate(jsonObject1.getString("date"));
                                recentMassageObject.setTime(jsonObject1.getString("time"));
                                rateButton.setVisibility(View.VISIBLE);
                                recentMassage.setVisibility(View.VISIBLE);
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
                paramV.put("username", sharedPreferences.getString("username", ""));
                paramV.put("userKey",  sharedPreferences.getString("userKey", ""));
                return paramV;
            }
        };
        queue.add(stringRequest);

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog rankDialog = new Dialog(MainActivity.this, R.style.FullHeightDialog);
                rankDialog.setContentView(R.layout.rate_dialog);
                rankDialog.setCancelable(true);
                RatingBar ratingBar = rankDialog.findViewById(R.id.dialog_ratingbar);
                ratingBar.setRating(1);

                TextView therapistName = rankDialog.findViewById(R.id.rate_therapist_name);
                therapistName.setText(recentMassageObject.getTherapistName());

                TextView massageName = rankDialog.findViewById(R.id.rate_massage_title);
                massageName.setText(recentMassageObject.getMassageName());

                Button updateButton = rankDialog.findViewById(R.id.rank_dialog_button);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        String url = Global.RootIP + "mobile/set_rating.php";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String status = jsonObject.getString("status");
                                            if (status.equals("success")) {
                                                recentMassage.setVisibility(View.GONE);
                                                rateButton.setVisibility(View.GONE);
                                                rankDialog.dismiss();
                                                Toast.makeText(MainActivity.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
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
                                paramV.put("username", sharedPreferences.getString("username", ""));
                                paramV.put("userKey",  sharedPreferences.getString("userKey", ""));
                                paramV.put("booking_id",  recentMassageObject.getId());
                                paramV.put("rating",  ratingBar.getRating() + "");
                                return paramV;
                            }
                        };
                        queue.add(stringRequest);
                    }
                });
                //now that the dialog is set up, it's time to show it
                rankDialog.show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Global.RootIP + "mobile/logout.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("success")) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged_in", "");
                                    editor.putString("id", "");
                                    editor.putString("name", "");
                                    editor.putString("username", "");
                                    editor.putString("userKey", "");
                                    editor.putString("role", "");
                                    editor.apply();

                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);

                                    Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                        paramV.put("username", sharedPreferences.getString("username", ""));
                        paramV.put("userKey",  sharedPreferences.getString("userKey", ""));
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScheduleSelectionActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onItemClick(int position) throws ParseException {
        Intent intent = new Intent(getApplicationContext(), MassageDetails.class);
        intent.putExtra("id", massageList.get(position).getId());
        intent.putExtra("status", massageList.get(position).getStatus());
        intent.putExtra("date", massageList.get(position).getDate());
        intent.putExtra("time", massageList.get(position).getTime());
        intent.putExtra("massage", massageList.get(position).getTitle());
        intent.putExtra("therapist", massageList.get(position).getTherapistName());
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }
}