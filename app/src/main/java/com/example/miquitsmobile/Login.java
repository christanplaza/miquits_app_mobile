package com.example.miquitsmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextInputEditText textInputEditTextUsername, textInputEditTextPassword;
    String username, password, name, role, userKey, userId;
    ProgressBar progressBar;
    Button login;
    TextView register, error;
    SharedPreferences sharedPreferences;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.loading);
        login = findViewById(R.id.submit);
        register = findViewById(R.id.register_now);
        error = findViewById(R.id.error);
        sharedPreferences = getSharedPreferences("miquits_app", MODE_PRIVATE);

        userKey = sharedPreferences.getString("userKey", "");
        username = sharedPreferences.getString("username", "");
        role = sharedPreferences.getString("role", "");
        if (userKey != "" && username != "" && role != "") {
            Intent intent;
            if (role.equals("massage_therapist")) {
                intent = new Intent(getApplicationContext(), TherapistMainActivity.class);
            } else {
                intent = new Intent(getApplicationContext(), MainActivity.class);
            }

            startActivity(intent);
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                login.setActivated(false);
                // Kwaon ang info nga gin type ka user
                username = textInputEditTextUsername.getText().toString();
                password = textInputEditTextPassword.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                // sa diin ka ga request
                String url = Global.RootIP + "mobile/login.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            // ano himuon mo after sa request
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if (status.equals("success")) {
                                        username = jsonObject.getString("username");
                                        name = jsonObject.getString("name");
                                        userKey = jsonObject.getString("userKey");
                                        role = jsonObject.getString("account_type");
                                        userId = jsonObject.getString("id");

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("logged_in", "true");
                                        editor.putString("id", userId);
                                        editor.putString("name", name);
                                        editor.putString("username", username);
                                        editor.putString("userKey", userKey);
                                        editor.putString("role", role);
                                        editor.apply();

                                        Intent intent;
                                        if (role.equals("massage_therapist")) {
                                            intent = new Intent(getApplicationContext(), TherapistMainActivity.class);
                                        } else {
                                            intent = new Intent(getApplicationContext(), MainActivity.class);
                                        }

                                        startActivity(intent);
                                        finish();
                                    } else {
                                        error.setVisibility(View.VISIBLE);
                                        error.setText(message);
                                        login.setActivated(true);
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
                    // ano ang need para sa request
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("username", username);
                        paramV.put("password", password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}