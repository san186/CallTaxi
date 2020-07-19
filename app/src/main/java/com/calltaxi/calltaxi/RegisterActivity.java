package com.calltaxi.calltaxi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.calltaxi.calltaxi.Database.PreferenceHelper;
import com.calltaxi.calltaxi.Interface.RegisterInterface;
import com.calltaxi.calltaxi.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout regUser, regMobile, regEmail, regPass;
    Button regBtn, loginPage;
    ProgressDialog progressDialog;
    PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        preferenceHelper = new PreferenceHelper(this);

        //Already User Login Or Not...
        if (preferenceHelper.getIsLogin()){
            Intent intent = new Intent(RegisterActivity.this,DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }

        //Hook(ID Declaration)
        loginPage = findViewById(R.id.signUp_login);
        regUser = findViewById(R.id.et_reg_username);
        regMobile = findViewById(R.id.et_reg_mobile);
        regEmail = findViewById(R.id.et_reg_email);
        regPass = findViewById(R.id.et_reg_pass);
        regBtn = findViewById(R.id.btn_reg);

        //Login Page Call Method
        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(login);
            }
        });

        //Register Button Method
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Store user input data
                String valUser = regUser.getEditText().getText().toString();
                String valMobile = regMobile.getEditText().getText().toString();
                String valEmail = regEmail.getEditText().getText().toString();
                String valPass = regPass.getEditText().getText().toString();

                //Validation
                if (valUser.isEmpty())
                {
                    regUser.setError("Username Required");
                    regUser.requestFocus();
                }
                else if (valMobile.isEmpty())
                {
                    regMobile.setError("Password Required");
                    regMobile.requestFocus();
                }
                else if (valEmail.isEmpty())
                {
                    regEmail.setError("Password Required");
                    regEmail.requestFocus();
                }
                else if (valPass.isEmpty())
                {
                    regPass.setError("Password Required");
                    regPass.requestFocus();
                }
                else
                {
                    //After Validation call login method...
                    register();
                    //ProgressDialog initialize...
                    progressDialog = new ProgressDialog(RegisterActivity.this);
                    //Show Dialog...
                    progressDialog.show();
                    //Set Content view
                    progressDialog.setContentView(R.layout.progress_dialog);
                    //Set Transparent Background
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                }

            }
        });
    }

    //Register Method...
    private void register() {

        //Store user input data
        String Username = regUser.getEditText().getText().toString();
        String Mobile = regMobile.getEditText().getText().toString();
        String Email = regEmail.getEditText().getText().toString();
        String Password = regPass.getEditText().getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RegisterInterface.REGIURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RegisterInterface api = retrofit.create(RegisterInterface.class);

        Call<String> call = api.getUserRegi(Username, Mobile, Email, Password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                
                if (response.isSuccessful())
                {
                    if (response.body() != null)
                    {
                        Log.i("onSuccess", response.body().toString());

                        String jsonResponse = response.body().toString();

                        try {
                            //Method call....
                            regData(jsonResponse);
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }else {
                        progressDialog.dismiss();
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "No Response", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void regData(String jsonResponse)throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonResponse);
        if (jsonObject.optString("status").equals("true")){

            saveInfo(jsonResponse);
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this,DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();

        }else {
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        }

    }

    private void saveInfo(String jsonResponse) {

        preferenceHelper.putIsLogin(true);
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.getString("status").equals("true")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject dataobj = dataArray.getJSONObject(i);
                    preferenceHelper.putUsername(dataobj.getString("username"));
                    preferenceHelper.putEmail(dataobj.getString("email"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
