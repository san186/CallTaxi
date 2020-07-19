package com.calltaxi.calltaxi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.calltaxi.calltaxi.Database.PreferenceHelper;
import com.calltaxi.calltaxi.Interface.LoginInterface;
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

public class LoginActivity extends AppCompatActivity {

    TextView registerPage;
    TextInputLayout loginUser, loginPass;
    Button loginBtn;
    ProgressDialog progressDialog;
    PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferenceHelper = new PreferenceHelper(this);

        //Already User Login Or Not...
        if (preferenceHelper.getIsLogin()){
            Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }

        //Hook(ID Declaration)
        registerPage = findViewById(R.id.login_sign_up);
        loginUser = findViewById(R.id.et_login_username);
        loginPass = findViewById(R.id.et_login_password);
        loginBtn = findViewById(R.id.btn_login);

        //Register Page Call Method
        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(register);
            }
        });

        //Login Button Method
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String valUser = loginUser.getEditText().getText().toString();
                String valPass = loginPass.getEditText().getText().toString();

                //Empty Validation part...
                if (valUser.isEmpty())
                {
                    loginUser.setError("Username Required");
                    loginUser.requestFocus();
                }
                else if (valPass.isEmpty())
                {
                    loginPass.setError("Password Required");
                    loginPass.requestFocus();
                }
                else
                {
                    //After Validation call login method...
//                    Toast.makeText(LoginActivity.this, userName + passWord,Toast.LENGTH_LONG).show();
                    login();
                    //ProgressDialog initialize...
                    progressDialog = new ProgressDialog(LoginActivity.this);
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

    private void login() {

        final String Username = loginUser.getEditText().getText().toString();
        final String Password = loginPass.getEditText().getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LoginInterface.LOGINURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        LoginInterface api = retrofit.create(LoginInterface.class);

        Call<String> call = api.getUserLogin(Username, Password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        parseLoginData(jsonresponse);

                    } else {
                        progressDialog.dismiss();
                        Log.i("onEmptyResponse", "Returned empty response");
                        //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void parseLoginData(String jsonresponse) {

        try {
            JSONObject jsonObject = new JSONObject(jsonresponse);
            if (jsonObject.getString("status").equals("true")) {

                saveInfo(jsonresponse);
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                this.finish();

            }else {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveInfo(String jsonresponse) {

        preferenceHelper.putIsLogin(true);
        try {
            JSONObject jsonObject = new JSONObject(jsonresponse);
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
