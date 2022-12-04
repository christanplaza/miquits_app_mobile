package com.example.miquitsmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView massageName, massageDate, massageTime, massageStatus;
    FloatingActionButton floatingActionButton;
    Button logout;
    String username, userKey;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.fab);
        logout = findViewById(R.id.logout);
        massageName = findViewById(R.id.massage_card_name);
        massageDate = findViewById(R.id.massage_card_date);
        massageTime = findViewById(R.id.massage_card_time);
        massageStatus = findViewById(R.id.massage_card_status);

        massageName.setVisibility(View.GONE);
        massageDate.setVisibility(View.GONE);
        massageTime.setVisibility(View.GONE);
        massageStatus.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences("miquits_app", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "true");
        userKey = sharedPreferences.getString("userKey", "true");

        String url = Global.RootIP + "miquits/mobile/user_dashboard.php";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                String booking = jsonObject.getString("booking");
                                JSONObject jsonObject1 = new JSONObject(booking);
                                massageName.setVisibility(View.VISIBLE);
                                massageDate.setVisibility(View.VISIBLE);
                                massageTime.setVisibility(View.VISIBLE);
                                massageStatus.setVisibility(View.VISIBLE);
                                massageName.setText(jsonObject1.getString("massageTitle"));
                                massageDate.setText(jsonObject1.getString("date"));
                                massageTime.setText(jsonObject1.getString("time"));
                                String massage_status = jsonObject1.getString("status").equals("pending") ? "Pending" : "Accepted";
                                massageStatus.setText(massage_status);
                            } else {
                                String message = jsonObject.getString("message");
                                massageName.setVisibility(View.VISIBLE);
                                massageName.setText(message);
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Global.RootIP + "miquits/mobile/logout.php";

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
}