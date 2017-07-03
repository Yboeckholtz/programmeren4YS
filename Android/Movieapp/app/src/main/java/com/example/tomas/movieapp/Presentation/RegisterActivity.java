package com.example.tomas.movieapp.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tomas.movieapp.Presentation.LoginActivity;
import com.example.tomas.movieapp.R;
import com.example.tomas.movieapp.Service.Config;
import com.example.tomas.movieapp.Service.VolleyRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tomas on 17-6-2017.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextREmail;
    private EditText editTextRFirstName;
    private EditText editTextRLastName;
    private EditText editTextRPassword;
    private Button btnRRegister;

    private String mREmail;
    private String mRPassword;
    private String mRFirstName;
    private String mRLastName;

    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextREmail = (EditText) findViewById(R.id.edittextREmail);
        editTextRPassword = (EditText) findViewById(R.id.edittextRPassword);
        editTextRFirstName = (EditText) findViewById(R.id.edittextRFirstname);
        editTextRLastName = (EditText) findViewById(R.id.edittextRLastName);
        btnRRegister = (Button) findViewById(R.id.btnRRegister);
        btnRRegister.setOnClickListener(this);


    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnRRegister:
                mREmail = editTextREmail.getText().toString();
                mRPassword = editTextRPassword.getText().toString();
                mRFirstName = editTextRFirstName.getText().toString();
                mRLastName = editTextRLastName.getText().toString();
                Log.i(TAG, "handleRegister - body =");

                //  Checken of username en password niet leeg zijn
                handleRegister(mREmail, mRFirstName, mRLastName, mRPassword);


                               Intent main = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(main);
                // Close the current activity
                finish();


                break;


            default:
                break;
        }

    }
    private void handleRegister(String remail, String rfirstname, String rlastname, String rpassword) {
        //
        // Maak een JSON object met username en password. Dit object sturen we mee
        // als request body (zoals je ook met Postman hebt gedaan)
        //


        String body = "{\"first_name\":\"" + rfirstname
                + "\",\"last_name\":\"" + rlastname +"\",\"email\":\"" + remail +"\",\"password\":\"" + rpassword + "\" }";

        Log.i(TAG, "handleRegister - body = " + body);

        try {
            JSONObject jsonBody = new JSONObject(body);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, Config.URL_REGISTER, jsonBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // Succesvol response - dat betekent dat we een geldig token hebben.



                            // We hebben nu het token. We kiezen er hier voor om
                            // het token in SharedPreferences op te slaan. Op die manier
                            // is het token tussen app-stop en -herstart beschikbaar -
                            // totdat het token expired.
                            try {
                                String token = response.getString("success");
                                Log.i(TAG, "handleRegister - body = " + token);


//

                                //Start the MovieList activity, and close the login activity
//                                Intent main = new Intent(getApplicationContext(), MovieList.class);
//                                startActivity(main);
//                                // Close the current activity
//                                finish();

                            } catch (JSONException e) {
                                // e.printStackTrace();
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleErrorResponse(error);
                        }
                    });

            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1500, // SOCKET_TIMEOUT_MS,
                    2, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Access the RequestQueue through your singleton class.
            VolleyRequestQueue.getInstance(this).addToRequestQueue(jsObjRequest);
        } catch (JSONException e) {
            //txtLoginErrorMsg.setText(e.getMessage());
            // e.printStackTrace();
        }
        return;
    }
    public void handleErrorResponse(VolleyError error) {
        Log.e(TAG, "handleErrorResponse");

        if(error instanceof com.android.volley.AuthFailureError) {

            String json = null;
            NetworkResponse response = error.networkResponse;
            if (response != null && response.data != null) {
                json = new String(response.data);
                json = trimMessage(json, "error");
                if (json != null) {
                    json = "Error " + response.statusCode + ": " + json;
                    displayMessage(json);
                }
            } else {
                Log.e(TAG, "handleErrorResponse: kon geen networkResponse vinden.");
            }
        } else if(error instanceof com.android.volley.NoConnectionError) {
            Log.e(TAG, "handleErrorResponse: server was niet bereikbaar");
           // txtLoginErrorMsg.setText("server offline");
        } else {
            Log.e(TAG, "handleErrorResponse: error = " + error);
        }
    }

    public String trimMessage(String json, String key){
        Log.i(TAG, "trimMessage: json = " + json);
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }
        return trimmedString;
    }

    public void displayMessage(String toastString){
        Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_LONG).show();
    }

    }

